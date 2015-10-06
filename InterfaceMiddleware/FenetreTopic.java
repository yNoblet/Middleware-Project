package InterfaceMiddleware;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class FenetreTopic extends Application {
	
    private TextField input;
    private TextArea output; 
    private final static String newline = "\n";

    public void start(Stage stage) {
 
    	GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

 Scene scene = new Scene(grid, 450, 300);

        stage.setTitle("Forum de discussion");
        stage.setScene(scene);
        stage.show();
        
        Text scenetitle = new Text("Topics disponibles :");
        grid.add(scenetitle, 0,0);
        
        Button btnR = new Button();
        btnR.setText("Retour");
        grid.add(btnR, 1,0);
        
        ListView<String> list = new ListView<String>();
        ObservableList<String> items =FXCollections.observableArrayList (
            "Soins et Beauté", "API Rest", "Chaîne de Markov", "Toilettage canin", "toto", "zob", "zaza","GL Squad", "Robespierre", "Titeuf");
        list.setItems(items);
 
        
        grid.add(list, 0,1);
        
        
        
    }
    public static void main(String[] args) {
        launch(args);
    }
}