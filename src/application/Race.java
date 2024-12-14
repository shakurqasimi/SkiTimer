package application;

import java.text.DecimalFormat;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;


public class Race extends Task<Void> {
	
    private List<Skier> skiers;
	private boolean raceHasStarted = false;
	private TimeSimulator timeSimulator;
	private static final DecimalFormat df = new DecimalFormat("0.00");

	public Race(List<Skier> skiers, double speedFactor) throws InterruptedException {
		this.skiers = skiers;
		this.timeSimulator = new TimeSimulator(speedFactor);
		
	}


	@Override
	protected Void call() throws Exception {
		raceHasStarted = true;
		
		while (raceHasStarted) {

			for (Skier skier : skiers) {
				 //Här läggs bakgrundsuppdateringar under körningen
				skier.move(timeSimulator.generateTime());
				 // updaterar åkarnas position beroende på passerad tid sedan start
			}
			
			Platform.runLater(() -> {
				 //Här läggs UI uppdateringar under körningen
				for (Skier skier : skiers) {

					System.out.print(
							"Åkare: " + skier.getStartnumber() + " har åkt " + df.format(skier.getPosition()) + " meter");
					System.out.println(" och har åktiden: " + timeSimulator.formatTime(skier.getRaceTime(timeSimulator.generateTime())));

				}
              
            });


			Thread.sleep(1000);
			// Begränsar hur snabbt loopen körs
		}
		return null;
	}
	

}