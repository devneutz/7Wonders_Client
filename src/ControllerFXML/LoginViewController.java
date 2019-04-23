package ControllerFXML;

import java.net.URL;
import java.util.ResourceBundle;

import application.ClientApplicationMain;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ch.fhnw.sevenwonders.messages.ClientStartupMessage;
import ch.fhnw.sevenwonders.messages.Message;
import ch.fhnw.sevenwonders.messages.ServerStartupMessage;
import ch.fhnw.sevenwonders.models.Player;
import ch.fhnw.sevenwonders.enums.StartupAction;
import ch.fhnw.sevenwonders.helper.MessageHelper;
import ch.fhnw.sevenwonders.interfaces.IPlayer;
import ch.fhnw.sevenwonders.encrypthelper.EncryptWithMD5;

public class LoginViewController implements Initializable {

	public ClientApplicationMain main;
	@FXML
	private TextField enterUsernameTxtField;
	@FXML
	private PasswordField enterPasswordPassField;
	private IPlayer player = new Player();
	
	@FXML
	private Button loginButton;
	
	@FXML
	private Button signUpButton;

	public void setMain(ClientApplicationMain main) {
		this.main = main;
	}

	public void handleloginButton(ActionEvent event) {
		enterUsernameTxtField.setDisable(true);
		enterPasswordPassField.setDisable(true);
		loginButton.setDisable(true);
		signUpButton.setDisable(true);
		
		String username = enterUsernameTxtField.getText();
		player.setName(username);

		String password = enterPasswordPassField.getText();

		String passwordHash = EncryptWithMD5.cryptWithMD5(password);

		player.setPasswordHash(passwordHash);

		ClientStartupMessage msg = new ClientStartupMessage(StartupAction.Login);
		msg.setPlayer(player);
		
		Thread t = new Thread() {
		    public void run() {
		    	Message tmpMessageFromServer = MessageHelper.sendMessageAndWaitForAnswer(msg);
		    	
		    	// Ist es eine korrekte Antwort?
		    	if(tmpMessageFromServer instanceof ServerStartupMessage) {
		    		tmpMessageFromServer = (ServerStartupMessage) tmpMessageFromServer;
		    		// TODO Handeln von Fehler (statuscode) und erfolgreichem Login
		    		
		    		// Buttons und Felder wieder aktivieren - nur im Fehlerfalle -> ansonsten Fenster schliessen und weiterleiten
			    	Platform.runLater(new Runnable() {
			            @Override public void run() {
			                loginButton.setDisable(false);
			        		enterUsernameTxtField.setDisable(false);
			        		enterPasswordPassField.setDisable(false);
			        		signUpButton.setDisable(false);
			            }
			        });
		    	
		    	}
		    }
		};
		t.start();
	}

	public void handlesignUpButton(ActionEvent event) {
		
		String username = enterUsernameTxtField.getText();
		player.setName(username);
		
		String password = enterPasswordPassField.getText();
		
		String passwordHash = EncryptWithMD5.cryptWithMD5(password);
			
		player.setPasswordHash(passwordHash);
		
		ClientStartupMessage msg = new ClientStartupMessage(StartupAction.Register);
		msg.setPlayer(player);
		
	}

	public void handlegoBackButton(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ViewFXML/MainView.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.show();
			
		    ((Node)event.getSource()).getScene().getWindow().hide();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
