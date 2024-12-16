package application;

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Serialization {

    // Serialisera en lista med Result-objekt till XML
    public void serialize(Map<Integer, Long> map, String filename) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(filename));
            XMLEncoder xmlEncoder = new XMLEncoder(fileOutputStream);
            xmlEncoder.writeObject(map);
            xmlEncoder.close();
            fileOutputStream.close();

            System.out.println("Data har sparats till " + filename);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Map<Integer, Long> deserialize(String filename) {
        Map<Integer, Long> loadedResults = new HashMap<>();
        try {
            // Skapa en FileInputStream för att läsa från filen
            FileInputStream fileInputStream = new FileInputStream(new File(filename));
            XMLDecoder xmlDecoder = new XMLDecoder(fileInputStream);
            // Dekoda XML och hämta den deserialiserade Map
            loadedResults = (Map<Integer, Long>) xmlDecoder.readObject();
            xmlDecoder.close();  // Stäng XMLDecoder
            fileInputStream.close();  // Stäng FileInputStream

            System.out.println("Data har laddats från " + filename);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return loadedResults;
    }
}





