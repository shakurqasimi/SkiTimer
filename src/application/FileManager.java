package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
	public void saveSkierData(List<Skier> skiers, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Skier skier : skiers) {
                writer.write(skier.getNumber() + "," + skier.getStartTime() + "," + skier.getSplitTime() + "," + skier.getFinishTime());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Skier> loadSkierData(String filePath) {
        List<Skier> skiers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Skier skier = new Skier(Integer.parseInt(data[0]), LocalTime.parse(data[1]));
                skier.setSplitTime(LocalTime.parse(data[2]));
                skier.setFinishTime(LocalTime.parse(data[3]));
                skiers.add(skier);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return skiers;
    }


}
