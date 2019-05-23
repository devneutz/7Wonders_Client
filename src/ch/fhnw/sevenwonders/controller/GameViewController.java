package ch.fhnw.sevenwonders.controller;

import java.net.URL;
import ch.fhnw.sevenwonders.enums.Age;
import ch.fhnw.sevenwonders.enums.GameAction;
import ch.fhnw.sevenwonders.interfaces.ICard;
import ch.fhnw.sevenwonders.interfaces.IPlayer;
import ch.fhnw.sevenwonders.messages.ClientGameMessage;
import ch.fhnw.sevenwonders.messages.Message;
import ch.fhnw.sevenwonders.messages.ServerEvaluationMessage;
import ch.fhnw.sevenwonders.messages.ServerGameMessage;
import ch.fhnw.sevenwonders.model.ClientModel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameViewController {
	private Scene parentScene;

	private ClientModel model;

	@FXML
	// Spielbuttons
	public Button UmmunzenButton, RessourceVerwendenButton, ZumBauVerwendenButton;
	// Namen der Spieler, Coins und Angriffspunkte
	public Label Player1Label, Player2Label, Player3Label, Player4Label, Player5Label, Player6Label, PlayerInGameLabel,
			PlayerCoinsLabel, PlayerAttackLabel;
	public HBox PCard1HBox, PCard2HBox, PCard3HBox, PCard4HBox, PCard5HBox, PCard6HBox, PCard7HBox,
			// Board-Steps
			TableWW1hbox, TableWW2hbox, TableWW3hbox;
	public ImageView TableWW1iv, TableWW2iv, TableWW3iv;
	@FXML
	// Anzeige von Coins und Angriffspunkten
	public HBox player1ResourceBox, player2ResourceBox, player3ResourceBox, player4ResourceBox, player5ResourceBox,
			player6ResourceBox;	
	// Spielkarten
	public ImageView PlayerCard1, PlayerCard2, PlayerCard3, PlayerCard4, PlayerCard5, PlayerCard6, PlayerCard7;
	
	public VBox UserRMVBox, UserMGVBox, UserCOMVBox, UserMSvBox, UserSSVBox, UserCSVBox, Player1ResVBox, Player2ResVBox,
			Player3ResVBox, Player4ResVBox, Player5ResVBox, Player6ResVBox;
	// Ressourcen vom Spieler selbst
	public ImageView UserRMIV1, UserRMIV2, UserRMIV3, UserRMIV4, UserRMIV5, UserRMIV6, UserRMIV7, UserMGIV1, UserMGIV2,
			UserMGIV3, UserMGIV4, UserMGIV5, UserMGIV6, UserMGIV7, UserCOMIV1, UserCOMIV2, UserCOMIV3, UserCOMIV4,
			UserCOMIV5, UserCOMIV6, UserCOMIV7, UserMSIV1, UserMSIV2, UserMSIV3, UserMSIV4, UserMSIV5, UserMSIV6,
			UserMSIV7, UserSSIV1, UserSSIV2, UserSSIV3, UserSSIV4, UserSSIV5, UserSSIV6, UserSSIV7, UserCSIV1,
			UserCSIV2, UserCSIV3, UserCSIV4, UserCSIV5, UserCSIV6, UserCSIV7;

	private ICard selectedCard;
	private int nextRM = 1;
	private int nextMG = 0;
	private int nextCOM = 0;
	private int nextMS = 0;
	private int nextSS = 0;
	private int nextCS = 0;
	
	/**
	 * Setzen der Ausgangslage
	 * @param inModel
	 */
	public void setModel(ClientModel inModel) {
		this.model = inModel;

		updateCardUi();
		updateOpponentsUi();
		
		UmmunzenButton.setDisable(true);
		RessourceVerwendenButton.setDisable(true);
		ZumBauVerwendenButton.setDisable(true);
		
		PlayerInGameLabel.setText(model.getPlayer().getName());
		PlayerCoinsLabel.setText(model.getPlayer().getCoinWallet().size() + "C");
		PlayerAttackLabel.setText(model.getPlayer().getMilitaryPoints() + "A");
	}

	/**
	 * 
	 */
	private void updateOpponentsUi() {
		Platform.runLater(new Runnable() {
			public void run() {
				Label[] tmpPlayerLabels = new Label[6];
				tmpPlayerLabels[0] = Player1Label;
				tmpPlayerLabels[1] = Player2Label;
				tmpPlayerLabels[2] = Player3Label;
				tmpPlayerLabels[3] = Player4Label;
				tmpPlayerLabels[4] = Player5Label;
				tmpPlayerLabels[5] = Player6Label;

				HBox[] tmpPlayerResourceBoxes = new HBox[6];
				tmpPlayerResourceBoxes[0] = player1ResourceBox;
				tmpPlayerResourceBoxes[1] = player2ResourceBox;
				tmpPlayerResourceBoxes[2] = player3ResourceBox;
				tmpPlayerResourceBoxes[3] = player4ResourceBox;
				tmpPlayerResourceBoxes[4] = player5ResourceBox;
				tmpPlayerResourceBoxes[5] = player6ResourceBox;

				for (int i = 0; i < model.getOpponentsListProperty().getValue().size(); i++) {
					tmpPlayerLabels[i].setText(model.getOpponentsListProperty().getValue().get(i).getName());
					tmpPlayerLabels[i].setVisible(true);
					tmpPlayerResourceBoxes[i].setVisible(true);
					tmpPlayerResourceBoxes[i].setUserData(model.getOpponentsListProperty().getValue().get(i));
					((Label) tmpPlayerResourceBoxes[i].getChildren().get(0))
							.setText(model.getOpponentsListProperty().getValue().get(i).getCoinWallet().size() + "C");

					((Label) tmpPlayerResourceBoxes[i].getChildren().get(1))
							.setText(model.getOpponentsListProperty().getValue().get(i).getMilitaryPoints() + "A");
				}
			}
		});

	}

	/**
	 * Aktualisieren der Karten - Setzen der Bilder, erstellen des Rahmens anhand der isPlayable Methode
	 * @author Lucas Ruesch  
	 */
	private void updateCardUi() {
		Platform.runLater(new Runnable() {
			public void run() {
				// Setzen der Coins und Militärpunkte
				PlayerCoinsLabel.setText(model.getPlayer().getCoinWallet().size() + "C");
				PlayerAttackLabel.setText(model.getPlayer().getMilitaryPoints() + "A");

				// Erstellen eines HBox-Arrays um das durchgehen per Schleife zu ermöglichen
				HBox[] HBoxArray = new HBox[7];

				HBoxArray[0] = PCard1HBox;
				HBoxArray[1] = PCard2HBox;
				HBoxArray[2] = PCard3HBox;
				HBoxArray[3] = PCard4HBox;
				HBoxArray[4] = PCard5HBox;
				HBoxArray[5] = PCard6HBox;
				HBoxArray[6] = PCard7HBox;

				// Erstellen der Ausgangslage, kein Rahmen, keine hinterlegte Karte
				for (HBox h : HBoxArray) {
					h.setStyle("");
					h.setUserData(null);
				}

				// Erstellen eines ImageView-Arrays um das durchgehen per Schleife zu ermöglichen
				ImageView[] ImageViewArray = new ImageView[7];

				ImageViewArray[0] = PlayerCard1;
				ImageViewArray[1] = PlayerCard2;
				ImageViewArray[2] = PlayerCard3;
				ImageViewArray[3] = PlayerCard4;
				ImageViewArray[4] = PlayerCard5;
				ImageViewArray[5] = PlayerCard6;
				ImageViewArray[6] = PlayerCard7;

				// Zusammensetzen des Prefixes um die richtigen Bilder aus dem richtigen Package zu laden
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

				// Standard-Bild für gespielte Karten
				URL tmpDefaultImage = getClass().getResource("/ch/fhnw/sevenwonders/resources/" + tmpAgeDefaultCard);

				// Setzen der Ausgangslage - alle Karten verdeckt
				for (ImageView i : ImageViewArray) {
					i.setImage(new Image(tmpDefaultImage.toExternalForm()));
				}

				// Durchgehen der Karten und setzen des Objekts in der HBox
				for (int x = 0; x < model.getPlayer().getCardStack().size(); x++) {
					HBoxArray[x].setUserData(model.getPlayer().getCardStack().get(x));

					URL tmpResource = getClass().getResource("/ch/fhnw/sevenwonders/resources/" + tmpAgePrefix
							+ model.getPlayer().getCardStack().get(x).getImageName());
					
					// Existiert die Ressource nicht (Bild fehlt o.ä.) wird es ausgegeben
					if(tmpResource == null) {
						System.out.println("IMAGE RESOURCE NULL: Betroffene Karte -> " + model.getPlayer().getCardStack().get(x).getImageName()
								+ " | vollständiger Pfad: /ch/fhnw/sevenwonders/resources/" + tmpAgePrefix	+ model.getPlayer().getCardStack().get(x).getImageName());
						continue;
					}

					// Setzen des Rahmens
					ImageViewArray[x].setImage(new Image(tmpResource.toExternalForm()));
					if (model.getPlayer().getCardStack().get(x).isPlayable(model.getPlayer())) {
						HBoxArray[x].setStyle("-fx-border-color: green;-fx-border-width: 2;");
					} else {
						HBoxArray[x].setStyle("-fx-border-color: red;-fx-border-width: 2;");
					}
				}
				
				// Setzen der Standard-Ressource des Boards
				URL boardResource = getClass()
						.getResource("/ch/fhnw/sevenwonders/resources/mini/m_RM_Steinbruch_3.png");
				UserRMIV1.setImage(new Image(boardResource.toExternalForm()));
				
				// Ruft Methode auf, um den Status der Bauten durch Farben zu visualisieren.
				makeBuildingVisible ();
			}
		});
	}

	/**
	 * @author Matte Diese Methode fuegt ein kleines Anzeigebild beim jeweiligen
	 *         Player in der Ressourcen-Uebersicht ein, wenn die Option "Ressource
	 *         verwenden" gewaehlt wird.
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

				String imageName = card.getImageName().substring(0, card.getImageName().length() - 4);
				String tmpM = "m_";
				String png = ".png";

				URL tmpResource = getClass()
						.getResource("/ch/fhnw/sevenwonders/resources/mini/" + tmpM + imageName + png);

				switch (card.getCardType()) {

				case RawMaterials:
					ImageViewArrayRM[nextRM].setImage(new Image(tmpResource.toExternalForm()));
					++nextRM;
					break;

				case ManufacturedGoods:
					ImageViewArrayMG[nextMG].setImage(new Image(tmpResource.toExternalForm()));
					++nextMG;
					break;

				case CommercialStructures:
					ImageViewArrayCOM[nextCOM].setImage(new Image(tmpResource.toExternalForm()));
					++nextCOM;
					break;

				case MilitaryStructures:
					ImageViewArrayMS[nextMS].setImage(new Image(tmpResource.toExternalForm()));
					++nextMS;
					break;

				case ScientificStructures:
					ImageViewArraySS[nextSS].setImage(new Image(tmpResource.toExternalForm()));
					++nextSS;
					break;

				case CivilianStructures:
					ImageViewArrayCS[nextCS].setImage(new Image(tmpResource.toExternalForm()));
					++nextCS;
					break;
				default:
					break;
				}
			}
		});
	}
	
	/**
	 * 
	 */
	public void makeBuildingVisible () {
		
		boolean stepOneBuilt = model.getPlayer().getBoard().getStepOneBuilt();
		boolean stepTwoBuilt = model.getPlayer().getBoard().getStepTwoBuilt();
		boolean stepThreeBuilt = model.getPlayer().getBoard().getStepThreeBuilt();
		boolean canBuild = model.getPlayer().getBoard().canBuild(model.getPlayer());
		int nextStep = model.getPlayer().getBoard().getNextStageToBuild();
		
		// Ausgangslage erstellen
		TableWW1hbox.setStyle("-fx-border-color: red;-fx-border-width: 2;");
		TableWW2hbox.setStyle("-fx-border-color: red;-fx-border-width: 2;");
		TableWW3hbox.setStyle("-fx-border-color: red;-fx-border-width: 2;");
		
		if(stepOneBuilt) {
			TableWW1hbox.setStyle("-fx-border-color: orange;-fx-border-width: 2;");
			if(stepTwoBuilt) {
				TableWW2hbox.setStyle("-fx-border-color: orange;-fx-border-width: 2;");
				if(stepThreeBuilt) {
					TableWW3hbox.setStyle("-fx-border-color: orange;-fx-border-width: 2;");
				}else {
					TableWW3hbox.setStyle("-fx-border-color: red;-fx-border-width: 2;");
					if(canBuild) {
						TableWW3hbox.setStyle("-fx-border-color: green;-fx-border-width: 2;");
					}
				}
			}
			else {
				TableWW2hbox.setStyle("-fx-border-color: red;-fx-border-width: 2;");
				if(canBuild) {
					TableWW2hbox.setStyle("-fx-border-color: green;-fx-border-width: 2;");
				}
				TableWW3hbox.setStyle("-fx-border-color: red;-fx-border-width: 2;");
			}
		}else {
			TableWW1hbox.setStyle("-fx-border-color: red;-fx-border-width: 2;");
			if(canBuild) {
				TableWW1hbox.setStyle("-fx-border-color: green;-fx-border-width: 2;");
			}
			TableWW2hbox.setStyle("-fx-border-color: red;-fx-border-width: 2;");
			TableWW3hbox.setStyle("-fx-border-color: red;-fx-border-width: 2;");
		}			
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

		updateCardUi();
	}

	/**
	 * Wird ausgeführt, wenn eine Ressource gespielt werden soll
	 * @param event
	 */
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

		// Ruft Methode auf, um die Ressourcen ebenfalls in der Spielerï¿½bersicht
		// hinzuzufuegen.
		addResourceToOverview(selectedCard);

		updateCardUi();
	}

	/**
	 * Wird ausgeführt, wenn eine Karte für den Bau eines Weltwunders gebraucht werden soll
	 * @param event
	 */
	public void handleZumBauVerwendenButton(ActionEvent event) {
		// Deaktivieren saemtlicher Interaktionsmoeglichkeiten des Spielers - solange
		// bis eine Nachricht vom Server zurueckkommt.
		RessourceVerwendenButton.setDisable(true);
		UmmunzenButton.setDisable(true);
		ZumBauVerwendenButton.setDisable(true);

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
		
		updateCardUi();
	}

	/**
	 * Toggle-Event für die Karten des Spielers 1
	 * @param event
	 */
	public void handlePlayer1Label(MouseEvent event) {
		Player1ResVBox.setVisible(!Player1ResVBox.isVisible());

		Player1ResVBox.getChildren().clear();
		for (ICard c : ((IPlayer) player1ResourceBox.getUserData()).getCards()) {
			ImageView img = new ImageView();
			URL tmpResource = getClass().getResource(
					"/ch/fhnw/sevenwonders/resources/klein/k_" + c.getImageName().replaceAll("jpg", "png"));
			if (tmpResource != null) {
				img.setImage(new Image(tmpResource.toExternalForm()));
				Player1ResVBox.getChildren().add(img);
			} else {
				System.out.println(c.getImageName());
			}
		}
	}

	/**
	 * Toggle-Event für die Karten des Spielers 2
	 * @param event
	 */
	public void handlePlayer2Label(MouseEvent event) {
		Player2ResVBox.setVisible(!Player2ResVBox.isVisible());

		Player2ResVBox.getChildren().clear();
		for (ICard c : ((IPlayer) player2ResourceBox.getUserData()).getCards()) {
			ImageView img = new ImageView();
			URL tmpResource = getClass().getResource(
					"/ch/fhnw/sevenwonders/resources/klein/k_" + c.getImageName().replaceAll("jpg", "png"));
			if (tmpResource != null) {
				img.setImage(new Image(tmpResource.toExternalForm()));
				Player2ResVBox.getChildren().add(img);
			} else {
				System.out.println(c.getImageName());
			}
		}
	}

	/***
	 * Toggle-Event für die Karten des Spielers 3
	 * @param event
	 */
	public void handlePlayer3Label(MouseEvent event) {
		Player3ResVBox.setVisible(!Player3ResVBox.isVisible());

		Player3ResVBox.getChildren().clear();
		for (ICard c : ((IPlayer) player3ResourceBox.getUserData()).getCards()) {
			ImageView img = new ImageView();
			URL tmpResource = getClass().getResource(
					"/ch/fhnw/sevenwonders/resources/klein/k_" + c.getImageName().replaceAll("jpg", "png"));
			if (tmpResource != null) {
				img.setImage(new Image(tmpResource.toExternalForm()));
				Player3ResVBox.getChildren().add(img);
			} else {
				System.out.println(c.getImageName());
			}
		}
	}

	/**
	 * Toggle-Event für die Karten des Spielers 4
	 * @param event
	 */
	public void handlePlayer4Label(MouseEvent event) {
		Player4ResVBox.setVisible(!Player4ResVBox.isVisible());

		Player4ResVBox.getChildren().clear();
		for (ICard c : ((IPlayer) player4ResourceBox.getUserData()).getCards()) {
			ImageView img = new ImageView();
			URL tmpResource = getClass().getResource(
					"/ch/fhnw/sevenwonders/resources/klein/k_" + c.getImageName().replaceAll("jpg", "png"));
			if (tmpResource != null) {
				img.setImage(new Image(tmpResource.toExternalForm()));
				Player4ResVBox.getChildren().add(img);
			} else {
				System.out.println(c.getImageName());
			}
		}
	}

	/**
	 * Toggle-Event für die Karten des Spielers 5
	 * @param event
	 */
	public void handlePlayer5Label(MouseEvent event) {
		Player5ResVBox.setVisible(!Player5ResVBox.isVisible());

		Player5ResVBox.getChildren().clear();
		for (ICard c : ((IPlayer) player5ResourceBox.getUserData()).getCards()) {
			ImageView img = new ImageView();
			URL tmpResource = getClass().getResource(
					"/ch/fhnw/sevenwonders/resources/klein/k_" + c.getImageName().replaceAll("jpg", "png"));
			if (tmpResource != null) {
				img.setImage(new Image(tmpResource.toExternalForm()));
				player5ResourceBox.getChildren().add(img);
			} else {
				System.out.println(c.getImageName());
			}
		}
	}

	/**
	 * Toggle-Event für die Karten des Spielers 6
	 * @param event
	 */
	public void handlePlayer6Label(MouseEvent event) {
		Player6ResVBox.setVisible(!Player6ResVBox.isVisible());

		Player6ResVBox.getChildren().clear();
		for (ICard c : ((IPlayer) player6ResourceBox.getUserData()).getCards()) {
			ImageView img = new ImageView();
			URL tmpResource = getClass().getResource(
					"/ch/fhnw/sevenwonders/resources/klein/k_" + c.getImageName().replaceAll("jpg", "png"));
			if (tmpResource != null) {
				img.setImage(new Image(tmpResource.toExternalForm()));
				Player6ResVBox.getChildren().add(img);
			} else {
				System.out.println(c.getImageName());
			}
		}
	}

	/**
	 * Registrieren der Listener für UI Aktionen bei einkommender Nachricht
	 * 
	 * @param inScene
	 */
	public void setupListener(Scene inScene) {
		this.parentScene = inScene;
		this.model.getLastReceivedMessage().addListener(this.changeListener);
	}

	/**
	 * Event wird beim Auswählen einer Karte ausgeführt. 
	 * @param inEvent
	 */
	@FXML
	public void onToggleCard(MouseEvent inEvent) {

		// Erstellen der Ausgangslage - Alle Buttons deaktiviert und aktuelle Karten angezeigt mit entsprechendem Rahmen
		this.RessourceVerwendenButton.setDisable(true);
		this.ZumBauVerwendenButton.setDisable(true);
		this.UmmunzenButton.setDisable(true);
		updateCardUi();

		// Es hat noch keine neue Runde begonne, es soll keine weitere Karte mehr ausgewählt werden können
		if(model.getPlayer().getHasPlayedCard()) {
			return;
		}
		
		HBox tmpSelectedHBox = (HBox) inEvent.getSource();

		// Setzen der selektieren Karte
		selectedCard = (ICard) tmpSelectedHBox.getUserData();

		// Wird eine Karte geklickt, welche es nicht gibt -> bei default-karten der Fall, soll nichts passieren
		if (selectedCard == null) {
			return;
		}

		// Zusammenstellen der Möglichkeiten was mit der Karte gemacht werden kann
		Platform.runLater(new Runnable() {
			public void run() {
				
				boolean isPlayable = selectedCard.isPlayable(model.getPlayer());
				boolean forBuilding = model.getPlayer().getBoard().canBuild(model.getPlayer());
				
				if (isPlayable && !forBuilding) {
					RessourceVerwendenButton.setDisable(false);
					ZumBauVerwendenButton.setDisable(true);
				}
				if (isPlayable && forBuilding) {
					RessourceVerwendenButton.setDisable(false);
					ZumBauVerwendenButton.setDisable(false);
				}
				if (!isPlayable && forBuilding) {
					RessourceVerwendenButton.setDisable(true);
					ZumBauVerwendenButton.setDisable(false);
				}
				UmmunzenButton.setDisable(false);

				// Oranger Rahmen heisst, Karte ausgewählt.
				tmpSelectedHBox.setStyle("-fx-border-color: orange;-fx-border-width: 5px;");
			}
		});
	}
	
	/**
	 * Changelistener, welcher auf Änderungen der einkommenenden Nachrichten reagiert
	 */
	private ChangeListener<Message> changeListener = new ChangeListener<Message>() {
		@Override
		public void changed(ObservableValue observable, Message oldValue, Message newValue) {
			// Handelt es sich bei der Message um eine Message, welche das Spiel betrifft?
			// Theoretisch koennte hier auch ein Broadcast kommen, welcher dem Client
			// mitteilt, dass eine neue Lobby erstellt wurde. Darauf muss aber nicht
			// reagiert werden.
			if (newValue instanceof ServerGameMessage) {
				ServerGameMessage tmpMessageReceived = (ServerGameMessage) newValue;

				// Setzen des Spielers, welcher vom Server zurueckgegeben wird. Verhindert eine
				// Manipulation auf dem Client.
				model.setPlayer(tmpMessageReceived.getPlayer());

				// Idee falls genug Zeit: Bei einem Success eine Meldung zurï¿½ckgeben, dass auf
				// andere Spieler gewartet wird.
				switch (tmpMessageReceived.getStatusCode()) {
				case ActionNotAvailable:
					throw new IllegalArgumentException("Aktion nicht mï¿½glich");
				case NewRound:
					// Aktualisieren sämtlicher Variablen und UI Komponenten
					updateCardUi();
					updateOpponentsUi();
					break;
				default:
					break;
				}
				return;
			}

			// Handelt es sich um den letzten Zug, kommt vom Server eine Auswertungs-Nachricht zurück
			if(newValue instanceof ServerEvaluationMessage) {
				// Listener entfernen damit nichts mehr ausgeführt wird im Hintergrund
				model.getLastReceivedMessage().removeListener(this);
				
				ServerEvaluationMessage tmpMessageReceived = (ServerEvaluationMessage) newValue;
				
				// Setzen des bereits evaluierten Spielers
				model.setPlayer(tmpMessageReceived.getPlayer());
				
				// Weiterleiten auf die Auswertungs-Übersicht
				Platform.runLater(new Runnable() {
					public void run() {
						try {
							FXMLLoader fxmlLoader = new FXMLLoader(
									getClass().getResource("/ch/fhnw/sevenwonders/view/AuswertungView.fxml"));
							Parent root1 = (Parent) fxmlLoader.load();
							AuswertungController controller = fxmlLoader.<AuswertungController>getController();
							controller.setModel(model);
							Stage stage = new Stage();
							Scene tmpScene = new Scene(root1);
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
	};
}
