package compiler.exceptions;

import compiler.lexical_analysis.Current;
import compiler.lexical_analysis.Token;

public class Errors extends RuntimeException {

    private static Throwable Syntactic;

    private static Throwable Semantic;

    public static RuntimeException SyntaticError(Token token, Current current) {
        String msg = "Syntactic Compilation Error !!  " +
                "\nLine " + current.getLine() + ",  Column "
                + current.getColumn() +
                "\nWas expecting  " + token.getDescription() + ", "
                + "but received Token " + current.getToken().getDescription() +
                ", lexeme: " + current.getLexeme();
        return new Exceptions(msg, Syntactic);
    }

    public static RuntimeException SemanticError(String msg, Current current) {
        String semantic = "Semantic Compilation Error !! " +
                "\nLine " + current.getLine() + ",  Column "
                + current.getColumn();
        if (msg != null)
            return new Exceptions(semantic +"\n"+ msg, Semantic);
        else
            return new Exceptions(semantic);
    }
}
