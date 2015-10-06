package InterfaceMiddleware;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class FenetreChat extends Application {
	
    private TextField input;
    private TextArea output; 
    private final static String newline = "\n";

    public void start(Stage stage) {
 
    	GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

 Scene scene = new Scene(grid, 750, 300);

        stage.setTitle("Forum de discussion");
        stage.setScene(scene);
        stage.show();
        
        Button btnR = new Button();
        btnR.setText("Retour");
        grid.add(btnR, 1,0);
        
        Text scenetitle = new Text("Topic Soins et beauté");
        grid.add(scenetitle, 0,0);
        

        output = new TextArea();
        grid.add(output, 0,1);
        output.setEditable(false);
        output.setStyle("-fx-border-style: none");
        output.setFocusTraversable(false);

        input = new TextField();
        grid.add(input,0,2);
        
        Button btn = new Button();
        btn.setText("Valider");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
            	String text = input.getText();
			    output.appendText(text + newline);
			    input.selectAll();
            }
       
        });
        grid.add(btn, 1,2);
    }
    public static void main(String[] args) {
        launch(args);
    }
}