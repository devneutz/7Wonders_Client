package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private Stage primaryStage;
		
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		mainWindow();
			
	}
	
	public void mainWindow() {
		try {
			
			//FXML Datei kann direkt im SceneBuilder geöffnet werden
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("MainWindow.fxml"));
			AnchorPane pane = loader.load();
			
			primaryStage.setMinHeight(720.00);
			primaryStage.setMinWidth(1080.00);
			
			//Für jede FXML Datei braucht es einen Controller
			MainWindowController mainWindowController = loader.getController();
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
	