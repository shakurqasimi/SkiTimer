package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RaceController extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Skapa GUI-komponenter
        Label startnummerLabel = new Label("Välj åkare:");
        TextField startnummerInput = new TextField();
        startnummerInput.setPromptText("Skriv in startnummer");
        
        Button startBtn = new Button ("Sart");
        startBtn.setFont(Font.font("Arial", FontWeight.BOLD,14));
		startBtn.setStyle("-fx-background-color:#4CC5E4");

        Label aktuellTidLabel = new Label("Ledartid (HH:mm:ss):");
        TextField ledartidInput = new TextField();
        ledartidInput.setPromptText("Exempel: 08:00:00");

        Button räknaUtButton = new Button("Räkna ut");
        räknaUtButton.setFont(Font.font("Arial", FontWeight.BOLD,14));
		räknaUtButton.setStyle("-fx-background-color:#4CC5E4");
        TextArea resultatArea = new TextArea();
        resultatArea.setEditable(false);

        // Kombobox för starttyp
        Label startTypeLabel = new Label("Välj starttyp:");
        ComboBox<String> startTypeComboBox = new ComboBox<>();
        startTypeComboBox.getItems().addAll("Individuell Start", "Jaktstart");

        // Lägg till funktionalitet till knappen
        räknaUtButton.setOnAction(event -> {
            try {
                String startnummer = startnummerInput.getText();
                String ledartid = ledartidInput.getText();

                // Här kan du lägga till mer logik, t.ex. validering
                resultatArea.setText("Startnummer: " + startnummer + "\nLedartid: " + ledartid);
            } catch (Exception ex) {
                resultatArea.setText("Fel: Kontrollera inmatningen.");
            }
        });

        // Layout
        VBox root = new VBox(10, startnummerLabel, startnummerInput, startBtn, aktuellTidLabel, ledartidInput, 
                startTypeLabel, startTypeComboBox, räknaUtButton, resultatArea);
        root.setPadding(new Insets(10));

	startnummerInput.setPrefWidth(200);
	ledartidInput.setPrefWidth(200);

        // Skapa scen och visa
        primaryStage.setTitle("Mellantidsräknare");
        primaryStage.setScene(new Scene(root, 400, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
