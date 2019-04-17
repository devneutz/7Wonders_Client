package ControllerFXML;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ch.fhnw.sevenwonders.messages.ClientStartupMessage;
import ch.fhnw.sevenwonders.enums.StartupAction;
import ch.fhnw.sevenwonders.interfaces.IPlayer;
import ch.fhnw.sevenwonders.encrypthelper.EncryptWithMD5;

public class LoginViewController implements Initializable {

	public Main main;
	@FXML
	private TextField enterUsernameTxtField;
	@FXML
	private PasswordField enterPasswordPassField;
	private IPlayer player;

	public void setMain(Main main) {
		this.main = main;
	}

	public void handleloginButton(ActionEvent event) {
		String username = enterUsernameTxtField.getText();
		player.setName(username);

		String password = enterPasswordPassField.getText();

		String passwordHash = EncryptWithMD5.cryptWithMD5(password);

		player.setPasswordHash(passwordHash);

		ClientStartupMessage msg = new ClientStartupMessage(StartupAction.Login);
		msg.setPlayer(player);

	}

	public void handlesignUpButton(ActionEvent event) {
		
		String username = enterUsernameTxtField.getText();
		player.setName(username);
		
		String password = enterPasswordPassField.getText();
		
		String passwordHash = EncryptWithMD5.cryptWithMD5(password);
			
		player.setPasswordHash(passwordHash);
		
		ClientStartupMessage msg = new ClientStartupMessage(StartupAction.Register);
		msg.setPlayer(player);
		
		/**
		try (
		Socket s = new Socket(127.0.0.1,5000);
		ObjectOutputStream out = new ObjectOutputSteam(s.getOutputStream());¨
		) {
		out.writeObject(msg);
		out.flush();	
	
	}**/}

	public void handlegoBackButton(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ViewFXML/MainView.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
