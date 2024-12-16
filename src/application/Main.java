
package application;

import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class Main extends Application implements EventHandler<ActionEvent> {

	static SpeedSimulator speedSimulator = new SpeedSimulator();

	private static final DecimalFormat df = new DecimalFormat("0.00");

	private Start start;

	@Override
	public void start(Stage primaryStage) throws InterruptedException {

		List<Double> splitPointPositions = new ArrayList<Double>(); 
		splitPointPositions.add(100.0); //Sätt checkpoints inom banan
		SkiTrack skiTrack = new SkiTrack();
		skiTrack.setTrackLength(1000);
		skiTrack.setSplitPoints(splitPointPositions);
		
		List<Skier> skiers = new ArrayList<Skier>();

		Skier skier1 = new Skier(1, 12, speedSimulator.generateSpeed(0), skiTrack, null);
		Skier skier2 = new Skier(2, 23, speedSimulator.generateSpeed(0), skiTrack, null);
		Skier skier3 = new Skier(3, 83, speedSimulator.generateSpeed(0), skiTrack, null);
		Skier skier4 = new Skier(4, 77, speedSimulator.generateSpeed(0), skiTrack, null);
		Skier skier5 = new Skier(5, 91, speedSimulator.generateSpeed(0), skiTrack, null);
		Skier skier6 = new Skier(6, 14, speedSimulator.generateSpeed(0), skiTrack, null);
		Skier skier7 = new Skier(7, 22, speedSimulator.generateSpeed(0), skiTrack, null);
		Skier skier8 = new Skier(8, 54, speedSimulator.generateSpeed(0), skiTrack, null);
		Skier skier9 = new Skier(9, 38, speedSimulator.generateSpeed(0), skiTrack, null);
		Skier skier10 = new Skier(10, 46, speedSimulator.generateSpeed(0), skiTrack, null);

		// Åkarobject: Startnummer, åkarnummer, hastighet

		skiers.add(skier1);
		skiers.add(skier2);
		skiers.add(skier3);
		skiers.add(skier4);
		skiers.add(skier5);
		skiers.add(skier6);
		skiers.add(skier7);
		skiers.add(skier8);
		skiers.add(skier9);
		skiers.add(skier10);

		VBox root = new VBox(10);
		root.setPadding(new Insets(15));

		ComboBox<Start.StartType> startTypeComboBox = new ComboBox<>();
		startTypeComboBox.getItems().addAll(Start.StartType.values());
		startTypeComboBox.setValue(Start.StartType.INDIVIDUAL_15);

		TextField startTimeField = new TextField();
		startTimeField.setPromptText("Ange starttid (t.ex. 10:00:00)");

		Button generateButton = new Button("Generera Starttider");

		TextArea resultArea = new TextArea(); 
		resultArea.setEditable(false);

		VBox pursuitBox = new VBox(5);
		pursuitBox.setPadding(new Insets(5));
		TextField timeGapsField = new TextField();
		timeGapsField.setPromptText("Tidsavstånd för jaktstart (t.ex. 10,20,30)");
		pursuitBox.getChildren().addAll(new Label("Tidsavstånd för Jaktstart:"), timeGapsField);
		pursuitBox.setVisible(false);

		startTypeComboBox.setOnAction(event -> {
			pursuitBox.setVisible(startTypeComboBox.getValue() == Start.StartType.PURSUIT_START);
		});

		generateButton.setOnAction(event -> {
			try {
				

				Start.StartType selectedType = startTypeComboBox.getValue();
				start = new Start(selectedType, skiers);
				Result result = new Result();
				Race race = new Race(skiers, 10, result);
				Thread raceThread = new Thread(race);
				raceThread.setDaemon(true);
				raceThread.start();

				String startTime = startTimeField.getText();

				if (!startTime.isEmpty()) {
					start.setFirstStartTime(startTime, skiers);
				}

				if (selectedType == Start.StartType.PURSUIT_START) {
					String[] gaps = timeGapsField.getText().split(",");
					List<Integer> timeGaps = new ArrayList<>();
					for (String gap : gaps) {
						timeGaps.add(Integer.parseInt(gap.trim()));
					}
					start.setTimeGaps(timeGaps, skiers);
				}

				List<String> startTimes = start.getFormattedStartTimes();
				resultArea.setText(String.join("\n", startTimes));

			} catch (Exception e) {
				resultArea.setText("Fel: " + e.getMessage());
			}
		});

		root.getChildren().addAll(new Label("Välj Starttyp:"), startTypeComboBox,
				new Label("Ange Första Starttid (hh:mm:ss):"), startTimeField, pursuitBox, generateButton,
				new Label("Genererade Starttider:"), resultArea);

		Scene scene = new Scene(root, 400, 400);
		primaryStage.setTitle("Starttidshantering");
		primaryStage.setScene(scene);
		primaryStage.show();
		
        primaryStage.setOnCloseRequest(event -> { 
        	System.out.println("Stänger av");
        	Platform.exit();
        	System.exit(0);
        	//Stänger alla trådar när fönstret stängs
        });

	}

	public static void main(String[] args) throws InterruptedException {
		launch(args);

	}

	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
