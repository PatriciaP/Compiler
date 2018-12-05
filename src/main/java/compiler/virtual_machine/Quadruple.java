package compiler.virtual_machine;

import java.util.ArrayList;
import java.util.List;

/**
 * Which instruction has a struct with a quadruple
 * (operation, address1, arg2, result)
 */
public class Quadruple {



    public String op;

    public String arg1;

    public String arg2;

    public String result;


    public Quadruple() {
    }

    public Quadruple(String op) {
        this.op = op;
    }

    //params operators
    public Quadruple(String op, String arg1) {
        this.op = op;
        this.arg1 = arg1;
    }

    // unary operators  (x = y)
    public Quadruple(String op, String arg1, String arg2) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }


    //example x = y op z, arg1: y, arg2: z and reuslt: x
    public Quadruple(String op, String arg1, String arg2, String result) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public static List<Quadruple> concatenateLists(List<Quadruple> list1, List<Quadruple> list2) {
        List<Quadruple> resultList = new ArrayList<>();
        if (list1 != null)
            resultList = list1;
        if (list2 != null)
            resultList.addAll(list2);
        return resultList;
    }


    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "\nQuadruple{" +
                "op='" + op + '\'' +
                ", arg1='" + arg1 + '\'' +
                ", arg2='" + arg2 + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
