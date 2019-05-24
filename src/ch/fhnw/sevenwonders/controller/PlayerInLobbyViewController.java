package ch.fhnw.sevenwonders.controller;

import java.net.URL;
import java.util.ResourceBundle;

import ch.fhnw.sevenwonders.application.ClientApplicationMain;
import ch.fhnw.sevenwonders.enums.LobbyAction;
import ch.fhnw.sevenwonders.enums.StatusCode;
import ch.fhnw.sevenwonders.interfaces.ILobby;
import ch.fhnw.sevenwonders.interfaces.IPlayer;
import ch.fhnw.sevenwonders.messages.ClientGameMessage;
import ch.fhnw.sevenwonders.messages.ClientLobbyMessage;
import ch.fhnw.sevenwonders.messages.Message;
import ch.fhnw.sevenwonders.messages.ServerLobbyMessage;
import ch.fhnw.sevenwonders.model.ClientModel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * 
 * @author Matteo Farneti
 * 
 *         Dieser Controller handelt alle Aktionen, welche auf der View
 *         PlayerInLobbyView ausgefuehrt werden koennen.
 * 
 */
public class PlayerInLobbyViewController implements Initializable {

	public ClientApplicationMain main;
	private ClientModel model;

	private Scene parentScene;
	/**
	 * In dieser Methode wird die Verarbeitung dargestellt, wenn der ChangeListener
	 * ausgeloest wurde
	 */
	private ChangeListener<Message> changeListener = new ChangeListener<Message>() {
		@Override
		public void changed(ObservableValue observable, Message oldValue, Message newValue) {
			if (newValue instanceof ServerLobbyMessage) {
				newValue = (ServerLobbyMessage) newValue;
				// Kommt eine LobbyDeleted-Message hierher, heisst dies, der Spieler befindet
				// sich in der zu loeschenden Lobby.
				if (((ServerLobbyMessage) newValue).getAction() == LobbyAction.LobbyDeleted) {
					model.getLastReceivedMessage().removeListener(this);
					Platform.runLater(new Runnable() {
						public void run() {
							try {
								FXMLLoader fxmlLoader = new FXMLLoader(
										getClass().getResource("/ch/fhnw/sevenwonders/view/LobbyView.fxml"));
								Parent root1 = (Parent) fxmlLoader.load();
								LobbyViewController controller = fxmlLoader.<LobbyViewController>getController();
								controller.setModel(model);
								Stage stage = new Stage();
								Scene tmpScene = new Scene(root1);
								controller.setupListener(tmpScene);
								stage.setScene(tmpScene);
								stage.show();

								parentScene.getWindow().hide();

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					return;
				}
				if (((ServerLobbyMessage) newValue).getAction() == LobbyAction.LobbyStarted) {
					model.getLastReceivedMessage().removeListener(this);
					model.setPlayer(((ServerLobbyMessage) newValue).getPlayer());
					Platform.runLater(new Runnable() {
						public void run() {
							try {
								FXMLLoader fxmlLoader = new FXMLLoader(
										getClass().getResource("/ch/fhnw/sevenwonders/view/GameView.fxml"));
								Parent root1 = (Parent) fxmlLoader.load();
								GameViewController controller = fxmlLoader.<GameViewController>getController();
								controller.setModel(model);
								Stage stage = new Stage();
								Scene tmpScene = new Scene(root1);
								controller.setupListener(tmpScene);
								stage.setScene(tmpScene);
								stage.show();
								stage.setMaximized(true);

								parentScene.getWindow().hide();

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					return;
				}

				if (((ServerLobbyMessage) newValue).getAction() == LobbyAction.PlayerJoined) {
					Platform.runLater(new Runnable() {
						public void run() {
							// ï¿½berprï¿½fung ob es der Master ist, wird im Model gemacht.
							if (model.getOpponentsListProperty().getValue().size() >= 3) {
								StartLobbyButton.setVisible(true);
								StartLobbyButton.setDisable(false);
							}
						}
					});
					return;
				}

				if (((ServerLobbyMessage) newValue).getStatusCode() == StatusCode.Success
						&& (((ServerLobbyMessage) newValue).getAction() == LobbyAction.DeleteLobby
								|| ((ServerLobbyMessage) newValue).getAction() == LobbyAction.LeaveLobby)) {
					model.setPlayer(((ServerLobbyMessage) newValue).getPlayer());
					model.getLastReceivedMessage().removeListener(this);
					Platform.runLater(new Runnable() {
						public void run() {
							try {
								FXMLLoader fxmlLoader = new FXMLLoader(
										getClass().getResource("/ch/fhnw/sevenwonders/view/LobbyView.fxml"));
								Parent root1 = (Parent) fxmlLoader.load();
								LobbyViewController controller = fxmlLoader.<LobbyViewController>getController();
								controller.setModel(model);
								Stage stage = new Stage();
								Scene scene = new Scene(root1);
								controller.setupListener(scene);
								stage.setScene(scene);
								stage.show();

								parentScene.getWindow().hide();

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		}
	};

	@FXML
	private Label LobbyNameLabel, PlayerInLobbyLabel, PlayerInLobbyViewPlayerLabel, LobbyPlayerCount;
	@FXML
	private Button StartLobbyButton, DeleteLobbyButton, StatButton;
	@FXML
	private ListView<IPlayer> PlayerInLobbyListView;

	/**
	 * In dieser Methode wird das Model mit den entsprechende Eigenschaften versehen
	 * 
	 * @param inModel
	 */
	public void setModel(ClientModel inModel) {
		this.model = inModel;
		this.PlayerInLobbyListView.itemsProperty().bind(model.getOpponentsListProperty());

		if (this.model.getPlayer().getLobby().getLobbyMaster().getName().equals(this.model.getPlayer().getName())) {
			DeleteLobbyButton.setVisible(true);
			DeleteLobbyButton.setDisable(false);

			// Start Button anfangs immer auf false setzen, da noch keine Spieler drin sind.
			StartLobbyButton.setVisible(true);
			StartLobbyButton.setDisable(true);

		} else {
			DeleteLobbyButton.setText("leave lobby");
			DeleteLobbyButton.setVisible(true);
			DeleteLobbyButton.setDisable(false);
		}

		PlayerInLobbyViewPlayerLabel.setText(model.getPlayer().getName());

		// Zeigt den Lobby Name an
		this.LobbyNameLabel.setText(model.getPlayer().getLobby().getLobbyName());

		// Zeigt an wie viele Spieler in der Lobby teilnehmen kÃ¶nnen
		this.LobbyPlayerCount.setText(model.getPlayer().getLobby().getNumPlayers() + " Players");
	}

	/**
	 * Diese Methode wird ausgeführt, wenn der Button Delete Lobby geklickt wird.
	 * 
	 * @param event
	 */
	public void handleDeleteLobbyButton(ActionEvent event) {
		if (this.model.getPlayer().getLobby().getLobbyMaster().getName().equals(this.model.getPlayer().getName())) {
			ClientLobbyMessage msg = new ClientLobbyMessage(LobbyAction.DeleteLobby);
			ILobby tmpLobby = model.getPlayer().getLobby();
			msg.setLobby(tmpLobby);
			msg.setPlayer(model.getPlayer());
			model.sendMessage(msg);
		} else {
			ClientLobbyMessage msg = new ClientLobbyMessage(LobbyAction.LeaveLobby);
			ILobby tmpLobby = model.getPlayer().getLobby();
			msg.setLobby(tmpLobby);
			msg.setPlayer(model.getPlayer());
			model.sendMessage(msg);
		}
	}

	/**
	 * Diese Methode wird ausgeführt, wenn der Button Start Lobby geklickt wird.
	 */
	public void handleStartLobbyButton() {
		ClientLobbyMessage msg = new ClientLobbyMessage(LobbyAction.StartLobby);
		ILobby tmpLobby = model.getPlayer().getLobby();
		msg.setLobby(tmpLobby);
		msg.setPlayer(model.getPlayer());
		model.sendMessage(msg);

	}

	/**
	 * In dieser Methode wird der ChangeListener neu aufgesetzt
	 * 
	 * @param inScene
	 */

	public void setupListener(Scene inScene) {
		this.parentScene = inScene;
		this.model.getLastReceivedMessage().removeListener(this.changeListener);
		this.model.getLastReceivedMessage().addListener(this.changeListener);
	}

	public void handleStatButton() {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

}
