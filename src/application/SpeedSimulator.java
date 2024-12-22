package application;

import java.util.Random;

public class SpeedSimulator {
	public static double generateSpeed(int skierNumber) {
		double trackLength = SkiTrack.getTrackLength();
		double maxSpeed = 0;
		double minSpeed = 0;
		
		long seed = skierNumber * 123456789L + System.nanoTime();
		
        Random random = new Random(seed);


        double variation = random.nextDouble(-5.0, 5.0);

		if (trackLength < 50000) {
			maxSpeed = 60 - (trackLength / 1000 * 2.4);
			minSpeed = 30 - (trackLength / 1000 * 2.4);
		} else {
			maxSpeed = 60 - (50 / 1000 * 2.4);
			minSpeed = 30 - (50 / 1000 * 2.4);
		}
		
		double speed = minSpeed + (maxSpeed - minSpeed) * random.nextDouble() + variation;

		double speedMperS = convertSpeed(speed);
		return speedMperS;

	}

	public static double convertSpeed(double kmPerH) {
		double mPerS = kmPerH / 3.6;

		return mPerS;
	}


}