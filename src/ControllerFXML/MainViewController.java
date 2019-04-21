package ControllerFXML;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainViewController implements Initializable{
	

	// Instanz der Main Klasse
	public Main main;
	public Button LogRegButton;
	
	public void setMain(Main main) {
		this.main = main;
	}
	
	/*In Scene Builder bei Code funktioniert onMouseClick nicht
	 * Mann muss die Methode ganz oben verlinken bei onActionEvent, sonst funktioniert button nicht
	 */
	@FXML
	public void handleLogRegButton (ActionEvent event)  {
		try {
		       FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ViewFXML/LoginView.fxml"));
		       Parent root1 = (Parent) fxmlLoader.load();
		       Stage stage = new Stage();
		       stage.setScene(new Scene(root1));  
		       stage.show();
		       
		       ((Node)event.getSource()).getScene().getWindow().hide();
		        
		   } catch(Exception e) {
		       e.printStackTrace();
		   }
	}
	
	@FXML
	public void handlePlayAsGuestButton (ActionEvent event){
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	
		
}
