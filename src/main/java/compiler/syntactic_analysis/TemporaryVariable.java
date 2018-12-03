package compiler.syntactic_analysis;

import java.util.ArrayList;
import java.util.List;

public class TemporaryVariable {

    //var temps
    public static int tempSeed = 0;
    public static List<String> freeTemps = new ArrayList<>();

    public static String createTemp(int actualBlock) {

        if (freeTemps.size() > 0) {
            return freeTemps.get(freeTemps.size() - 1);
        }
        tempSeed++;
        return "_block_" + actualBlock + " _temp_" + tempSeed;

    }

    public static void freeLabels(String temp) {
        for (int i = 0; i < freeTemps.size(); i++) {
            if (!freeTemps.get(i).equals(temp)) {
                freeTemps.add(temp);
            }
        }
    }
}
