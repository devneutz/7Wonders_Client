package ControllerFXML;

import java.net.URL;
import java.util.ResourceBundle;

import application.ClientApplicationMain;
import application.ClientModel;
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
		PlayerInLobbyViewPlayerLabel.setText(model.getPlayer().getName());
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
	
	public void handleStatButton() {
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {		
	}
	
}
