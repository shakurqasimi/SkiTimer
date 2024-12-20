package application;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Skier implements Serializable {

	private static final long serialVersionUID = 1;
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

	public List<Boolean> getPassedSplitPoints() {
		return passedSplitPoints;
	}

	public void setPassedSplitPoints(List<Boolean> passedSplitPoints) {
		this.passedSplitPoints = passedSplitPoints;
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

	public boolean hasFinished() {
		return hasFinished;
	}

	public void setHasFinished(boolean hasFinished) {
		this.hasFinished = hasFinished;
	}

	public void setRaceTime(long currentTime) {
		if (currentTime >= startTime && !hasFinished) {
			this.raceTime = currentTime - startTime;
		} else {
			this.raceTime = 0;
		}
	}

	public long getRaceTime() {
		return raceTime;
	}

	public void move(long currentTime) {
		double deltaTime = currentTime - startTime;
		if (currentTime >= startTime && !hasFinished) {
			position = speed * deltaTime / 1000;
			previousTime = currentTime;
		}
		// if satsen säkerställer att åkaren bara kan röra sig om de har startat och
		// inte gått i mål
	}

}
