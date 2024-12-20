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
	private TimeSimulator timeSimulator;
	private static final DecimalFormat df = new DecimalFormat("0.00");
	private Result result;
	private ScheduledFuture<?> updateRaceFuture;
	private boolean raceFinished = false;
	private boolean raceFinishedFinal = false;

	private long raceStartTime;
	private long raceFinishTime;
	private List<Skier> skiers;

	public Race(List<Skier> skiers, double speedFactor, Result result) throws InterruptedException {
		this.skiers = skiers;
		this.timeSimulator = new TimeSimulator(speedFactor);
		this.result = result;

	}

	public Race() {

	}
	
	public boolean hasFinished() {
		return raceFinished;
	}

	public void InitializeRace(List<Skier> skiers, double speedFactor, Result result) {
		this.skiers = skiers;
		this.timeSimulator = new TimeSimulator(speedFactor);
		this.result = result;
	}
	

	public void resetRace() {
		for (Skier skier : skiers) {
			skier.resetSkier();
		}
		result.clearResults();
	    scheduler.shutdownNow();		
	}

	@Override
	protected Void call() throws Exception {
		raceStartTime = timeSimulator.generateTime();

		scheduler = Executors.newScheduledThreadPool(2);

		Runnable updateRace = () -> {
			synchronized (skiers) {

				for (Skier skier : skiers) {

					long currentTime = timeSimulator.generateTime();
					skier.move(currentTime);
					skier.setRaceTime(currentTime);
					result.checkSplitPoints(skier);
					result.checkFinishLine(skier);
					// Här läggs bakgrundsuppdateringar under körningen (setters)
					// updaterar åkarnas position beroende på passerad tid sedan start

				}
				if (skiers.stream().allMatch(Skier::hasFinished)) {
					// när alla är klara sorteras resultatet för serialisering
					raceFinishTime = timeSimulator.generateTime();
					skiers.sort((skier1, skier2) -> Long.compare(skier1.getRaceTime(), skier2.getRaceTime()));
					Serialization.serialize(skiers, "result.txt");
					raceFinished = true;
					resetRace();
					updateRaceFuture.cancel(false);
				}
			}

		};

		Runnable printRace = () -> {

			Platform.runLater(() -> {
				// Här skickas UI uppdateringar under körningen (getters)
				if (!raceFinished) {

					for (Skier skier : skiers) {
						System.out.print("Åkare: " + skier.getSkierNumber() + " har åkt "
								+ df.format(skier.getPosition()) + " meter");
						System.out.println(" och har åktiden: " + timeSimulator.formatTime(skier.getRaceTime()));

					}
				}
				if (raceFinished && !raceFinishedFinal) {
					result.displayFinalResults();
					System.out.println(
							"LOPPET AVSLUTAT EFTER " + timeSimulator.formatTime(raceFinishTime - raceStartTime));
					raceFinishedFinal = true;
				}

			});
		};
		scheduler.scheduleAtFixedRate(updateRace, 0, 16, TimeUnit.MILLISECONDS);
		scheduler.scheduleAtFixedRate(printRace, 0, 1, TimeUnit.SECONDS);

		return null;

	}

}