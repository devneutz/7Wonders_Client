package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import ch.fhnw.sevenwonders.interfaces.*;
import ch.fhnw.sevenwonders.messages.Message;
import ch.fhnw.sevenwonders.messages.ServerLobbyMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientModel {
	private Socket socket;
	private IPlayer player;
	private ObservableList<IPlayer> LobbyPlayers;
	private boolean isObserving;

	/**
	 * Wird benötigt für die Listview in der Lobby-Übersicht!
	 * 
	 * @return
	 */
	public ObservableList<IPlayer> getOpponentsList() {
		return this.LobbyPlayers;
	}
	
	public void setPlayer(IPlayer inPlayer) {
		this.player = inPlayer;
	}

	/**
	 * Verbindung zum Server aufbauen
	 * 
	 * @param inIpAddress
	 * @param inPort
	 */
	public void connect(String inIpAddress, int inPort) {
		try {
			LobbyPlayers = FXCollections.observableArrayList();
			socket = new Socket(inIpAddress, inPort);

		} catch (Exception inEx) {

		}
	}
	
	/**
	 * Sendet eine Message an den Server und gibt die Antwort zurück
	 * @param inMessage Die zu sendende Nachricht
	 * @return die Antwort des Servers
	 */
	public Message sendMessageAndWaitForAnswer(Message inMessage) {
		try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); 
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());) {
			out.writeObject(inMessage);
			out.flush();
			// ACHTUNG - Blocking call wenn keine Antwort zurück kommt.
			Message inMessageFromServer = (Message)in.readObject();
			return inMessageFromServer;
		}
		catch(Exception inEx) {
			inEx.printStackTrace();
		}
		return null;
	}

	/**
	 * Wenn eine Lobby erstellt wird, muss diese überwacht werden. Sobald Spieler der Lobby
	 * beitreten oder diese verlassen, wird dies vom Server mitgeteilt und der Client muss entsprechend
	 * reagieren.
	 */
	public void startLobbyObserving() {
		try {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					// 
					while (isObserving) {
						try {
							ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
							Message tmpMessage = (Message)in.readObject();
							if(tmpMessage instanceof ServerLobbyMessage) {
								switch(((ServerLobbyMessage) tmpMessage).getAction()) {
								case PlayerJoined:
									LobbyPlayers.add(((ServerLobbyMessage) tmpMessage).getPlayer());
									break;
								case PlayerLeft:
									LobbyPlayers.remove(((ServerLobbyMessage) tmpMessage).getPlayer());
									break;
								default:
									// Invalid Message
									break;
								}
							}
						}
						catch(Exception inEx) {
							
						}
					}
				}
			};

			Thread t = new Thread(r);
			t.start();

		} catch (Exception inEx) {

		}

	}

	public void stopLobbyObserving() {
		this.isObserving = false;
	}
	
	public void disconnect() {
		if(socket != null) {
			try {
				socket.close();
			}catch(IOException inEx) {
				
			}
		}
	}
}
