package application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SkierGenerator {


    public static List<Skier> generateSkiers(int count) {
        if (count <= 0) {
            count = 10; 
        }

        List<Skier> skiers = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            skiers.add(new Skier(i, i, SpeedSimulator.generateSpeed(1)));
        }
        return skiers;
    }

    public static List<Skier> generateSkiers() {
        return generateSkiers(10);
    }
}
