/*
 * IFMG - COMPILERS - 2018
 * Lexical Analysis - Tokens
 */
package compiler.lexical_analysis;

/**
 *
 * @author Patricia Pieroni
 */
public enum Token {
    
    
    
    //TOKENS PONTUATION
    OPEN_PAR("("), CLOSE_PAR(")"), COMMA(","), 
    OPEN_BRACKET("{"), CLOSE_BRACKET("}"), SEMICOLON(";"),
    RECEIVE("="),
    
    //DATA TYPE
    INT("int"), FLOAT("float"),  
    
    //TOKENS DATA
    IDENT("Identifier"), NUM_INT("Integer number"), 
    NUM_FLOAT("Float number"), STR("String"),
    
    
    //TOKENS OPERATIONS
    SUM("+"), SUBTRACTION("-"), MULT("*"), DIV("/"), 
    MOD("%"), OR("||"), AND("&&"), NOT("!"), EQUAL("=="),
    DIFFERENT("!="), BIGGER(">"), BIGGER_EQUAL(">="),
    SMALLER("<"), SMALLER_EQUAL("<="),
    
    
    //TOKENS KEYWORDS
    BREAK("break"), CONTINUE("continue"), FOR("for"), 
    SCAN("scan"), PRINT("print"),  WHILE("while"),  
    IF("if"), ELSE("else"), RETURN("return"),
    
    //END OF FILE
    EOF("End of file"),
    
    //ERROR
    ERROR("Not interpreted");
    

    public final String description;

    public String getDescription() {
        return description;
    }
    
    Token(String description){
        this.description = description;
    }
    
    
    
    
   
    
 
  
}
