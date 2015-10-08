package InterfaceMiddleware;


import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class FenetreChat extends Application {

	private TextField input;
	private TextArea output; 
	private final static String newline = "\n";
	String Topic;
	String Identifiants;

	public void start(Stage primaryStage) {

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Scene scene = new Scene(grid, 750, 300);

		primaryStage.setTitle("Forum de discussion");
		primaryStage.setScene(scene);
		primaryStage.show();

		Button btnDeco = new Button();
		btnDeco.setText("Déconnexion");
		btnDeco.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				openFenetreConnexion();
			}
			public void openFenetreConnexion(){
				FenetreConnexion ft = new FenetreConnexion();
				ft.start(primaryStage);
				System.out.println("zozo)");

			}
		});

		Button btnR = new Button();
		btnR.setText("Retour");
		grid.add(btnR, 1,1);
		btnR.setOnAction(
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {

						openFenetreTopic();

					}
					public void openFenetreTopic(){
						FenetreTopic ft = new FenetreTopic();
						ft.start(primaryStage);
					}
				});

		Text scenetitle = new Text("Topic "+ Topic + " :");
		grid.add(scenetitle, 0,1);

		Text identifiants = new Text("Bienvenue "+ Identifiants +" !");
		
		HBox hbButtons = new HBox();
		hbButtons.getChildren().addAll(identifiants);
		VBox vbButtons = new VBox();
		vbButtons.setSpacing(5);
		vbButtons.getChildren().addAll(btnDeco,btnR);
		
		
		grid.add(hbButtons, 0,0);
		grid.add(vbButtons, 1,0);

		output = new TextArea();
		grid.add(output, 0,2);
		output.setEditable(false);
		output.setStyle("-fx-border-style: none");
		output.setFocusTraversable(false);

		input = new TextField();
		input.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					AfficherMsg();
				}

			}
		});
		grid.add(input,0,3);

		Button btn = new Button();
		btn.setText("Valider");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				AfficherMsg();
			}

		});
		grid.add(btn, 1,3);
	}


	public void GetTopic (String Top, String Name){
		Topic = Top;
		Identifiants= Name;
	}
	public static void main(String[] args) {
		launch(args);
	}
	public  void AfficherMsg(){
		String text = input.getText();
		output.appendText(Identifiants +" : "+text + newline);
		input.selectAll();
	}


}
