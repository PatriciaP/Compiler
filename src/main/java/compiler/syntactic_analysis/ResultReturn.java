/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.syntactic_analysis;

import compiler.virtual_machine.Quadruple;

/**
 * @author Patricia Pieroni
 */
public class ResultReturn {

    public boolean leftValue;

    public Quadruple listQuadruple;

    public String nameResult;

    public ResultReturn() {
    }

    public ResultReturn(boolean leftValue, Quadruple listQuadruple, String nameResult) {
        this.leftValue = leftValue;
        this.listQuadruple = listQuadruple;
        this.nameResult = nameResult;
    }

    public boolean isLeftValue() {
        return leftValue;
    }

    public void setLeftValue(boolean leftValue) {
        this.leftValue = leftValue;
    }

    public Quadruple getListQuadruple() {
        return listQuadruple;
    }

    public void setListQuadruple(Quadruple listQuadruple) {
        this.listQuadruple = listQuadruple;
    }

    public String getNameResult() {
        return nameResult;
    }

    public void setNameResult(String nameResult) {
        this.nameResult = nameResult;
    }
}
