package application;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Skier {
	private int startNumber;
	private int skierNumber; // Unikt åkarnummer
	private double position;
	private double speed;
	private long startTime;
	private long previousTime;
	private long raceTime;
	private List<Boolean> passedSplitPoints; // vilka stationer deltagern passerat
	private boolean hasFinished; // om deltagaren åkt i mål
	private SkiTrack track;
	private static final DecimalFormat df = new DecimalFormat("0.00");


	public Skier() {
	}

	public Skier(int startnumber, int skierNumber, double speed, SkiTrack skiTrack, TimeSimulator timeSimulator) {
		this.skierNumber = skierNumber;
		this.startNumber = startnumber;
		this.speed = speed;
		this.track = skiTrack;
		this.passedSplitPoints = new ArrayList<>(track.getSplitPoints().size());
		this.hasFinished = false;

		for (int i = 0; i < track.getSplitPoints().size(); i++) {
			passedSplitPoints.add(false);
		}
	}

	public int getStartnumber() {
		return startNumber;
	}

	public void setStartnumber(int startnumber) {
		startNumber = startnumber;
	}
	
	public int getSkierNumber() {
		return skierNumber;
	}

	public void setSkierNumber(int skierNumber) {
		this.skierNumber = skierNumber;
	}

	public double getPosition() {
		return position;
	}

	public void setPosition(double position) {
		this.position = position;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getPreviousTime() {
		return previousTime;
	}

	public void setPreviousTime(long previousTime) {
		this.previousTime = previousTime;
	}
	
	

	public void move(long currentTime) {
		double deltaTime = currentTime - startTime;
		if (currentTime >= startTime && !hasFinished) {
		position = speed * deltaTime / 1000;
		previousTime = currentTime;
		}
		//if satsen säkerställer att åkaren bara kan röra sig om de har startat och inte gått i mål
	}
	
	public void setRaceTime(long currentTime) {
		if (currentTime >= startTime) {
		this.raceTime = currentTime - startTime;
		}
	}
	
	public long getRaceTime() {
		return raceTime;
	}
	
	public void checkSplitPoints(Result result) {
		for(int i = 0; i < track.getSplitPoints().size(); i++) {
			if(!passedSplitPoints.get(i) && position >= track.getSplitPoints().get(i)) {
				passedSplitPoints.set(i, true);
				result.registerSplitTime(skierNumber, raceTime);
			}
		}
	}
	
	
	public void checkFinishLine(Result result) {
		if(!hasFinished && position >= track.getTrackLength()) {
			result.registerFinishTime(skierNumber, raceTime);
			hasFinished = true;
			System.out.println("ÅKARE " + skierNumber + " HAR PASSERAT MÅLLINJEN VID " + df.format(position) + " METER");

		}
	}

}
