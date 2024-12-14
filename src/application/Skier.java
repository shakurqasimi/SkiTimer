package application;

import java.util.List;

public class Skier {
	private int Startnumber;
	private double position;
	private double speed;
	private long startTime;
	private long previousTime;
	private long raceTime;
	
	TimeSimulator timeSimulator = new TimeSimulator(1);
	speedSimulator speedSimulator = new speedSimulator();

	public Skier() {
	}

	public Skier(int startnumber, double position, double speed, long startTime, long raceTime) {
		Startnumber = startnumber;
		this.position = 0;
		this.speed = speed;
		this.startTime = startTime;
		this.raceTime = raceTime;
	}

	public int getStartnumber() {
		return Startnumber;
	}

	public void setStartnumber(int startnumber) {
		Startnumber = startnumber;
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
		long deltaTime = currentTime - previousTime;
		if (currentTime >= startTime) {
		position += speed * (deltaTime / 1000);
		previousTime = currentTime;
		}
		//if satsen säkerställer att åkaren bara kan röra sig om de har startat
	}
	
	public long getRaceTime(long currentTime) {
		if (currentTime >= startTime) {
		raceTime = currentTime - startTime;
		}
		return raceTime;
	}

}
