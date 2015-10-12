package gui;

import java.rmi.RemoteException;
import java.util.Collection;

import core.IClient;
import core.IServer;
import javafx.application.Application;
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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;


public class TopicWindow extends Application {
	
	IClient client;
	IServer server;
	Text identifiants = new Text("Bienvenue test");
	ObservableList<String> subscribedTopics = FXCollections.observableArrayList();
	ObservableList<String> availableTopics = FXCollections.observableArrayList();
	private Stage primaryStage;

	public void start(Stage primaryStage) throws RemoteException {

		this.primaryStage=primaryStage;
		
		identifiants.setStyle("-fx-font-size:19px"); 
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(0);
		grid.setVgap(0);
		grid.setPadding(new Insets(0, 0, 0, 0));

		Scene scene = new Scene(grid, 400, 500);
		primaryStage.setTitle("Forum de discussion");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();

		Text TopicsIns = new Text("Sujets inscrits :");
		Text TopicsDispos = new Text("Sujets disponibles :");
		
		Button btnNew = new Button();
		btnNew.setText("+ Nouveau sujet");
		btnNew.setPrefWidth(400);
		btnNew.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				final Stage dialog = new Stage();
				dialog.initModality(Modality.APPLICATION_MODAL);
				dialog.initOwner(primaryStage);
				VBox dialogVbox = new VBox(20);
				dialogVbox.getChildren().add(new Text("Choisissez le titre du nouveau sujet :"));
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
						onEnterNewTopic(Topic.getText(), dialog);
					}
				});
				
				Topic.setOnKeyPressed(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent event) {
						if (event.getCode() == KeyCode.ENTER) 
							onEnterNewTopic(Topic.getText(), dialog);
					}
				});
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


		ListView<String> ListInscrits = new ListView<String>();
		ListInscrits.setItems(subscribedTopics);
		

		Button btnGo = new Button();
		btnGo.setText("Aller");
		btnGo.setDisable(true);
		btnGo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ChatWindow cw = new ChatWindow();
				try {
					client.setChatWindow(cw, ListInscrits.getSelectionModel().getSelectedItem());
					cw.start(primaryStage);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		Button btnInscri = new Button();
		btnInscri.setText("S'inscrire");
		btnInscri.setDisable(true);
		
		Button btnDes = new Button();
		btnDes.setText("Se désinscrire");
		btnDes.setDisable(true);
		
		btnDes.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					String title = ListInscrits.getSelectionModel().getSelectedItem();
					client.removeSubscribedTopic(title);
					subscribedTopics.remove(title);
					availableTopics.add(title);
					if (subscribedTopics.isEmpty()){
						btnGo.setDisable(true);
						btnDes.setDisable(true);
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		ListInscrits.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				btnGo.setDisable(newValue==null);
				btnDes.setDisable(newValue==null);
			}
		});

		ListView<String> ListDispo = new ListView<String>();
		ListDispo.setItems(availableTopics);

		btnInscri.setOnAction(
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						try {
							String title = ListDispo.getSelectionModel().getSelectedItem();
							client.addSubscribedTopic(title);
							subscribedTopics.add(title);
							availableTopics.remove(title);
							if (availableTopics.isEmpty())
								btnInscri.setDisable(true);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
		
		ListDispo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				btnInscri.setDisable(newValue==null);
			}
		});
		
		HBox vbButtons = new HBox();
		//vbButtons.setStyle("-fx-background-color: #336699;");
		vbButtons.setPrefWidth(400);
		vbButtons.setPadding(new Insets(0, 0, 2, 3));
		identifiants.setWrappingWidth(292);
		vbButtons.getChildren().addAll(identifiants,btnDeco);
		HBox InsButtons = new HBox();
		InsButtons.setSpacing(10);
		InsButtons.getChildren().addAll(btnGo,btnDes);


		grid.add(vbButtons, 0,0);
		grid.add(btnNew, 0, 1);
		grid.add(TopicsIns, 0,2);
		grid.add(ListInscrits, 0,3);
		grid.add(InsButtons, 0,4);
		grid.add(TopicsDispos, 0,5);
	    grid.add(ListDispo, 0,6);
	    grid.add(btnInscri, 0,7);

	}
	public void setClient(IClient cl) {
		client = cl;
	}
	public void setPseudo(String p){
		Text t= new Text("Bienvenue "+p);
		t.setStyle("-fx-font-size:19px"); 
		t.setWrappingWidth(292);
		identifiants = t;
	}
	
	public void setSubscribedTopic(Collection<String> c){
		subscribedTopics.addAll(c);
	}
	
	public void setAvailableTopic(Collection<String> c){
		availableTopics.addAll(c);
	}
	
	public void addAvailableTopic(String s){
		availableTopics.add(s);
	}
	
	public void removeTopic(String s){
		availableTopics.remove(s);
		subscribedTopics.remove(s);
	}
	
	public void setServer(IServer server) {
		this.server = server;
	}
	
	public void serverDown(){
		ServerConfigWindow scw = new ServerConfigWindow();
		scw.start(primaryStage);
	}
	
	private void onEnterNewTopic(String title, Stage dialog){
		if (title.equals("")){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Erreur de nom de sujet");
			alert.setContentText("Vous n'avez pas rentré votre nom de sujet !");
			alert.showAndWait();	
		}
		else {
			try {
				if (server.newTopic(title, client.getPseudo())){
					dialog.close();
					client.addSubscribedTopic(title);
					subscribedTopics.add(title);
					availableTopics.remove(title);
				}
				else{
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