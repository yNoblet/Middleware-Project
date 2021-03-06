package fr.univnantes.middleware.gui;

import java.rmi.RemoteException;

import fr.univnantes.middleware.core.Client;
import fr.univnantes.middleware.core.IClient;
import fr.univnantes.middleware.core.IServer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The Class SignInWindow.
 */
public class SignInWindow extends Application {
	
	/** The login. */
	TextField login;
	
	/** The server chosen. */
	IServer server;
	
	private Stage primaryStage;
	
	/**
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage=primaryStage;
		login= new TextField();
		
		login.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onEnter();
				}
			}
		});
	
		Button btnValidate = new Button();
		btnValidate.setText("Valider");
		btnValidate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onEnter();
			}
		});
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.getStylesheets().add("style.css");

		Scene scene = new Scene(grid, 350, 150);

		primaryStage.setTitle("Forum de discussion");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();

		Text scenetitle = new Text("Bienvenue");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 1, 0, 2, 1);

		Label userName = new Label("Identifiant:");
		grid.add(userName, 0, 1);
		grid.add(login, 1, 1);
		grid.add(btnValidate, 1, 4);

	}
	
	/**
	 * Sets the server.
	 *
	 * @param s the new server
	 */
	public void setServer(IServer s){
		server=s;
	}
	
	/**
	 * On enter.
	 */
	private void onEnter(){
		if (login.getText().equals("")){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Erreur d'identifiant");
			alert.setContentText("Vous n'avez pas rentré votre identifiant !");
			alert.showAndWait();	
		}
		else {
			TopicWindow ft = new TopicWindow();
			try {
				IClient cl = new Client(login.getText(), server);
				cl.setTopicWindow(ft);
				ft.start(primaryStage);
			} catch (RemoteException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Erreur de serveur");
				alert.setContentText("Le serveur n'a pu être joint!");
				alert.showAndWait();
				
				ServerConfigWindow scw = new ServerConfigWindow();
				scw.start(primaryStage);
			}
		}
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
