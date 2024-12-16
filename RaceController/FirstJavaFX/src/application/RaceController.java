package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RaceController extends Application {

    // GUI-komponenter
    private Button startBtn = new Button("Start");
    private Button räknaUtButton = new Button("Räkna ut");
    private TextArea resultatArea = new TextArea();
    private TextField startnummerInput = new TextField();
    private TextField ledartidInput = new TextField();
    private Slider banlängdSlider = new Slider(100, 20000, 2000); // Slider för 100 till 20000 meter, standard 2000 meter
    private Label skidbanaLabel = new Label();
    private Line skidbana = new Line(50, 100, 50, 100);
    private Line mållinje = new Line();
    private Line checkpoint = new Line(); // Ny line för checkpoint på 5000 meter

    @Override
    public void start(Stage primaryStage) {

        // Skapa GUI-komponenter
        setupStartnummerInput();
        setupLedartidInput();
        setupBanlängdSlider();
        setupMållinje();
        setupCheckpoint();
        setupButtons();
        
        // Layout
        VBox root = new VBox(10,
                createLabel("Välj åkare:"), startnummerInput,
                startBtn, createLabel("Ledartid (HH:mm:ss):"), ledartidInput,
                createLabel("Skidbanans längd:"), skidbanaLabel,
                banlängdSlider, skidbana, mållinje, checkpoint,
                räknaUtButton, resultatArea
        );
        root.setPadding(new Insets(10));

        // Skapa scen och visa
        primaryStage.setTitle("Mellantidsräknare");
        primaryStage.setScene(new Scene(root, 400, 500));
        primaryStage.show();
    }

    // Skapa och konfigurera komponenterna för startnummer
    private void setupStartnummerInput() {
        startnummerInput.setPromptText("Skriv in startnummer");
        startnummerInput.setPrefWidth(200);
    }

    // Skapa och konfigurera komponenterna för ledartid
    private void setupLedartidInput() {
        ledartidInput.setPromptText("Exempel: 08:00:00");
        ledartidInput.setPrefWidth(200);
    }

    // Skapa och konfigurera banlängdslider och uppdatera skidbana, mållinje och checkpoint
    private void setupBanlängdSlider() {
        skidbanaLabel.setText("Skidbanans längd: " + (int) banlängdSlider.getValue() + " meter");
        banlängdSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            updateSkidbanaAndMållinje(newValue.doubleValue());
            updateCheckpoint(newValue.doubleValue());
            skidbanaLabel.setText("Skidbanans längd: " + (int) newValue.doubleValue() + " meter");
        });
    }

    // Skapa och konfigurera mållinjen
    private void setupMållinje() {
        mållinje.setStroke(Color.RED);  // Sätt mållinjen till röd
        mållinje.setStrokeWidth(3);     // Sätt linjens tjocklek
    }

    // Skapa och konfigurera checkpointen
    private void setupCheckpoint() {
        checkpoint.setStroke(Color.GREEN);  // Sätt checkpoint-linjen till grön
        checkpoint.setStrokeWidth(3);       // Sätt linjens tjocklek
    }

    // Skapa och konfigurera knapparna
    private void setupButtons() {
        startBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        startBtn.setStyle("-fx-background-color:#4CC5E4");

        räknaUtButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        räknaUtButton.setStyle("-fx-background-color:#4CC5E4");

        räknaUtButton.setOnAction(event -> {
            try {
                String startnummer = startnummerInput.getText();
                String ledartid = ledartidInput.getText();
                resultatArea.setText("Startnummer: " + startnummer + "\nLedartid: " + ledartid);
            } catch (Exception ex) {
                resultatArea.setText("Fel: Kontrollera inmatningen.");
            }
        });
    }

    // Skapa en metod för att skapa etiketter
    private Label createLabel(String text) {
        return new Label(text);
    }

    // Uppdatera skidbanans och mållinjens position baserat på slidern
    private void updateSkidbanaAndMållinje(double newLength) {
        skidbana.setEndX(50 + newLength); // Uppdatera slutpunkten för skidbanan
        skidbana.setStartX(50);           // Håll startpunkten konstant på X = 50
        skidbana.setStroke(Color.BLUE);
        skidbana.setStrokeWidth(5);

        // Uppdatera mållinjens position
        mållinje.setStartX(50 + newLength); // Mållinjen vid slutet av banan
        mållinje.setEndX(50 + newLength);   // Mållinje vid samma position
    }

    // Uppdatera checkpointens position
    private void updateCheckpoint(double newLength) {
        double checkpointPosition = 5000; // Checkpoint på 5000 meter (5 km)
        if (newLength >= checkpointPosition) {
            checkpoint.setStartX(50 + checkpointPosition);  // Positionera checkpointen
            checkpoint.setEndX(50 + checkpointPosition);    // Checkpointen är en vertikal linje
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

