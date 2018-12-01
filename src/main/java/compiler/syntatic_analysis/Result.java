/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.syntatic_analysis;

import compiler.lexical_analysis.Token;

import java.util.List;

/**
 *
 * @author Patricia Pieroni
 */
public class Result {

    public  boolean leftValue;

    public Token type;

    public List<Object> values;

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public Result() {
        values = new List<Object>;
    }

    public boolean isLeftValue() {
        return leftValue;
    }

    public void setLeftValue(boolean leftValue) {
        this.leftValue = leftValue;
    }
}
