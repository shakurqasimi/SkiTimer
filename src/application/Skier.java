package application;

import java.text.DecimalFormat;
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
	private Result result;
	private static final DecimalFormat df = new DecimalFormat("0.00");
	private TimeSimulator timeSimulator;


	public Skier() {
	}

	public Skier(int startnumber, int skierNumber, double speed, SkiTrack skiTrack) {
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
		if (currentTime >= startTime) {
		position = speed * deltaTime / 1000;
		previousTime = currentTime;
		}
		//if satsen säkerställer att åkaren bara kan röra sig om de har startat
	}
	
	public void setRaceTime(long currentTime) {
		if (currentTime >= startTime) {
		this.raceTime = currentTime - startTime;
		}
	}
	
	public long getRaceTime() {
		return raceTime;
	}
	
	public void checkSplitPoints(double updatedPosition) {
		for(int i = 0; i < track.getSplitPoints().size(); i++) {
			double splitPointPosition = updatedPosition;
			if(!passedSplitPoints.get(i) && position >= track.getSplitPoints().get(i)) {
				passedSplitPoints.set(i, true);
				System.out.println("ÅKARE " + skierNumber + " HAR PASSERAT VID " + df.format(splitPointPosition) + " METER" +
				" OCH ÅKTTIDEN " + timeSimulator.formatTime(raceTime) );
				
			}
		}
	}
	
	public void checkFinishLine(double updatedPosition) {
		if(!hasFinished && position >= track.getTrackLength()) {
			double splitPointPosition = updatedPosition;
			hasFinished = true;
			System.out.println("ÅKARE " + skierNumber + " HAR PASSERAT MÅLLINJEN VID " + df.format(splitPointPosition) + " METER");

		}
	}

}
