package application;

public class Skier {
	private int Startnumber;
	private double position;
	private double speed;
	private double previousTime;
	TimeSimulator timeSimulator = new TimeSimulator(20);
	speedSimulator speedSimulator = new speedSimulator();

	public Skier() {
	}

	public Skier(int startnumber, double position, double speed, double previousTime) {
		Startnumber = startnumber;
		this.position = 0;
		this.speed = speed;
		this.previousTime = timeSimulator.generateTime();
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

	public double getPreviousTime() {
		return previousTime;
	}

	public void setPreviousTime(double previousTime) {
		this.previousTime = previousTime;
	}

	public void move(long currentTime) {

		double deltaTime = (currentTime - previousTime);
		position += speed * (deltaTime / 1000);
		previousTime = currentTime;
	}

}
