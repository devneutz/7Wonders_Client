package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ch.fhnw.sevenwonders.enums.StatusCode;
import ch.fhnw.sevenwonders.interfaces.*;
import ch.fhnw.sevenwonders.messages.Message;
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

	private ObjectProperty<ObservableList<IPlayer>> LobbyPlayers;
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
		return this.LobbyPlayers;
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

	/**
	 * Verbindung zum Server aufbauen
	 * 
	 * @param inIpAddress
	 * @param inPort
	 */
	public void connect(String inIpAddress, int inPort) {
		try
		{
			LobbyPlayers = new SimpleObjectProperty<ObservableList<IPlayer>>();
			LobbyPlayers.setValue(FXCollections.observableArrayList());
			Lobbies = new SimpleObjectProperty<ObservableList<ILobby>>();
			Lobbies.setValue(FXCollections.observableArrayList());
			this.lastReceivedMessage = new SimpleObjectProperty<Message>();
			socket = new Socket(inIpAddress, inPort);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			this.startMessageObserving();
		} catch (Exception inEx)
		{

		}
	}

	public void sendMessage(Message inMessage) {
		try
		{
			out.writeObject(inMessage);
			out.flush();
		} catch (Exception inEx)
		{
			inEx.printStackTrace();
		}
	}

	private void handlePlayerJoinedMessage(ServerLobbyMessage inMessage) {
		Platform.runLater(new Runnable() {
			public void run() {
				LobbyPlayers.getValue().add(inMessage.getPlayer());
			}
		});
	}

	private void handlePlayerLeftMessage(ServerLobbyMessage inMessage) {
		Platform.runLater(new Runnable() {
			public void run() {
				LobbyPlayers.getValue().remove(inMessage.getPlayer());
			}
		});
	}

	private void handleLobbyCreatedMessage(ServerLobbyMessage inMessage) {
		Platform.runLater(new Runnable() {
			public void run() {
				Lobbies.getValue().add(inMessage.getLobby());
			}
		});
	}

	private void handleLobbyDeletedMessage(ServerLobbyMessage inMessage) {
		Platform.runLater(new Runnable() {
			public void run() {
				Lobbies.getValue().remove(inMessage.getLobby());
			}
		});

		// Ist die gelöschte Lobby diejenige, in der ich mich aktuell befinde? In diesem
		// Fall muss die View über die 'lastReceivedMessage' informiert werden
		if (inMessage.getLobby().getLobbyName() == this.player.getLobby().getLobbyName())
		{
			lastReceivedMessage.setValue(inMessage);
		}
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
		
		if(inMessage.getStatusCode() == StatusCode.Success) {
			this.Lobbies.setValue(FXCollections.observableArrayList(inMessage.getLobbies()));
		}
	}
	
	private void handleDefaultMessges(Message inMessage) {
		lastReceivedMessage.setValue(inMessage);
	}

	private void startMessageObserving() {
		try
		{
			Runnable r = new Runnable() {
				@Override
				public void run() {
					//
					while (true)
					{
						try
						{
							Message tmpMessage = (Message) in.readObject();
							if (tmpMessage instanceof ServerLobbyMessage)
							{
								switch (((ServerLobbyMessage) tmpMessage).getAction())
								{
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
								default:
									handleDefaultMessges(tmpMessage);
									break;
								}
							} 
							else if (tmpMessage instanceof ServerStartupMessage)
							{
								handleStartupMessages((ServerStartupMessage) tmpMessage);
							}
							
						} catch (Exception inEx)
						{

						}
					}
				}
			};

			Thread t = new Thread(r);
			t.setDaemon(true);
			t.start();

		} catch (Exception inEx)
		{

		}

	}

	public void disconnect() {
		if (socket != null)
		{
			try
			{
				socket.close();
			} catch (IOException inEx)
			{

			}
		}
	}
}
