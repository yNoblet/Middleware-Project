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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class FenetreTopic extends Application {
	
    private TextField input;
    private TextArea output; 
    private final static String newline = "\n";

    public void start(Stage primaryStage) {
 
    	GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

 Scene scene = new Scene(grid, 450, 300);

 primaryStage.setTitle("Forum de discussion");
 primaryStage.setScene(scene);
 primaryStage.show();
        
        Text scenetitle = new Text("Topics inscrits :");
        grid.add(scenetitle, 0,0);
        
        Button btnN = new Button();
        btnN.setText("Nouveau");
        btnN.setOnAction(
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
                                		ft.start(primaryStage);
                                		System.out.println("zozo)");
                                		
                                	}
                                });
                    }
                 });
        grid.add(btnN, 1,0);
        
        Button btnR = new Button();
        btnR.setText("Déconnexion");
        btnR.setOnAction(new EventHandler<ActionEvent>() {
 
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
        grid.add(btnR, 1,1);
        
        ListView<String> list = new ListView<String>();
        ObservableList<String> items =FXCollections.observableArrayList (
            "Soins et Beauté", "API Rest", "Chaîne de Markov");
        list.setItems(items);
        grid.add(list, 0,1);
        
        
        Button btnA = new Button();
        btnA.setText("Aller");
        grid.add(btnA, 0,2);
        btnA.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                    	
                    	openFenetreChat();
                    	
                    }
                    public void openFenetreChat(){
                		FenetreChat ft = new FenetreChat();
                		ft.start(primaryStage);
                		System.out.println("zozo)");
                		
                	}
                });
        
        
        
        Text scenetitle2 = new Text("Topics disponibles :");
        grid.add(scenetitle2, 0,3);
        
        
        ListView<String> list2 = new ListView<String>();
        ObservableList<String> items2 =FXCollections.observableArrayList (
            "Toilettage canin", "toto", "zob", "zaza","GL Squad", "Robespierre", "Titeuf");
        list2.setItems(items2);
        grid.add(list2, 0,4);
        
        Button btnS = new Button();
        btnS.setText("S'inscrire");
        grid.add(btnS, 0,5);
        btnS.setOnAction(
        new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Text("Topic créé !"));
                Button btnNT = new Button();
                dialogVbox.getChildren().add(btnNT);
                btnNT.setText("Rejoindre");
                Scene dialogScene = new Scene(dialogVbox, 90, 70);
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
                        		ft.start(primaryStage);
                        		System.out.println("zozo)");
                        		
                        	}
                        });
            }
        });
        
    }
    public static void main(String[] args) {
        launch(args);
    }
}