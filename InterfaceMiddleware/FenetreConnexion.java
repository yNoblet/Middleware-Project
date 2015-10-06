package InterfaceMiddleware;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FenetreConnexion extends Application {
	TextField jtf = new TextField();
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Valider");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                openFenetreChat();
            }
        });
    
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

 Scene scene = new Scene(grid, 350, 150);

        primaryStage.setTitle("Forum de discussion");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        Text scenetitle = new Text("Bienvenue");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
        
        Label userName = new Label("Identifiant:");
        grid.add(userName, 0, 1);
        grid.add(jtf, 1, 1);
        grid.add(btn, 1, 4);

    }
 public static void main(String[] args) {
        launch(args);
    }
 
 public void openFenetreChat(){
		System.out.println(jtf.getText()+" a été identifié");
		FenetreChat ft = new FenetreChat();
		System.out.println("zozo)");
		
	}
}
