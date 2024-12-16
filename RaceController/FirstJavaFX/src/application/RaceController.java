package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RaceController extends Application {
	Button startBtn = new Button ("Sart");
	Button räknaUtButton = new Button("Räkna ut");
	
	
	// Skapa en instans av Skier-klassen 
	//Skier skier = new Skier(1, 15.5);  // Startnummer 1 och hastighet 15.5

	@Override
	public void start(Stage primaryStage) {
		// Skapa GUI-komponenter
		Label startnummerLabel = new Label("Välj åkare:");
		TextField startnummerInput = new TextField();
		startnummerInput.setPromptText("Skriv in startnummer");

		Label aktuellTidLabel = new Label("Ledartid (HH:mm:ss):");
		TextField ledartidInput = new TextField();
		ledartidInput.setPromptText("Exempel: 08:00:00");

	    
	   // Koppla en metod till knappen som sätter starttiden
            //startBtn.setOnAction(e -> {
            // long currentTime = System.currentTimeMillis();  // Hämta nuvarande tid
            // skier.setStartTime(currentTime);  // Sätt starttid i Skier-objektet
            //startnummerLabel.setText("Starttid: " + skier.getStartTime()+ ", Hastighet " + skier.getSpeed());  // Uppdatera UI:t med starttiden
            //  });
		
		startBtn.setFont(Font.font("Arial", FontWeight.BOLD,14));
		startBtn.setStyle("-fx-background-color:#4CC5E4");

		räknaUtButton.setFont(Font.font("Arial", FontWeight.BOLD,14));
		räknaUtButton.setStyle("-fx-background-color:#4CC5E4");
		TextArea resultatArea = new TextArea();
		resultatArea.setEditable(false);

		// Kombobox för starttyp
		Label startTypeLabel = new Label("Välj starttyp:");
		ComboBox<String> startTypeComboBox = new ComboBox<>();
		startTypeComboBox.getItems().addAll("Individuell Start 15", "Individuell Start 30", "Masstart", "Jaktstart");
		//startTypeComboBox.setOnAction(e->generateStartTime()); anropar metoden i start klassen.

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
