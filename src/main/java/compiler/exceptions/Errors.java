package compiler.exceptions;

import compiler.lexical_analysis.Token;
import compiler.syntactic_analysis.Parser;

public class Errors extends Throwable {

    private static Throwable Syntactic;

    private static Throwable Semantic;

    public static Throwable SyntaticError(Token token) {
        String msg = "Syntactic Compilation Error !!  " +
                "\nLine " + Parser.actualToken.getLine() + ",  Column "
                + Parser.actualToken.getColumn() +
                "\nWas expecting  " + token.getDescription() + ", "
                + "but received Token " + Parser.actualToken.getToken().getDescription() +
                ", lexeme: " + Parser.actualToken.getLexeme();
        return new Exceptions(msg, Syntactic);
    }

    public static Throwable SemanticError(String msg) {
        if (msg != null)
            return new Exceptions(msg, Semantic);
        else
            return new Exceptions("Semantic Compilation Error !! " +
                    "\nLine " + Parser.actualToken.getLine() + ",  Column "
                    + Parser.actualToken.getColumn());
    }
}
