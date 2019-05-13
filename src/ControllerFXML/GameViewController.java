package ControllerFXML;

import java.net.URL;
import java.util.ResourceBundle;

import application.ClientApplicationMain;
import application.ClientModel;
import ch.fhnw.sevenwonders.enums.GameAction;
import ch.fhnw.sevenwonders.interfaces.ICard;
import ch.fhnw.sevenwonders.interfaces.IPlayer;
import ch.fhnw.sevenwonders.messages.ClientGameMessage;
import ch.fhnw.sevenwonders.messages.ServerGameMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class GameViewController implements Initializable {
	
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
	
	private ICard selectedCard;
	
	public void setMain(ClientApplicationMain main) {
		this.main = main;
	}
	
	/**
	 * 
	 * @author Matteo
	 */
	
	public void handleUmmunzenButton(ActionEvent event) {
		// Deaktivieren s�mtlicher Interaktionsm�glichkeiten des Spielers - solange bis eine Nachricht vom Server zur�ckkommt.
		RessourceVerwendenButton.setDisable(true);
		UmmunzenButton.setDisable(true);
		ZumBauVerwendenButton.setDisable(true);
		
		// TODO Karten sollen ebenfalls nicht mehr selektierbar sein - Warten auf Umsetzung durch ruluke
		
		// Zusammenstellen der Nachricht an den Server. Diese beinhaltet die Aktion, die vom Spieler durchgef�hrt werden will.
		ClientGameMessage msg = new ClientGameMessage(GameAction.MonetizeCard);
		
		// TODO Setzen der ausgew�hlten Karte - Warten auf Umsetzung durch ruluke
		msg.setCard(null);
		
		// Setzen des Spielers, damit der Server Bescheid weiss um welchen es sich handelt.
		msg.setPlayer(model.getPlayer());
		
		// Senden
		model.sendMessage(msg);
	}
	
	public void handleRessourceVerwendenButton(ActionEvent event) {
		// Deaktivieren s�mtlicher Interaktionsm�glichkeiten des Spielers - solange bis eine Nachricht vom Server zur�ckkommt.
		RessourceVerwendenButton.setDisable(true);
		UmmunzenButton.setDisable(true);
		ZumBauVerwendenButton.setDisable(true);
		
		// Zusammenstellen der Nachricht an den Server. Diese beinhaltet die Aktion, die vom Spieler durchgef�hrt werden will.
		ClientGameMessage msg = new ClientGameMessage(GameAction.PlayCard);
		
		// TODO Setzen der ausgew�hlten Karte - Warten auf Umsetzung durch ruluke
		msg.setCard(null);
		
		// Setzen des Spielers, damit der Server Bescheid weiss um welchen es sich handelt.
		msg.setPlayer(model.getPlayer());
		
		// Senden
		model.sendMessage(msg);		
	}
	
	public void handleZumBauVerwendenButton() {
		// Deaktivieren s�mtlicher Interaktionsm�glichkeiten des Spielers - solange bis eine Nachricht vom Server zur�ckkommt.
		RessourceVerwendenButton.setDisable(true);
		UmmunzenButton.setDisable(true);
		ZumBauVerwendenButton.setDisable(false);
		
		// Zusammenstellen der Nachricht an den Server. Diese beinhaltet die Aktion, die vom Spieler durchgef�hrt werden will.
		ClientGameMessage msg = new ClientGameMessage(GameAction.BuildCard);
		
		// TODO Setzen der ausgew�hlten Karte - Warten auf Umsetzung durch ruluke
		msg.setCard(null);
		
		// Setzen des Spielers, damit der Server Bescheid weiss um welchen es sich handelt.
		msg.setPlayer(model.getPlayer());
		
		// Setzen des Boards, damit der Server Bescheid weiss um welches es sich handelt.
		msg.setBoard(player.getBoard());
		
		// Senden
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
			// Handelt es sich bei der Message um eine Message, welche das Spiel betrifft? Theoretisch k�nnte hier auch ein Broadcast kommen, welcher dem Client
			// mitteilt, dass eine neue Lobby erstellt wurde. Darauf muss aber nicht reagiert werden.
			if (newValue instanceof ServerGameMessage) {
				ServerGameMessage tmpMessageReceived = (ServerGameMessage) newValue;

				// Setzen des Spielers, welcher vom Server zur�ckgegeben wird. Verhindert eine Manipulation auf dem Client.
				this.model.setPlayer(tmpMessageReceived.getPlayer());
				
				// Idee falls genug Zeit: Bei einem Success eine Meldung zur�ckgeben, dass auf andere Spieler gewartet wird.
				switch (tmpMessageReceived.getStatusCode()) {		
					case ActionNotAvailable:
						// TODO Alles wieder aktivieren f�r eine n�chste Auswahl? D�rfte gar nie der Fall sein. Aktuell ignorieren
						throw new IllegalArgumentException("Aktion nicht m�glich");
					case NewRound:
						// TODO Alles wieder aktivieren, eine neue Runde hat begonnen. Alle ben�tigten Variablen wurden bereits vom Server gesetzt.
						break;
					default:
						break;
				}
			}
		});
	}

	private void deselectAllCards() {
		// TODO playerCard1.setBorder() zu weniger dick und so weiter
	}
	
	@FXML
	public void onToggleCard(ActionEvent inEvent) {
		deselectAllCards();
		
		ImageView cardImageView = (ImageView) inEvent.getSource();
		
		selectedCard = (ICard)cardImageView.getUserData();
		
		// TODO setBorder() zu fett
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Iterieren �ber alle Karten im aktuellen Spieler
			// TODO Setzen der Rahmen anhand der canBuild-Methode | playerCard1.setBorder() oder �hnlich
		
			// TODO Setzen der Kartenobjekte | setUserData()
		
	}
}
