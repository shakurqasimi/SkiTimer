package application;

import java.text.DecimalFormat;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;


public class Race extends Task<Void> {
	
    private Start start;
    private List<Skier> skiers;
	private boolean raceHasStarted = false;
	TimeSimulator timeSimulator = new TimeSimulator(1);
	private static final DecimalFormat df = new DecimalFormat("0.00");

	public Race(Start start, List<Skier> skiers, long currentTime) throws InterruptedException {
		this.start = start;
		this.skiers = skiers;
		
	}


	@Override
	protected Void call() throws Exception {
		raceHasStarted = true;
		while (raceHasStarted) {

			for (Skier skier : skiers) {
				skier.move(timeSimulator.generateTime());
				// Inom denna loop updateras åkarnas position beroende på tid
			}
			
			Platform.runLater(() -> {
				for (Skier skier : skiers) {
					skier.move(timeSimulator.generateTime());
					System.out.println(
							"Åkare: " + skier.getStartnumber() + " har åkt " + df.format(skier.getPosition()) + " meter");
					// Inom denna loop updateras åkarnas position beroende på tid
				}
               //Här läggs UI uppdateringar under körningen
            });


			Thread.sleep(1000);
			// Begränsar hur snabbt loopen körs
		}
		return null;
	}
	

}
