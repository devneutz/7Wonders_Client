package ch.fhnw.sevenwonders.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;

import ch.fhnw.sevenwonders.enums.StatusCode;
import ch.fhnw.sevenwonders.interfaces.*;
import ch.fhnw.sevenwonders.messages.Message;
import ch.fhnw.sevenwonders.messages.ServerEvaluationMessage;
import ch.fhnw.sevenwonders.messages.ServerGameMessage;
import ch.fhnw.sevenwonders.messages.ServerLobbyMessage;
import ch.fhnw.sevenwonders.messages.ServerStartupMessage;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientModel {
	private Socket socket;
	private IPlayer player;

	private ObjectProperty<ObservableList<IPlayer>> Opponents;
	private ObjectProperty<ObservableList<ILobby>> Lobbies;
	private ObjectProperty<Message> lastReceivedMessage;

	private ObjectOutputStream out;
	private ObjectInputStream in;

	/**
	 * Wird benötigt für die Listview in der Lobby-Übersicht!
	 * 
	 * @return
	 */
	public ObjectProperty<ObservableList<IPlayer>> getOpponentsListProperty() {
		return this.Opponents;
	}

	public void setPlayer(IPlayer inPlayer) {
		this.player = inPlayer;
	}

	public IPlayer getPlayer() {
		return this.player;
	}

	public ObjectProperty<ObservableList<ILobby>> getLobbyListProperty() {
		return this.Lobbies;
	}

	public ObjectProperty<Message> getLastReceivedMessage() {
		return this.lastReceivedMessage;
	}
	
	public boolean isPlayerInAnyLobby() {
		if(this.player == null) {return false;}
		
		return this.player.getLobby() != null;
	}

	/**
	 * Verbindung zum Server aufbauen
	 * 
	 * @param inIpAddress
	 * @param inPort
	 */
	public void connect(String inIpAddress, int inPort) {
		try {
			Opponents = new SimpleObjectProperty<ObservableList<IPlayer>>();
			Opponents.setValue(FXCollections.observableArrayList());
			Lobbies = new SimpleObjectProperty<ObservableList<ILobby>>();
			Lobbies.setValue(FXCollections.observableArrayList());
			this.lastReceivedMessage = new SimpleObjectProperty<Message>();
			socket = new Socket(inIpAddress, inPort);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			this.startMessageObserving();
		} catch (Exception inEx) {
			inEx.printStackTrace();
		}
	}

	public void sendMessage(Message inMessage) {
		try {
			out.reset();
			out.writeObject(inMessage);
			out.flush();			
		} catch (Exception inEx) {
			inEx.printStackTrace();
		}
	}

	private void handlePlayerJoinedMessage(ServerLobbyMessage inMessage) {
		
		if(isPlayerInAnyLobby() && inMessage.getLobby().getLobbyName().equals(this.player.getLobby().getLobbyName())) {
			Platform.runLater(new Runnable() {
				public void run() {
					Opponents.getValue().clear();
					Opponents.getValue().addAll(inMessage.getLobby().getLobbyPlayers());
				}
			});			
		}
		if(inMessage.getLobby().getLobbyMaster().getName().equals(this.player.getName())) {
			this.lastReceivedMessage.setValue(inMessage);
		}
	}

	private void handlePlayerLeftMessage(ServerLobbyMessage inMessage) {
		// Bin ich es, der die Lobby verlässt? Dann muss noch etwas ausgeführt werden
		if(inMessage.getPlayer().getName().equals(this.player.getName())) {
			this.lastReceivedMessage.setValue(inMessage);
		}

		if(isPlayerInAnyLobby() && inMessage.getLobby().getLobbyName().equals(this.player.getLobby().getLobbyName())) {
			Platform.runLater(new Runnable() {
				public void run() {
					Opponents.getValue().clear();
					Opponents.getValue().addAll(inMessage.getLobby().getLobbyPlayers());
				}
			});
		}
	}

	private void handleLobbyCreatedMessage(ServerLobbyMessage inMessage) {
			Platform.runLater(new Runnable() {
				public void run() {
					Lobbies.getValue().add(inMessage.getLobby());

					if(isPlayerInAnyLobby() && inMessage.getLobby().getLobbyName().equals(inMessage.getLobby().getLobbyName())) {
						Opponents.getValue().clear();
						Opponents.getValue().addAll(inMessage.getLobby().getLobbyPlayers());
					}
				}
			});
	}

	private void handleLobbyDeletedMessage(ServerLobbyMessage inMessage) {
		if(isPlayerInAnyLobby() && inMessage.getLobby().getLobbyName().equals(player.getLobby().getLobbyName())) {
			this.lastReceivedMessage.setValue(inMessage);
		}
		
		Platform.runLater(new Runnable() {
			public void run() {
				Iterator<ILobby> iter = Lobbies.getValue().iterator();
				while (iter.hasNext()) {
					ILobby L = iter.next();
					if (L.getLobbyName().equals(inMessage.getLobby().getLobbyName())) {
						iter.remove();
					}
				}
			}
		});
	}
	
	private void handleLobbyStartedMessage(ServerLobbyMessage inMessage) {
		Platform.runLater(new Runnable() {
			public void run() {
				Opponents.getValue().clear();
				Opponents.getValue().addAll(inMessage.getOpponents());
			}
		});
		this.lastReceivedMessage.setValue(inMessage);
	}

	/**
	 * Beim Startup handelt es sich um Login und Registrierungsprozesse. Diese
	 * werden in den Controllern durch entsprechende Listener abgehandelt, da darauf
	 * zumeist eine UI Interaktion stattfindet.
	 * 
	 * @param inMessage
	 */
	private void handleStartupMessages(ServerStartupMessage inMessage) {
		// Die letzte erhaltene Message wird gesetzt sprich, das Handling wird über den
		// Listener gesteuert.
		lastReceivedMessage.setValue(inMessage);

		if (inMessage.getStatusCode() == StatusCode.Success) {
			this.Lobbies.setValue(FXCollections.observableArrayList(inMessage.getLobbies()));
		}
	}
	
	private void handleJoinLobby(ServerLobbyMessage inMessage) {
		lastReceivedMessage.setValue(inMessage);
		Opponents.getValue().clear();
		Opponents.getValue().addAll(inMessage.getLobby().getLobbyPlayers());
	}

	private void handleDefaultMessages(Message inMessage) {
		lastReceivedMessage.setValue(inMessage);
	}

	private void startMessageObserving() {
		try {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					//
					while (true) {
						try {
							Message tmpMessage = (Message) in.readObject();
							if (tmpMessage instanceof ServerLobbyMessage) {
								switch (((ServerLobbyMessage) tmpMessage).getAction()) {
								case PlayerJoined:
									handlePlayerJoinedMessage((ServerLobbyMessage) tmpMessage);
									break;
								case PlayerLeft:
									handlePlayerLeftMessage((ServerLobbyMessage) tmpMessage);
									break;
								case LobbyCreated:
									handleLobbyCreatedMessage((ServerLobbyMessage) tmpMessage);
									break;
								case LobbyDeleted:
									handleLobbyDeletedMessage((ServerLobbyMessage) tmpMessage);
									break;
								case JoinLobby:
									handleJoinLobby((ServerLobbyMessage)tmpMessage);
									break;
								case LobbyStarted:
									handleLobbyStartedMessage((ServerLobbyMessage) tmpMessage);
									break;				
								default:
									handleDefaultMessages((ServerLobbyMessage)tmpMessage);
									break; 
								}
							} else if (tmpMessage instanceof ServerStartupMessage) {
								handleStartupMessages((ServerStartupMessage) tmpMessage);
							}
							else if(tmpMessage instanceof ServerGameMessage && ((ServerGameMessage) tmpMessage).getStatusCode() == StatusCode.NewRound) {
								Opponents.getValue().clear();
								Opponents.getValue().addAll(((ServerGameMessage)tmpMessage).getOpponents());
								handleDefaultMessages(tmpMessage);
							}
							else if(tmpMessage instanceof ServerEvaluationMessage) {
								Opponents.getValue().clear();
								Opponents.getValue().addAll(((ServerEvaluationMessage)tmpMessage).getOpponents());
								handleDefaultMessages(tmpMessage);
							}
							else {
								handleDefaultMessages(tmpMessage);
							}
							
						} catch (Exception inEx)
						{
							inEx.printStackTrace();
						}
					}
				}
			};

			Thread t = new Thread(r);
			t.setDaemon(true);
			t.start();

		} catch (Exception inEx) {
			inEx.printStackTrace();
		}

	}

	public void disconnect() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException inEx) {
				inEx.printStackTrace();
			}
		}
	}
}
