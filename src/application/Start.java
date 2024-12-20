package application;

import java.util.ArrayList;
import java.util.Collections;
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
    private List<Skier> skiers;
    private List<Integer> timeGaps;

    public Start(StartType startType, List<Skier> skiers) {
        this.startType = startType;
        this.startTimes = new ArrayList<>();
        this.skiers = skiers;
        this.timeGaps = new ArrayList<>();
        this.timeSimulator = new TimeSimulator();
        this.firstStartTime = timeSimulator.generateTime();
        generateStartTimes();
    }
    
    public Start() {
    	
    }


    public void generateStartTimes() {
        int i = 0;
        
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
                for (Skier skier : skiers) {
                    skier.setRaceTime(0);
                    skier.setPreviousTime(0);
                    skier.setPosition(0);
                }


            	break;
        }
    }
    
    
    public void calculatePursuitTimeGaps (List<Skier> previousSkiers) {
    	long timeGap = previousSkiers.get(0).getStartTime();
    	previousSkiers.get(0).setStartTime(0);
    	previousSkiers.get(0).setPreviousTime(0);
    	for (int i = 1; i < previousSkiers.size(); i++) {
            Skier previousSkier = previousSkiers.get(i);
            timeGap += previousSkier.getRaceTime() - previousSkiers.get(i - 1).getRaceTime();
            previousSkier.setStartTime(timeGap);
            previousSkier.setPreviousTime(timeGap);
            System.out.println(previousSkier.getStartTime());
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
