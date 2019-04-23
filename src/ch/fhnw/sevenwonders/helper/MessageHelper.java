package ch.fhnw.sevenwonders.helper;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import application.Config;
import ch.fhnw.sevenwonders.messages.Message;

public class MessageHelper {
	
	/**
	 * Sendet eine Message an den Server und gibt die Antwort zurück
	 * @param inMessage Die zu sendende Nachricht
	 * @return die Antwort des Servers
	 */
	public static Message sendMessageAndWaitForAnswer(Message inMessage) {
		try (Socket s = new Socket(Config.ServerIP, Config.ServerPort);	
					ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream()); 
				ObjectInputStream in = new ObjectInputStream(s.getInputStream());) {
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
}
