package core;

import gui.ServerConfigWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class StartClient extends Application{
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		ServerConfigWindow scw = new ServerConfigWindow();
		scw.start(primaryStage);

	}
}