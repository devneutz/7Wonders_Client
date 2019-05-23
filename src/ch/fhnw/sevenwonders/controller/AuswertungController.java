package ch.fhnw.sevenwonders.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import ch.fhnw.sevenwonders.application.ClientApplicationMain;
import ch.fhnw.sevenwonders.interfaces.IPlayer;
import ch.fhnw.sevenwonders.model.ClientModel;
import ch.fhnw.sevenwonders.models.Player;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;




public class AuswertungController implements Initializable {

	private ClientModel model;
	
	public ClientApplicationMain main;
	
	
	public void setMain(ClientApplicationMain main) {
		this.main = main;
	}

	public void setModel(ClientModel inModel) {
		this.model = inModel;
		
		// Spieler sortieren
		ArrayList<IPlayer> tmpAllPlayers = new ArrayList<IPlayer>();
		tmpAllPlayers.addAll(model.getOpponentsListProperty().getValue());
		tmpAllPlayers.add(model.getPlayer());
		
		Collections.sort(tmpAllPlayers);
		
		for(IPlayer p : tmpAllPlayers) {
			System.out.println("Punktezahl: " + p.evaluate().get("TOTAL") + " - " + p.getName());
		}
	}
	
	@FXML
	public void handleQuiteGameButton(ActionEvent event) {
	}

	@FXML
	public void handleNewGameButton(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ch/fhnw/sevenwonders/view/Lobby.fxml"));
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	

}
