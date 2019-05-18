package ch.fhnw.sevenwonders.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import ch.fhnw.sevenwonders.application.ClientApplicationMain;
import ch.fhnw.sevenwonders.enums.GameAction;
import ch.fhnw.sevenwonders.interfaces.ICard;
import ch.fhnw.sevenwonders.interfaces.IPlayer;
import ch.fhnw.sevenwonders.messages.ClientGameMessage;
import ch.fhnw.sevenwonders.messages.ServerGameMessage;
import ch.fhnw.sevenwonders.model.ClientModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class GameViewController implements Initializable {
	
	public ClientApplicationMain main;
	
	private ClientModel model;

	@FXML
	public Button UmmunzenButton, RessourceVerwendenButton, ZumBauVerwendenButton;
	public Label Player1Label, Player2Label,Player3Label, Player4Label, Player5Label, Player6Label;
	public HBox PCard1HBox, PCard2HBox, PCard3HBox, PCard4HBox, PCard5HBox, PCard6HBox, PCard7HBox;
	public ImageView PlayerCard1, PlayerCard2, PlayerCard3, PlayerCard4, PlayerCard5, PlayerCard6, PlayerCard7;
	
	
	private ICard selectedCard;
	
	public void setMain(ClientApplicationMain main) {
		this.main = main;
	}
	
	
	
	
	public void setModel(ClientModel inModel) {
		this.model = inModel;
		setUpCards(null);
			
	}
	
	/**
	 * 
	 * @author lucas rueesch
	 * Diese Methode prüft ob die Karten zum Bau verwendet werden können oder nicht. 
	 * Wenn Ja ändert sich der in eine HBox gewrappte Imageview von rot auf grün
	 * 
	 */
	
	private void setUpCards(ArrayList<ICard> inCards) {
		
		HBox[] HBoxArray = new HBox [7];
		
		HBoxArray[0] = PCard1HBox;
		HBoxArray[1] = PCard2HBox;
		HBoxArray[2] = PCard3HBox;
		HBoxArray[3] = PCard4HBox;
		HBoxArray[4] = PCard5HBox;
		HBoxArray[5] = PCard6HBox;
		HBoxArray[6] = PCard7HBox;
		
		ImageView[] ImageViewArray = new ImageView[7];
		
		ImageViewArray[0] = PlayerCard1;
		ImageViewArray[1] = PlayerCard2;
		ImageViewArray[2] = PlayerCard3;
		ImageViewArray[3] = PlayerCard4;
		ImageViewArray[4] = PlayerCard5;
		ImageViewArray[5] = PlayerCard6;
		ImageViewArray[6] = PlayerCard7;		
		
	
		for (int x = 0; x < model.getPlayer().getCardStack().size(); x++) {
			HBoxArray[x].setUserData(model.getPlayer().getCardStack().get(x));
			
			if(model.getPlayer().getCardStack().get(x).isPlayable(null)) {
				HBoxArray[x].setStyle("-fx-border-color: green;");		
			}
		}
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
		
		// Zusammenstellen der Nachricht an den Server. Diese beinhaltet die Aktion, die vom Spieler durchgefuehrt werden will.
		ClientGameMessage msg = new ClientGameMessage(GameAction.MonetizeCard);
		
		// TODO Setzen der ausgewaehlten Karte - Warten auf Umsetzung durch ruluke
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
		msg.setBoard(model.getPlayer().getBoard());
		
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
		
	}
}
