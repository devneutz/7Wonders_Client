package ControllerFXML;

import java.net.URL;
import java.util.ResourceBundle;

import application.ClientApplicationMain;
import application.ClientModel;
import ch.fhnw.sevenwonders.enums.StatusCode;
import ch.fhnw.sevenwonders.messages.ServerLobbyMessage;
import javafx.application.Platform;
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

public class LobbyViewController implements Initializable {

	public ClientApplicationMain main;
	private ClientModel model;
	
	@FXML
	private ListView lobbyListView;
	@FXML
	private Label existingLobbyLabel;
	@FXML
	private Button CreateLobbyButton, JoinLobbyButton;

	public void setMain(ClientApplicationMain main) {
		this.main = main;
	}

	public void setModel(ClientModel inModel) {
		this.model = inModel;

		this.lobbyListView.itemsProperty().bind(model.getLobbyListProperty());
	}

	public void handleCreateLobbyButton(ActionEvent event) {
		try {
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
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ViewFXML/PlayerInLobbyView.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			PlayerInLobbyViewController controller = fxmlLoader.<PlayerInLobbyViewController>getController();
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
	


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

}
