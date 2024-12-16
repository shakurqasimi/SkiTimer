package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SkiTrack {

	private double trackLength;
	private List<Double> splitPoints;

	public SkiTrack() {
	}
	
	public SkiTrack(double trackLength) {
		this.trackLength = trackLength;
		this.splitPoints = new ArrayList<Double>();
	}
	
	public void setTrackLength (double trackLength) {
		this.trackLength = trackLength;

	}
	
	public double getTrackLength() {
		return trackLength;
	}
	
	public void setSplitPoints(List<Double> splitPointPositions) {
		for (Double position : splitPointPositions) {
			if (position < 0 || position > trackLength) {
				throw new IllegalArgumentException("Ledarplatsen ligger utanför spåret");
			}
		}
		this.splitPoints = new ArrayList<>(splitPointPositions);
		Collections.sort(this.splitPoints);
	}
	
	public List<Double> getSplitPoints() {
		return splitPoints;
	}
	
	

	


}
