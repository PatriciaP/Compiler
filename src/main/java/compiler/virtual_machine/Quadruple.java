package compiler.virtual_machine;

/**
 * Which instruction has a struct with a quadruple
 * (operation, address1, address2, address3)
 */
public class Quadruple {


    public String op;

    public String address1;

    public String address2;

    public String address3;


    public Quadruple() {
    }

    public Quadruple(String op, String address1, String address2) {
        this.op = op;
        this.address1 = address1;
        this.address2 = address2;
    }

    public Quadruple(String op, String address1, String address2, String address3) {
        this.op = op;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
    }
}
