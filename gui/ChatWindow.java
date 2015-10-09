package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class ChatWindow extends Application {

	private TextField input;
	private TextArea output; 
	private final static String newline = "\n";
	String topic;
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

		Text scenetitle = new Text("Topic "+ topic + " :");
		Text identifiants = new Text("Bienvenue "+ Identifiants +" !");
		
		
		
		
		Button btnDeco = new Button();
		btnDeco.setText("Déconnexion");
		btnDeco.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				openFenetreConnexion();
			}
			public void openFenetreConnexion(){
				SignInWindow ft = new SignInWindow();
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
						TopicWindow ft = new TopicWindow();
						ft.start(primaryStage);
					}
				});

		
		

		
		
		HBox hbButtons = new HBox();
		hbButtons.getChildren().addAll(identifiants);
		VBox vbButtons = new VBox();
		vbButtons.setSpacing(5);
		vbButtons.getChildren().addAll(btnDeco,btnR);
		
		

		output = new TextArea();
		
		output.setEditable(false);
		output.setStyle("-fx-border-style: none");
		output.setFocusTraversable(false);

		input = new TextField();
		input.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					displayMsg();
				}

			}
		});
		

		Button btn = new Button();
		btn.setText("Valider");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				displayMsg();
			}

		});	
		
		grid.add(scenetitle, 0,1);
		grid.add(hbButtons, 0,0);
		grid.add(vbButtons, 1,0);
		grid.add(output, 0,2);
		grid.add(input,0,3);
		grid.add(btn, 1,3);	
	}


	public void getTopic (String Top, String Name){
		topic = Top;
		Identifiants= Name;
	}
	public static void main(String[] args) {
		launch(args);
	}
	public  void displayMsg(){
		String text = input.getText();
		output.appendText(Identifiants +" : "+text + newline);
		input.selectAll();
		input.clear();
	}


}
