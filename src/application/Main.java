package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main extends Application implements EventHandler<ActionEvent> {

    static SpeedSimulator speedSimulator = new SpeedSimulator();

    private Start start;

    @Override
    public void start(Stage primaryStage) throws InterruptedException {

        List<Double> splitPointPositions = new ArrayList<>();
        splitPointPositions.add(100.0);
        SkiTrack track = new SkiTrack();
        track.setTrackLength(1000);
        track.setSplitPoints(splitPointPositions);

        List<Skier> skiers = new CopyOnWriteArrayList<>();
        for (int i = 1; i <= 10; i++) {
            skiers.add(new Skier(i, i * 10, speedSimulator.generateSpeed(0), track));
        }

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        ComboBox<Start.StartType> startTypeComboBox = new ComboBox<>();
        startTypeComboBox.getItems().addAll(Start.StartType.values());
        startTypeComboBox.setValue(Start.StartType.INDIVIDUAL_15);

        TextField startTimeField = new TextField();
        startTimeField.setPromptText("Ange starttid (t.ex. 10:00:00)");

        Button generateButton = new Button("kör");

        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefWidth(400); 
        resultArea.setPrefHeight(600); 

        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefWidth(400); 
        logArea.setPrefHeight(600); 

      
        HBox resultLogBox = new HBox(10);
        resultLogBox.getChildren().addAll(logArea, resultArea );
        HBox.setHgrow(resultArea, Priority.ALWAYS);
        HBox.setHgrow(logArea, Priority.ALWAYS);

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
                Result result = new Result(track, resultArea, logArea);
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
                logArea.appendText(".\n");

            } catch (Exception e) {
                resultArea.setText("Fel: " + e.getMessage());
                logArea.appendText("Ett fel uppstod: " + e.getMessage() + "\n");
            }
        });

        root.getChildren().addAll(new Label("Välj Starttyp:"), startTypeComboBox,
                new Label("Ange Första Starttid (hh:mm:ss):"), startTimeField, pursuitBox, generateButton,
                new Label("Genererade Starttider och Logg:"), resultLogBox);

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setTitle("Starttidshantering");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Stänger av");
            Platform.exit();
            System.exit(0); // Stänger alla trådar när fönstret stängs
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
