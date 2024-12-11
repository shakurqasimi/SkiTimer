package application;

import java.time.LocalTime;

public interface TimeInterface {
	void addSkier(int number, LocalTime startTime);
    void registerSplitTime(int number, LocalTime time);
    void registerFinishTime(int number, LocalTime finishTime);

}
