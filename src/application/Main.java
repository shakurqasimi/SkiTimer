package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import java.math.RoundingMode;
import java.text.DecimalFormat;




public class Main extends Application implements EventHandler<ActionEvent> {
	TimeSimulator timeSimulator = new TimeSimulator();
	private static final DecimalFormat df = new DecimalFormat("0.00");

	
	Button button;
	Button button1;
	
	public static void main(String[] args) {
	

		
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		

	    speedSimulator speedSimulator = new speedSimulator();
				
		primaryStage.setTitle("Test");
		button = new Button();
		button.setText("Slumphastighet");
		button.setOnAction(e -> System.out.println(df.format(speedSimulator.generateSpeed(90)) + " m/s"));
		
		button1 = new Button();
		button1.setText("Simulera tid");
		button1.setOnAction(e -> System.out.println(timeSimulator.generateTime(30)));
		
	    HBox layout = new HBox(20);
		layout.setPadding(new Insets(50));

	    layout.getChildren().add(button);
	    layout.getChildren().add(button1);

		
		Scene scene = new Scene(layout, 300, 250);
		primaryStage.setTitle("Test");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void handle(ActionEvent e) {
		if(e.getSource() ==button) {
			System.out.println("");
		}
		
	}
	
	
	
	
}
