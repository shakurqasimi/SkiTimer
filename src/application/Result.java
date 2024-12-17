package application;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result {

	private Map<Integer, Long> splitTimes;
	private Map<Integer, Long> finishTimes;
	private SkiTrack track;
	private static final DecimalFormat df = new DecimalFormat("0.00");


	public Result(SkiTrack track) {
		this.splitTimes = new HashMap<>();
		this.finishTimes = new HashMap<>();
		this.track = track;

	}

	// mellantid (100 m)
	public void registerSplitTime(int skierNumber, long raceTime, double splitPoint) {
		if (!finishTimes.containsKey(skierNumber)) {
			splitTimes.put(skierNumber, raceTime);
			System.out.println("Åkare " + skierNumber + " registrerade mellantid vid "+ splitPoint + " m: " + formatTime(raceTime));
		}
	}

	// sluttid (20 km)
	public void registerFinishTime(int skierNumber, long raceTime) {
		if (!finishTimes.containsKey(skierNumber)) {
			finishTimes.put(skierNumber, raceTime);
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

	// slutresultat
	public void displayFinalResults() {
		System.out.println("Slutresultat (sorterat efter sluttid):");
		finishTimes.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.forEach(entry -> System.out.println("Åkare " + entry.getKey() + ": " + formatTime(entry.getValue())));
	}
	
	public void checkSplitPoints(Skier skier) {
		List<Boolean> passedSplitPoints = skier.getPassedSplitPoints();
		for (int i = 0; i < track.getSplitPoints().size(); i++) {
			if (!passedSplitPoints.get(i) && skier.getPosition() >= track.getSplitPoints().get(i)) {
				passedSplitPoints.set(i, true);
				registerSplitTime(skier.getSkierNumber(), skier.getRaceTime(), track.getSplitPoint(i));
			}
		}
	}
	
	public void checkFinishLine(Skier skier) {
		if (!skier.hasFinished() && skier.getPosition() >= track.getTrackLength()) {
			registerFinishTime(skier.getSkierNumber(), skier.getRaceTime());
			skier.setHasFinished(true);
			System.out
					.println("ÅKARE " + skier.getSkierNumber() + " HAR PASSERAT MÅLLINJEN VID " + df.format(skier.getPosition()) + " METER");

		}
	}

	public String formatTime(long time) {

		Duration duration = Duration.ofMillis((long) time);

		long hours = duration.toHours();
		long minutes = duration.toMinutesPart();
		long seconds = duration.toSecondsPart();
		long millis = duration.toMillisPart();
		return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, millis);
		// Formaterad tid för läsbarhet
	}

}
