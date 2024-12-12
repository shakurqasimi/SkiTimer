package application;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;

public class Start {
	
        // Alla starttyper
	public enum StartType {
		INDIVIDUAL_15,
		INDIVIDUAL_30,
		MASS_START, 
		PURSUIT_START
	}

	private StartType startType; // Vilken typ av start
	private LocalTime firstStartTime; // Första starttid
	private List<LocalTime> startTimes; // Lista över starttider för alla åkare
	private List<Integer> timeGaps; // Används endast för jaktstart

	// Konstruktor
	public Start(StartType startType, LocalTime firstStartTime) {
		this.startType = startType;
		this.firstStartTime = firstStartTime;
		this.startTimes = new ArrayList<>();
		this.timeGaps = new ArrayList<>();
		generateStartTimes();
	}

	// Starttider baserat på starttypen
	private void generateStartTimes() {
		startTimes.clear();
		switch (startType) {
		case INDIVIDUAL_15 -> {
			for (int i = 0; i < 50; i++) { // räknar med 50 åkare
				startTimes.add(firstStartTime.plusSeconds(15 * i));
			}
		}
		case INDIVIDUAL_30 -> {
			for (int i = 0; i < 50; i++) {
				startTimes.add(firstStartTime.plusSeconds(30 * i));
			}

		}
		case MASS_START -> {
			startTimes.add(firstStartTime); // Alla startar samtidigt
		}
		case PURSUIT_START -> {
			if (timeGaps.isEmpty()) {
				throw new IllegalStateException("Det finns inget tidsavstånd för jaktstart än");
			}
			LocalTime currentTime = firstStartTime;
			for (int gap : timeGaps) {
				startTimes.add(currentTime);
				currentTime = currentTime.plusSeconds(gap);
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

		public LocalTime getFirstStartTime() {
			return firstStartTime;
		}

		public void setFirstStartTime(LocalTime firstStartTime) {
			this.firstStartTime = firstStartTime;
			generateStartTimes();
		}

		public List<LocalTime> getStartTimes() {
			return startTimes;
		}

}




		
