package application;
import java.util.ArrayList;
import java.util.List;

public class Start {


	// Alla starttyper
	public enum StartType {
		INDIVIDUAL_15,
		INDIVIDUAL_30,
		MASS_START, 
		PURSUIT_START
	}

	private StartType startType; // Vilken typ av start
	private TimeSimulator timeSimulator;
	private List<Long> startTimes; // Lista över starttider i millisekunder
	private long firstStartTime; // första starttiden hämtas från TimeSimulator
	private List<Integer> timeGaps; // Används endast för jaktstart

	// Konstruktor
	public Start(StartType startType, double speedFactor) {
		this.startType = startType;
		this.startTimes = new ArrayList<>();
		this.timeGaps = new ArrayList<>();
		this.timeSimulator = new TimeSimulator(speedFactor);
		this.firstStartTime = timeSimulator.getCurrentTimeMillis() / 1000; // Hämta starttiden från TimeSimulator
		generateStartTimes();
	}

	// Starttider baserat på starttypen
	private void generateStartTimes() {
		startTimes.clear();
		
		switch (startType) {
		case INDIVIDUAL_15 -> {
			for (int i = 0; i < 10; i++) { // räknar med 10 åkare
				startTimes.add(firstStartTime + (15 * i * 1000));
			}
		}
		case INDIVIDUAL_30 -> {
			for (int i = 0; i < 10; i++) {
				startTimes.add(firstStartTime + (30 * i * 1000));
			}

		}
		case MASS_START -> {
			startTimes.add(firstStartTime); // Alla startar samtidigt
		}
		case PURSUIT_START -> {
			if (timeGaps.isEmpty()) {
				throw new IllegalStateException("Det finns inget tidsavstånd för jaktstart än");
			}
			long currentTime = firstStartTime;
			for (int gap : timeGaps) {
				startTimes.add(currentTime);
				currentTime += gap * 1000;
			}
		}
		}
	}
		
		// Ställ in tidsavstånd för jaktstart
		public void setTimeGaps(List<Integer> timeGaps) {
			if (startType != StartType.PURSUIT_START) {
				throw new IllegalArgumentException("Tidsavstånd kan endast anges för jaktstart");
			}
			this.timeGaps = timeGaps;
			generateStartTimes(); // Uppdatera starttider
		}

		// Getter och Setter
		public StartType getStartType() {
			return startType;
		}

		public void setStartType(StartType startType) {
			this.startType = startType;
			generateStartTimes();
		}


		public List<Long> getStartTimes() {
			
			return startTimes;
		}
		
		public String getCurrentTime() {
			return timeSimulator.getCurrentTime();
		}
		
		public long getCurrentTimeMillis() {
			return timeSimulator.getCurrentTimeMillis();
		}
		
		

}





		
