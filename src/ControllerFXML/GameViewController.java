package ControllerFXML;

import application.ClientApplicationMain;
import application.ClientModel;
import ch.fhnw.sevenwonders.enums.GameAction;
import ch.fhnw.sevenwonders.interfaces.IPlayer;
import ch.fhnw.sevenwonders.messages.ClientGameMessage;
import ch.fhnw.sevenwonders.messages.ServerGameMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
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
		// Deaktivieren sämtlicher Interaktionsmöglichkeiten des Spielers - solange bis eine Nachricht vom Server zurückkommt.
		RessourceVerwendenButton.setDisable(true);
		UmmunzenButton.setDisable(true);
		ZumBauVerwendenButton.setDisable(true);
		
		// TODO Karten sollen ebenfalls nicht mehr selektierbar sein - Warten auf Umsetzung durch ruluke
		
		// Zusammenstellen der Nachricht an den Server. Dies beinhält die Aktion, die vom Spieler durchgeführt werden will.
		ClientGameMessage msg = new ClientGameMessage(GameAction.MonetizeCard);
		
		// TODO Setzen der ausgewählten Karte - Warten auf Umsetzung durch ruluke
		msg.setCard(null);
		
		// Setzen des Spielers, damit der Server Bescheid weiss um welchen es sich handelt.
		msg.setPlayer(model.getPlayer());
		
		// Senden
		model.sendMessage(msg);
	}
	
	public void handleRessourceVerwendenButton(ActionEvent event) {
		RessourceVerwendenButton.setDisable(false);
		ClientGameMessage msg = new ClientGameMessage(GameAction.PlayCard);
		
		msg.setCard(null);
		msg.setPlayer(model.getPlayer());
				
		model.sendMessage(msg);		
	}
	
	public void handleZumBauVerwendenButton() {
		ZumBauVerwendenButton.setDisable(false);
		ClientGameMessage msg = new ClientGameMessage(GameAction.BuildCard);
		
		msg.setCard(null);
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
	
	/***
	 * Registrieren der Listener
	 * @param inScene
	 */
	public void setupListener(Scene inScene) {
		this.model.getLastReceivedMessage().addListener((observable, oldvalue, newValue) -> {
			// Handelt es sich bei der Message um eine Message, welche das Spiel betrifft? Theoretisch könnte hier auch ein Broadcast kommen, welcher dem Client
			// mitteilt, dass eine neue Lobby erstellt wurde. Darauf muss aber nicht reagiert werden.
			if (newValue instanceof ServerGameMessage) {
				ServerGameMessage tmpMessageReceived = (ServerGameMessage) newValue;

				// Setzen des Spielers, welcher vom Server zurückgegeben wird. Verhindert eine Manipulation auf dem Client.
				this.model.setPlayer(tmpMessageReceived.getPlayer());
				
				// Idee falls genug Zeit: Bei einem Success eine Meldung zurückgeben, dass auf andere Spieler gewartet wird.
				switch (tmpMessageReceived.getStatusCode()) {		
					case ActionNotAvailable:
						// TODO Alles wieder aktivieren für eine nächste Auswahl? Dürfte gar nie der Fall sein. Aktuell ignorieren
						throw new IllegalArgumentException("Aktion nicht möglich");
					case NewRound:
						// TODO Alles wieder aktivieren, eine neue Runde hat begonnen. Alle benötigten Variablen wurden bereits vom Server gesetzt.
						break;
					default:
						break;
				}
			}
		});
	}
}
