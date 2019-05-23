package ch.fhnw.sevenwonders.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import ch.fhnw.sevenwonders.application.ClientApplicationMain;
import ch.fhnw.sevenwonders.interfaces.IPlayer;
import ch.fhnw.sevenwonders.model.ClientModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Auswertung des Spiels visualisieren
 * @author joeln
 *
 */
public class AuswertungController implements Initializable {

	private ClientModel model;
	
	public ClientApplicationMain main;
	
	// Spieler-Labels für Anzeige des Namens
	@FXML
	public Label WinnerIDLabel, SecondIDLabel, ThirdIDLabel, FourthIDLabel, FifthIDLabel, SixthIDLabel, SeventhIDLabel ;
	
	// Labels für Anzeige der Coins
	@FXML
	public Label VictoryCoinWinner, VictoryCoinSecond, VictoryCoinThird, VictoryCoinFourth, VictoryCoinFifth, VictoryCoinSixth, VictoryCoinSeventh;
	
	// Totale Anzahl Punkte
	@FXML
	public Label TotalWinner, TotalSecond, TotalThird, TotalFourth, TotalFifth, TotalSixth, TotalSeventh;
	
	// Militärpunkte durch Konflikt Ende des zweiten Zeitalters
	@FXML
	public Label MilitaryWarPointsWinner, MilitaryWarPointsSecond, MilitaryWarPointsThird,
	MilitaryWarPointsFourth, MilitaryWarPointsFifth, MilitaryWarPointsSixth, MilitaryWarPointsSeventh;
	
	// Weltwunder Punkte
	@FXML
	public Label VictoryWonderWinner, VictoryWonderSecond, VictoryWonderThird,
	VictoryWonderFourth, VictoryWonderFifth, VictoryWonderSixth, VictoryWonderSeventh;
	
	// Punkte durch blaue karten
	@FXML
	public Label VictoryDirectWinner, VictoryDirectSecond, VictoryDirectThird,
	VictoryDirectFourth, VictoryDirectFifth, VictoryDirectSixth, VictoryDirectSeventh;
	
	// Punkte durch Forschung (grüne Karten)
	@FXML
	public Label VictoryResearchWinner, VictoryResearchSecond, VictoryResearchThird,
	VictoryResearchFourth, VictoryResearchFifth, VictoryResearchSixth, VictoryResearchSeventh;
	
	
	/**
	 * Zusammenstellung eines Arrays für die Coin Labels, um den Code übersichtlicher zu gestalten. Das Array enthält nur die Labels,
	 * welche wirklich benötigt werden.
	 * @param inPlayerNumber Anzahl der Spieler
	 * @return
	 */
	private Label[] getCoinLabelArray(int inPlayerNumber) {
		Label[] tmpBaseCoinLabelArray = new Label[7];
		tmpBaseCoinLabelArray[0] = VictoryCoinWinner;
		tmpBaseCoinLabelArray[1] = VictoryCoinSecond;
		tmpBaseCoinLabelArray[2] = VictoryCoinThird;
		tmpBaseCoinLabelArray[3] = VictoryCoinFourth;
		tmpBaseCoinLabelArray[4] = VictoryCoinFifth;
		tmpBaseCoinLabelArray[5] = VictoryCoinSixth;
		tmpBaseCoinLabelArray[6] = VictoryCoinSeventh;
		
		Label[] out = new Label[inPlayerNumber];
		for(int i = 0; i < inPlayerNumber; i++) {
		    out[i] = tmpBaseCoinLabelArray[i];
		}
		
		return out;
	}
	
	/**
	 * Zusammenstellung eines Arrays für die Total-Punkte Labels, um den Code übersichtlicher zu gestalten. Das Array enthält nur die Labels,
	 * welche wirklich benötigt werden.
	 * @param inPlayerNumber Anzahl der Spieler
	 * @return
	 */
	private Label[] getTotalLabelArray(int inPlayerNumber) {
		Label[] tmpBaseTotalLabelArray = new Label[7];
		tmpBaseTotalLabelArray[0] = TotalWinner;
		tmpBaseTotalLabelArray[1] = TotalSecond;
		tmpBaseTotalLabelArray[2] = TotalThird;
		tmpBaseTotalLabelArray[3] = TotalFourth;
		tmpBaseTotalLabelArray[4] = TotalFifth;
		tmpBaseTotalLabelArray[5] = TotalSixth;
		tmpBaseTotalLabelArray[6] = TotalSeventh;
		
		Label[] out = new Label[inPlayerNumber];
		for(int i = 0; i < inPlayerNumber; i++) {
		    out[i] = tmpBaseTotalLabelArray[i];
		}
		
		return out;
	}
	
	/**
	 * Zusammenstellung eines Arrays für die Spielernamen Labels, um den Code übersichtlicher zu gestalten. Das Array enthält nur die Labels,
	 * welche wirklich benötigt werden.
	 * @param inPlayerNumber Anzahl der Spieler
	 * @return
	 */
	private Label[] getPlayernameLabelArray(int inPlayerNumber) {
			Label[] tmpBasePlayerLabelArray = new Label[7];
			tmpBasePlayerLabelArray[0] = WinnerIDLabel;
			tmpBasePlayerLabelArray[1] = SecondIDLabel;
			tmpBasePlayerLabelArray[2] = ThirdIDLabel;
			tmpBasePlayerLabelArray[3] = FourthIDLabel;
			tmpBasePlayerLabelArray[4] = FifthIDLabel;
			tmpBasePlayerLabelArray[5] = SixthIDLabel;
			tmpBasePlayerLabelArray[6] = SeventhIDLabel;
			
			Label[] out = new Label[inPlayerNumber];
			for(int i = 0; i < inPlayerNumber; i++) {
			    out[i] = tmpBasePlayerLabelArray[i];
			}
			
			return out;
	}
	
	/**
	 * Zusammenstellung eines Arrays für die Militärpunkte Labels, um den Code übersichtlicher zu gestalten. Das Array enthält nur die Labels,
	 * welche wirklich benötigt werden.
	 * @param inPlayerNumber Anzahl der Spieler
	 * @return
	 */
	private Label[] getMilitaryWarPointsLabelArray(int inPlayerNumber) {
		Label[] tmpBaseMilitaryWarPointsLabelArray = new Label[7];
		tmpBaseMilitaryWarPointsLabelArray[0] = MilitaryWarPointsWinner;
		tmpBaseMilitaryWarPointsLabelArray[1] = MilitaryWarPointsSecond;
		tmpBaseMilitaryWarPointsLabelArray[2] = MilitaryWarPointsThird;
		tmpBaseMilitaryWarPointsLabelArray[3] = MilitaryWarPointsFourth;
		tmpBaseMilitaryWarPointsLabelArray[4] = MilitaryWarPointsFifth;
		tmpBaseMilitaryWarPointsLabelArray[5] = MilitaryWarPointsSixth;
		tmpBaseMilitaryWarPointsLabelArray[6] = MilitaryWarPointsSeventh;
		
		Label[] out = new Label[inPlayerNumber];
		for(int i = 0; i < inPlayerNumber; i++) {
		    out[i] = tmpBaseMilitaryWarPointsLabelArray[i];
		}
		
		return out;
	}
	
	/**
	 * Zusammenstellung eines Arrays für die blauen Karten Labels, um den Code übersichtlicher zu gestalten. Das Array enthält nur die Labels,
	 * welche wirklich benötigt werden.
	 * @param inPlayerNumber Anzahl der Spieler
	 * @return
	 */
	private Label[] getVictoryDirectLabelArray(int inPlayerNumber) {
		Label[] tmpBaseVictoryDirectLabelArray = new Label[7];
		tmpBaseVictoryDirectLabelArray[0] = VictoryDirectWinner;
		tmpBaseVictoryDirectLabelArray[1] = VictoryDirectSecond;
		tmpBaseVictoryDirectLabelArray[2] = VictoryDirectThird;
		tmpBaseVictoryDirectLabelArray[3] = VictoryDirectFourth;
		tmpBaseVictoryDirectLabelArray[4] = VictoryDirectFifth;
		tmpBaseVictoryDirectLabelArray[5] = VictoryDirectSixth;
		tmpBaseVictoryDirectLabelArray[6] = VictoryDirectSeventh;
		
		Label[] out = new Label[inPlayerNumber];
		for(int i = 0; i < inPlayerNumber; i++) {
		    out[i] = tmpBaseVictoryDirectLabelArray[i];
		}
		
		return out;
	}
	
	/**
	 * Zusammenstellung eines Arrays für die Weltwunder Labels, um den Code übersichtlicher zu gestalten. Das Array enthält nur die Labels,
	 * welche wirklich benötigt werden.
	 * @param inPlayerNumber Anzahl der Spieler
	 * @return
	 */
	private Label[] getVictoryWonderLabelArray(int inPlayerNumber) {
		Label[] tmpBaseVictoryWonderLabelArray = new Label[7];
		tmpBaseVictoryWonderLabelArray[0] = VictoryWonderWinner;
		tmpBaseVictoryWonderLabelArray[1] = VictoryWonderSecond;
		tmpBaseVictoryWonderLabelArray[2] = VictoryWonderThird;
		tmpBaseVictoryWonderLabelArray[3] = VictoryWonderFourth;
		tmpBaseVictoryWonderLabelArray[4] = VictoryWonderFifth;
		tmpBaseVictoryWonderLabelArray[5] = VictoryWonderSixth;
		tmpBaseVictoryWonderLabelArray[6] = VictoryWonderSeventh;
		
		Label[] out = new Label[inPlayerNumber];
		for(int i = 0; i < inPlayerNumber; i++) {
		    out[i] = tmpBaseVictoryWonderLabelArray[i];
		}
		
		return out;
	}
	
	/**
	 * Zusammenstellung eines Arrays für die Forschungs-Punkte Labels, um den Code übersichtlicher zu gestalten. Das Array enthält nur die Labels,
	 * welche wirklich benötigt werden.
	 * @param inPlayerNumber Anzahl der Spieler
	 * @return
	 */
	private Label[] getVictoryResearchLabelArray(int inPlayerNumber) {
		Label[] tmpBaseVictoryResearchLabelArray = new Label[7];
		tmpBaseVictoryResearchLabelArray[0] = VictoryResearchWinner;
		tmpBaseVictoryResearchLabelArray[1] = VictoryResearchSecond;
		tmpBaseVictoryResearchLabelArray[2] = VictoryResearchThird;
		tmpBaseVictoryResearchLabelArray[3] = VictoryResearchFourth;
		tmpBaseVictoryResearchLabelArray[4] = VictoryResearchFifth;
		tmpBaseVictoryResearchLabelArray[5] = VictoryResearchSixth;
		tmpBaseVictoryResearchLabelArray[6] = VictoryResearchSeventh;
		
		Label[] out = new Label[inPlayerNumber];
		for(int i = 0; i < inPlayerNumber; i++) {
		    out[i] = tmpBaseVictoryResearchLabelArray[i];
		}
		
		return out;
	}
	
	public void setMain(ClientApplicationMain main) {
		this.main = main;
	}

	public void setModel(ClientModel inModel) {
		this.model = inModel;
		
		// Spieler zusammenstellen und sortieren
		ArrayList<IPlayer> tmpAllPlayers = new ArrayList<IPlayer>();
		tmpAllPlayers.addAll(model.getOpponentsListProperty().getValue());
		tmpAllPlayers.add(model.getPlayer());
		
		Collections.sort(tmpAllPlayers);
		Collections.reverse(tmpAllPlayers);
		
		// Zusammensuchen der Labels
		Label[] tmpTotalArr = getTotalLabelArray(tmpAllPlayers.size());
		Label[] tmpCoinsArr = getCoinLabelArray(tmpAllPlayers.size());
		Label[] tmpPlayerNameArr = getPlayernameLabelArray(tmpAllPlayers.size());
		Label[] tmpWarPointsArr = getMilitaryWarPointsLabelArray(tmpAllPlayers.size());
		Label[] tmpVictoryDirectsArr = getVictoryDirectLabelArray(tmpAllPlayers.size());
		Label[] tmpVictoryWonderArr = getVictoryWonderLabelArray(tmpAllPlayers.size());
		Label[] tmpVictoryResearchArr = getVictoryResearchLabelArray(tmpAllPlayers.size());
		
		// Durchgehen jedes Spielers und setzen der entsprechenden Punkte-Labels
		for(int i = 0; i < tmpAllPlayers.size(); i++) {
			try {
				tmpTotalArr[i].setText(tmpAllPlayers.get(i).evaluate().get("TOTAL").toString());
				tmpTotalArr[i].setVisible(true);
				
				tmpCoinsArr[i].setText(tmpAllPlayers.get(i).evaluate().get("Coins").toString());
				tmpCoinsArr[i].setVisible(true);
				
				// Handelt es sich beim Spieler um den aktuellen Spieler, mache das ersichtlich
				String tmpPlayerName = tmpAllPlayers.get(i).getName();
				if(tmpAllPlayers.get(i).getName().equals(model.getPlayer().getName())) {
					tmpPlayerName +=  " (Du)";
					tmpPlayerNameArr[i].setStyle(tmpPlayerNameArr[i].getStyle() + "-fx-font-style:italic;");
				}
				tmpPlayerNameArr[i].setText(tmpPlayerName);
				
				tmpPlayerNameArr[i].setVisible(true);
				
				tmpWarPointsArr[i].setText(tmpAllPlayers.get(i).evaluate().get("Militärkonflikt").toString());
				tmpWarPointsArr[i].setVisible(true);
				
				tmpVictoryWonderArr[i].setText(tmpAllPlayers.get(i).evaluate().get("Weltwunder").toString());
				tmpVictoryWonderArr[i].setVisible(true);
				
				tmpVictoryDirectsArr[i].setText(tmpAllPlayers.get(i).evaluate().get("Profanbauten").toString());
				tmpVictoryDirectsArr[i].setVisible(true);
				
				tmpVictoryResearchArr[i].setText(tmpAllPlayers.get(i).evaluate().get("Forschungsgebäude").toString());
				tmpVictoryResearchArr[i].setVisible(true);
			}
			catch(NullPointerException inEx) {
				
			}
		}
	}
	
	@FXML
	public void handleQuiteGameButton(MouseEvent event) {
		Platform.exit();
	}

	/**
	 * Weiterleiten auf die Hauptansicht um ein neues Spiel starten zu können.
	 * @param event
	 */
	@FXML
	public void handleNewGameButton(MouseEvent event) {
		Platform.runLater(new Runnable() {
			public void run() {
				try {
					FXMLLoader fxmlLoader = new FXMLLoader(
							getClass().getResource("/ch/fhnw/sevenwonders/view/MainView.fxml"));
					Parent root1 = (Parent) fxmlLoader.load();
					MainViewController controller = fxmlLoader.<MainViewController>getController();
					controller.setModel(model);
					Stage stage = new Stage();
					Scene tmpScene = new Scene(root1);
					controller.setupListeners(tmpScene);
					stage.setScene(tmpScene);
					stage.show();

					((Node) event.getSource()).getScene().getWindow().hide();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}
}
