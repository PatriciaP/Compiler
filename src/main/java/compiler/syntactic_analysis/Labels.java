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
    static int cont = 0;

    public static String createLabel() {

        return "_label_"+cont++;

    }


}
