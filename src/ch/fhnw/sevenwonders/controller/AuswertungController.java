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
import javafx.scene.control.Label;
import javafx.stage.Stage;




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
	
	@FXML
	public Label MilitaryWarPointsWinner, MilitaryWarPointsSecond, MilitaryWarPointsThird,
	MilitaryWarPointsFourth, MilitaryWarPointsFifth, MilitaryWarPointsSixth, MilitaryWarPointsSeventh;
	
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
		Collections.reverse(tmpAllPlayers);
		
		Label[] tmpTotalArr = getTotalLabelArray(tmpAllPlayers.size());
		Label[] tmpCoinsArr = getCoinLabelArray(tmpAllPlayers.size());
		Label[] tmpPlayerNameArr = getPlayernameLabelArray(tmpAllPlayers.size());
		Label[] tmpWarPointsArr = getMilitaryWarPointsLabelArray(tmpAllPlayers.size());
		
		for(int i = 0; i < tmpAllPlayers.size(); i++) {
			try {
				tmpTotalArr[i].setText(tmpAllPlayers.get(i).evaluate().get("TOTAL").toString());
				tmpTotalArr[i].setVisible(true);
				tmpCoinsArr[i].setText(tmpAllPlayers.get(i).evaluate().get("Coins").toString());
				tmpCoinsArr[i].setVisible(true);
				tmpPlayerNameArr[i].setText(tmpAllPlayers.get(i).getName());
				tmpPlayerNameArr[i].setVisible(true);
				tmpWarPointsArr[i].setText(tmpAllPlayers.get(i).evaluate().get("Militärkonflikt").toString());
				tmpWarPointsArr[i].setVisible(true);
			}
			catch(NullPointerException inEx) {
				
			}
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
