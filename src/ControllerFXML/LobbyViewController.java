package ControllerFXML;

import java.net.URL;
import java.util.ResourceBundle;

import application.ClientApplicationMain;
import application.ClientModel;
import ch.fhnw.sevenwonders.enums.LobbyAction;
import ch.fhnw.sevenwonders.enums.StatusCode;
import ch.fhnw.sevenwonders.interfaces.ILobby;
import ch.fhnw.sevenwonders.messages.ClientLobbyMessage;
import ch.fhnw.sevenwonders.messages.Message;
import ch.fhnw.sevenwonders.messages.ServerLobbyMessage;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LobbyViewController implements Initializable {

	public ClientApplicationMain main;
	private ClientModel model;
	private Scene parentScene;
	
	@FXML
	private ListView<ILobby> lobbyListView;
	@FXML
	private Label existingLobbyLabel, LobbyViewPlayerLabel;
	@FXML
	private Button CreateLobbyButton, JoinLobbyButton;
	
	private ChangeListener<Message> changeListener = new ChangeListener<Message>() {
		@Override
		public void changed(ObservableValue observable, Message oldValue, Message newValue) {
			if (newValue instanceof ServerLobbyMessage) {
				newValue = (ServerLobbyMessage) newValue;
				if (((ServerLobbyMessage) newValue).getStatusCode() == StatusCode.Success) {
					model.setPlayer(((ServerLobbyMessage) newValue).getPlayer());
					model.getLastReceivedMessage().removeListener(this);
					Platform.runLater(new Runnable() {
						public void run() {
							try {
								FXMLLoader fxmlLoader = new FXMLLoader(
										getClass().getResource("/ViewFXML/PlayerInLobbyView.fxml"));
								Parent root1 = (Parent) fxmlLoader.load();
								PlayerInLobbyViewController controller = fxmlLoader.<PlayerInLobbyViewController>getController();
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
					
				} else if (((ServerLobbyMessage) newValue).getStatusCode() == StatusCode.LobbyMaxPlayerReached) {
					Platform.runLater(new Runnable() {
						public void run() {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Kein Platz");
							alert.setHeaderText("Maximale Anzahl Spieler erreicht");
							alert.setContentText("Bitte wähle eine andere Lobby");
							alert.showAndWait();
						}
					});
				} else if (((ServerLobbyMessage) newValue).getStatusCode() == StatusCode.LobbyNotAvailable) {
					Platform.runLater(new Runnable() {
						public void run() {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Lobby nicht verfügbar");
							alert.setHeaderText("Leider steht diese Lobby nicht zum Beitritt bereit");
							alert.setContentText("Bitte wähle eine andere Lobby");
							alert.showAndWait();
						}
					});
				}
			}	
		}
	};

	public void setMain(ClientApplicationMain main) {
		this.main = main;
	}

	public void setModel(ClientModel inModel) {
		this.model = inModel;
		LobbyViewPlayerLabel.setText(model.getPlayer().getName());
		this.lobbyListView.itemsProperty().bind(model.getLobbyListProperty());
	}

	public void handleCreateLobbyButton(ActionEvent event) {
		try {
				model.getLastReceivedMessage().removeListener(this.changeListener);
		       FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ViewFXML/CreateLobbyView.fxml"));

		       Parent root1 = (Parent) fxmlLoader.load();
		       CreateLobbyController controller = fxmlLoader.<CreateLobbyController>getController();
		       controller.setModel(model);
		       Stage stage = new Stage();
		       Scene tmpScene = new Scene(root1);
		       controller.setupListener(tmpScene);
		       stage.setScene(tmpScene);  
		       stage.show();
		       
		       ((Node)event.getSource()).getScene().getWindow().hide();
		        
		   } catch(Exception e) {
		       e.printStackTrace();
		   }
	}

	public void handleJoinLobbyButton(ActionEvent event) {
		ClientLobbyMessage msg = new ClientLobbyMessage(LobbyAction.JoinLobby);
		
		msg.setLobby((ILobby)lobbyListView.getSelectionModel().getSelectedItem());
		msg.setPlayer(model.getPlayer());
		model.sendMessage(msg);		
	}
	
	
	public void setupListener(Scene inScene) {
		this.parentScene = inScene;
		this.model.getLastReceivedMessage().removeListener(this.changeListener);
		this.model.getLastReceivedMessage().addListener(this.changeListener);
	}
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

}
			