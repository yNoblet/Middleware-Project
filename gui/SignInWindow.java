package gui;
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

public class SignInWindow extends Application {
	TextField login = new TextField();
	
	@Override
	public void start(Stage primaryStage) {
		
		login.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					if (login.getText().equals("")){
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Erreur");
						alert.setHeaderText("Erreur d'identifiant");
						alert.setContentText("Vous n'avez pas rentré votre identifiant !");
						alert.showAndWait();	
					}
					else {
						TopicWindow ft = new TopicWindow();
						ft.getID(login.getText());
						ft.start(primaryStage);
					}
				}
			}
		});
	
		Button btn = new Button();
		btn.setText("Valider");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (login.getText().equals("")){
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Erreur");
					alert.setHeaderText("Erreur d'identifiant");
					alert.setContentText("Vous n'avez pas rentré votre identifiant !");
					alert.showAndWait();	
				}
				else {
					TopicWindow ft = new TopicWindow();
					ft.getID(login.getText());
					ft.start(primaryStage);
				}	
			}
		});

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.getStylesheets().add("GUI/style.css");

		Scene scene = new Scene(grid, 350, 150);

		primaryStage.setTitle("Forum de discussion");
		primaryStage.setScene(scene);
		primaryStage.show();

		Text scenetitle = new Text("Bienvenue");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 1, 0, 2, 1);

		Label userName = new Label("Identifiant:");
		grid.add(userName, 0, 1);
		grid.add(login, 1, 1);
		grid.add(btn, 1, 4);

	}
	public static void main(String[] args) {
		launch(args);
	}

}
