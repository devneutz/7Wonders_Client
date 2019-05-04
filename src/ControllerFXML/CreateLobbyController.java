package ControllerFXML;

import application.ClientApplicationMain;
import application.ClientModel;
import ch.fhnw.sevenwonders.enums.LobbyAction;
import ch.fhnw.sevenwonders.enums.StatusCode;
import ch.fhnw.sevenwonders.messages.ClientLobbyMessage;
import ch.fhnw.sevenwonders.messages.ServerLobbyMessage;
import ch.fhnw.sevenwonders.models.Lobby;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author Matteo
 *
 */

public class CreateLobbyController {

	public ClientApplicationMain main;

	private ClientModel model;

	@FXML
	private TextField NumberOfPlayerTextField;
	@FXML
	private Button LessPlayerButton;
	@FXML
	private TextField CountOfPlayersTextField;
	@FXML
	private Button MorePlayerButton;
	@FXML
	private TextField EnterLobbynameTextField;
	@FXML
	private Button createLobbyOkButton;
	@FXML
	private Button createLobbyCancelButton;

	public void setMain(ClientApplicationMain main) {
		this.main = main;
	}

	public void setModel(ClientModel inModel) {
		this.model = inModel;
	}

	public void handleCreateLobbyCancelButton(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ViewFXML/LobbyView.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			LobbyViewController controller = fxmlLoader.<LobbyViewController>getController();
			controller.setModel(model);
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.show();

			((Node) event.getSource()).getScene().getWindow().hide();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void handleCreateLobbyOkButton(ActionEvent event) {
		ClientLobbyMessage msg = new ClientLobbyMessage(LobbyAction.CreateLobby);

		Lobby lobby = new Lobby();
		String lobbyName = EnterLobbynameTextField.getText();
		lobby.setLobbyName(lobbyName);

		int numPlayers = Integer.parseInt(CountOfPlayersTextField.getText());
		lobby.setNumPlayers(numPlayers);

		msg.setLobby(lobby);

		msg.setPlayer(model.getPlayer());

		model.sendMessage(msg);
	}

	public void handleLessPlayerButton(ActionEvent event) {
		MorePlayerButton.setDisable(false);
		int tempPlayers = Integer.parseInt(CountOfPlayersTextField.getText());
		if (tempPlayers > 3) {
			tempPlayers--;
			String numPlayers = Integer.toString(tempPlayers);
			CountOfPlayersTextField.setText(numPlayers);
			if (tempPlayers == 3) {
				LessPlayerButton.setDisable(true);
			}
		}
	}

	public void handleMorePlayerButton(ActionEvent event) {
		LessPlayerButton.setDisable(false);
		int tempPlayers = Integer.parseInt(CountOfPlayersTextField.getText());
		if (tempPlayers < 7) {
			tempPlayers++;
			String numPlayers = Integer.toString(tempPlayers);
			CountOfPlayersTextField.setText(numPlayers);
			if (tempPlayers == 7) {
				MorePlayerButton.setDisable(true);
			}
		}
	}

	public void setupListener(Scene inScene) {
		this.model.getLastReceivedMessage().addListener((observable, oldvalue, newValue) -> {
			if (newValue instanceof ServerLobbyMessage) {
				newValue = (ServerLobbyMessage) newValue;
				if (((ServerLobbyMessage) newValue).getStatusCode() == StatusCode.Success) {
					Platform.runLater(new Runnable() {
						public void run() {
							try {
								FXMLLoader fxmlLoader = new FXMLLoader(
										getClass().getResource("/ViewFXML/AdminInLobbyView.fxml"));
								Parent root1 = (Parent) fxmlLoader.load();
								AdminInLobbyViewController controller = fxmlLoader
										.<AdminInLobbyViewController>getController();
								controller.setModel(model);
								Stage stage = new Stage();
								stage.setScene(new Scene(root1));
								stage.show();

								inScene.getWindow().hide();

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});
	}
}
