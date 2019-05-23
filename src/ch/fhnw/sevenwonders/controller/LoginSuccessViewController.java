package ch.fhnw.sevenwonders.controller;

import ch.fhnw.sevenwonders.application.ClientApplicationMain;
import ch.fhnw.sevenwonders.model.ClientModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * 
 * @author Gabriel de Castilho
 * 
 *         Diese Klasse handelt die View ab welche angezeigt wird wenn der
 *         Benutzer sich erfolgreich einloggen konnte. Auf dieser View hat er
 *         dann die Moeglichkeit auf die LobbyView zuzgreifen.
 * 
 *
 */
public class LoginSuccessViewController {

	public ClientApplicationMain main;
	public Button TakeMeToTheGameButton;

	private ClientModel model;

	@FXML
	private Label signUpSuccessLabel;

	public void setModel(ClientModel inModel) {
		this.model = inModel;
	}

	/**
	 * Diese Methode handelt die Action ab wenn man auf dem Button "Take me to the
	 * Game" klickt
	 * 
	 */
	public void handleTakeMeToTheGameButton(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ch/fhnw/sevenwonders/view/LobbyView.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			LobbyViewController controller = fxmlLoader.<LobbyViewController>getController();
			controller.setModel(this.model);
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

}
