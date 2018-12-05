/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.syntactic_analysis;

import compiler.virtual_machine.Quadruple;

import java.util.List;

/**
 * @author Patricia Pieroni
 */
public class ResultReturn {

    //IF FALSE, MEANS THAT THE VALUE CAN NOT SHOW ON THE LEFT SIDE of '='
    private boolean leftValue;

    private List<Quadruple> listQuadruple;

    private String nameResult;

    public ResultReturn() {
    }

    public ResultReturn(List<Quadruple> listQuadruple) {
        this.listQuadruple = listQuadruple;
    }

    public ResultReturn(boolean leftValue, List<Quadruple> listQuadruple, String nameResult) {
        this.leftValue = leftValue;
        this.listQuadruple = listQuadruple;
        this.nameResult = nameResult;
    }

    public ResultReturn(List<Quadruple> listQuadruple, String nameResult) {
        this.listQuadruple = listQuadruple;
        this.nameResult = nameResult;
    }

    public boolean isLeftValue() {
        return leftValue;
    }

    public void setLeftValue(boolean leftValue) {
        this.leftValue = leftValue;
    }

    public List<Quadruple> getListQuadruple() {
        return listQuadruple;
    }

    public void setListQuadruple(List<Quadruple> listQuadruple) {
        this.listQuadruple = listQuadruple;
    }

    public String getNameResult() {
        return nameResult;
    }

    public void setNameResult(String nameResult) {
        this.nameResult = nameResult;
    }


}
