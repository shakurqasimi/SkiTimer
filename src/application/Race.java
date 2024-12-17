package application;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class Race extends Task<Void> {

	private ScheduledExecutorService scheduler;
	private List<Skier> skiers;
	private TimeSimulator timeSimulator;
	private static final DecimalFormat df = new DecimalFormat("0.00");
	private Result result;
	private ScheduledFuture<?> updateRaceFuture;
	private boolean raceFinished = false;
	private boolean raceFinishedFinal = false;
	private Start start;
	private long raceStartTime;
	private long raceFinishTime;


	public Race(List<Skier> skiers, double speedFactor, Result result) throws InterruptedException {
		this.skiers = skiers;
		this.timeSimulator = new TimeSimulator(speedFactor);
		this.result = result;

	}

	@Override
	protected Void call() throws Exception {

		raceStartTime = timeSimulator.generateTime();
		
		scheduler = Executors.newScheduledThreadPool(2);

		Runnable updateRace = () -> {

			for (Skier skier : skiers) {

				long currentTime = timeSimulator.generateTime();
				skier.move(currentTime);
				skier.setRaceTime(currentTime);
				skier.checkSplitPoints(result);
				skier.checkFinishLine(result);
				// Här läggs bakgrundsuppdateringar under körningen
				// updaterar åkarnas position beroende på passerad tid sedan start
			}
			if (skiers.stream().allMatch(Skier::hasFinished)) {
				Serialization.serialize(skiers, "result.txt");
				System.out.println("Resultat sparade");
				raceFinishTime = timeSimulator.generateTime();
				raceFinished = true;
				updateRaceFuture.cancel(false);
			}

		};

		Runnable printRace = () -> {

			Platform.runLater(() -> {
				// Här läggs UI uppdateringar under körningen
				if (!raceFinishedFinal) {
					if (raceFinished) {
						System.out.println("SLUTRESULTAT:");
						raceFinishedFinal = true;
					}
					for (Skier skier : skiers) {


						System.out.print("Åkare: " + skier.getSkierNumber() + " har åkt "
								+ df.format(skier.getPosition()) + " meter");
						System.out.println(" och har åktiden: " + timeSimulator.formatTime(skier.getRaceTime()));

					}
					if (raceFinishedFinal) {
						System.out.println("LOPPET AVSLUTAT EFTER " + timeSimulator.formatTime(raceFinishTime - raceStartTime));

					}
				}

			});
		};
		scheduler.scheduleAtFixedRate(updateRace, 0, 10, TimeUnit.MILLISECONDS);
		scheduler.scheduleAtFixedRate(printRace, 0, 1, TimeUnit.SECONDS);

		return null;

	}

}