package ch.fhnw.sevenwonders.controller;

import java.net.URL;
import java.util.ResourceBundle;

import ch.fhnw.sevenwonders.application.ClientApplicationMain;
import ch.fhnw.sevenwonders.enums.Age;
import ch.fhnw.sevenwonders.enums.GameAction;
import ch.fhnw.sevenwonders.interfaces.ICard;
import ch.fhnw.sevenwonders.messages.ClientGameMessage;
import ch.fhnw.sevenwonders.messages.ServerGameMessage;
import ch.fhnw.sevenwonders.model.ClientModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameViewController implements Initializable {

	public ClientApplicationMain main;

	private ClientModel model;

	@FXML
	public Button UmmunzenButton, RessourceVerwendenButton, ZumBauVerwendenButton;
	public Label Player1Label, Player2Label, Player3Label, Player4Label, Player5Label, Player6Label, PlayerInGameLabel,
			PlayerCoinsLabel, PlayerAttackLabel;
	public HBox PCard1HBox, PCard2HBox, PCard3HBox, PCard4HBox, PCard5HBox, PCard6HBox, PCard7HBox;
	public ImageView PlayerCard1, PlayerCard2, PlayerCard3, PlayerCard4, PlayerCard5, PlayerCard6, PlayerCard7;
	public VBox UserRMVBox, UserMGVBox, UserCOMVBox, UserMSvBox, UserSSVBox, UserCSVBox;
	public ImageView UserRMIV1, UserRMIV2, UserRMIV3, UserRMIV4, UserRMIV5, UserRMIV6, UserRMIV7, UserMGIV1, UserMGIV2,
			UserMGIV3, UserMGIV4, UserMGIV5, UserMGIV6, UserMGIV7, UserCOMIV1, UserCOMIV2, UserCOMIV3, UserCOMIV4,
			UserCOMIV5, UserCOMIV6, UserCOMIV7, UserMSIV1, UserMSIV2, UserMSIV3, UserMSIV4, UserMSIV5, UserMSIV6,
			UserMSIV7, UserSSIV1, UserSSIV2, UserSSIV3, UserSSIV4, UserSSIV5, UserSSIV6, UserSSIV7, UserCSIV1,
			UserCSIV2, UserCSIV3, UserCSIV4, UserCSIV5, UserCSIV6, UserCSIV7;

	private ICard selectedCard;
	private int nextRM = 0;
	private int nextMG = 0;
	private int nextCOM = 0;
	private int nextMS = 0;
	private int nextSS = 0;
	private int nextCS = 0;
	
	public void setMain(ClientApplicationMain main) {
		this.main = main;
	}

	public void setModel(ClientModel inModel) {
		this.model = inModel;
		setUpCards();
		UmmunzenButton.setDisable(true);
		RessourceVerwendenButton.setDisable(true);
		ZumBauVerwendenButton.setDisable(true);
		PlayerInGameLabel.setText(model.getPlayer().getName());
		PlayerCoinsLabel.setText(model.getPlayer().getCoinWallet().size() + "C");
		PlayerAttackLabel.setText(model.getPlayer().getMilitaryWarPoints() + "A");
	}

	/**
	 * 
	 * @author Lucas Ruesch Diese Methode prueft ob die Karten zum Bau verwendet
	 *         werden koennen oder nicht. Wenn Ja, aendert sich der in eine HBox
	 *         gewrappte Imageview von rot auf gruen
	 * 
	 */

	private void setUpCards() {
		Platform.runLater(new Runnable() {
			public void run() {

				PlayerCoinsLabel.setText(model.getPlayer().getCoinWallet().size() + "C");
				PlayerAttackLabel.setText(model.getPlayer().getMilitaryWarPoints() + "A");

				HBox[] HBoxArray = new HBox[7];

				HBoxArray[0] = PCard1HBox;
				HBoxArray[1] = PCard2HBox;
				HBoxArray[2] = PCard3HBox;
				HBoxArray[3] = PCard4HBox;
				HBoxArray[4] = PCard5HBox;
				HBoxArray[5] = PCard6HBox;
				HBoxArray[6] = PCard7HBox;

				for (HBox h : HBoxArray) {
					h.setStyle("");
					h.setUserData(null);
				}

				ImageView[] ImageViewArray = new ImageView[7];

				ImageViewArray[0] = PlayerCard1;
				ImageViewArray[1] = PlayerCard2;
				ImageViewArray[2] = PlayerCard3;
				ImageViewArray[3] = PlayerCard4;
				ImageViewArray[4] = PlayerCard5;
				ImageViewArray[5] = PlayerCard6;
				ImageViewArray[6] = PlayerCard7;

				String tmpAgePrefix = "";
				String tmpAgeDefaultCard = "";
				if (model.getPlayer().getCardStack().get(0).getAge() == Age.AgeI) {
					tmpAgePrefix = "/AGE I/";
					tmpAgeDefaultCard = tmpAgePrefix + "AGE I.jpg";
				} else if (model.getPlayer().getCardStack().get(0).getAge() == Age.AgeII) {
					tmpAgePrefix = "/AGE II/";
					tmpAgeDefaultCard = tmpAgePrefix + "AGE II.jpg";
				} else {
					tmpAgePrefix = "/AGE III/";
				}

				URL tmpDefaultImage = getClass().getResource("/ch/fhnw/sevenwonders/resources/" + tmpAgeDefaultCard);

				for (ImageView i : ImageViewArray) {
					i.setImage(new Image(tmpDefaultImage.toExternalForm()));
				}

				for (int x = 0; x < model.getPlayer().getCardStack().size(); x++) {
					HBoxArray[x].setUserData(model.getPlayer().getCardStack().get(x));

					URL tmpResource = getClass().getResource("/ch/fhnw/sevenwonders/resources/" + tmpAgePrefix
							+ model.getPlayer().getCardStack().get(x).getImageName());

					ImageViewArray[x].setImage(new Image(tmpResource.toExternalForm()));
					if (model.getPlayer().getCardStack().get(x).isPlayable(model.getPlayer())) {
						HBoxArray[x].setStyle("-fx-border-color: green;-fx-border-width: 2;");
					} else {
						HBoxArray[x].setStyle("-fx-border-color: red;-fx-border-width: 2;");
					}
				}
			}
		});
	}
	/**
	 * @author Matte
	 * Diese Methode fuegt ein kleines Anzeigebild beim jeweiligen Player in der Ressourcen-Uebersicht ein,
	 * wenn die Option "Resource verwenden" gewählt wird.
	 * @param card
	 */
	public void addResourceToOverview(ICard card) {
		Platform.runLater(new Runnable() {
			public void run() {
		
		VBox[] VBoxArray = new VBox[6];
		
		VBoxArray[0] = UserRMVBox;
		VBoxArray[1] = UserMGVBox;
		VBoxArray[2] = UserCOMVBox;
		VBoxArray[3] = UserMSvBox;
		VBoxArray[4] = UserSSVBox;
		VBoxArray[5] = UserCSVBox;
		
		for (VBox v : VBoxArray) {
			v.setStyle("");
			v.setUserData(null);
		}
		
		ImageView[] ImageViewArrayRM = new ImageView[7];

		ImageViewArrayRM[0] = UserRMIV1;
		ImageViewArrayRM[1] = UserRMIV2;
		ImageViewArrayRM[2] = UserRMIV3;
		ImageViewArrayRM[3] = UserRMIV4;
		ImageViewArrayRM[4] = UserRMIV5;
		ImageViewArrayRM[5] = UserRMIV6;
		ImageViewArrayRM[6] = UserRMIV7;
		
		ImageView[] ImageViewArrayMG = new ImageView[7];

		ImageViewArrayMG[0] = UserMGIV1;
		ImageViewArrayMG[1] = UserMGIV2;
		ImageViewArrayMG[2] = UserMGIV3;
		ImageViewArrayMG[3] = UserMGIV4;
		ImageViewArrayMG[4] = UserMGIV5;
		ImageViewArrayMG[5] = UserMGIV6;
		ImageViewArrayMG[6] = UserMGIV7;
		
		ImageView[] ImageViewArrayCOM = new ImageView[7];

		ImageViewArrayCOM[0] = UserCOMIV1;
		ImageViewArrayCOM[1] = UserCOMIV2;
		ImageViewArrayCOM[2] = UserCOMIV3;
		ImageViewArrayCOM[3] = UserCOMIV4;
		ImageViewArrayCOM[4] = UserCOMIV5;
		ImageViewArrayCOM[5] = UserCOMIV6;
		ImageViewArrayCOM[6] = UserCOMIV7;
		
		ImageView[] ImageViewArrayMS = new ImageView[7];

		ImageViewArrayMS[0] = UserMSIV1;
		ImageViewArrayMS[1] = UserMSIV2;
		ImageViewArrayMS[2] = UserMSIV3;
		ImageViewArrayMS[3] = UserMSIV4;
		ImageViewArrayMS[4] = UserMSIV5;
		ImageViewArrayMS[5] = UserMSIV6;
		ImageViewArrayMS[6] = UserMSIV7;
		
		ImageView[] ImageViewArraySS = new ImageView[7];

		ImageViewArraySS[0] = UserSSIV1;
		ImageViewArraySS[1] = UserSSIV2;
		ImageViewArraySS[2] = UserSSIV3;
		ImageViewArraySS[3] = UserSSIV4;
		ImageViewArraySS[4] = UserSSIV5;
		ImageViewArraySS[5] = UserSSIV6;
		ImageViewArraySS[6] = UserSSIV7;
		
		ImageView[] ImageViewArrayCS = new ImageView[7];

		ImageViewArrayCS[0] = UserCSIV1;
		ImageViewArrayCS[1] = UserCSIV2;
		ImageViewArrayCS[2] = UserCSIV3;
		ImageViewArrayCS[3] = UserCSIV4;
		ImageViewArrayCS[4] = UserCSIV5;
		ImageViewArrayCS[5] = UserCSIV6;
		ImageViewArrayCS[6] = UserCSIV7;
		
		String imageName = card.getImageName().substring(0, card.getImageName().length()-4);
		String tmpM = "k_";
		String png = ".png";
		
		URL tmpResource = getClass().getResource("/ch/fhnw/sevenwonders/resources/klein/" + tmpM + imageName + png);
		
		switch (card.getCardType()) {
		
		case RawMaterials: ImageViewArrayRM[nextRM].setImage(new Image(tmpResource.toExternalForm()));
		++nextRM;
		break;
		
		case ManufacturedGoods: ImageViewArrayMG[nextMG].setImage(new Image(tmpResource.toExternalForm()));
		++nextMG;
		break;
		
		case CommercialStructures: ImageViewArrayCOM[nextCOM].setImage(new Image(tmpResource.toExternalForm()));
		++nextCOM;
		break;
		
		case MilitaryStructures: ImageViewArrayMS[nextMS].setImage(new Image(tmpResource.toExternalForm()));
		++nextMS;
		break;
		
		case ScientificStructures: ImageViewArraySS[nextSS].setImage(new Image(tmpResource.toExternalForm()));
		++nextSS;
		break;
		
		case CivilianStructures: ImageViewArrayCS[nextCS].setImage(new Image(tmpResource.toExternalForm()));
		++nextCS;
		break;
		}
			}
		});
	}

	/**
	 * 
	 * @author Matteo
	 */

	public void handleUmmunzenButton(ActionEvent event) {
		// Deaktivieren saemtlicher Interaktionsmoeglichkeiten des Spielers - solange
		// bis eine Nachricht vom Server zurueckkommt.
		RessourceVerwendenButton.setDisable(true);
		UmmunzenButton.setDisable(true);
		ZumBauVerwendenButton.setDisable(true);

		// TODO Karten sollen ebenfalls nicht mehr selektierbar sein - Warten auf
		// Umsetzung durch ruluke

		// Zusammenstellen der Nachricht an den Server. Diese beinhaltet die Aktion, die
		// vom Spieler durchgefuehrt werden will.
		ClientGameMessage msg = new ClientGameMessage(GameAction.MonetizeCard);

		// TODO Setzen der ausgewaehlten Karte - Warten auf Umsetzung durch ruluke
		msg.setCard(selectedCard);

		// Setzen des Spielers, damit der Server Bescheid weiss um welchen es sich
		// handelt.
		msg.setPlayer(model.getPlayer());

		// Senden
		model.sendMessage(msg);
	}

	public void handleRessourceVerwendenButton(ActionEvent event) {
		// Deaktivieren saemtlicher Interaktionsmoeglichkeiten des Spielers - solange
		// bis eine Nachricht vom Server zuruekkommt.
		RessourceVerwendenButton.setDisable(true);
		UmmunzenButton.setDisable(true);
		ZumBauVerwendenButton.setDisable(true);

		// Zusammenstellen der Nachricht an den Server. Diese beinhaltet die Aktion, die
		// vom Spieler durchgefuehrt werden will.
		ClientGameMessage msg = new ClientGameMessage(GameAction.PlayCard);
		
		// Setzen der ausgewaehlten Karte
		msg.setCard(selectedCard);
		
		// Setzen des Spielers, damit der Server Bescheid weiss um welchen es sich
		// handelt.
		msg.setPlayer(model.getPlayer());

		// Senden
		model.sendMessage(msg);
		
		// Ruft Methode auf, um die Ressourcen ebenfalls in der Spielerübersicht hinzuzufügen.
		addResourceToOverview(selectedCard);
	}

	public void handleZumBauVerwendenButton(ActionEvent event) {
		// Deaktivieren saemtlicher Interaktionsmoeglichkeiten des Spielers - solange
		// bis eine Nachricht vom Server zurueckkommt.
		RessourceVerwendenButton.setDisable(true);
		UmmunzenButton.setDisable(true);
		ZumBauVerwendenButton.setDisable(false);

		// Zusammenstellen der Nachricht an den Server. Diese beinhaltet die Aktion, die
		// vom Spieler durchgefuehrt werden will.
		ClientGameMessage msg = new ClientGameMessage(GameAction.BuildCard);

		// Setzen der ausgewaehlten Karte
		msg.setCard(selectedCard);

		// Setzen des Spielers, damit der Server Bescheid weiss um welchen es sich
		// handelt.
		msg.setPlayer(model.getPlayer());

		// Setzen des Boards, damit der Server Bescheid weiss um welches es sich
		// handelt.
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

	/**
	 * Registrieren der Listener
	 * 
	 * @param inScene
	 */
	public void setupListener(Scene inScene) {
		this.model.getLastReceivedMessage().addListener((observable, oldvalue, newValue) -> {
			// Handelt es sich bei der Message um eine Message, welche das Spiel betrifft?
			// Theoretisch koennte hier auch ein Broadcast kommen, welcher dem Client
			// mitteilt, dass eine neue Lobby erstellt wurde. Darauf muss aber nicht
			// reagiert werden.
			if (newValue instanceof ServerGameMessage) {
				ServerGameMessage tmpMessageReceived = (ServerGameMessage) newValue;

				// Setzen des Spielers, welcher vom Server zurueckgegeben wird. Verhindert eine
				// Manipulation auf dem Client.
				this.model.setPlayer(tmpMessageReceived.getPlayer());

				// Idee falls genug Zeit: Bei einem Success eine Meldung zurï¿½ckgeben, dass auf
				// andere Spieler gewartet wird.
				switch (tmpMessageReceived.getStatusCode()) {
				case ActionNotAvailable:
					// TODO Alles wieder aktivieren fï¿½r eine nï¿½chste Auswahl? Duerfte gar nie
					// der Fall sein. Aktuell ignorieren
					throw new IllegalArgumentException("Aktion nicht mï¿½glich");
				case NewRound:
					setUpCards();
					// TODO Alles wieder aktivieren, eine neue Runde hat begonnen. Alle benoetigten
					// Variablen wurden bereits vom Server gesetzt.
					break;
				default:
					break;
				}
			}
		});
	}

	private void deselectAllCards() {
		setUpCards();
	}

	@FXML
	public void onToggleCard(MouseEvent inEvent) {

		this.RessourceVerwendenButton.setDisable(true);
		this.ZumBauVerwendenButton.setDisable(true);
		this.UmmunzenButton.setDisable(true);
		deselectAllCards();

		HBox tmpSelectedHBox = (HBox) inEvent.getSource();

		selectedCard = (ICard) tmpSelectedHBox.getUserData();

		if (selectedCard == null) {
			return;
		}

		if (selectedCard.isPlayable(model.getPlayer())) {
			this.RessourceVerwendenButton.setDisable(false);
			this.ZumBauVerwendenButton.setDisable(false);
		}
		this.UmmunzenButton.setDisable(false);

		tmpSelectedHBox.setStyle("-fx-border-color: orange;-fx-border-width: 5px;");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}
}
