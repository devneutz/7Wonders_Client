package ch.fhnw.sevenwonders.controller;

import ch.fhnw.sevenwonders.application.ClientApplicationMain;
import ch.fhnw.sevenwonders.enums.LobbyAction;
import ch.fhnw.sevenwonders.enums.StatusCode;
import ch.fhnw.sevenwonders.messages.ClientLobbyMessage;
import ch.fhnw.sevenwonders.messages.Message;
import ch.fhnw.sevenwonders.messages.ServerLobbyMessage;
import ch.fhnw.sevenwonders.model.ClientModel;
import ch.fhnw.sevenwonders.models.Lobby;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * @author Matteo Farneti
 *
 */

public class CreateLobbyController {

	public ClientApplicationMain main;

	private ClientModel model;

	private Scene parentScene;

	@FXML
	private Label NumberOfPlayerLabel, CountOfPlayersLabel, CreateLobbyViewPlayerLabel;
	@FXML
	private Button LessPlayerButton, MorePlayerButton, createLobbyOkButton, createLobbyCancelButton;
	@FXML
	private TextField EnterLobbynameTextField;

	public void setMain(ClientApplicationMain main) {
		this.main = main;
	}

	public void setModel(ClientModel inModel) {
		this.model = inModel;
		CreateLobbyViewPlayerLabel.setText(model.getPlayer().getName());
	}

	/**
	 * In dieser Methode wird die Verarbeitung dargestellt, wenn der ChangeListener
	 * ausgeloest wurde
	 */
	private ChangeListener<Message> changeListener = new ChangeListener<Message>() {
		@Override
		public void changed(ObservableValue observable, Message oldValue, Message newValue) {
			// TODO Auto-generated method stub
			if (newValue instanceof ServerLobbyMessage) {
				newValue = (ServerLobbyMessage) newValue;
				if (((ServerLobbyMessage) newValue).getStatusCode() == StatusCode.Success) {
					model.setPlayer(((ServerLobbyMessage) newValue).getPlayer());
					model.getLastReceivedMessage().removeListener(this);
					Platform.runLater(new Runnable() {
						public void run() {
							try {
								FXMLLoader fxmlLoader = new FXMLLoader(
										getClass().getResource("/ch/fhnw/sevenwonders/view/PlayerInLobbyView.fxml"));
								Parent root1 = (Parent) fxmlLoader.load();
								PlayerInLobbyViewController controller = fxmlLoader
										.<PlayerInLobbyViewController>getController();
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
				} else if (((ServerLobbyMessage) newValue).getStatusCode() == StatusCode.LobbyAlreadyExists) {
					Platform.runLater(new Runnable() {
						public void run() {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Lobby existiert bereits");
							alert.setHeaderText("Lobby mit dem gew�hlten Lobbynamen existiert bereits");
							alert.setContentText("Bitte w�hle einen anderen Lobbynamen aus");
							alert.showAndWait();
						}
					});
				}
			}
		}
	};

	public void handleCreateLobbyCancelButton(ActionEvent event) {
		try {
			model.getLastReceivedMessage().removeListener(this.changeListener);
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ch/fhnw/sevenwonders/view/LobbyView.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			LobbyViewController controller = fxmlLoader.<LobbyViewController>getController();
			controller.setModel(model);
			Stage stage = new Stage();
			Scene tmpScene = new Scene(root1);
			controller.setupListener(tmpScene);
			stage.setScene(tmpScene);
			stage.show();

			((Node) event.getSource()).getScene().getWindow().hide();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void handleCreateLobbyOkButton(ActionEvent event) {
		if (EnterLobbynameTextField.getText().equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNUNG");
			alert.setHeaderText("Lobbynamen fehlt");
			alert.setContentText("Bitte fehlende Felder ausfuellen");
			alert.showAndWait();
		} else {
			ClientLobbyMessage msg = new ClientLobbyMessage(LobbyAction.CreateLobby);

			Lobby lobby = new Lobby();
			String lobbyName = EnterLobbynameTextField.getText();
			lobby.setLobbyName(lobbyName);

			int numPlayers = Integer.parseInt(CountOfPlayersLabel.getText());
			lobby.setNumPlayers(numPlayers);

			msg.setLobby(lobby);

			msg.setPlayer(model.getPlayer());

			model.sendMessage(msg);
		}
	}

	public void handleLessPlayerButton(ActionEvent event) {
		MorePlayerButton.setDisable(false);
		int tempPlayers = Integer.parseInt(CountOfPlayersLabel.getText());
		if (tempPlayers > 3) {
			tempPlayers--;
			String numPlayers = Integer.toString(tempPlayers);
			CountOfPlayersLabel.setText(numPlayers);
			if (tempPlayers == 3) {
				LessPlayerButton.setDisable(true);
			}
		}
	}

	public void handleMorePlayerButton(ActionEvent event) {
		LessPlayerButton.setDisable(false);
		int tempPlayers = Integer.parseInt(CountOfPlayersLabel.getText());
		if (tempPlayers < 7) {
			tempPlayers++;
			String numPlayers = Integer.toString(tempPlayers);
			CountOfPlayersLabel.setText(numPlayers);
			if (tempPlayers == 7) {
				MorePlayerButton.setDisable(true);
			}
		}
	}

	public void setupListener(Scene inScene) {
		this.parentScene = inScene;
		this.model.getLastReceivedMessage().removeListener(this.changeListener);
		this.model.getLastReceivedMessage().addListener(this.changeListener);
	}
}