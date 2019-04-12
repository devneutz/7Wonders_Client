package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ch.fhnw.sevenwonders.messages.ClientStartupMessage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private Stage primaryStage;
		private Socket socket;
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		/*try {
			socket = new Socket("127.0.0.1", 50000);
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(new ClientStartupMessage("TEST"));
			out.flush();
		}catch(Exception inEx) {
			
		}*/
		mainWindow();
			
	}
	
	public void mainWindow() {
		try {
			
			//FXML Datei kann direkt im SceneBuilder geöffnet werden
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreenFive4Seven.fxml"));
			AnchorPane pane = loader.load();
						
			primaryStage.setMinHeight(720.00);
			primaryStage.setMinWidth(1080.00);
			
			//Für jede FXML Datei braucht es einen Controller
			MainScreenFive4SevenController mainWindowController = loader.getController();
			mainWindowController.setMain(this);
						
			Scene scene = new Scene(pane);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	 
	public static void main(String[] args) {
		launch(args);
	}
}
	