package application;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Result {
    private int athleteNumber;
    private LocalTime startTime;
    private List<LocalTime> splitTimes;
    private LocalTime finishTime;

    public Result(int athleteNumber, LocalTime startTime) {
        this.athleteNumber = athleteNumber;
        this.startTime = startTime;
        this.splitTimes = new ArrayList<>();
        this.finishTime = null;
    }

    // Lägg till mellantid
    public void addSplitTime(LocalTime splitTime) {
        splitTimes.add(splitTime);
    }

    // Registrera målgångstid
    public void setFinishTime(LocalTime finishTime) {
        this.finishTime = finishTime;
    }

    // Beräkna total åktid fram till en viss tidpunkt
    public String getCurrentRaceTime(LocalTime currentTime) {
        if (startTime == null) {
            return "Starttid saknas!";
        }
        long elapsedSeconds = java.time.Duration.between(startTime, currentTime).getSeconds();
        return formatTime(elapsedSeconds);
    }

    // Hämta åktid vid målgång
    public String getFinalRaceTime() {
        if (finishTime == null || startTime == null) {
            return "Tid ej tillgänglig.";
        }
        long elapsedSeconds = java.time.Duration.between(startTime, finishTime).getSeconds();
        return formatTime(elapsedSeconds);
    }

    // Hjälpmetod för att formatera tid i HH:MM:SS-format
    private String formatTime(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public int getAthleteNumber() {
        return athleteNumber;
    }

    public List<LocalTime> getSplitTimes() {
        return splitTimes;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getFinishTime() {
        return finishTime;
    }
}