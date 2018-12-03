package compiler.virtual_machine;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class VirtualMachine {

    private List<Quadruple> quadruples;

    // key = label, value = code index
    private HashMap<String, Integer> labels;

    private Set<String> vars;

    public VirtualMachine() {
    }

    public VirtualMachine(List<Quadruple> quadruples, HashMap<String, Integer> labels, Set<String> vars) {
        this.quadruples = quadruples;
        this.labels = labels;
        this.vars = vars;
    }


}
