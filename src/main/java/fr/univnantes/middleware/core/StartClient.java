package fr.univnantes.middleware.core;

import fr.univnantes.middleware.gui.ServerConfigWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The Class StartClient.
 */
public class StartClient extends Application{
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		ServerConfigWindow scw = new ServerConfigWindow();
		scw.start(primaryStage);

	}
}