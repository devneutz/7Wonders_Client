package ControllerFXML;

import application.ClientApplicationMain;
import application.Config;
import ch.fhnw.sevenwonders.enums.LobbyAction;
import ch.fhnw.sevenwonders.enums.StatusCode;
import ch.fhnw.sevenwonders.helper.MessageHelper;
import ch.fhnw.sevenwonders.interfaces.ILobby;
import ch.fhnw.sevenwonders.interfaces.IPlayer;
import ch.fhnw.sevenwonders.messages.ClientLobbyMessage;
import ch.fhnw.sevenwonders.messages.Message;
import ch.fhnw.sevenwonders.messages.ServerLobbyMessage;
import ch.fhnw.sevenwonders.messages.ServerStartupMessage;
import ch.fhnw.sevenwonders.models.Lobby;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateLobbyController {
	
	public ClientApplicationMain main;
	private ILobby lobby = new Lobby();
	@FXML
	private TextField NumberOfPlayerTextField;
	@FXML
	private Button LessPlayerButton;
	@FXML
	private TextField CountOfPlayersTextField;
	@FXML
	private Button MorePlayerButton;
	@FXML
	private TextField EnterLobbynameTextField;
	@FXML
	private Button createLobbyOkButton;
	@FXML
	private Button createLobbyCancelButton;
	
	public void setMain(ClientApplicationMain main) {
		this.main = main;
	}	
	
	public void handleCreateLobbyCancelButton(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ViewFXML/LobbyView.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));  
			stage.show();
		       
		    ((Node)event.getSource()).getScene().getWindow().hide();
		        
		   	} catch(Exception e) {
		   		e.printStackTrace();
		   	}
		
	}

	public void handleCreateLobbyOkButton(ActionEvent event) {
		ClientLobbyMessage msg = new ClientLobbyMessage(LobbyAction.CreateLobby);
		msg.setLobby(lobby);
		
		Thread t = new Thread() {
			public void run() {
				Message tmpMessageFromServer = MessageHelper.sendMessageAndWaitForAnswer(msg);
				
				if (tmpMessageFromServer instanceof ServerLobbyMessage) {
					tmpMessageFromServer = (ServerLobbyMessage) tmpMessageFromServer;
					if (((ServerLobbyMessage) tmpMessageFromServer).getStatusCode() == StatusCode.Success) {
						
						String lobbyName = EnterLobbynameTextField.getText();
						lobby.setLobbyName(lobbyName);
						
						int numPlayers = Integer.parseInt(CountOfPlayersTextField.getText());
						lobby.setNumPlayers(numPlayers);
						
						Platform.runLater(new Runnable() {
						
							public void run() {		
								try {
									IPlayer player = Config.player;
									FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ViewFXML/AdminInLobbyView.fxml"));
									Parent root1 = (Parent) fxmlLoader.load();
									Stage stage = new Stage();
									stage.setScene(new Scene(root1));  
									stage.show();
		       
									((Node)event.getSource()).getScene().getWindow().hide();
		        
								} catch(Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
				}
			}
		};
		t.start();
	}
	
	public void handleLessPlayerButton(ActionEvent event) {
		MorePlayerButton.setDisable(false);
		int tempPlayers = Integer.parseInt(CountOfPlayersTextField.getText()); 
		if (tempPlayers > 3) {
			tempPlayers--;
			String numPlayers = Integer.toString(tempPlayers);
			CountOfPlayersTextField.setText(numPlayers);
			if(tempPlayers == 3) {
				LessPlayerButton.setDisable(true);
			}	
		} 
	}			
	
	
	public void handleMorePlayerButton(ActionEvent event) {
		LessPlayerButton.setDisable(false);
		int tempPlayers = Integer.parseInt(CountOfPlayersTextField.getText()); 
		if (tempPlayers < 7) {
			tempPlayers++;
			String numPlayers = Integer.toString(tempPlayers);
			CountOfPlayersTextField.setText(numPlayers);
			if(tempPlayers == 7) {
				MorePlayerButton.setDisable(true);
			}
		}
	}
}
