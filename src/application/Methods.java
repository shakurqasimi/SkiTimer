package application;

import java.time.LocalTime;
import java.util.Scanner;

public class Methods {
	TimeTracking timeTracking = new TimeTracking();
    Scanner scanner = new Scanner(System.in);

    public void addSkier() {
        timeTracking.addSkier(1, LocalTime.of(10, 0, 0));
        timeTracking.addSkier(2, LocalTime.of(10, 0, 15));
        timeTracking.addSkier(3, LocalTime.of(10, 0, 30));

        timeTracking.startRace();
    }

    public void registerSplitTime() {
        timeTracking.registerSplitTime(1, LocalTime.of(10, 5, 30));
        timeTracking.registerFinishTime(1, LocalTime.of(10, 15, 0));
    }

    public void getSkierByNumber() {
        System.out.print("Enter skier number: ");
        int number = scanner.nextInt();
        Skier skier = timeTracking.getSkierByNumber(number);
        if (skier != null) {
            System.out.println("Skier " + skier.getNumber() + " - Start Time: " + skier.getStartTime());
            System.out.println("Skier " + skier.getNumber() + " - Split Time: " + skier.getSplitTime());
            System.out.println("Skier " + skier.getNumber() + " - Finish Time: " + skier.getFinishTime());
        } else {
            System.out.println("Skier not found.");
        }
    }
}



