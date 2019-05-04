package ControllerFXML;

import application.ClientApplicationMain;
import application.ClientModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LobbyViewController {

	public ClientApplicationMain main;
	private ClientModel model;

	public void setMain(ClientApplicationMain main) {
		this.main = main;
	}

	public void setModel(ClientModel inModel) {
		this.model = inModel;
	}

	public void handleCreateLobbyButton(ActionEvent event) {
		try {
		       FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ViewFXML/CreateLobbyView.fxml"));

		       Parent root1 = (Parent) fxmlLoader.load();
		       CreateLobbyController controller = fxmlLoader.<CreateLobbyController>getController();
		       controller.setModel(model);
		       Stage stage = new Stage();
		       stage.setScene(new Scene(root1));  
		       stage.show();
		       
		       ((Node)event.getSource()).getScene().getWindow().hide();
		        
		   } catch(Exception e) {
		       e.printStackTrace();
		   }
	}

	public void handleJoinLobbyBButton(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ViewFXML/PlayerInLobbyView.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			PlayerInLobbyViewController controller = fxmlLoader.<PlayerInLobbyViewController>getController();
			controller.setModel(model);
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.show();

			((Node) event.getSource()).getScene().getWindow().hide();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
