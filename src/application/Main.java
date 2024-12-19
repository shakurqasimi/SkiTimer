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
import java.util.concurrent.CopyOnWriteArrayList;


public class Main extends Application implements EventHandler<ActionEvent> {

	static SpeedSimulator speedSimulator = new SpeedSimulator();

	private static final DecimalFormat df = new DecimalFormat("0.00");

	private Start start;

	@Override
	public void start(Stage primaryStage) throws InterruptedException {

		List<Double> splitPointPositions = new ArrayList<Double>(); 
		splitPointPositions.add(100.0); //Sätt checkpoints inom banan
		SkiTrack track = new SkiTrack();
		track.setTrackLength(1000);
		track.setSplitPoints(splitPointPositions);
		
		List<Skier> skiers = new ArrayList<Skier>();

		Skier skier1 = new Skier(1, 12, speedSimulator.generateSpeed(0), track);
		Skier skier2 = new Skier(2, 23, speedSimulator.generateSpeed(0), track);
		Skier skier3 = new Skier(3, 83, speedSimulator.generateSpeed(0), track);
		Skier skier4 = new Skier(4, 77, speedSimulator.generateSpeed(0), track);
		Skier skier5 = new Skier(5, 91, speedSimulator.generateSpeed(0), track);
		Skier skier6 = new Skier(6, 14, speedSimulator.generateSpeed(0), track);
		Skier skier7 = new Skier(7, 22, speedSimulator.generateSpeed(0), track);
		Skier skier8 = new Skier(8, 54, speedSimulator.generateSpeed(0), track);
		Skier skier9 = new Skier(9, 38, speedSimulator.generateSpeed(0), track);
		Skier skier10 = new Skier(10, 46, speedSimulator.generateSpeed(0), track);

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

		Button generateButton = new Button("Start");

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
				Result result = new Result(track);
				Race race = new Race(skiers, 10, result);
				Thread raceThread = new Thread(race);
				raceThread.setDaemon(true);
				raceThread.start();

				String startTime = startTimeField.getText();
				
				if (selectedType == Start.StartType.PURSUIT_START) {
		            
		            start.setTimeGaps(new ArrayList<>(), skiers);
		        }

				List<String> startTimes = start.getFormattedStartTimes(skiers);
				resultArea.setText(String.join("\n", startTimes));

			} catch (Exception e) {
				resultArea.setText("Fel: " + e.getMessage());
			}
		});

		root.getChildren().addAll(new Label("Välj Starttyp:"), startTypeComboBox,
				new Label("Start"), startTimeField, pursuitBox, generateButton,
				new Label("Genererade Starttider:"), resultArea);


		Button loadPreviousResultsButton = new Button("Ladda Tidigare Resultat");

		loadPreviousResultsButton.setOnAction(event -> {
			try {
				List<Skier> previousSkiers = Serialization.deserialize("result.txt");

				if (previousSkiers != null && !previousSkiers.isEmpty()) {

					StringBuilder results = new StringBuilder("Tidigare resultat:\n");
					for (Skier skier : previousSkiers) {
						results.append("Åkare ").append(skier.getSkierNumber())
						.append(": Tid ").append(df.format(skier.getRaceTime() / 1000.0)) 
						.append(" sekunder\n");
					}
					// Uppdatera TextArea med resultaten
					resultArea.setText(results.toString());
				} else {
					resultArea.setText("Inga tidigare resultat att visa.");
				}
			} catch (Exception e) {
				// Visa felmeddelande om något går fel
				resultArea.setText("Fel vid laddning av tidigare resultat: " + e.getMessage());
			}
		});

		root.getChildren().add(loadPreviousResultsButton); 


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
