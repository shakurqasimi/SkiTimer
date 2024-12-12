package application;

import java.util.ArrayList;
import java.util.List;

public class Start {

    // Enum som representerar de olika starttyperna
    public enum StartType {
        INDIVIDUAL_15,   
        INDIVIDUAL_30,   
        MASS_START,      
        PURSUIT_START    
    }

    private StartType startType;  
    private TimeSimulator timeSimulator;  // Instans av TimeSimulator för att hantera tid
    private List<Long> startTimes; // Lista för att lagra alla starttider i millisekunder
    private long firstStartTime;  // Första starttiden, hämtas från TimeSimulator
    private List<Integer> timeGaps; // Lista för tidsavstånd för jaktstart

    // Konstruktor 
    public Start(StartType startType, double speedFactor) {
        this.startType = startType;
        this.startTimes = new ArrayList<>();
        this.timeGaps = new ArrayList<>();
        this.timeSimulator = new TimeSimulator(speedFactor);  // Skapar en TimeSimulator med en speed factor
        this.firstStartTime = timeSimulator.generateTime();  // Hämtar den aktuella tiden i millisekunder direkt från TimeSimulator
        generateStartTimes(); // Generera starttider baserat på starttypen
    }

    // Metod för att sätta starttyp
    public void setStartType(StartType startType) {
        this.startType = startType;
        generateStartTimes();  // Uppdatera starttider när starttyp ändras
    }



    // Metod för att sätta starttid i hh:mm:ss format
    public void setFirstStartTime(String startTimeString) {
        // Om starttiden anges av användaren
        this.firstStartTime = timeSimulator.generateTime();  // Konvertera till millisekunder
        generateStartTimes(); // Uppdatera starttider när starttiden ändras
    }

    // Metod för att generera starttider baserat på vald starttyp
    private void generateStartTimes() {
        startTimes.clear(); 

        switch (startType) {
            case INDIVIDUAL_15:
                for (int i = 0; i < 10; i++) { // 10 åkare
                    startTimes.add(firstStartTime + (15 * i * 1000)); 
                }
                break;
            case INDIVIDUAL_30:
                for (int i = 0; i < 10; i++) { // 10 åkare
                    startTimes.add(firstStartTime + (30 * i * 1000)); 
                }
                break;
            case MASS_START:
                startTimes.add(firstStartTime); 
                break;
            case PURSUIT_START:
                if (timeGaps.isEmpty()) {
                    throw new IllegalStateException("Det finns inget tidsavstånd för jaktstart än"); // Om inga tidsavstånd är satta för jaktstart
                }
                long currentTime = firstStartTime;
                for (int gap : timeGaps) {
                    startTimes.add(currentTime);  // Lägg till starttiden för varje åkare
                    currentTime += gap * 1000;  // Lägg till tidsavstånd för nästa åkare (gap i sekunder)
                }
                break;
        }
    }

    // Metod för att sätta tidsavstånd för jaktstart (endast för PURSUIT_START)
    public void setTimeGaps(List<Integer> timeGaps) {
        if (startType != StartType.PURSUIT_START) {
            throw new IllegalArgumentException("Tidsavstånd kan endast anges för jaktstart"); // Kasta ett undantag om starttypen inte är jaktstart
        }
        this.timeGaps = timeGaps;
        generateStartTimes(); // Uppdatera starttider när tidsavstånd sätts
    }

    // Getter för att hämta starttider som en lista av hh:mm:ss-format
    public List<String> getFormattedStartTimes() {
        List<String> formattedTimes = new ArrayList<>();
        for (Long time : startTimes) {
            formattedTimes.add(timeSimulator.formatTime(time));
        }
        return formattedTimes;
    }

}






		
