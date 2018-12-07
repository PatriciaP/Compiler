package compiler.virtual_machine;

import compiler.lexical_analysis.LexicalAnalysis;
import compiler.syntactic_analysis.Parser;
import compiler.syntactic_analysis.ResultReturn;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class VirtualMachine {


    public static int lenghtVariables;

    public static List<Variables> variablesList;

    private static int lenVariablesMachine;

    private static List<Variables> listVariablesMachine;


    private ResultReturn codeResult;

    // key = label, value = code index
    private HashMap<String, Integer> labels = new HashMap<>();

    private HashMap<String, Object> vars = new HashMap<>();

    private Set<String> operators = new HashSet<>();


    public VirtualMachine(ResultReturn codeResult, HashMap<String, Integer> labels, HashMap<String, Object> vars) {
        this.codeResult = codeResult;
        this.labels = labels;
        this.vars = vars;
    }


    private VirtualMachine() {
    }

    public static void main(String[] args) throws Throwable {
        String fileName = "src\\main\\java\\compiler\\input\\test";

        VirtualMachine virtualMachine = new VirtualMachine();
        virtualMachine.start(fileName);


    }

    private void createLabels() {
        //storage the index of code labels
        for (int i = 0; i < this.codeResult.getListQuadruple().size(); i++) {

            if (this.codeResult.getListQuadruple().get(i).getparam1().equals("label")) {
                this.labels.put(this.codeResult.getListQuadruple().get(i).getparam2(), i);
            }

        }
    }

    private void populateOperators() {
        operators.add("-");
        operators.add("+");
        operators.add("/");
        operators.add("//");
        operators.add("%");
        operators.add("*");
        operators.add("=");
        operators.add(">=");
        operators.add("<=");
        operators.add("<");
        operators.add(">");
        operators.add("==");
        operators.add("!=");
        operators.add("&&");
        operators.add("||");
        operators.add("!");
    }


    private void start(String fileName) throws Throwable {

        LexicalAnalysis.file = new File("/home/renan/IdeaProjects/compiler/src/main/java/compiler/input/test").getCanonicalFile();
        LexicalAnalysis.contentFile = LexicalAnalysis.loadArq(LexicalAnalysis.file.toPath());
        System.out.println(LexicalAnalysis.contentFile);
        this.populateOperators();
        this.codeResult = Parser.parser();
        this.createLabels();
        this.run();
        System.exit(0);
    }


    private void run(List<Quadruple> list) {

        List<Quadruple> quadruple = list;
        int i;



        for (i = 0; i < list.size(); i++) {
            //int 1 -> float, int 0 -> int
            int type;
            String op = quadruple.get(i).op;
            String opquadruple;
            float value;
            String str;

            switch (op) {

                case "=":
                    type = Integer.parseInt(quadruple.get(i).getparam2());
                    //float
                    if (type == 1) {
                        value = Float.parseFloat(quadruple.get(i).getparam3());
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 1));
                    } else {
                        value = Integer.parseInt(quadruple.get(i).getparam3());
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 0));
                    }

                    break;
                case "==":
                    type = Integer.parseInt(quadruple.get(i).getparam2());
                    if (quadruple.get(i).getparam3().equals(quadruple.get(i).getparam4())) {
                        //true
                        value = 1;
                    } else {
                        value = 0;
                    }
                    //float
                    if (type == 1) {
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 1));
                    } else {
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 0));
                    }
                    break;
                case ">":
                    type = Integer.parseInt(quadruple.get(i).getparam2());

                    //float
                    if (type == 1) {
                        if (Float.parseFloat(quadruple.get(i).getparam3()) > Float.parseFloat(quadruple.get(i).getparam4())) {
                            //true
                            value = 1;
                        } else {
                            value = 0;
                        }
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 1));
                    } else {
                        if (Integer.parseInt(quadruple.get(i).getparam3()) > Integer.parseInt(quadruple.get(i).getparam4())) {
                            //true
                            value = 1;
                        } else {
                            value = 0;
                        }
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 0));
                    }
                    break;
                case ">=":
                    type = Integer.parseInt(quadruple.get(i).getparam2());

                    //float
                    if (type == 1) {
                        if (Float.parseFloat(quadruple.get(i).getparam3()) >= Float.parseFloat(quadruple.get(i).getparam4())) {
                            //true
                            value = 1;
                        } else {
                            value = 0;
                        }
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 1));
                    } else {
                        if (Integer.parseInt(quadruple.get(i).getparam3()) >= Integer.parseInt(quadruple.get(i).getparam4())) {
                            //true
                            value = 1;
                        } else {
                            value = 0;
                        }
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 0));
                    }
                    break;
                case "<":
                    type = Integer.parseInt(quadruple.get(i).getparam2());

                    //float
                    if (type == 1) {
                        if (Float.parseFloat(quadruple.get(i).getparam3()) < Float.parseFloat(quadruple.get(i).getparam4())) {
                            //true
                            value = 1;
                        } else {
                            value = 0;
                        }
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 1));
                    } else {
                        if (Integer.parseInt(quadruple.get(i).getparam3()) < Integer.parseInt(quadruple.get(i).getparam4())) {
                            //true
                            value = 1;
                        } else {
                            value = 0;
                        }
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 0));
                    }
                    break;
                case "<=":
                    type = Integer.parseInt(quadruple.get(i).getparam2());

                    //float
                    if (type == 1) {
                        if (Float.parseFloat(quadruple.get(i).getparam3()) <= Float.parseFloat(quadruple.get(i).getparam4())) {
                            //true
                            value = 1;
                        } else {
                            value = 0;
                        }
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 1));
                    } else {
                        if (Integer.parseInt(quadruple.get(i).getparam3()) <= Integer.parseInt(quadruple.get(i).getparam4())) {
                            //true
                            value = 1;
                        } else {
                            value = 0;
                        }
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 0));
                    }
                    break;
                case "!=":
                    type = Integer.parseInt(quadruple.get(i).getparam2());

                    //float
                    if (type == 1) {
                        if (Float.parseFloat(quadruple.get(i).getparam3()) != Float.parseFloat(quadruple.get(i).getparam4())) {
                            //true
                            value = 1;
                        } else {
                            value = 0;
                        }
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 1));
                    } else {
                        if (Integer.parseInt(quadruple.get(i).getparam3()) != Integer.parseInt(quadruple.get(i).getparam4())) {
                            //true
                            value = 1;
                        } else {
                            value = 0;
                        }
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 0));
                    }
                    break;
                case "!":
                    type = Integer.parseInt(quadruple.get(i).getparam2());

                    //float
                    if (type == 1) {
                        if (Float.parseFloat(quadruple.get(i).getparam3()) == 0) {
                            //true
                            value = 1;
                        } else {
                            value = 0;
                        }
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 1));
                    } else {
                        if (Integer.parseInt(quadruple.get(i).getparam3()) == 0) {
                            //true
                            value = 1;
                        } else {
                            value = 0;
                        }
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 0));
                    }
                    break;
                case "&&":
                    type = Integer.parseInt(quadruple.get(i).getparam2());

                    //float
                    if (type == 1) {
                        if (Float.parseFloat(quadruple.get(i).getparam3()) == 1 && Float.parseFloat(quadruple.get(i).getparam4()) == 1) {
                            //true
                            value = 1;
                        } else {
                            value = 0;
                        }
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 1));
                    } else {
                        if (Integer.parseInt(quadruple.get(i).getparam3()) == 1 && Integer.parseInt(quadruple.get(i).getparam4()) == 1) {
                            //true
                            value = 1;
                        } else {
                            value = 0;
                        }
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 0));
                    }
                    break;
                case "||":
                    type = Integer.parseInt(quadruple.get(i).getparam2());

                    //float
                    if (type == 1) {
                        if (Float.parseFloat(quadruple.get(i).getparam3()) == 0 && Float.parseFloat(quadruple.get(i).getparam4()) == 0) {
                            //true
                            value = 0;
                        } else {
                            value = 1;
                        }
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 1));
                    } else {
                        if (Integer.parseInt(quadruple.get(i).getparam3()) == 0 && Integer.parseInt(quadruple.get(i).getparam4()) == 0) {
                            //true
                            value = 0;
                        } else {
                            value = 1;
                        }
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 0));
                    }
                    break;
                case "if":
                    type = Integer.parseInt(quadruple.get(i).getparam2());

                    //float
                    if (type == 1) {
                        if (Float.parseFloat(quadruple.get(i).getparam2()) == 1) {
                            //true
                            i = getLabel(list, quadruple.get(i).getparam3());
                            continue;
                        } else {
                            i = getLabel(list,quadruple.get(i).getparam4());
                            continue;
                        }

                    } else {
                        if (Integer.parseInt(quadruple.get(i).getparam2()) == 1) {
                            //true
                            i = getLabel(list, quadruple.get(i).getparam3());
                            continue;
                        } else {
                            i = getLabel(list,quadruple.get(i).getparam4());
                            continue;
                        }
                    }
                    break;
                case "jump":
                    i = getLabel(list, quadruple.get(i).getparam2());
                    continue;
                    break;
                case "call":
                    type = Integer.parseInt(quadruple.get(i).getparam2());
                    break;
                case "+":
                    type = Integer.parseInt(quadruple.get(i).getparam2());

                    //float
                    if (type == 1) {

                        value = Float.parseFloat(quadruple.get(i).getparam3()) + Float.parseFloat(quadruple.get(i).getparam4());
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 1));
                    } else {
                        value = Integer.parseInt(quadruple.get(i).getparam3()) + Integer.parseInt(quadruple.get(i).getparam4());
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 0));
                    }
                    break;
                case "-":
                    type = Integer.parseInt(quadruple.get(i).getparam2());

                    //float
                    if (type == 1) {

                        value = Float.parseFloat(quadruple.get(i).getparam3()) - Float.parseFloat(quadruple.get(i).getparam4());
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 1));
                    } else {
                        value = Integer.parseInt(quadruple.get(i).getparam3()) - Integer.parseInt(quadruple.get(i).getparam4());
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 0));
                    }
                case "*":
                    type = Integer.parseInt(quadruple.get(i).getparam2());

                    //float
                    if (type == 1) {

                        value = Float.parseFloat(quadruple.get(i).getparam3()) * Float.parseFloat(quadruple.get(i).getparam4());
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 1));
                    } else {
                        value = Integer.parseInt(quadruple.get(i).getparam3()) * Integer.parseInt(quadruple.get(i).getparam4());
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 0));
                    }
                case "/":
                    type = Integer.parseInt(quadruple.get(i).getparam2());

                    //float
                    if (type == 1) {

                        value = Float.parseFloat(quadruple.get(i).getparam3()) / Float.parseFloat(quadruple.get(i).getparam4());
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 1));
                    } else {
                        value = Integer.parseInt(quadruple.get(i).getparam3()) / Integer.parseInt(quadruple.get(i).getparam4());
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 0));
                    }
                    break;
                case "%":
                    type = Integer.parseInt(quadruple.get(i).getparam2());

                    //float
                    if (type == 1) {

                        value = Float.parseFloat(quadruple.get(i).getparam3()) % Float.parseFloat(quadruple.get(i).getparam4());
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 1));
                    } else {
                        value = Integer.parseInt(quadruple.get(i).getparam3()) % Integer.parseInt(quadruple.get(i).getparam4());
                        listVariablesMachine.add(new Variables(quadruple.get(i).getparam2(), value, 0));
                    }
                    break;
            }
        }
        System.out.println("\nFinalized: without return.\n");


    }


    private int getLabel (List<Quadruple> aux, String lexeme){
        for (int i = 0; i < aux.size() ; i++) {
            if(aux.get(i).getparam2().equals("label")){
                if(aux.get(i).getparam3().equals(lexeme)){
                    return i;
                }
            }
        }
        return -1;
    }




}
