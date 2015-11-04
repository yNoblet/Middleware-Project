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

public class TopicWindow extends Application {

	IClient client;
	IServer server;
	Text identifiants = new Text("Bienvenue test");
	ObservableList<String> subscribedTopics = FXCollections.observableArrayList();
	ObservableList<String> availableTopics = FXCollections.observableArrayList();
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws RemoteException {

		this.primaryStage = primaryStage;

		identifiants.setStyle("-fx-font-size:19px");

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

		Button btnUnsub = new Button();
		btnUnsub.setText("Se désinscrire");
		btnUnsub.setDisable(true);

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
						deleteTopic(listSubscribed, btnGo, btnUnsub, btnDelete);
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

		btnUnsub.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					String title = listSubscribed.getSelectionModel().getSelectedItem();
					client.removeSubscribedTopic(title);
					server.getTopic(title).unsubscribe((client.getPseudo()));
					subscribedTopics.remove(title);
					availableTopics.add(title);
					if (subscribedTopics.isEmpty()) {
						btnGo.setDisable(true);
						btnUnsub.setDisable(true);
						btnDelete.setDisable(true);
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		btnDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				deleteTopic(listSubscribed, btnGo, btnUnsub, btnDelete);
			}
		});

		Button btnInscri = new Button();

		listSubscribed.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (listAvailables.getSelectionModel().getSelectedItem() != null) {
					btnInscri.setDisable(newValue);
				}
			}
		});
		listAvailables.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (listSubscribed.getSelectionModel().getSelectedItem() != null) {
					btnGo.setDisable(newValue);
					btnUnsub.setDisable(newValue);
					btnDelete.setDisable(newValue);
				}
			}
		});

		/**/
		listSubscribed.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				btnGo.setDisable(newValue == null);
				btnUnsub.setDisable(newValue == null);
				btnDelete.setDisable(newValue == null);

			}
		});

		/* Creation des boutons pour les sujets non-inscrits */
		btnInscri.setText("S'inscrire");
		btnInscri.setDisable(true);

		/* Definition des evenements associés pour chacun des boutons */
		btnInscri.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					String title = listAvailables.getSelectionModel().getSelectedItem();
					client.addSubscribedTopic(title);
					server.getTopic(title).subscribe(client.getPseudo());
					subscribedTopics.add(title);
					availableTopics.remove(title);
					if (availableTopics.isEmpty()) {
						btnInscri.setDisable(true);
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		listAvailables.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				btnInscri.setDisable(newValue == null);
			}
		});

		/* Definition de la taille des objets graphiques */
		identifiants.setWrappingWidth(400);
		btnNew.setPrefWidth(420);
		HBox hb = new HBox();
		hb.setPrefWidth(100);
		btnDeco.setPrefWidth(110);
		btnGo.setPrefWidth(100);
		btnDelete.setPrefWidth(100);
		btnInscri.setPrefWidth(100);
		btnDelete.setPrefWidth(110);

		/* Positionnement des éléments dans la grille/fenêtre principale */
		grid.add(identifiants, 0, 0, 2, 1);
		grid.add(hb, 2, 0, 1, 1);
		grid.add(btnDeco, 3, 0, 1, 1);
		grid.add(btnNew, 0, 1, 4, 1);
		grid.add(topicsIns, 0, 2, 2, 1);
		grid.add(listSubscribed, 0, 3, 4, 2);
		grid.add(btnGo, 0, 5, 1, 1);
		grid.add(btnUnsub, 1, 5, 1, 1);
		grid.add(btnDelete, 3, 5, 1, 1);
		grid.add(topicsDispos, 0, 6, 2, 1);
		grid.add(listAvailables, 0, 7, 4, 2);
		grid.add(btnInscri, 0, 9, 1, 1);

	}

	public void setClient(IClient cl) {
		client = cl;
	}

	public void setPseudo(String p) {
		Text t = new Text("Bienvenue " + p);
		t.setStyle("-fx-font-size:19px");
		t.setWrappingWidth(292);
		identifiants = t;
	}

	public void setSubscribedTopic(Collection<String> c) {
		subscribedTopics.addAll(c);
	}

	public void setAvailableTopic(Collection<String> c) {
		availableTopics.addAll(c);
	}

	public void addAvailableTopic(String s) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				availableTopics.add(s);
			}
		});
	}

	public void removeTopic(String s) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				availableTopics.remove(s);
				subscribedTopics.remove(s);
			}
		});
	}

	public void setServer(IServer server) {
		this.server = server;
	}

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

	private void createNewTopic() {
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(primaryStage);
		VBox dialogVbox = new VBox(20);
		dialogVbox.getChildren().add(new Text("Choisissez le titre du nouveau sujet :"));
		TextField topicTitle = new TextField();
		dialogVbox.getChildren().add(topicTitle);
		Button btnNT = new Button();
		dialogVbox.getChildren().add(btnNT);
		btnNT.setText("Créer");
		Scene dialogScene = new Scene(dialogVbox, 300, 120);
		dialog.setScene(dialogScene);
		dialog.show();

		btnNT.setOnAction(new EventHandler<ActionEvent>() {
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

	private void deleteTopic(ListView<String> listeInscrits, Button btnGo, Button btnDes, Button btnSuppr) {
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
					String title = listeInscrits.getSelectionModel().getSelectedItem();
					if (server.getTopic(title).getAuthor().equals(client.getPseudo())) {
						server.deleteTopic(title);
						if (subscribedTopics.isEmpty()) {
							btnGo.setDisable(true);
							btnDes.setDisable(true);
							btnSuppr.setDisable(true);
						}
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Erreur");
						alert.setHeaderText("Erreur de permission");
						alert.setContentText("Seul l'auteur d'un sujet a le droit de le détruire.");
						alert.showAndWait();
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

	private void onEnterNewTopic(String title, Stage dialog) {
		if (title.equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Erreur de nom de sujet");
			alert.setContentText("Vous n'avez pas rentré votre nom de sujet !");
			alert.showAndWait();
		} else {
			try {
				if (server.newTopic(title, client.getPseudo())) {
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}