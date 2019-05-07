package ControllerFXML;

import application.ClientApplicationMain;
import application.ClientModel;
import application.Config;
import ch.fhnw.sevenwonders.enums.GameAction;
import ch.fhnw.sevenwonders.interfaces.ICard;
import ch.fhnw.sevenwonders.interfaces.IPlayer;
import ch.fhnw.sevenwonders.messages.ClientGameMessage;
import ch.fhnw.sevenwonders.messages.ServerGameMessage;
import ch.fhnw.sevenwonders.models.Card;
import ch.fhnw.sevenwonders.models.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GameViewController {
	
	public ClientApplicationMain main;
	
	private ClientModel model;
	private IPlayer player;

	@FXML
	public Button UmmunzenButton;
	@FXML
	public Button RessourceVerwendenButton;
	@FXML
	public Button ZumBauVerwendenButton;
	public Label Player1Label;
	public Label Player2Label;
	public Label Player3Label;
	public Label Player4Label;
	public Label Player5Label;
	public Label Player6Label;
	

	
	public void setMain(ClientApplicationMain main) {
		this.main = main;
	}
	
	/**
	 * 
	 * @author Matteo
	 */
	
	public void handleUmmunzenButton(ActionEvent event) {
		UmmunzenButton.setDisable(false);
		ClientGameMessage msg = new ClientGameMessage(GameAction.MonetizeCard);
		
		msg.setCard(player.getSelectedCard());
		msg.setPlayer(model.getPlayer());
		
		model.sendMessage(msg);
	}
	
	public void handleRessourceVerwendenButton(ActionEvent event) {
		RessourceVerwendenButton.setDisable(false);
		ClientGameMessage msg = new ClientGameMessage(GameAction.PlayCard);
		
		msg.setCard(player.getSelectedCard());
		msg.setPlayer(model.getPlayer());
				
		model.sendMessage(msg);		
	}
	
	public void handleZumBauVerwendenButton() {
		ZumBauVerwendenButton.setDisable(false);
		ClientGameMessage msg = new ClientGameMessage(GameAction.BuildCard);
		
		msg.setCard(player.getSelectedCard());
		msg.setPlayer(model.getPlayer());
		msg.setBoard(player.getBoard());
		
		model.sendMessage(msg);
	}
	
	public void handlePlayer1Label() {
		
	}
	
	public void handlePlayer2Label() {
		
	}
	
	public void handlePlayer3Label() {
		
	}
	
	public void handlePlayer4Label() {
		
	}
	
	public void handlePlayer5Label() {
		
	}
	
	public void handlePlayer6Label() {
		
	}
}
