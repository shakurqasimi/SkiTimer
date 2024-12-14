
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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application implements EventHandler<ActionEvent> {

	static speedSimulator speedSimulator = new speedSimulator();

	private static final DecimalFormat df = new DecimalFormat("0.00");

	private Start start;

	@Override
	public void start(Stage primaryStage) throws InterruptedException {

		List<Skier> skiers = new ArrayList<Skier>();

		Skier skier1 = new Skier(1, 0, speedSimulator.generateSpeed(0), 0, 0);
		Skier skier2 = new Skier(2, 0, speedSimulator.generateSpeed(0), 0, 0);
		Skier skier3 = new Skier(3, 0, speedSimulator.generateSpeed(0), 0, 0);
		Skier skier4 = new Skier(4, 0, speedSimulator.generateSpeed(0), 0, 0);
		Skier skier5 = new Skier(5, 0, speedSimulator.generateSpeed(0), 0, 0);
		Skier skier6 = new Skier(6, 0, speedSimulator.generateSpeed(0), 0, 0);
		Skier skier7 = new Skier(7, 0, speedSimulator.generateSpeed(0), 0, 0);
		Skier skier8 = new Skier(8, 0, speedSimulator.generateSpeed(0), 0, 0);
		Skier skier9 = new Skier(9, 0, speedSimulator.generateSpeed(0), 0, 0);
		Skier skier10 = new Skier(10, 0, speedSimulator.generateSpeed(0), 0, 0);

		// Åkarobject: Startnummer, position, hastighet, starttid

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

				TimeSimulator timeSimulator = new TimeSimulator();


				Start.StartType selectedType = startTypeComboBox.getValue();
				start = new Start(selectedType, skiers);

				Race race = new Race(skiers, 200);
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
					start.setTimeGaps(timeGaps, skiers, timeSimulator.generateTime());
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
	}

	public static void main(String[] args) throws InterruptedException {
		launch(args);

	}

	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
