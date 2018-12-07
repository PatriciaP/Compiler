package compiler.virtual_machine;

import java.util.ArrayList;
import java.util.List;

/**
 * Which instruction has a struct with a quadruple
 * (param1eration, address1, param3, param4)
 */
public class Quadruple {



    public String param1;

    public String param2;

    public String param3;

    public String param4;


    public Quadruple() {
    }

    public Quadruple(String param1) {
        this.param1 = param1;
    }

    //params param1erators
    public Quadruple(String param1, String param2) {
        this.param1 = param1;
        this.param2 = param2;
    }

    // unary param1erators  (x = y)
    public Quadruple(String param1, String param2, String param3) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
    }


    //example x = y param1 z, param2: y, param3: z and reuslt: x
    public Quadruple(String param1, String param2, String param3, String param4) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
    }

    public static List<Quadruple> concatenateLists(List<Quadruple> list1, List<Quadruple> list2) {
        List<Quadruple> param4List = new ArrayList<>();
        if (list1 != null)
            param4List = list1;
        if (list2 != null)
            param4List.addAll(list2);
        return param4List;
    }


    public String getparam1() {
        return param1;
    }

    public void setparam1(String param1) {
        this.param1 = param1;
    }

    public String getparam2() {
        return param2;
    }

    public void setparam2(String param2) {
        this.param2 = param2;
    }

    public String getparam3() {
        return param3;
    }

    public void setparam3(String param3) {
        this.param3 = param3;
    }

    public String getparam4() {
        return param4;
    }

    public void setparam4(String param4) {
        this.param4 = param4;
    }

    @Override
    public String toString() {
        return "\nQuadruple{" +
                "param1='" + param1 + '\'' +
                ", param2='" + param2 + '\'' +
                ", param3='" + param3 + '\'' +
                ", param4='" + param4 + '\'' +
                '}';
    }
}
