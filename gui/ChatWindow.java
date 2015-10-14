package gui;

import java.rmi.RemoteException;

import core.IClient;
import core.IServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

	private TextField input = new TextField();
	private TextArea output = new TextArea();
	String topic;
	private Stage primaryStage;
	String Identifiants;
	IServer server;
	IClient client;

	public void setClient(IClient client) {
		this.client = client;
	}

	public String getTopic() {
		return topic;
	}

	public void setServer(IServer server) {
		this.server = server;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setIdentifiants(String identifiants) {
		Identifiants = identifiants;
	}

	@Override
	public void start(Stage primaryStage) {

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		Scene scene = new Scene(grid, 750, 300);

		this.primaryStage = primaryStage;
		primaryStage.setTitle("Forum de discussion");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();

		Text scenetitle = new Text("Topic " + topic + " :");
		Text identifiants = new Text("Bienvenue " + Identifiants + " !");

		Button btnDeco = new Button();
		btnDeco.setText("Déconnexion");
		btnDeco.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				SignInWindow ft = new SignInWindow();
				ft.setServer(server);
				ft.start(primaryStage);
				try {
					server.removeClient(client);
					client.removeConnectedTopic(topic);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Logout");
			}
		});

		Button btnR = new Button();
		btnR.setText("Retour");
		grid.add(btnR, 1, 1);
		btnR.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TopicWindow ft = new TopicWindow();
				try {
					client.removeConnectedTopic(topic);
					client.setTopicWindow(ft);
					ft.start(primaryStage);
					System.out.println("Go back");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		HBox hbButtons = new HBox();
		hbButtons.getChildren().addAll(identifiants);
		VBox vbButtons = new VBox();
		vbButtons.setSpacing(5);
		vbButtons.getChildren().addAll(btnDeco, btnR);

		output.setEditable(false);
		output.setStyle("-fx-border-style: none");
		output.setFocusTraversable(false);

		input = new TextField();
		input.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					post();
				}

			}
		});

		Button btn = new Button();
		btn.setText("Valider");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				post();
			}

		});

		grid.add(scenetitle, 0, 1);
		grid.add(hbButtons, 0, 0);
		grid.add(vbButtons, 1, 0);
		grid.add(output, 0, 2);
		grid.add(input, 0, 3);
		grid.add(btn, 1, 3);
	}

	public void onDeleteTopic() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Sujet Supprimé");
				alert.setContentText(
						"Le sujet a été supprimé par un autre utilisateur !\nRetour sur la page précédente.");
				alert.showAndWait();
				System.out.println("c");
				TopicWindow ft = new TopicWindow();
				try {
					client.removeConnectedTopic(topic);
					client.setTopicWindow(ft);
					ft.start(primaryStage);
					System.out.println("Go back");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void post() {
		try {
			client.post(input.getText(), topic);
			input.clear();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void displayMsg(String msg) {
		output.appendText(msg);
	}

	public void serverDown() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Erreur de serveur");
				alert.setContentText("Le serveur a été perdu!");
				alert.showAndWait();

				ServerConfigWindow scw = new ServerConfigWindow();
				scw.start(primaryStage);
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

}
