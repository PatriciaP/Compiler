package compiler.syntactic_analysis;

import java.util.ArrayList;
import java.util.List;


/**
 * Used to label (to name) a section of the code,
 * so that we can define a region od the code through a label .
 * <p>
 * Each instruction can be preceded by a label.
 */
public class Labels {

    //labels
    public static int labelSeed = 0;
    public static List<String> freeLabels = new ArrayList<>();

    public static String createLabel() {

        if (freeLabels.size() > 0) {
            return freeLabels.get(freeLabels.size() - 1);
        }
        labelSeed++;
        return "_label_" + labelSeed;

    }

    public static void freeLabels(String label) {
        for (int i = 0; i < freeLabels.size(); i++) {
            if (!freeLabels.get(i).equals(label)) {
                freeLabels.add(label);
            }
        }
    }
}
