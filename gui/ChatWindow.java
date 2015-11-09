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

/**
 * The Class ChatWindow.
 */
public class ChatWindow extends Application {

	/** The textfield to type a message. */
	private TextField input = new TextField();
	
	/** The textarea to display messages send. */
	private TextArea output = new TextArea();
	
	/** Display the stats of the account. */
	private Text account = new Text();
	
	/** The current topic. */
	String topic;
	
	/** The primary stage. */
	private Stage primaryStage;
	
	/** The pseudonym of the account. */
	String pseudo;
	IServer server;
	IClient client;

	/**
	 * Sets the client.
	 *
	 * @param client the new client
	 */
	public void setClient(IClient client) {
		this.client = client;
	}

	/**
	 * Gets the topic.
	 *
	 * @return the topic
	 */
	public String getTopic() {
		return this.topic;
	}

	/**
	 * Sets the server.
	 *
	 * @param server the new server
	 */
	public void setServer(IServer server) {
		this.server = server;
	}

	/**
	 * Sets the topic.
	 *
	 * @param topic the new topic
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * Sets the pseudo.
	 *
	 * @param id the new pseudo
	 */
	public void setPseudo(String id) {
		this.pseudo = id;
	}

	/**
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
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

		Text id = new Text("Bienvenue " + pseudo + " !");
		Text scenetitle = new Text("Sujet : " + topic);
		Text infoText = new Text("créé par " + " le");

		try {
			infoText.setText(
					"créé par " + this.server.getTopic(this.topic).getAuthor() + "\nle " + this.server.getTopic(this.topic).getDate());
			this.account.setText(
					this.pseudo + "\n" + this.server.getAccount(this.pseudo).getNbMsg() + " message(s) au total dont "
							+ this.server.getTopic(this.topic).getClientList().get(this.pseudo) + " dans ce sujet");
		} catch (RuntimeException e) {
			this.account.setText(this.pseudo + "\n" + "0 message(s) au total dont 0 dans ce sujet");
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
					client.removeConnectedTopic();
				} catch (RemoteException e) {
					System.err.println("Exception : (ChatWindow) event button deconnection");
					//e.printStackTrace();
				}
				System.out.println("Logout");
			}
		});

		Button btnBack = new Button();
		btnBack.setText("Retour");
		btnBack.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TopicWindow ft = new TopicWindow();
				try {
					client.removeConnectedTopic();
					client.setTopicWindow(ft);
					topic = "";
					ft.start(primaryStage);
					System.out.println("Go back");
				} catch (RemoteException e) {
					System.err.println("Exception : (ChatWindow) event button back to Topic Window");
					//e.printStackTrace();
				}
			}
		});

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

		Button btnValidate = new Button();
		btnValidate.setText("Valider");
		btnValidate.setOnAction(new EventHandler<ActionEvent>() {

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
		grid.add(btnBack, 5, 1, 1, 1);
		grid.add(this.account, 5, 4, 2, 1);
		grid.add(btnValidate, 5, 9, 1, 1);
	}

	/**
	 * On delete topic.
	 */
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
					client.removeConnectedTopic();
					client.setTopicWindow(ft);
					ft.start(primaryStage);
					ft.removeTopic(topic);
					System.out.println("Go back");
				} catch (RemoteException e) {
					System.err.println("Exception : (ChatWindow) onDeleteTopic : client disconnected");
					//e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Send the message to the server.
	 */
	private void post() {
		try {
			if (!input.getText().equals("")) {
				client.post(input.getText(), topic);
				server.getAccount(pseudo).incrementsNbMsg();
				server.getTopic(topic).refreshNbMsg(pseudo);
				account.setText(pseudo + "\n" + server.getAccount(pseudo).getNbMsg()
						+ " message(s) au total\n dont " + server.getTopic(topic).getClientList().get(pseudo)
						+ " dans ce sujet\n\n");
				input.clear();
			}
		} catch (RemoteException e) {
			System.err.println("Exception : (ChatWindow) post : server offline");
			//e.printStackTrace();
		}
	}

	/**
	 * Display msg.
	 *
	 * @param msg the msg
	 */
	public void displayMsg(String msg) {
		output.appendText(msg);
	}

	/**
	 * Server down.
	 */
	public void serverDown() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {}
		});
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
