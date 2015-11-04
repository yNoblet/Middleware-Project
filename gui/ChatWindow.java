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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ChatWindow extends Application {

	private TextField input = new TextField();
	private TextArea output = new TextArea();
	private Text account = new Text();
	String topic;
	private Stage primaryStage;
	String identifiants;
	IServer server;
	IClient client;

	public void setClient(IClient client) {
		this.client = client;
	}

	public String getTopic() {
		return this.topic;
	}

	public void setServer(IServer server) {
		this.server = server;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setIdentifiants(String id) {
		this.identifiants = id;
	}

	@Override
	public void start(Stage primaryStage) throws RemoteException {

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		Scene scene = new Scene(grid, 750, 400);

		this.primaryStage = primaryStage;
		primaryStage.setTitle("Forum de discussion");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();

		Text id = new Text("Bienvenue " + identifiants + " !");
		Text scenetitle = new Text("Sujet : " + topic);
		Text infoText = new Text("créé par " + " le");

		try {
			infoText.setText(
					"créé par " + this.server.getTopic(this.topic).getAuthor() + "\nle " + this.server.getTopic(this.topic).getDate());
			this.account.setText(
					this.identifiants + "\n" + this.server.getAccount(this.identifiants).getNbMsg() + " message(s) au total dont "
							+ this.server.getTopic(this.topic).getClientList().get(this.identifiants) + " dans ce sujet");
		} catch (RuntimeException e) {
			this.account.setText(this.identifiants + "\n" + "0 message(s) au total dont 0 dans ce sujet");
		}

		this.account.setWrappingWidth(120);
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
		btnR.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TopicWindow ft = new TopicWindow();
				try {
					client.removeConnectedTopic(topic);
					client.setTopicWindow(ft);
					topic = "";
					ft.start(primaryStage);
					System.out.println("Go back");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// HBox hbID = new HBox();
		// hbID.getChildren().addAll(id);

		this.output.setEditable(false);
		this.output.setStyle("-fx-border-style: none");
		this.output.setFocusTraversable(false);

		this.input = new TextField();
		this.input.setOnKeyPressed(new EventHandler<KeyEvent>() {
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

		grid.add(id, 0, 0, 3, 1);
		grid.add(scenetitle, 0, 1, 3, 1);
		grid.add(infoText, 0, 2, 3, 1);
		grid.add(this.output, 0, 4, 4, 4);
		grid.add(this.input, 0, 9, 4, 1);

		grid.add(btnDeco, 5, 0, 1, 1);
		grid.add(btnR, 5, 1, 1, 1);
		grid.add(this.account, 5, 4, 2, 1);
		grid.add(btn, 5, 9, 1, 1);
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
			if (!input.getText().equals("")) {
				client.post(input.getText(), topic);
				server.getAccount(identifiants).addNbMsg(1);
				server.getTopic(topic).addNbMsg(identifiants);
				account.setText(identifiants + "\n" + server.getAccount(identifiants).getNbMsg()
						+ " message(s) au total\n dont " + server.getTopic(topic).getClientList().get(identifiants)
						+ " dans ce sujet\n\n");
				input.clear();
			}
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
				/*Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Erreur de serveur");
				alert.setContentText("Le serveur a été perdu !");
				alert.showAndWait();*/

				/*ServerConfigWindow scw = new ServerConfigWindow();
				scw.start(primaryStage);*/
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

}
