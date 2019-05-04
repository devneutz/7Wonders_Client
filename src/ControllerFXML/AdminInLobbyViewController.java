package ControllerFXML;

import application.ClientApplicationMain;
import application.ClientModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminInLobbyViewController {
	
	public ClientApplicationMain main;
	private ClientModel model;
		
	public void setMain(ClientApplicationMain main) {
		this.main = main;
	}
	
	public void setModel(ClientModel inModel) {
		this.model = inModel;
	}
	
	public void handleDeleteLobbyButton(ActionEvent event) {
		try {
		       FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ViewFXML/LobbyView.fxml"));
		       Parent root1 = (Parent) fxmlLoader.load();
		       LobbyViewController controller = fxmlLoader.<LobbyViewController>getController();
		       controller.setModel(model);
		       Stage stage = new Stage();
		       stage.setScene(new Scene(root1));  
		       stage.show();
		       
		       ((Node)event.getSource()).getScene().getWindow().hide();
		        
		   } catch(Exception e) {
		       e.printStackTrace();
		   }
		
	}
	
	public void handleStartLobbyButton() {
		
	}
	
	public void setupListener(Scene inScene) {
		
	}
	
}
