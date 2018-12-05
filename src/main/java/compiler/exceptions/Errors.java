package compiler.exceptions;

import compiler.lexical_analysis.LexicalAnalysis;
import compiler.lexical_analysis.Token;

public class Errors extends RuntimeException {

    private static Throwable Syntactic;

    private static Throwable Semantic;

    public static RuntimeException SyntaticError(Token token) {
        String msg = "Syntactic Compilation Error !!  " +
                "\nLine " + LexicalAnalysis.current.getLine() + ",  Column "
                + LexicalAnalysis.current.getColumn() +
                "\nWas expecting  " + token.getDescription() + ", "
                + "but received Token " + LexicalAnalysis.current.getToken().getDescription() +
                ", lexeme: " + LexicalAnalysis.current.getLexeme();
        return new Exceptions(msg, Syntactic);
    }

    public static RuntimeException SemanticError(String msg) {
        if (msg != null)
            return new Exceptions(msg, Semantic);
        else
            return new Exceptions("Semantic Compilation Error !! " +
                    "\nLine " + LexicalAnalysis.current.getLine() + ",  Column "
                    + LexicalAnalysis.current.getColumn());
    }
}
