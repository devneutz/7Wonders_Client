package ControllerFXML;

import application.ClientApplicationMain;
import application.ClientModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LoginSuccessViewController {
	
	public ClientApplicationMain main;
	public Button TakeMeToTheGameButton;

	private ClientModel model;
	
	public void TakeMeToTheGameButton(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ViewFXML/LobbyView.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			LobbyViewController controller = fxmlLoader.<LobbyViewController>getController();
			controller.setModel(this.model);
			Stage stage = new Stage();
			Scene tmpScene = new Scene(root1);
			stage.setScene(tmpScene);
			stage.show();

			((Node) event.getSource()).getScene().getWindow().hide();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
