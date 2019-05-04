package application;

import java.io.IOException;

import ControllerFXML.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ClientApplicationMain extends Application {
	
	private Stage primaryStage;
	private ClientModel model;
	
	@Override
	public void start(Stage primaryStage) {		
		this.primaryStage = primaryStage;
		this.model = new ClientModel();
		this.model.connect(Config.ServerIP, Config.ServerPort);
		mainWindow();
			
	}
	
	public void mainWindow() {
		try {
			
			//FXML Datei kann direkt im SceneBuilder geöffnet werden
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewFXML/MainView.fxml"));
			AnchorPane pane = loader.load();

			MainViewController controller = loader.<MainViewController>getController();
			controller.setModel(this.model);
			primaryStage.setMinHeight(720.00);
			primaryStage.setMinWidth(1080.00);
			
			//Für jede FXML Datei braucht es einen Controller
			MainViewController mainWindowController = loader.getController();
			mainWindowController.setMain(this);
						
			Scene scene = new Scene(pane);
			mainWindowController.setupListeners(scene);
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
	