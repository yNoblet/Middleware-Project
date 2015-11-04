package gui;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import core.IServer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ServerConfigWindow extends Application {
	TextField ip;
	TextField port;
	Stage primaryStage;
	
	public final String IP_DEF = "localhost";
	public final String PORT_DEF = "1097";
	
	@Override
	public void start(Stage primaryStage) {
		
		this.primaryStage=primaryStage;
		ip = new TextField(IP_DEF);
		port = new TextField(PORT_DEF);
		
		ip.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onEnter();
				}
			}
		});
		
		port.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					onEnter();
				}
			}
		});
	
		Button btn = new Button();
		btn.setText("Valider");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onEnter();
			}
		});

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.getStylesheets().add("gui/style.css");

		Scene scene = new Scene(grid, 350, 150);

		primaryStage.setTitle("Forum de discussion");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();

		Text scenetitle = new Text("Configuration du serveur");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		Label labelIP = new Label("IP serveur:");
		grid.add(labelIP, 0, 1);
		grid.add(ip, 1, 1);
		Label labelPort = new Label("Port serveur:");
		grid.add(labelPort, 0, 2);
		grid.add(port, 1, 2);
		grid.add(btn, 1, 3);

	}
	
	private void onEnter(){
		if (ip.getText().equals("") ){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Erreur d'adresse IP");
			alert.setContentText("Vous n'avez pas rentré d'IP pour le serveur !");
			alert.showAndWait();	
		}
		else if (port.getText().equals("") ){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Erreur de port");
			alert.setContentText("Vous n'avez pas rentré de port pour le serveur !");
			alert.showAndWait();	
		}
		else {
			try {
				
				int remotePort = Integer.parseInt(port.getText());
				String remoteIp = ip.getText();
				String remoteObjectName = "Server";

				Registry registry = LocateRegistry.getRegistry(remoteIp, remotePort);
				IServer s = (IServer) registry.lookup(remoteObjectName);

				if(s != null){
					IServer s2 = (IServer) s.checkPartition();
					SignInWindow sw = new SignInWindow();
					if(s2 != null){
						sw.setServer(s2);
					}
					sw.start(primaryStage);
					
				} else {
					System.out.println("No server!!!");
				}
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Erreur de serveur");
				alert.setContentText("Les paramètres ne correpondent pas à un serveur valide !");
				alert.showAndWait();	
			}
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
