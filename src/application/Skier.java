package application;

import java.time.LocalTime;

public class Skier {
	private int number;
	private LocalTime startTime;
	private LocalTime splitTime;
	private LocalTime finishTime;
	
	public Skier(int number, LocalTime startTime) {
		this.number = number;
		this.startTime = startTime;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getSplitTime() {
		return splitTime;
	}

	public void setSplitTime(LocalTime splitTime) {
		this.splitTime = splitTime;
	}

	public LocalTime getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(LocalTime finishTime) {
		this.finishTime = finishTime;
	}


}
