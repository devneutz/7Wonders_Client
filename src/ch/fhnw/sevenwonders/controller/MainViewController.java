package ch.fhnw.sevenwonders.controller;

import java.net.URL;
import java.util.ResourceBundle;

import ch.fhnw.sevenwonders.application.ClientApplicationMain;
import ch.fhnw.sevenwonders.enums.StartupAction;
import ch.fhnw.sevenwonders.enums.StatusCode;
import ch.fhnw.sevenwonders.interfaces.IPlayer;
import ch.fhnw.sevenwonders.messages.ClientStartupMessage;
import ch.fhnw.sevenwonders.messages.Message;
import ch.fhnw.sevenwonders.messages.ServerStartupMessage;
import ch.fhnw.sevenwonders.model.ClientModel;
import ch.fhnw.sevenwonders.models.Player;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * 
 * @author Gabriel de Castilho
 *
 *         Diese Klasse stellt den Controller fuer die MainView dar. Hier wird
 *         anhand vom Userklick entschieden ob dem User die LoginView angezeigt
 *         wird oder ob sich der User als Gast am Spiel anmelden will.
 * 
 */

public class MainViewController implements Initializable {

	// Instanz der Main Klasse
	public ClientApplicationMain main;
	public Button LogRegButton, PlayAsGuestButton;

	private Scene parentScene;

	private ClientModel model;
	private IPlayer player = new Player();

	private ChangeListener<Message> changeListener = new ChangeListener<Message>() {
		@Override
		public void changed(ObservableValue observable, Message oldValue, Message newValue) {
			// Ist es eine korrekte Antwort?
			if (newValue instanceof ServerStartupMessage) {
				newValue = (ServerStartupMessage) newValue;
				if (((ServerStartupMessage) newValue).getStatusCode() == StatusCode.Success) {
					model.setPlayer(((ServerStartupMessage) newValue).getPlayer());
					model.getLastReceivedMessage().removeListener(this);
					Platform.runLater(new Runnable() {
						public void run() {
							try {
								FXMLLoader fxmlLoader = new FXMLLoader(
										getClass().getResource("/ch/fhnw/sevenwonders/view/LobbyView.fxml"));
								Parent root1 = (Parent) fxmlLoader.load();
								LobbyViewController controller = fxmlLoader.<LobbyViewController>getController();
								controller.setModel(model);
								Stage stage = new Stage();
								Scene tmpScene = new Scene(root1);
								controller.setupListener(tmpScene);
								stage.setScene(tmpScene);
								stage.show();

								parentScene.getWindow().hide();

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		}
	};

	public void setMain(ClientApplicationMain main) {
		this.main = main;
	}

	public void setModel(ClientModel inModel) {
		this.model = inModel;
	}

	/*
	 * Wenn der User auf den Button "Einloggen/Registrieren" klickt, wird er auf die
	 * LoginView weitergeleitet.
	 */
	@FXML
	public void handleLogRegButton(ActionEvent event) {
		try {
			model.getLastReceivedMessage().removeListener(this.changeListener);
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ch/fhnw/sevenwonders/view/LoginView.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			LoginViewController controller = fxmlLoader.<LoginViewController>getController();
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

	/*
	 * Diese Methode verarbeitet die Anfrage des Users sobald er auf den Button als
	 * Gast spielen geklickt hat.
	 */
	@FXML
	public void handlePlayAsGuestButton(ActionEvent event) {
		ClientStartupMessage msg = new ClientStartupMessage(StartupAction.LoginAsGuest);
		model.sendMessage(msg);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public void setupListeners(Scene inScene) {
		parentScene = inScene;
		this.model.getLastReceivedMessage().removeListener(this.changeListener);
		this.model.getLastReceivedMessage().addListener(this.changeListener);
	}

}
