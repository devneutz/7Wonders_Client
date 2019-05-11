package ControllerFXML;

import java.util.Observable;
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
import ch.fhnw.sevenwonders.models.Lobby;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class PlayerInLobbyViewController implements Initializable{
	
	public ClientApplicationMain main;
	private ClientModel model;
	
	private Scene parentScene;
	
	private ChangeListener<Message> changeListener = new ChangeListener() {
		@Override
		public void changed(ObservableValue observable, Object oldValue, Object newValue) {
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
										getClass().getResource("/ViewFXML/LobbyView.fxml"));
								Parent root1 = (Parent) fxmlLoader.load();
								LobbyViewController controller = fxmlLoader.<LobbyViewController>getController();
								controller.setModel(model);
								Stage stage = new Stage();
								stage.setScene(new Scene(root1));
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
	private Label LobbyNameLabel, PlayerInLobbyLabel, PlayerInLobbyViewPlayerLabel;
	@FXML
	private Button StartLobbyButton, DeleteLobbyButton, StatButton;
	@FXML
	private ListView PlayerInLobbyListView;
	
		
	public void setMain(ClientApplicationMain main) {
		this.main = main;
	}
	
	public void setModel(ClientModel inModel) {
		this.model = inModel;
		this.PlayerInLobbyListView.itemsProperty().bind(model.getOpponentsListProperty());
		
//		if(this.model.getPlayer().getLobby().getLobbyMaster().getName().equalsIgnoreCase(this.model.getPlayer().getName())) {
//			DeleteLobbyButton.setVisible(true);
//		}
		PlayerInLobbyViewPlayerLabel.setText(model.getPlayer().getName());
	}
	
	public void handleDeleteLobbyButton(ActionEvent event) {
		ClientLobbyMessage msg = new ClientLobbyMessage(LobbyAction.DeleteLobby);
		ILobby tmpLobby = model.getPlayer().getLobby();
		msg.setLobby(tmpLobby);
		msg.setPlayer(model.getPlayer());
		model.sendMessage(msg);
			
		
	}
	
	public void handleStartLobbyButton() {
		
	}
	
	public void setupListener(Scene inScene) {
		this.parentScene = inScene;
		this.model.getLastReceivedMessage().addListener(this.changeListener);
	}
	
	public void handleStatButton() {
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {		
	}
	
}
