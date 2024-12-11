package application;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TimeTracking implements TimeInterface{
	Scanner scanner = new Scanner(System.in);
    private List<Skier> skiers = new ArrayList<>();

    @Override
    public void addSkier(int number, LocalTime startTime) {
    	System.out.println("ange åkares nummer");
    	number = scanner.nextInt();
        skiers.add(new Skier(number, startTime));
    }

    @Override
    public void registerSplitTime(int number, LocalTime time) {
        for (Skier skier : skiers) {
            if (skier.getNumber() == number) {
                skier.setSplitTime(time);
                break;
            }
        }
    }

    @Override
    public void registerFinishTime(int number, LocalTime finishTime) {
        for (Skier skier : skiers) {
            if (skier.getNumber() == number) {
                skier.setFinishTime(finishTime);
                break;
            }
        }
    }

    public Skier getSkierByNumber(int number) {
        for (Skier skier : skiers) {
            if (skier.getNumber() == number) {
                return skier;
            }
        }
        return null;
    }

    public List<Skier> getAllSkiers() {
        return new ArrayList<>(skiers);
    }
    
    public void startRace() {
        for (int i = 0; i < skiers.size(); i++) {
            Skier skier = skiers.get(i);
            LocalTime adjustedStartTime = skier.getStartTime().plusSeconds(i * 15); 
            skier.setStartTime(adjustedStartTime);
        }
    }


}
