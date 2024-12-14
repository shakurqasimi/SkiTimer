package application;

import java.time.Duration;

public class TimeSimulator {
	private long startingTime;
	//starttid i realtid
	private double speedFactor;

	//med hur många gånger snabbt tiden ska gå, så om faktorn är 1 så körs verkligt tidsflöde

public TimeSimulator(double speedFactor){
	this.startingTime = System.currentTimeMillis();
	this.speedFactor = speedFactor;
}

public TimeSimulator(){
	this.startingTime = System.currentTimeMillis();
}

	public long generateTime() {
		long elapsedTime = System.currentTimeMillis() - startingTime; 
		long time = (long) (elapsedTime * speedFactor);
		
		return time;
		//Rå tid för användning av uträkningar, koppla denna till åkare
	}
	
	public String formatTime(long time) {

		Duration duration = Duration.ofMillis((long) time);

		long hours = duration.toHours();
		long minutes = duration.toMinutesPart();
		long seconds = duration.toSecondsPart();
		long millis = duration.toMillisPart();
		return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, millis);
		//Formaterad tid för läsbarhet

	}

}
