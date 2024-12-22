package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main extends Application {

	private static final DecimalFormat df = new DecimalFormat("0.00");
	private List<Skier> previousSkiers;
	private Race race;
	private static final SkiTrack track = new SkiTrack();
	private Start start;
	private Result result;
	private static final TextArea resultArea = new TextArea();
	private static final TextArea statusArea = new TextArea();

	private TextArea splitArea;

	@Override
	public void start(Stage primaryStage) {
		List<Double> splitPointPositions = new ArrayList<>();
		splitPointPositions.add(100.0);
		SkiTrack.setTrackLength(1000);
		track.setSplitPoints(splitPointPositions);

		List<Long> previousTimes = new ArrayList<>();

		statusArea.setEditable(false);

		start = new Start();
		result = new Result(track, resultArea, statusArea);
		VBox.setVgrow(statusArea, Priority.ALWAYS);
		splitArea = new TextArea();
		splitArea.setEditable(false);
		VBox.setVgrow(splitArea, Priority.ALWAYS);
		resultArea.setEditable(false);
		resultArea.setPrefHeight(150);

		TextField startNumberInput = new TextField();
		startNumberInput.setPromptText("Ange Åkarnummer");
		
        TextField skierCountInput = new TextField();
        skierCountInput.setPromptText("Ange antal skidåkare");

		ComboBox<Start.StartType> startTypeComboBox = new ComboBox<>();
		startTypeComboBox.getItems().addAll(Start.StartType.values());
		startTypeComboBox.setValue(Start.StartType.INDIVIDUAL_15);

		TextField trackLengthInput = new TextField();
		trackLengthInput.setPromptText("Ange banlängd (meter)");

		TextField splitPointsInput = new TextField();
		splitPointsInput.setPromptText("Ange mellantidsstationer (kommaseparerat)");

		TextField raceSpeedFactorInput = new TextField();
		raceSpeedFactorInput.setPromptText("Ange hastighetsfaktor");

		Button generateButton = new Button("Starta/Stoppa lopp");

		generateButton.setOnAction(event -> {
			try {
				Start.StartType selectedType = startTypeComboBox.getValue();
				double raceSpeedFactor = 10;
				
                int skierCount = 10; // Standardvärde
                if (!skierCountInput.getText().isEmpty()) {
                    skierCount = Integer.parseInt(skierCountInput.getText().trim());
                }

                List<Skier> skiers = SkierGenerator.generateSkiers(skierCount);


				if (!raceSpeedFactorInput.getText().isEmpty()) {
					raceSpeedFactor = Double.parseDouble(raceSpeedFactorInput.getText().trim());
				}

				if (!trackLengthInput.getText().isEmpty()) {
					int trackLength = Integer.parseInt(trackLengthInput.getText().trim());
					SkiTrack.setTrackLength(trackLength);
				}

				if (!splitPointsInput.getText().isEmpty()) {
					String[] splits = splitPointsInput.getText().trim().split(",");
					List<Double> newSplits = new ArrayList<>();
					for (String split : splits) {
						newSplits.add(Double.parseDouble(split.trim()));
					}
					track.setSplitPoints(newSplits);
				}

				if (race != null && race.hasFinished()) {
					race.resetRace();
					statusArea.clear();
					splitArea.clear();
					result.clearResults();
					race = null;
				}

				if (race != null) {
					race.resetRace();
					statusArea.clear();
					splitArea.clear();
					result.clearResults();
					race = null;
				} else {
					if (selectedType != Start.StartType.PURSUIT_START) {
						start = new Start(selectedType, skiers);
						List<String> startTimes = start.getFormattedStartTimes(skiers);
						resultArea.setText(String.join("\n", startTimes));

						race = new Race(skiers, raceSpeedFactor, result, statusArea);
						race.InitializeRace(skiers, raceSpeedFactor, result);
						new Thread(race).start();
					}

					if (selectedType == Start.StartType.PURSUIT_START) {
						start = new Start(selectedType, previousSkiers);
						List<String> startTimes = start.getFormattedStartTimes(previousSkiers);
						resultArea.setText(String.join("\n", startTimes));
						race = new Race(previousSkiers, raceSpeedFactor, result, statusArea);
						race.InitializeRace(previousSkiers, raceSpeedFactor, result);
						new Thread(race).start();
					}
				}
			} catch (Exception e) {
				resultArea.setText("Fel: " + e.getMessage());
			}
		});

		Button loadPreviousResultsButton = new Button("Ladda Tidigare Resultat");
		loadPreviousResultsButton.setOnAction(event -> {
			try {
				previousSkiers = Serialization.deserialize("result.txt");

				if (previousSkiers != null && !previousSkiers.isEmpty()) {

					StringBuilder results = new StringBuilder("Tidigare resultat:\n");
					for (Skier skier : previousSkiers) {
						results.append("Åkare ").append(skier.getSkierNumber()).append(": Tid ")
								.append(result.formatTime(skier.getRaceTime())).append("\n");
						previousTimes.add(skier.getRaceTime());
					}
					start.calculatePursuitTimeGaps(previousSkiers);
					resultArea.setText(results.toString());

				} else {
					resultArea.setText("Inga tidigare resultat att visa.");
				}
			} catch (Exception e) {

				resultArea.setText("Fel vid laddning av tidigare resultat: " + e.getMessage());
			}
		});

		Button fetchSkierButton = new Button("Hämta Åkare");
		fetchSkierButton.setOnAction(event -> {
			try {
				int skierNumber = Integer.parseInt(startNumberInput.getText().trim());
				previousSkiers = Serialization.deserialize("result.txt");

				if (previousSkiers != null && !previousSkiers.isEmpty()) {
					previousSkiers.sort((a, b) -> Double.compare(a.getRaceTime(), b.getRaceTime()));

					Skier foundSkier = previousSkiers.stream().filter(skier -> skier.getSkierNumber() == skierNumber)
							.findFirst().orElse(null);

					if (foundSkier != null) {
						int place = previousSkiers.indexOf(foundSkier) + 1;
						String skierResult = "Åkare " + foundSkier.getSkierNumber() + ":\n" + "Tid: "
								+ df.format(foundSkier.getRaceTime() / 1000.0) + " Position: "
								+ df.format(foundSkier.getPosition()) + " meter" + "Plats: " + place + "\n";
						resultArea.setText(skierResult);
					} else {
						resultArea.setText("Ingen åkare hittades med nummer " + skierNumber);
					}
				} else {
					resultArea.setText("Inga tidigare resultat att söka i.");
				}
			} catch (NumberFormatException e) {
				resultArea.setText("Felaktigt åkarnummer. Ange ett giltigt heltal.");
			} catch (Exception e) {
				resultArea.setText("Fel vid hämtning av åkare: " + e.getMessage());
			}
		});

		Button showSplitsButton = new Button("Visa Mellantider");
		showSplitsButton.setOnAction(event -> {
			try {
				splitArea.clear();
				int startNumber = Integer.parseInt(startNumberInput.getText().trim());
				int place = 0;
				List<Map.Entry<Integer, Long>> splitTimesStartNum = result.getSortedSplitTimes();


				splitArea.appendText("Mellantider för åkare " + ":\n");
				for (int i = 0; i < splitTimesStartNum.size(); i++) {
					Map.Entry<Integer, Long> entry = splitTimesStartNum.get(i);
					splitArea.appendText("Åkare " + entry.getKey() + " Plats: " + (i + 1) + ": "
							+ result.formatTime(entry.getValue()) + "\n ");
				}
				
				for (int j = 0; j < splitTimesStartNum.size(); j++) {
					if (splitTimesStartNum.get(j).getKey() == startNumber) {
						place = j + 1;
						Map.Entry<Integer, Long> entry = splitTimesStartNum.get(j);
						splitArea.appendText("___________________________" + "\n ");
						splitArea.appendText(
								"Åkare " + splitTimesStartNum.get(j).getKey() + " har åktid " + result.formatTime(entry.getValue()) + " och placering: " + place + "\n ");
						break;
					}

				}

			} catch (NumberFormatException e) {
				splitArea.setText("Fel: Ogiltigt startnummer.");
			}
		});

		VBox leftBox = new VBox(10);
		leftBox.setPadding(new Insets(20));
		leftBox.setAlignment(Pos.TOP_LEFT);

		VBox rightBox = new VBox(5);
		leftBox.setPadding(new Insets(10));
		leftBox.setAlignment(Pos.TOP_LEFT);

		leftBox.getChildren().addAll(new Label("Välj Starttyp:"), startTypeComboBox, skierCountInput, trackLengthInput, splitPointsInput,
				raceSpeedFactorInput, generateButton, new Label("Sök placering med startnummer:"), startNumberInput,
				showSplitsButton, new Label("Genererade Starttider:"), resultArea, loadPreviousResultsButton);

		rightBox.getChildren().addAll(new Label("Status:"), statusArea, new Label("Mellantider:"), splitArea);

		SplitPane sp = new SplitPane();

		sp.getItems().addAll(leftBox, rightBox);
		sp.setDividerPositions(0.4);
		Scene scene = new Scene(sp, 700, 600);

		primaryStage.setTitle("Starttidshantering");
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.setOnCloseRequest(event -> {
			System.out.println("Stänger av");
			Platform.exit();
			System.exit(0);
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
