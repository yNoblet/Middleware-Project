package InterfaceMiddleware;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class FenetreTopic extends Application {

	private TextField input;
	private TextArea output; 
	private final static String newline = "\n";
	String Identifiants;

	public void start(Stage primaryStage) {

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Scene scene = new Scene(grid, 500, 500);

		primaryStage.setTitle("Forum de discussion");
		primaryStage.setScene(scene);
		primaryStage.show();



		Text identifiants = new Text("Bienvenue "+ Identifiants);


		Text TopicsIns = new Text("Topics inscrits :");


		Button btnNew = new Button();
		btnNew.setText("Nouveau");
		btnNew.setOnAction(
				new EventHandler<ActionEvent>() {
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
						btnNT.setOnAction(
								new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {

										openFenetreChat();
										dialog.close();
									}
									public void openFenetreChat(){
										FenetreChat ft = new FenetreChat();
										ft.GetTopic(Topic.getText(),Identifiants);
										ft.start(primaryStage);
										System.out.println("zozo)");

									}
								});
					}
				});


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


		ListView<String> ListInscrits = new ListView<String>();
		ObservableList<String> items =FXCollections.observableArrayList (
				"Soins et Beauté", "API Rest", "Chaîne de Markov");
		ListInscrits.setItems(items);

		

		Button btnGo = new Button();
		btnGo.setText("Aller");

		btnGo.setOnAction(
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {

						openFenetreChat();


					}
					public void openFenetreChat(){
						FenetreChat ft = new FenetreChat();
						ft.GetTopic(ListInscrits.getSelectionModel().getSelectedItem(), Identifiants);
						ft.start(primaryStage);
						System.out.println("zozo)");

					}
				});
		
		Button btnDes = new Button();
		btnDes.setText("Se désinscrire");
		btnDes.setOnAction(
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {

						


					}
				});


		Text TopicsDispos = new Text("Topics disponibles :");



		ListView<String> ListDispo = new ListView<String>();
		ObservableList<String> items2 =FXCollections.observableArrayList (
				"Toilettage canin", "toto", "zob", "zaza","GL Squad", "Robespierre", "Titeuf");
		ListDispo.setItems(items2);


		Button btnInscri = new Button();
		btnInscri.setText("S'inscrire");

		btnInscri.setOnAction(
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						
					}
				});
		HBox vbButtons = new HBox();
		vbButtons.setSpacing(150);
		vbButtons.getChildren().addAll(identifiants,btnDeco);
		HBox InsButtons = new HBox();
		InsButtons.setSpacing(10);
		InsButtons.getChildren().addAll(btnGo,btnDes);


		grid.add(vbButtons, 0,0);
		//        grid.add(btnNew      , 1,0);
		        grid.add(TopicsIns  , 0,1);
		       grid.add(ListInscrits, 0,2);
		       grid.add(InsButtons   , 0,3);
		       grid.add(TopicsDispos, 0,4);
	           grid.add(ListDispo   , 0,5);
		       grid.add(btnInscri    , 0,6);

	}
	public void getID (String ID) {
		Identifiants = ID;
	}
	public static void main(String[] args) {
		launch(args);
	}
}