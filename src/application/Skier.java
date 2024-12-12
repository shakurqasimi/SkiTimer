package application;

public class Skier {
	private int Startnumber;
	private double position;
	private double speed;
	private long startTime;
	
	TimeSimulator timeSimulator = new TimeSimulator(20);
	speedSimulator speedSimulator = new speedSimulator();

	public Skier() {
	}

	public Skier(int startnumber, double position, double speed, long startTime) {
		Startnumber = startnumber;
		this.position = 0;
		this.speed = speed;
		this.startTime = startTime;
		//Ska skötas av startklassen senare
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

	public double getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void move(long currentTime) {
		double deltaTime = (currentTime - startTime);
		if (currentTime > startTime) {
		position += speed * (deltaTime / 1000);
		startTime = currentTime;
		}
	}
	
	public long getRaceTime(long currentTime) {
		long raceTime = currentTime - startTime;
		return raceTime;
	}

}
