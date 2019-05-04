package ControllerFXML;

import java.net.URL;
import java.util.ResourceBundle;
import application.ClientApplicationMain;
import application.ClientModel;
import application.Config;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ch.fhnw.sevenwonders.messages.ClientStartupMessage;
import ch.fhnw.sevenwonders.messages.Message;
import ch.fhnw.sevenwonders.messages.ServerStartupMessage;
import ch.fhnw.sevenwonders.models.Player;
import ch.fhnw.sevenwonders.enums.StartupAction;
import ch.fhnw.sevenwonders.enums.StatusCode;
import ch.fhnw.sevenwonders.interfaces.IPlayer;
import ch.fhnw.sevenwonders.encrypthelper.EncryptWithMD5;

/**
 * 
 * @author Gabriel de Castilho, Joel Neutzner
 * 
 *         Diese Klasse stellt den Controller f�r die LoginView dar. Hier wird
 *         entschieden ob sich der User einloggen will oder einen neuen User
 *         erstellt indem sich der User registriert.
 *
 */
public class LoginViewController implements Initializable {

	public ClientApplicationMain main;
	private ClientModel model;
	
	private IPlayer player = new Player();
	@FXML
	private TextField enterUsernameTxtField;
	@FXML
	private PasswordField enterPasswordPassField;
	@FXML
	private Button loginButton;
	@FXML
	private Button signUpButton;
	@FXML
	private Button goBackButton;

	public void setMain(ClientApplicationMain main) {
		this.main = main;
	}

	public void setModel(ClientModel inModel) {
		this.model = inModel;
	}
	
	/*
	 * Wenn der User auf den Button "Login" klickt wird diese Methode ausgef�hrt
	 */
	public void handleloginButton(ActionEvent event) {
		if (enterUsernameTxtField.getText().equals("") || enterPasswordPassField.getText().equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNUNG");
			alert.setHeaderText("Benutzername oder Passwort fehlt");
			alert.setContentText("Bitte fehlende Felder ausf�llen");
			alert.showAndWait();
		}

		else {
			enterUsernameTxtField.setDisable(true);
			enterPasswordPassField.setDisable(true);
			loginButton.setDisable(true);
			signUpButton.setDisable(true);
			goBackButton.setDisable(true);

			String username = enterUsernameTxtField.getText();
			player.setName(username);

			String password = enterPasswordPassField.getText();

			String passwordHash = EncryptWithMD5.cryptWithMD5(password);

			player.setPasswordHash(passwordHash);

			ClientStartupMessage msg = new ClientStartupMessage(StartupAction.Login);
			msg.setPlayer(player);

			Thread t = new Thread() {
				public void run() {
					Message tmpMessageFromServer = model.sendMessageAndWaitForAnswer(msg);

					// Ist es eine korrekte Antwort?
					if (tmpMessageFromServer instanceof ServerStartupMessage) {
						tmpMessageFromServer = (ServerStartupMessage) tmpMessageFromServer;
						if (((ServerStartupMessage) tmpMessageFromServer).getStatusCode() == StatusCode.LoginFailed) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									Alert alert = new Alert(AlertType.ERROR);
									alert.setTitle("FEHLER");
									alert.setHeaderText("Benutzername oder Passwort falsch");
									alert.setContentText("Bitte korrekte Eingabe t�tigen");
									alert.showAndWait();

									loginButton.setDisable(false);
									enterUsernameTxtField.setDisable(false);
									enterPasswordPassField.setDisable(false);
									signUpButton.setDisable(false);
									goBackButton.setDisable(false);

								}
							});

						}
						if (((ServerStartupMessage) tmpMessageFromServer).getStatusCode() == StatusCode.Success) {

							model.setPlayer(((ServerStartupMessage) tmpMessageFromServer).getPlayer());
							Platform.runLater(new Runnable() {
								public void run() {
									try {
										FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ViewFXML/LobbyView.fxml"));
										Parent root = (Parent) fxmlLoader.load();
										LobbyViewController controller = fxmlLoader.<LobbyViewController>getController();
										controller.setModel(model);
										Stage stage = new Stage();
										stage.setScene(new Scene(root));
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
	}

	/*
	 * Wenn der User auf den Button "Einloggen/Registrieren" klickt wird diese
	 * Methode ausgef�hrt
	 */
	public void handlesignUpButton(ActionEvent event) {

		if (enterUsernameTxtField.getText().equals("") || enterPasswordPassField.getText().equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNUNG");
			alert.setHeaderText("Benutzername oder Passwort fehlt");
			alert.setContentText("Bitte fehlende Felder ausf�llen");
			alert.showAndWait();
		}

		else {
			enterUsernameTxtField.setDisable(true);
			enterPasswordPassField.setDisable(true);
			loginButton.setDisable(true);
			signUpButton.setDisable(true);
			goBackButton.setDisable(true);

			String username = enterUsernameTxtField.getText();
			player.setName(username);

			String password = enterPasswordPassField.getText();

			String passwordHash = EncryptWithMD5.cryptWithMD5(password);

			player.setPasswordHash(passwordHash);

			ClientStartupMessage msg = new ClientStartupMessage(StartupAction.Register);
			msg.setPlayer(player);

			Thread t = new Thread() {
				public void run() {
					Message tmpMessageFromServer = model.sendMessageAndWaitForAnswer(msg);

					// Ist es eine korrekte Antwort?
					if (tmpMessageFromServer instanceof ServerStartupMessage) {
						tmpMessageFromServer = (ServerStartupMessage) tmpMessageFromServer;
						if (((ServerStartupMessage) tmpMessageFromServer)
								.getStatusCode() == StatusCode.RegistrationFailed) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									Alert alert = new Alert(AlertType.ERROR);
									alert.setTitle("FEHLER");
									alert.setHeaderText("Fehler bei der Verarbeitung der Benutzerangaben aufgetreten");
									alert.setContentText("Bitte nochmals Benutzerangaben eingeben");
									alert.showAndWait();

									loginButton.setDisable(false);
									enterUsernameTxtField.setDisable(false);
									enterPasswordPassField.setDisable(false);
									signUpButton.setDisable(false);
									goBackButton.setDisable(false);

								}
							});

						}
						if (((ServerStartupMessage) tmpMessageFromServer).getStatusCode() == StatusCode.Success) {

							model.setPlayer(((ServerStartupMessage) tmpMessageFromServer).getPlayer());
							Platform.runLater(new Runnable() {
								public void run() {
									try {
										FXMLLoader fxmlLoader = new FXMLLoader(
										getClass().getResource("/ViewFXML/LoginSuccessView.fxml"));
										Parent root = (Parent) fxmlLoader.load();
										Stage stage = new Stage();
										stage.setScene(new Scene(root));
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
	}

	/*
	 * Wenn der User auf den Button "<" klickt wird diese Methode ausgef�hrt. Der
	 * User wird auf das vorherige MainView Fenster gef�hrt.
	 */
	public void handlegoBackButton(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ViewFXML/MainView.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			MainViewController controller = fxmlLoader.<MainViewController>getController();
			controller.setModel(model);
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.show();

			((Node) event.getSource()).getScene().getWindow().hide();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
