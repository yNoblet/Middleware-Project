package gui;

import java.rmi.RemoteException;
import java.util.Collection;

import core.IClient;
import core.IServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The Class TopicWindow.
 */
public class TopicWindow extends Application {

	/** The client. */
	IClient client;
	
	/** The server. */
	IServer server;
	
	/** The pseudonym of the client. */
	Text pseudonym = new Text("Bienvenue test");
	
	/** The subscribed topics. */
	ObservableList<String> subscribedTopics = FXCollections.observableArrayList();
	
	/** The available topics. */
	ObservableList<String> availableTopics = FXCollections.observableArrayList();
	
	private Stage primaryStage;

	/**
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws RemoteException {

		this.primaryStage = primaryStage;

		pseudonym.setStyle("-fx-font-size:19px");

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(0);
		grid.setVgap(0);
		grid.setPadding(new Insets(5, 5, 5, 5));

		Scene scene = new Scene(grid, 425, 505);
		primaryStage.setTitle("Forum de discussion");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();

		Text topicsIns = new Text("Sujets inscrits :");
		Text topicsDispos = new Text("Sujets disponibles :");

		ListView<String> listSubscribed = new ListView<String>();
		listSubscribed.setItems(subscribedTopics);
		ListView<String> listAvailables = new ListView<String>();
		listAvailables.setItems(availableTopics);

		/* Creation des boutons pour les sujets inscrits */
		Button btnGo = new Button();
		btnGo.setText("Aller");
		btnGo.setDisable(true);

		Button btnUnsubscribe = new Button();
		btnUnsubscribe.setText("Se désinscrire");
		btnUnsubscribe.setDisable(true);

		Button btnDelete = new Button();
		btnDelete.setText("Détruire");
		btnDelete.setDisable(true);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ADD || event.getCode() == KeyCode.PLUS) {
					createNewTopic();
				} else if (event.getCode() == KeyCode.DELETE) {
					if (listSubscribed.getSelectionModel().getSelectedItem() != null && listSubscribed.isFocused()) {
						deleteTopic(listSubscribed, btnGo, btnUnsubscribe, btnDelete);
					}
				}
			}
		});

		Button btnNew = new Button();
		btnNew.setText("+ Nouveau sujet");
		btnNew.setPrefWidth(400);
		btnNew.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				createNewTopic();
			}
		});

		Button btnDeco = new Button();
		btnDeco.setText("Déconnexion");
		btnDeco.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					server.removeClient(client);
					SignInWindow ft = new SignInWindow();
					ft.setServer(server);
					ft.start(primaryStage);
					System.out.println("Logout");
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});

		/* Definition des evenements associés pour chacun des boutons */
		btnGo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ChatWindow cw = new ChatWindow();
				try {
					client.setChatWindow(cw, listSubscribed.getSelectionModel().getSelectedItem());
					cw.start(primaryStage);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		btnUnsubscribe.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					String topicTitle = listSubscribed.getSelectionModel().getSelectedItem();
					server.unsubscribe(topicTitle, client.getPseudo());
					subscribedTopics.remove(topicTitle);
					availableTopics.add(topicTitle);
					if (subscribedTopics.isEmpty()) {
						btnGo.setDisable(true);
						btnUnsubscribe.setDisable(true);
						btnDelete.setDisable(true);
					}
				} catch (RemoteException e) {
					System.err.println("Exception : (TopicWindow) event button unsubscribe");
					//e.printStackTrace();
				}
			}
		});

		btnDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				deleteTopic(listSubscribed, btnGo, btnUnsubscribe, btnDelete);
			}
		});

		Button btnSubscribe = new Button();

		listSubscribed.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (listAvailables.getSelectionModel().getSelectedItem() != null) {
					btnSubscribe.setDisable(newValue);
				}
			}
		});
		listAvailables.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (listSubscribed.getSelectionModel().getSelectedItem() != null) {
					btnGo.setDisable(newValue);
					btnUnsubscribe.setDisable(newValue);
					btnDelete.setDisable(newValue);
				}
			}
		});

		listSubscribed.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				btnGo.setDisable(newValue == null);
				btnUnsubscribe.setDisable(newValue == null);
				btnDelete.setDisable(newValue == null);

			}
		});

		/* Creation des boutons pour les sujets non-inscrits */
		btnSubscribe.setText("S'inscrire");
		btnSubscribe.setDisable(true);

		/* Definition des evenements associés pour chacun des boutons */
		btnSubscribe.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					String topicTitle = listAvailables.getSelectionModel().getSelectedItem();
					//client.addSubscribedTopic(topicTitle);
					//server.getTopic(title).subscribe(client.getPseudo());
					server.subscribe(topicTitle, client.getPseudo());
					subscribedTopics.add(topicTitle);
					availableTopics.remove(topicTitle);
					if (availableTopics.isEmpty()) {
						btnSubscribe.setDisable(true);
					}
				} catch (RemoteException e) {
					System.err.println("Exception : (TopicWindow) event button subscribe");
					//e.printStackTrace();
				}

			}
		});

		listAvailables.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				btnSubscribe.setDisable(newValue == null);
			}
		});

		/* Definition de la taille des objets graphiques */
		pseudonym.setWrappingWidth(400);
		btnNew.setPrefWidth(420);
		HBox hb = new HBox();
		hb.setPrefWidth(100);
		btnDeco.setPrefWidth(110);
		btnGo.setPrefWidth(100);
		btnDelete.setPrefWidth(100);
		btnSubscribe.setPrefWidth(100);
		btnDelete.setPrefWidth(110);

		/* Positionnement des éléments dans la grille/fenêtre principale */
		grid.add(pseudonym, 0, 0, 2, 1);
		grid.add(hb, 2, 0, 1, 1);
		grid.add(btnDeco, 3, 0, 1, 1);
		grid.add(btnNew, 0, 1, 4, 1);
		grid.add(topicsIns, 0, 2, 2, 1);
		grid.add(listSubscribed, 0, 3, 4, 2);
		grid.add(btnGo, 0, 5, 1, 1);
		grid.add(btnUnsubscribe, 1, 5, 1, 1);
		grid.add(btnDelete, 3, 5, 1, 1);
		grid.add(topicsDispos, 0, 6, 2, 1);
		grid.add(listAvailables, 0, 7, 4, 2);
		grid.add(btnSubscribe, 0, 9, 1, 1);

	}

	/**
	 * Sets the client.
	 *
	 * @param cl the new client
	 */
	public void setClient(IClient cl) {
		client = cl;
	}

	/**
	 * Sets the pseudo.
	 *
	 * @param p the new pseudo
	 */
	public void setPseudo(String p) {
		Text t = new Text("Bienvenue " + p);
		t.setStyle("-fx-font-size:19px");
		t.setWrappingWidth(292);
		pseudonym = t;
	}

	/**
	 * Sets the subscribed topic.
	 *
	 * @param c the new subscribed topic
	 */
	public void setSubscribedTopic(Collection<String> c) {
		subscribedTopics.addAll(c);
	}

	/**
	 * Sets the available topic.
	 *
	 * @param c the new available topic
	 */
	public void setAvailableTopic(Collection<String> c) {
		availableTopics.addAll(c);
	}

	/**
	 * Adds the available topic.
	 *
	 * @param s the s
	 */
	public void addAvailableTopic(String s) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				availableTopics.add(s);
			}
		});
	}

	/**
	 * Removes the topic.
	 *
	 * @param s the s
	 */
	public void removeTopic(String s) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				availableTopics.remove(s);
				subscribedTopics.remove(s);
			}
		});
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
	 * Server down.
	 */
	public void serverDown() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Erreur de serveur");
				alert.setContentText("Le serveur a été perdu !");
				alert.showAndWait();

				ServerConfigWindow scw = new ServerConfigWindow();
				scw.start(primaryStage);
			}
		});
	}

	/**
	 * Creates the new topic.
	 */
	private void createNewTopic() {
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(primaryStage);
		VBox dialogVbox = new VBox(20);
		dialogVbox.getChildren().add(new Text("Choisissez le titre du nouveau sujet :"));
		TextField topicTitle = new TextField();
		dialogVbox.getChildren().add(topicTitle);
		Button btnNewTopic = new Button();
		dialogVbox.getChildren().add(btnNewTopic);
		btnNewTopic.setText("Créer");
		Scene dialogScene = new Scene(dialogVbox, 300, 120);
		dialog.setScene(dialogScene);
		dialog.show();

		btnNewTopic.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onEnterNewTopic(topicTitle.getText(), dialog);
			}
		});

		topicTitle.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onEnterNewTopic(topicTitle.getText(), dialog);
				}
			}
		});
	}

	/**
	 * Delete topic.
	 *
	 * @param listSubscribed the list of subscribed topics
	 * @param btnGo the button to go to the topic
	 * @param btnUnsub the button to unsubscribe from a topic
	 * @param btnDelete the button to delete a topic
	 */
	private void deleteTopic(ListView<String> listSubscribed, Button btnGo, Button btnUnsub, Button btnDelete) {
		final Stage dialog = new Stage();
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 20, 20, 20));
		Scene scene = new Scene(grid, 350, 100);
		dialog.setTitle("Destruction d'un sujet");
		dialog.setScene(scene);
		dialog.setResizable(false);
		dialog.show();
		Text question = new Text("Êtes-vous sûr de vouloir supprimer ce sujet ?");
		Button btnYes = new Button();
		btnYes.setText("Oui");
		Button btnNo = new Button();
		btnNo.setText("Non");
		grid.add(question, 1, 0, 2, 1);
		grid.add(btnYes, 3, 1, 1, 1);
		grid.add(btnNo, 4, 1, 1, 1);

		btnYes.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					String title = listSubscribed.getSelectionModel().getSelectedItem();
					if (server.getTopic(title).getAuthor().equals(client.getPseudo())) {
						server.onDeleteTopic(title);
						if (subscribedTopics.isEmpty()) {
							btnGo.setDisable(true);
							btnUnsub.setDisable(true);
							btnDelete.setDisable(true);
						}
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Erreur");
						alert.setHeaderText("Erreur de permission");
						alert.setContentText("Seul l'auteur d'un sujet a le droit de le détruire.");
						alert.showAndWait();
					}
				} catch (RemoteException e) {
					System.err.println("Exception : (TopicWindow) deleteTopic : server offline");
					//e.printStackTrace();
				}
				dialog.close();
			}
		});

		btnNo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dialog.close();
			}
		});
	}

	/**
	 * On enter new topic.
	 *
	 * @param title the title of the topic
	 * @param dialog the window displayed
	 */
	private void onEnterNewTopic(String title, Stage dialog) {
		if (title.equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Erreur de nom de sujet");
			alert.setContentText("Vous n'avez pas rentré votre nom de sujet !");
			alert.showAndWait();
		} else {
			try {
				if (server.onCreateNewTopic(title, client.getPseudo())) {
					dialog.close();
					subscribedTopics.add(title);
					availableTopics.remove(title);
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Erreur");
					alert.setHeaderText("Erreur de nom de sujet");
					alert.setContentText("Un sujet du même nom existe déja !");
					alert.showAndWait();
				}
			} catch (RemoteException e) {
				System.err.println("Exception : (TopicWindow) onEnterNewTopic : server offline");
				//e.printStackTrace();
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