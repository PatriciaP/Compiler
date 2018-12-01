/*
 * IFMG - COMPILERS - 2018
 * Lexical Analysis - Info about the current token 
 */
package compiler.lexical_analysis;

/**
 *
 * @author Patricia Pieroni
 */
public class Current {

    int line;
    int column;
    Token token;
    String lexeme;

    public Current() {
        line = 1;
        column = 0;
        token = null;
        lexeme = "";
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return "Current Token: " + token + ", " + "line: " + line + ", column: " + column + ", " + "lexeme: " + lexeme;
    }

}
