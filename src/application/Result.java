
package application;

import javafx.scene.control.TextArea;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Result {

	private Map<Integer, Long> splitTimesSkiNum;
	private Map<Integer, Long> splitTimesStartNum;

	private Map<Integer, Long> finishTimesSkiNum;
	private Map<Integer, Long> finishTimesStartNum;

	private static final DecimalFormat df = new DecimalFormat("0.00");
	private TextArea resultArea;
	private Result result;
	private SkiTrack track;
	private TextArea statusArea;

	public Result(SkiTrack track, TextArea resultArea, TextArea statusArea) {
		this.splitTimesSkiNum = new HashMap<>();
		this.splitTimesStartNum = new HashMap<>();

		this.finishTimesSkiNum = new HashMap<>();
		this.finishTimesStartNum = new HashMap<>();

		this.track = track;
		this.resultArea = resultArea;
		this.statusArea = statusArea;
	}

	// mellantid (100 m)
	public void registerSplitTime(int skierNumber, int startNumber, long raceTime, double splitPoint) {
		if (!finishTimesSkiNum.containsKey(skierNumber)) {
			splitTimesSkiNum.put(skierNumber, raceTime);
			splitTimesStartNum.put(startNumber, raceTime);

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
		}
		return "Mellantid ej registrerad";
	}

	public String getFinishTime(int skierNumber) {
		if (finishTimesSkiNum.containsKey(skierNumber)) {
			return formatTime(finishTimesSkiNum.get(skierNumber));
		}
		return "Sluttid ej registrerad";
	}

	public Map<Integer, Long> getSplitTimesStartNum() {
		return splitTimesStartNum;
	}

	public Map<Integer, Long> getFinishTimesStartNum() {
		return finishTimesStartNum;
	}

	public String formatTime(long milliseconds) {
		Duration duration = Duration.ofMillis(milliseconds);
		long minutes = duration.toMinutes();
		long seconds = duration.getSeconds() % 60;
		long millis = duration.toMillisPart();
		return String.format("%02d:%02d.%03d", minutes, seconds, millis);
	}

	public void displayResults() {
		StringBuilder sb = new StringBuilder();

		for (int skierNumber : splitTimesSkiNum.keySet()) {
			sb.append("Åkare ").append(skierNumber).append(":\n");
			sb.append("Mellantid: ").append(getSplitTime(skierNumber)).append("\n");
			sb.append("Sluttid: ").append(getFinishTime(skierNumber)).append("\n");
			sb.append("------------------------------\n");
		}

		resultArea.setText(sb.toString());
	}

	public void clearResults() {
		splitTimesSkiNum.clear();
		splitTimesStartNum.clear();
		finishTimesSkiNum.clear();
		finishTimesStartNum.clear();

	}

	public void displayFinalResults(List<Skier> skiers) {
		System.out.println("Slutresultat (sorterat efter sluttid):");
		statusArea.appendText("\n " + "Slutresultat (sorterat efter sluttid):"+ "\n ");
		for (Skier skier : skiers) {
			System.out.println("Åkarnum: " + skier.getSkierNumber() + ", startnum: " + skier.getStartNumber()
					+ ", åktid: " + formatTime(skier.getRaceTime()));
			statusArea.appendText("Åkarnum: " + skier.getSkierNumber() + ", startnum: " + skier.getStartNumber()
					+ ", åktid: " + formatTime(skier.getRaceTime())+ "\n ");
		}

	}

	public void checkFinishLine(Skier skier) {
		if (!skier.hasFinished() && skier.getPosition() >= SkiTrack.getTrackLength()) {
			registerFinishTime(skier.getSkierNumber(), skier.getStartNumber(), skier.getRaceTime());
			skier.setHasFinished(true);
			System.out.println("ÅKARE " + skier.getSkierNumber() + " HAR PASSERAT MÅLLINJEN VID "
					+ df.format(skier.getPosition()) + " METER");

		}
	}

	public void checkSplitPoints(Skier skier) {
		List<Boolean> passedSplitPoints = skier.getPassedSplitPoints();
		for (int i = 0; i < SkiTrack.getSplitPoints().size(); i++) {
			if (!passedSplitPoints.get(i) && skier.getPosition() >= track.getSplitPoints().get(i)) {
				passedSplitPoints.set(i, true);
				registerSplitTime(skier.getSkierNumber(), skier.getStartNumber(), skier.getRaceTime(),
						SkiTrack.getSplitPoint(i));
			}
		}
	}

	public List<Map.Entry<Integer, Long>> getSortedSplitTimes() {
		return splitTimesStartNum.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());
	}
}
