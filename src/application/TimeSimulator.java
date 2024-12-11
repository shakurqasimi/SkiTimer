package application;

import java.time.Duration;

public class TimeSimulator {
	private long startingTime;
	//starttid i realtid
	//med hur många gånger snabbt tiden ska gå, så om faktorn är 1 så körs verkligt tidsflöde

public TimeSimulator(){
	this.startingTime = System.currentTimeMillis();
}

	public String generateTime(double speedFactor) {
		long elapsedTime = System.currentTimeMillis() - startingTime; 
		long time = (long) (elapsedTime * speedFactor);
		
		Duration duration = Duration.ofMillis((long) time);

		long hours = duration.toHours();
		long minutes = duration.toMinutesPart();
		long seconds = duration.toSecondsPart();
		long millis = duration.toMillisPart();
		return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, millis);

		
	}

}
