package application;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result {

	private Map<Integer, Long> splitTimesSkiNum; 
	private Map<Integer, Long> splitTimesStartNum;

	private Map<Integer, Long> finishTimesSkiNum;
	private Map<Integer, Long> finishTimesStartNum;

	
	private SkiTrack track;
	private static final DecimalFormat df = new DecimalFormat("0.00");


	public Result(SkiTrack track) {
		this.splitTimesSkiNum = new HashMap<>();
		this.splitTimesStartNum = new HashMap<>();

		this.finishTimesSkiNum = new HashMap<>();
		this.finishTimesStartNum = new HashMap<>();

		this.track = track;

	}

	// mellantid (100 m)
	public void registerSplitTime(int skierNumber, int startNumber, long raceTime, double splitPoint) {
		if (!finishTimesSkiNum.containsKey(skierNumber)) {
			splitTimesSkiNum.put(skierNumber, raceTime);
			splitTimesStartNum.put(startNumber, raceTime);
			System.out.println("Åkare " + skierNumber + " registrerade mellantid vid "+ splitPoint + " m: " + formatTime(raceTime));
		}
	}


	// sluttid (20 km)
	public void registerFinishTime(int skierNumber, int startNumber, long raceTime) {
		if (!finishTimesSkiNum.containsKey(skierNumber)) {
			finishTimesSkiNum.put(skierNumber, raceTime);
			finishTimesStartNum.put(startNumber, raceTime);

		}
	}

	public String getSplitTime(int skierNumber) {
		if (splitTimesSkiNum.containsKey(skierNumber)) {
			return formatTime(splitTimesSkiNum.get(skierNumber));
		} else {
			return "Mellantid saknas för åkare " + skierNumber;
		}
	}
	
	public String getSplitTimeStartNum(int startNum) {
		if (splitTimesStartNum.containsKey(startNum)) {
			return formatTime(splitTimesStartNum.get(startNum));
		} else {
			return "Mellantid saknas för åkare med startnummer" + startNum;
		}
	}
	
	public  Map<Integer, Long> getSplitTimesStartNum(int startNum) {
		splitTimesStartNum.entrySet().stream().sorted(Map.Entry.comparingByValue());
			return splitTimesStartNum;
	}


	public String getFinishTime(int skierNumber) {
		if (finishTimesSkiNum.containsKey(skierNumber)) {
			return formatTime(finishTimesSkiNum.get(skierNumber));
		} else {
			return "Sluttid saknas för åkare " + skierNumber;
		}
	}
	public String getFinishTimeStartNum(int startNum) {
		if (finishTimesStartNum.containsKey(startNum)) {
			return formatTime(finishTimesStartNum.get(startNum));
		} else {
			return "Sluttid saknas för åkare med startnummer" + startNum;
		}
	}

	// slutresultat
	public void displayFinalResults() {
		System.out.println("Slutresultat (sorterat efter sluttid):");
		finishTimesSkiNum.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.forEach(entry -> System.out.println("Åkare " + entry.getKey() + ": " + formatTime(entry.getValue())));
	}
	
	public void checkSplitPoints(Skier skier) {
		List<Boolean> passedSplitPoints = skier.getPassedSplitPoints();
		for (int i = 0; i < track.getSplitPoints().size(); i++) {
			if (!passedSplitPoints.get(i) && skier.getPosition() >= track.getSplitPoints().get(i)) {
				passedSplitPoints.set(i, true);
				registerSplitTime(skier.getSkierNumber(), skier.getStartnumber(), skier.getRaceTime(), track.getSplitPoint(i));
			}
		}
	}
	
	public void clearResults() {
		splitTimesSkiNum.clear();
		splitTimesStartNum.clear();
		finishTimesSkiNum.clear();
		finishTimesStartNum.clear();
		
	}
	
	public void checkFinishLine(Skier skier) {
		if (!skier.hasFinished() && skier.getPosition() >= track.getTrackLength()) {
			registerFinishTime(skier.getSkierNumber(), skier.getStartnumber(), skier.getRaceTime());
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
