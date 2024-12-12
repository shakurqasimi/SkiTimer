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
import java.util.ArrayList;
import java.util.List;

public class Main extends Application implements EventHandler<ActionEvent> {

	static speedSimulator speedSimulator = new speedSimulator();
	static TimeSimulator timeSimulator = new TimeSimulator(20);

	private static final DecimalFormat df = new DecimalFormat("0.00");

	Button button;
	Button button1;

	public static void main(String[] args) throws InterruptedException {
		
		List<Skier> skiers = new ArrayList<Skier>();
		
		Skier skier1 = new Skier(1, 0 , 5, 0);
		Skier skier2 = new Skier(2, 0 , 7, 0);
		
		skiers.add(skier1);
		skiers.add(skier2);

		

		
		boolean raceHasStarted = false;
		raceHasStarted = true;
		while (raceHasStarted) {
			
			for (Skier skier : skiers) {
				skier.move(timeSimulator.generateTime());
				System.out.println("Åkare: " + skier.getStartnumber() + " Har åkt " + df.format(skier.getPosition()));
			}			

			Thread.sleep(16);
		}

		launch(args);
	}

	public void start(Stage primaryStage) {

		primaryStage.setTitle("Test");
		button = new Button();
		button.setText("Slumphastighet");
		button.setOnAction(e -> System.out.println(df.format(speedSimulator.generateSpeed(90)) + " m/s"));

		button1 = new Button();
		button1.setText("Simulera tid");
		button1.setOnAction(e -> System.out.println(timeSimulator.formatTime(timeSimulator.generateTime())));

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
		if (e.getSource() == button) {
			System.out.println("");
		}

	}
}
