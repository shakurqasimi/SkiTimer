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

    private Button startBtn = new Button("Start");
    private Button räknaUtButton = new Button("Räkna ut");
    private Line skidbana = new Line(50, 100, 50, 100); //representera skidbanan en visuell representation av skidbanans längd.
    private Line mållinje = new Line();
    private Slider banlängdSlider = new Slider(100, 20000, 2000);
    private Label skidbanaLabel = new Label("Skidbanans längd: " + (int) banlängdSlider.getValue() + " meter");
    private TextArea resultatArea = new TextArea();

    // Skapa en instans av Skier-klassen
    // Skier skier = new Skier(1, 15.5); // Startnummer 1 och hastighet 15.5

    @Override
    public void start(Stage primaryStage) {
        // Skapa GUI-komponenter
        Label startnummerLabel = new Label("Välj åkare:");
        TextField startnummerInput = createTextField("Skriv in startnummer");
        Label aktuellTidLabel = new Label("Ledartid (HH:mm:ss):");
        TextField ledartidInput = createTextField("Exempel: 08:00:00");

        // Uppdatera banans längd och mållinjens position när slidern ändras
        updateBanlängdSlider();

        // Lägg till funktionalitet till knappar
        setupButtons(startnummerLabel, startnummerInput, ledartidInput);

        // Layout
        VBox root = createLayout(startnummerLabel, startnummerInput, aktuellTidLabel, ledartidInput);

        // Skapa scen och visa
        primaryStage.setTitle("Mellantidsräknare");
        primaryStage.setScene(new Scene(root, 400, 500));
        primaryStage.show();
    }

    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setPrefWidth(200);
        return textField;
    }

    private void updateBanlängdSlider() {
        banlängdSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            double newLength = newValue.doubleValue();
            skidbana.setEndX(50 + newLength); // Uppdatera slutpunkten för skidbanan
            skidbana.setStartX(50 + newLength); // Uppdatera mållinjens startpunkt
            skidbana.setEndX(50 + newLength);   // Uppdatera mållinjens slutpunkt
            skidbanaLabel.setText("Skidbanans längd: " + (int) newLength + " meter");
        });
    }

    private void setupButtons(Label startnummerLabel, TextField startnummerInput, TextField ledartidInput) {
        startBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        startBtn.setStyle("-fx-background-color:#4CC5E4");
        startBtn.setOnAction(e -> {
            // Exempel på att sätta starttid - detta kan implementeras senare när Skier-klassen finns
            // long currentTime = System.currentTimeMillis();  
            // skier.setStartTime(currentTime);
            // startnummerLabel.setText("Starttid: " + skier.getStartTime() + ", Hastighet: " + skier.getHastighet());
        });

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

    private VBox createLayout(Label startnummerLabel, TextField startnummerInput, Label aktuellTidLabel, TextField ledartidInput) {
        VBox root = new VBox(10, startnummerLabel, startnummerInput, startBtn, aktuellTidLabel, ledartidInput,
                createStartTypeComboBox(), räknaUtButton, resultatArea, skidbana, banlängdSlider, skidbanaLabel, mållinje);
        root.setPadding(new Insets(10));

        return root;
    }

    private ComboBox<String> createStartTypeComboBox() {
        ComboBox<String> startTypeComboBox = new ComboBox<>();
        startTypeComboBox.getItems().addAll("Individuell Start 15", "Individuell Start 30", "Masstart", "Jaktstart");
        return startTypeComboBox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

