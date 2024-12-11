package application;

import java.util.Random;

public class speedSimulator {
	
	public double generateSpeed(double trackLength) {

		Random random = new Random();
		double maxSpeed = 0;
		double minSpeed = 0;

		if (trackLength < 50) {
			maxSpeed = 60 - (trackLength / 2.4);
			minSpeed = 30 - (trackLength / 2.4);
		} else {
			maxSpeed = 60 - (50 / 2.4);
			minSpeed = 30 - (50 / 2.4);
		}
		/*
		 * Linjärt avtagande i hastighet efter hur lång banan är. Efter 50 km blir
		 * ökar inte hastighetsavtagandet, vilket speglar hur atleters hastighet planar ut efter en viss längd och
		 * undviker att hastigheten kan bli negativ vid tillräckligt långa längder
		 */
		
		double speed = minSpeed + (maxSpeed - minSpeed) * random.nextDouble();

		double speedMperS = convertSpeed(speed);
		return speedMperS;

	}

	public double convertSpeed(double kmPerH) {
		// Omvandlar km/t till m/s
		double mPerS = kmPerH / 3.6;

		return mPerS;
	}

}
