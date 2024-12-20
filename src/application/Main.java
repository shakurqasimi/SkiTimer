package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class Main extends Application {

    static SpeedSimulator speedSimulator = new SpeedSimulator();

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private List<Skier> previousSkiers;
    private Race race;
    private SkiTrack track;
    private Start start;
    private Result result; 
    private TextArea resultArea; 
    private TextArea statusArea;

    @Override
    public void start(Stage primaryStage) {
    	List<Double> splitPointPositions = new ArrayList<Double>();
		splitPointPositions.add(100.0); 
		track = new SkiTrack();
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

		
		start = new Start();

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
		
		
		Button showSplitsButton = new Button("Visa Mellantider");
		List<Integer> timeGaps = new ArrayList<>();
		List<Long> previousTimes = new ArrayList<>();

        start = new Start();
        result = new Result(track, resultArea, statusArea); 
        statusArea = new TextArea();
        statusArea.setEditable(false); 
        statusArea.setPrefHeight(300);
        resultArea = new TextArea(); 
        resultArea.setEditable(false); 

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        TextField startNumberInput = new TextField();
        startNumberInput.setPromptText("Ange Åkarnummer");

        ComboBox<Start.StartType> startTypeComboBox = new ComboBox<>();
        startTypeComboBox.getItems().addAll(Start.StartType.values());
        startTypeComboBox.setValue(Start.StartType.INDIVIDUAL_15);

        Button generateButton = new Button("Starta/Stoppa lopp");

        VBox pursuitBox = new VBox(5);
        pursuitBox.setPadding(new Insets(5));
        pursuitBox.setVisible(false);

        startTypeComboBox.setOnAction(event -> {
            pursuitBox.setVisible(startTypeComboBox.getValue() == Start.StartType.PURSUIT_START);
        });

        generateButton.setOnAction(event -> {
			try {
				Start.StartType selectedType = startTypeComboBox.getValue();

				double raceSpeedFactor = 10;

				if (race != null) {
					race.resetRace();
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
						race = new Race();
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
								.append(df.format(skier.getRaceTime() / 1000.0)).append(" sekunder\n");
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
                    // Sortera åkarna efter raceTime för att beräkna plats
                    previousSkiers.sort((a, b) -> Double.compare(a.getRaceTime(), b.getRaceTime()));

                    Skier foundSkier = previousSkiers.stream()
                            .filter(skier -> skier.getSkierNumber() == skierNumber)
                            .findFirst()
                            .orElse(null);

                    if (foundSkier != null) {
                        int place = previousSkiers.indexOf(foundSkier) + 1; 
                        String skierResult = "Åkare " + foundSkier.getSkierNumber() + ":\n"
                                + "Tid: " + df.format(foundSkier.getRaceTime() / 1000.0) + " sekunder\n"
                                + "Position: " + df.format(foundSkier.getPosition()) + " meter\n"
                                + "Plats: " + place + "\n";
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

        root.getChildren().addAll(
                new Label("Välj Starttyp:"),
                startTypeComboBox,
                new Label("Sök placering med startnummer:"),
                startNumberInput,
                fetchSkierButton,
                pursuitBox,
                generateButton,
                new Label(""),
                statusArea,
                new Label("Genererade Starttider:"),
                resultArea,
                loadPreviousResultsButton
        );

        Scene scene = new Scene(root, 400, 400);
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
