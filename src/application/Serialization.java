package application;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.List;

public class Serialization {

    // Serialisera listan med åkare
    public static void serialize(List<Skier> skiers, String filename) {
    	
        try (FileOutputStream fileOutputStream = new FileOutputStream(filename);
             XMLEncoder xmlEncoder = new XMLEncoder(fileOutputStream)) {
            xmlEncoder.writeObject(skiers); // Skriv hela listan med åkare
            System.out.println("Skidåkare har sparats till fil: " + filename);
        } catch (IOException ex) {
            System.err.println("Fel vid serialisering av åkare: " + ex.getMessage());
        }
    }

    // Deserialisera listan med åkare
    public static List<Skier> deserialize(String filename) {
        List<Skier> skiers = null;
        try (FileInputStream fileInputStream = new FileInputStream(filename);
             XMLDecoder xmlDecoder = new XMLDecoder(fileInputStream)) {
            skiers = (List<Skier>) xmlDecoder.readObject();
            System.out.println("Skidåkare har laddats från fil: " + filename);
        } catch (IOException ex) {
            System.err.println("Fel vid deserialisering av åkare: " + ex.getMessage());
        }
        return skiers;
    }
}