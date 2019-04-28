package ControllerFXML;

import java.net.URL;
import java.util.ResourceBundle;
import application.ClientApplicationMain;
import ch.fhnw.sevenwonders.enums.StartupAction;
import ch.fhnw.sevenwonders.enums.StatusCode;
import ch.fhnw.sevenwonders.helper.MessageHelper;
import ch.fhnw.sevenwonders.interfaces.IPlayer;
import ch.fhnw.sevenwonders.messages.ClientStartupMessage;
import ch.fhnw.sevenwonders.messages.Message;
import ch.fhnw.sevenwonders.messages.ServerStartupMessage;
import ch.fhnw.sevenwonders.models.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * 
 * @author Gabriel de Castilho
 *
 *         Diese Klasse stellt den Controller für die MainView dar. Hier wird
 *         anhand vom Userklick entschieden ob dem User die LoginView angezeigt
 *         wird oder ob sich der User als Gast am Spiel anmelden will.
 * 
 */

public class MainViewController implements Initializable {

	// Instanz der Main Klasse
	public ClientApplicationMain main;
	public Button LogRegButton;

	private IPlayer player = new Player();

	public void setMain(ClientApplicationMain main) {
		this.main = main;
	}

	/*
	 * Wenn der User auf den Button "Einloggen/Registrieren" klickt, wird er auf die
	 * LoginView weitergeleitet.
	 */
	@FXML
	public void handleLogRegButton(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ViewFXML/LoginView.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.show();

			((Node) event.getSource()).getScene().getWindow().hide();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Diese Methode verarbeitet die Anfrage des Users sobald er auf den Button als
	 * Gast spielen geklickt hat.
	 */
	@FXML
	public void handlePlayAsGuestButton(ActionEvent event) {
		ClientStartupMessage msg = new ClientStartupMessage(StartupAction.LoginAsGuest);

		Thread t = new Thread() {
			public void run() {
				Message tmpMessageFromServer = MessageHelper.sendMessageAndWaitForAnswer(msg);

				// Ist es eine korrekte Antwort?
				if (tmpMessageFromServer instanceof ServerStartupMessage) {
					tmpMessageFromServer = (ServerStartupMessage) tmpMessageFromServer;
					if (((ServerStartupMessage) tmpMessageFromServer).getStatusCode() == StatusCode.Success) {
						Platform.runLater(new Runnable() {
							public void run() {
								try {
									FXMLLoader fxmlLoader = new FXMLLoader(
											getClass().getResource("/ViewFXML/LobbyView.fxml"));
									Parent root1 = (Parent) fxmlLoader.load();
									Stage stage = new Stage();
									stage.setScene(new Scene(root1));
									stage.show();

									((Node) event.getSource()).getScene().getWindow().hide();

								} catch (Exception e) {
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
