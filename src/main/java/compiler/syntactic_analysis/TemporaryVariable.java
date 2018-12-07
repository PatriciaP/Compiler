package compiler.syntactic_analysis;

import java.util.ArrayList;
import java.util.List;

public class TemporaryVariable {

    //var temps
    public static int tempSeed = 0;
    public static List<String> freeTemps = new ArrayList<>();

    public static String createTemp(int actualBlock) {

        tempSeed++;
        return   " _temp_" + tempSeed;

    }

}
