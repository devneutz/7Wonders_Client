package ControllerFXML;

import application.ClientApplicationMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CreateLobbyController {
	

	public ClientApplicationMain main;
	
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
		try {
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
	
	public void handleLessPlayerButton() {
		
	}
	
	public void handleMorePlayerButton() {
		
	}

	public void handleEnterLobbynameTextField() {
		
	}

}
