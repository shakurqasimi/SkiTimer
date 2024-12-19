package application;

import java.util.ArrayList;
import java.util.List;

public class Start {

    public enum StartType {
        INDIVIDUAL_15, 
        INDIVIDUAL_30, 
        MASS_START, 
        PURSUIT_START
    }

    private StartType startType;
    private TimeSimulator timeSimulator;
    private List<Long> startTimes;
    private long firstStartTime;
    private List<Integer> timeGaps;

    public Start(StartType startType, List<Skier> skiers) {
        this.startType = startType;
        this.startTimes = new ArrayList<>();
        this.timeGaps = new ArrayList<>();
        this.timeSimulator = new TimeSimulator();
        this.firstStartTime = timeSimulator.generateTime();
        generateStartTimes(skiers);
    }

    public void setStartType(StartType startType, List<Skier> skiers) {
        this.startType = startType;
        generateStartTimes(skiers);
    }

    public void generateStartTimes(List<Skier> skiers) {
        int i = 0;
        startTimes.clear();

        switch (startType) {
            case INDIVIDUAL_15:
                for (Skier skier : skiers) {
                    long startTime = 15 * i * 1000;
                    skier.setStartTime(startTime);
                    skier.setPreviousTime(startTime);
                    startTimes.add(startTime);
                    i++;
                }
                break;
            case INDIVIDUAL_30:
                for (Skier skier : skiers) {
                    long startTime = 30 * i * 1000;
                    skier.setStartTime(startTime);
                    skier.setPreviousTime(startTime);
                    startTimes.add(startTime);
                    i++;
                }
                break;
            case MASS_START:
                for (Skier skier : skiers) {
                    skier.setStartTime(0);
                    skier.setPreviousTime(0);
                }
                startTimes.add(firstStartTime);
                break;
            case PURSUIT_START:
            	
            	  skiers.sort((s1, s2) -> Double.compare(s1.getRaceTime(), s2.getRaceTime()));

            	long currentStartTime = firstStartTime;
            	for (int j = 0; j < skiers.size(); j++) {
            		Skier skier = skiers.get(j);

            	    // Sätt starttiden för åkaren baserat på jaktstarten
            		skier.setStartTime(currentStartTime);
            		skier.setPreviousTime(currentStartTime);

            		// Lägg till starttiden i listan för formatering
            		startTimes.add(currentStartTime);
    

            		// Beräkna mellanskillnaden i sluttid till nästa åkare (om det finns en nästa)
            		if (j < skiers.size() - 1) {
            			// Beräkna skillnaden i race time mellan åkarna
            			double timeGap = skiers.get(j + 1).getRaceTime() - skier.getRaceTime();
            			currentStartTime += (long) (timeGap * 1000); // Konvertera till millisekunder
            		}
            	}
            	break;
        }
    }


    public void setTimeGaps(List<Integer> timeGaps, List<Skier> skiers) {
        this.timeGaps = timeGaps;

        long currentStartTime = firstStartTime;
        for (int i = 0; i < skiers.size(); i++) {
            Skier skier = skiers.get(i);
            skier.setStartTime(currentStartTime);
            if (i < timeGaps.size()) {
                currentStartTime += timeGaps.get(i) * 1000L;
            }
        }
    }

    public List<String> getFormattedStartTimes(List<Skier> skiers) {
        List<String> formattedTimes = new ArrayList<>();
        int position = 1; // Första åkaren är på plats 1
        for (Skier skier : skiers) {
            String timeStr = timeSimulator.formatTime(skier.getStartTime());
            String resultLine = "Plats " + position + ": Åkare " + skier.getSkierNumber() + " - " + timeStr;
            formattedTimes.add(resultLine);
            position++;
        }
        return formattedTimes;
    }
}
