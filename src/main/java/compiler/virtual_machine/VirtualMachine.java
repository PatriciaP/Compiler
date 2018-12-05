package compiler.virtual_machine;

import compiler.lexical_analysis.LexicalAnalysis;
import compiler.syntactic_analysis.Parser;
import compiler.syntactic_analysis.ResultReturn;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class VirtualMachine {

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

            if (this.codeResult.getListQuadruple().get(i).getOp().equals("label")) {
                this.labels.put(this.codeResult.getListQuadruple().get(i).getArg1(), i);
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

        LexicalAnalysis.file = new File("src\\main\\java\\compiler\\input\\test").getCanonicalFile();
        LexicalAnalysis.contentFile = LexicalAnalysis.loadArq(LexicalAnalysis.file.toPath());
        System.out.println(LexicalAnalysis.contentFile);
        this.codeResult = Parser.parser();
        this.populateOperators();
        this.createLabels();
        this.run();
    }


    private void run() {

        // calls: scan, print
        // jumps: if, jump

        //process count
        int pc = 0;

        while (!this.codeResult.getListQuadruple().get(pc).getOp().equals("stop")) {

            //jump labels
            if (this.codeResult.getListQuadruple().get(pc).getOp().equals("label")) {
                pc++;

                //execute jumps
            } else if (this.codeResult.getListQuadruple().get(pc).getOp().matches("if||jump")) {

                String function = this.codeResult.getListQuadruple().get(pc).getOp();

                Object expression = this.codeResult.getListQuadruple().get(pc).getArg1();

                if (vars.containsKey(expression)) {
                    expression = vars.get(expression);
                }

                int newPC;

                if (function.equals("if")) {
                    //function if
                    newPC = ifFunction(expression,
                            this.codeResult.getListQuadruple().get(pc).getArg2(),
                            this.codeResult.getListQuadruple().get(pc).getResult());

                } else {
                    // function jump
                    newPC = jump(expression,
                            this.codeResult.getListQuadruple().get(pc).getArg2(),
                            this.codeResult.getListQuadruple().get(pc).getResult());

                }

                if (newPC != -1) {
                    pc = newPC;
                }

            }

            //execute operators
            else if (operators.contains(this.codeResult.getListQuadruple().get(pc).getOp())) {
                Object op1 = this.codeResult.getListQuadruple().get(pc).getArg2();
                Object op2 = this.codeResult.getListQuadruple().get(pc).getResult();

                String function = this.codeResult.getListQuadruple().get(pc).getOp();

                //verifies if the value of the operators is in some variable, determine the real value of each one
                if (vars.containsKey(op1)) {
                    op1 = vars.get(op1);
                }

                if (vars.containsKey(op2)) {
                    op2 = vars.get(op2);
                }

                Object result = null;
                //operation execute and treatment of results types
                switch (function) {

                    case "-":
                        result = subtraction(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                        break;
                    case "+":
                        result = sum(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                        break;
                    case "/":
                        result = division(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                        break;
                    case "%":
                        result = mod(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                        break;
                    case "*":
                        result = mult(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                        break;
                    case "=":
                        result = attribution(op1, op2);
                        break;
                    case ">=":
                        result = biggerEqual(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                        break;
                    case "<=":
                        result = smallerEqual(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                        break;
                    case "<":
                        result = smaller(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                        break;
                    case ">":
                        result = bigger(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                        break;
                    case "==":
                        result = equals(op1, op2);
                        break;
                    case "!=":
                        result = difference(op1, op2);
                        break;
                    case "&&":
                        result = and(op1, op2);
                        break;
                    case "||":
                        result = or(op1, op2);
                        break;
                    case "!":
                        result = not(op1, op2);
                        break;

                }

                if (this.vars.containsKey(this.codeResult.getListQuadruple().get(pc).getArg1())) {
                    String type = (String) this.vars.get(this.codeResult.getListQuadruple().get(pc).getArg1());
                    if (type.equals("int")) {
                        result = Integer.parseInt(String.valueOf(result));
                    }
                }

                this.vars.put(this.codeResult.getListQuadruple().get(pc).getArg1(), String.valueOf(result));


            } else if (this.codeResult.getListQuadruple().get(pc).getOp().matches("scan||print")) {

                String function = this.codeResult.getListQuadruple().get(pc).getOp();

                String op1 = this.codeResult.getListQuadruple().get(pc).getArg2();

                String op2 = this.codeResult.getListQuadruple().get(pc).getResult();


                if (function.equals("scan")) {

                    if (this.codeResult.getListQuadruple().get(pc).getArg1() != null) {
                        String type = (String) this.vars.get(this.codeResult.getListQuadruple().get(pc).getArg1());
                        if (type.equals("int")) {
                            this.vars.put(this.codeResult.getListQuadruple().get(pc).getArg1(), Integer.parseInt((String) scan(op1, op2)));
                        } else {
                            this.vars.put(this.codeResult.getListQuadruple().get(pc).getArg1(), Float.parseFloat((String) scan(op1, op2)));

                        }
                    }

                } else {
                    print(op1, op2);

                }


                pc++;

            }

        }


    }


    private int ifFunction(Object expression, String label1, String label2) {

        if (expression.equals("false")) {
            return labels.get(label2);
        }

        return -1;
    }


    private Integer jump(Object index, String label1, String label2) {
        return labels.get(index);
    }

    private Float sum(Float x, Float y) {
        return x + y;
    }

    private Float subtraction(Float x, Float y) {
        return x - y;
    }

    private Float division(Float x, Float y) {
        return x / y;
    }

    private Float mult(Float x, Float y) {
        return x * y;
    }

    private Float mod(Float x, Float y) {
        return x % y;

    }

    private Object attribution(Object x, Object y) {
        return x;
    }

    private Boolean biggerEqual(Float x, Float y) {
        return x >= y;
    }

    private Boolean smallerEqual(Float x, Float y) {
        return x <= y;
    }

    private Boolean difference(Object x, Object y) {
        return !x.equals(y);
    }

    private Boolean bigger(Float x, Float y) {
        return x > y;
    }

    private Boolean smaller(Float x, Float y) {
        return x < y;
    }

    private Boolean and(Object x, Object y) {
        Boolean bX = true, bY = true;
        if (x.equals("false")) {
            bX = false;
        }
        if (y.equals("false")) {
            bY = false;
        }
        return bX && bY;
    }

    private Boolean or(Object x, Object y) {
        Boolean bX = true, bY = true;
        if (x.equals("false")) {
            bX = false;
        }
        if (y.equals("false")) {
            bY = false;
        }
        return bX || bY;
    }

    private Boolean not(Object x, Object y) {
        Boolean bX = true, bY = true;
        if (x.equals("false")) {
            bX = false;
        }
        if (y.equals("false")) {
            bY = false;
        }
        return !bY;
    }

    private Boolean equals(Object x, Object y) {
        return x.equals(y);
    }


    private Object scan(Object x, Object y) {
        String str = "";
        if (x != null) {
            return x;
        } else {
            return str;
        }

    }

    private String print(Object x, Object y) {
        if (x != null) {
            System.out.print(x);
        }
        if (y != null) {
            System.out.print(y);
        }
        return null;
    }



}
