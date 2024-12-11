package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private Start start;

    @Override
    public void start(Stage primaryStage) {
        // Skapa grundläggande layout
        VBox root = new VBox(10); //10 representerar mellanrummet (eller spacing) mellan de olika komponenterna (barnkomponenterna) som läggs till i VBox.
        root.setPadding(new Insets(10));//Insets definierar marginaler (avstånd) mellan element i en layout. 

        // Kombobox för starttyp
        Label startTypeLabel = new Label("Välj starttyp:"); //Label: Används för att visa en textbeskrivning.
        ComboBox<Start.StartType> startTypeComboBox = new ComboBox<>();//ComboBox (rullgardinsmeny)kombinerar funktionerna av en textfält och en drop-down lista.
        startTypeComboBox.getItems().addAll(Start.StartType.values());//Start.stratType en enum, vilket innebär att det är en fördefinierad konstanter
        startTypeComboBox.setValue(Start.StartType.INDIVIDUAL_15); // Standardvärde

        // Textfält för första starttid
        Label firstStartTimeLabel = new Label("Första starttid (HH:mm:ss):");
        TextField firstStartTimeField = new TextField();
        firstStartTimeField.setPromptText("Exempel: 08:00:00");

        // Textfält för tidsavstånd (endast för jaktstart)
        Label timeGapsLabel = new Label("Tidsavstånd (sekunder, för jaktstart):");
        TextField timeGapsField = new TextField();
        timeGapsField.setPromptText("Exempel: 30, 60, 90");

        // Resultatruta för att visa starttider
        TextArea resultTextArea = new TextArea();
        resultTextArea.setEditable(false);

        // Knapp för att skapa starttider
        Button createStartTimesButton = new Button("Generera Starttider");

        // Skapa starttider när knappen trycks
        createStartTimesButton.setOnAction(e -> {
            try {
                // Hämta första starttid från TextField
                LocalTime firstStartTime = LocalTime.parse(firstStartTimeField.getText());
                start = new Start(startTypeComboBox.getValue(), firstStartTime);

                // Hämta tidsavstånd om starttypen är PURSUIT_START
                if (startTypeComboBox.getValue() == Start.StartType.PURSUIT_START) {
                    String[] timeGapsText = timeGapsField.getText().split(",");
                    List<Integer> timeGaps = new ArrayList<>();
                    for (String gap : timeGapsText) {
                        timeGaps.add(Integer.parseInt(gap.trim()));
                    }
                    start.setTimeGaps(timeGaps);
                }

                // Visa starttider i resultatrutan
                resultTextArea.setText("Genererade starttider:\n");
                for (LocalTime startTime : start.getStartTimes()) {
                    resultTextArea.appendText(startTime.toString() + "\n");
                }

            } catch (Exception ex) {
                resultTextArea.setText("Fel: " + ex.getMessage());
            }
        });

        // Lägg till komponenterna till layouten
        root.getChildren().addAll(startTypeLabel, startTypeComboBox, firstStartTimeLabel, firstStartTimeField, 
                                  timeGapsLabel, timeGapsField, createStartTimesButton, resultTextArea);

        // Skapa scen och visa
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setTitle("Starttidsgenerator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
