package gui;
import java.rmi.RemoteException;
import java.util.Collection;

import core.IClient;
import core.IServer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class TopicWindow extends Application {
	
	IClient client;
	IServer server;
	Text identifiants = new Text("");
	ObservableList<String> subscribedTopics = FXCollections.observableArrayList();
	ObservableList<String> availableTopics = FXCollections.observableArrayList();

	public void start(Stage primaryStage) throws RemoteException {

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(0);
		grid.setVgap(5);
		grid.setPadding(new Insets(5, 5, 5, 5));

		Scene scene = new Scene(grid, 400, 500);
		primaryStage.setTitle("Forum de discussion");
		primaryStage.setScene(scene);
		primaryStage.show();

		Text TopicsIns = new Text("Topics inscrits :");
		Text TopicsDispos = new Text("Topics disponibles :");

		Button btnNew = new Button();
		btnNew.setText("Nouveau");
		btnNew.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				final Stage dialog = new Stage();
				dialog.initModality(Modality.APPLICATION_MODAL);
				dialog.initOwner(primaryStage);
				VBox dialogVbox = new VBox(20);
				dialogVbox.getChildren().add(new Text("Choisissez le titre du nouveau Topic :"));
				TextField Topic = new TextField();
				dialogVbox.getChildren().add(Topic);
				Button btnNT = new Button();
				dialogVbox.getChildren().add(btnNT);
				btnNT.setText("Créer");
				Scene dialogScene = new Scene(dialogVbox, 300, 120);
				dialog.setScene(dialogScene);
				dialog.show();
				btnNT.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						ChatWindow ft = new ChatWindow();
						try {
							ft.getTopic(Topic.getText(),client.getPseudo());
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ft.start(primaryStage);
						System.out.println("zozo)");
						dialog.close();
					}
				});
			}
		});


		Button btnDeco = new Button();
		btnDeco.setText("Déconnexion");
		btnDeco.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				SignInWindow ft = new SignInWindow();
				ft.setServer(server);
				ft.start(primaryStage);
				System.out.println("Logout");
			}
		});


		ListView<String> ListInscrits = new ListView<String>();
		ListInscrits.setItems(subscribedTopics);
		

		Button btnGo = new Button();
		btnGo.setText("Aller");
		btnGo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ChatWindow ft = new ChatWindow();
				try {
					ft.getTopic(ListInscrits.getSelectionModel().getSelectedItem(), client.getPseudo());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ft.start(primaryStage);
				System.out.println("zozo)");
			}
		});
		
		Button btnDes = new Button();
		btnDes.setText("Se désinscrire");
		btnDes.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					String title = ListInscrits.getSelectionModel().getSelectedItem();
					client.removeSubscribedTopic(title);
					subscribedTopics.remove(title);
					availableTopics.add(title);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		ListView<String> ListDispo = new ListView<String>();
		ListDispo.setItems(availableTopics);


		Button btnInscri = new Button();
		btnInscri.setText("S'inscrire");

		btnInscri.setOnAction(
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						try {
							String title = ListDispo.getSelectionModel().getSelectedItem();
							client.addSubscribedTopic(title);
							subscribedTopics.add(title);
							availableTopics.remove(title);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
		HBox vbButtons = new HBox();
		vbButtons.setSpacing(150);
		vbButtons.getChildren().addAll(identifiants,btnDeco);
		HBox InsButtons = new HBox();
		InsButtons.setSpacing(10);
		InsButtons.getChildren().addAll(btnGo,btnDes);


		grid.add(vbButtons, 0,0);
		grid.add(TopicsIns, 0,1);
		grid.add(ListInscrits, 0,2);
		grid.add(InsButtons, 0,3);
		grid.add(TopicsDispos, 0,4);
	    grid.add(ListDispo, 0,5);
	    grid.add(btnInscri, 0,6);

	}
	public void setClient(IClient cl) {
		client = cl;
	}
	public void setPseudo(String p){
		identifiants = new Text("Bienvenue "+p);
	}
	
	public void setSubscribedTopic(Collection<String> c){
		subscribedTopics.addAll(c);
	}
	public void setAvailableTopic(Collection<String> c){
		availableTopics.addAll(c);
	}
	
	public void setServer(IServer server) {
		this.server = server;
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
}