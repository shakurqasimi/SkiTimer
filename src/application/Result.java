package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result {

    private Map<Integer, Long> splitTimes; 
    private Map<Integer, Long> finishTimes; 
    private long raceStartTime; 

    public Result(long raceStartTime) {
        this.splitTimes = new HashMap<>();
        this.finishTimes = new HashMap<>();
    }

   // mellantid (100 m)
    public void registerSplitTime(int skierNumber, long raceTime) {
        if (!splitTimes.containsKey(skierNumber)) {
            splitTimes.put(skierNumber, raceTime);
            System.out.println("Åkare " + skierNumber + " registrerade mellantid vid 100 m: " + formatTime(raceTime));
        }
    }

    //  sluttid (20 km)
    public void registerFinishTime(int skierNumber, long currentTime) {
        if (!finishTimes.containsKey(skierNumber)) {
            long elapsedTime = currentTime - raceStartTime;
            finishTimes.put(skierNumber, elapsedTime);
            System.out.println("Åkare " + skierNumber + " gick i mål: " + formatTime(elapsedTime));
        }
    }

     
    public String getSplitTime(int skierNumber) {
        if (splitTimes.containsKey(skierNumber)) {
            return formatTime(splitTimes.get(skierNumber));
        } else {
            return "Mellantid saknas för åkare " + skierNumber;
        }
    }

    
    public String getFinishTime(int skierNumber) {
        if (finishTimes.containsKey(skierNumber)) {
            return formatTime(finishTimes.get(skierNumber));
        } else {
            return "Sluttid saknas för åkare " + skierNumber;
        }
    }

    //  slutresultat 
    public void displayFinalResults() {
        System.out.println("Slutresultat (sorterat efter sluttid):");
        finishTimes.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> System.out.println(
                        "Åkare " + entry.getKey() + ": " + formatTime(entry.getValue())));
    }

    
    private String formatTime(long timeMillis) {
        long hours = timeMillis / 3600000;
        long minutes = (timeMillis % 3600000) / 60000;
        long seconds = (timeMillis % 60000) / 1000;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
