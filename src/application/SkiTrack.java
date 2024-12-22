package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SkiTrack {

	private static double trackLength;
	private static List<Double> splitPoints;

	public SkiTrack() {
	}

	public SkiTrack(double trackLength, List<Double> splitPointPositions) {
		splitPoints = new ArrayList<Double>();
	}

	public static void setTrackLength(double trackLengthInput) {
		trackLength = trackLengthInput;

	}

	public static double getTrackLength() {
		return trackLength;
	}

	public void setSplitPoints(List<Double> splitPointPositions) {
		for (Double position : splitPointPositions) {
			if (position < 0 || position > trackLength) {
				throw new IllegalArgumentException("Stationen ligger utanför spåret");
			}
		}
		splitPoints = new ArrayList<>(splitPointPositions);
		Collections.sort(splitPoints);
	}

	public static List<Double> getSplitPoints() {
		return splitPoints;
	}
	
	public static Double getSplitPoint(int index) {
		return splitPoints.get(index);
	}
}
