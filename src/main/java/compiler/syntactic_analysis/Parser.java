/*
 * IFMG - COMPILERS - 2018
 * Syntactic Analysis
 */
package compiler.syntactic_analysis;

import compiler.lexical_analysis.Current;
import compiler.lexical_analysis.Token;

import java.util.HashMap;

/**
 *
 * @author Patricia Pieroni
 */
public class Parser {

    public static Current actualToken;

    public static HashMap<String,Token> symbolsTable;



    //token validation
//    public static void token_consume(Token token) {
//        System.out.println("Token expected " + token + " Token received " + actualToken.getToken());
//        if (actualToken.getToken().equals(token)) {
//            actualToken = LexicalAnalysis.getToken();
//        } else {
//            error(actualToken, token);
//        }
//    }

//    public static void token_consume(Set<Token> tokens) {
//        System.out.println("Token expected " + token + " Token received " + actualToken.getToken());
//        if (tokens.contains(actualToken.getToken().equals(token))) {
//            actualToken = LexicalAnalysis.getToken();
//        } else {
//            error(actualToken, token);
//        }
//    }

//    private static void error(Current current, Token token) {
//
//        System.out.println("Compilation ERROR !!  Line " + current.getLine() + ",  Column "
//                + current.getColumn());
//        System.out.println("Was expecting  " + token.getDescription() + ", "
//                + "but received Token " + actualToken.getToken().getDescription() + ", lexeme: " + actualToken.getLexeme());
//        System.exit(0);
//    }
//
//    private static void invalidAttribution(Current current) {
//
//        System.out.println("Compilation ERROR !! Invalid Attribution in line " + current.getLine() + ",  column "
//                + current.getColumn());
//        System.out.println("Invalid Attribution");
//        System.exit(0);
//    }
//
//    public static void parser() {
//        actualToken = LexicalAnalysis.getToken();
//        function();
//    }
//
//    /*GRAMATIC MINI C*/
//    private static void function() {
//        type();
//        token_consume(Token.IDENT);
//        token_consume(Token.OPEN_PAR);
//        argList();
//        token_consume(Token.CLOSE_PAR);
//        bloco();
//
//    }
//
//    private static void argList() {
//
//        if (actualToken.getToken().equals(Token.INT)
//                || actualToken.getToken().equals(Token.FLOAT)) {
//            arg();
//            restoArgList();
//        }
//
//    }
//
//    private static void arg() {
//        type();
//        token_consume(Token.IDENT);
//    }
//
//    private static void restoArgList() {
//        if (actualToken.getToken().equals(Token.COMMA)) {
//            token_consume(Token.COMMA);
//            argList();
//        }
//    }
//
//    private static ResultReturn type() {
//        ResultReturn r = new ResultReturn();
//        if (actualToken.getToken().equals(Token.INT) || actualToken.getToken().equals(Token.FLOAT)) {
//            r.type = actualToken.getToken();
//            //
//            token_consume(actualToken.getToken());
//        } else {
//            //
//        }
//
//        return r;
//
//    }
//
//    private static void bloco() {
//        token_consume(Token.OPEN_BRACKET);
//        stmtList();
//        token_consume(Token.CLOSE_BRACKET);
//
//    }
//
//    public static void stmtList() {
//
//        if (actualToken.getToken().equals(Token.FOR)
//                || actualToken.getToken().equals(Token.SCAN)
//                || actualToken.getToken().equals(Token.PRINT)
//                || actualToken.getToken().equals(Token.WHILE)
//                || actualToken.getToken().equals(Token.SUM)
//                || actualToken.getToken().equals(Token.NOT)
//                || actualToken.getToken().equals(Token.SUBTRACTION)
//                || actualToken.getToken().equals(Token.NUM_INT)
//                || actualToken.getToken().equals(Token.NUM_FLOAT)
//                || actualToken.getToken().equals(Token.IDENT)
//                || actualToken.getToken().equals(Token.OPEN_PAR)
//                || actualToken.getToken().equals(Token.IF)
//                || actualToken.getToken().equals(Token.OPEN_BRACKET)
//                || actualToken.getToken().equals(Token.BREAK)
//                || actualToken.getToken().equals(Token.CONTINUE)
//                || actualToken.getToken().equals(Token.INT)
//                || actualToken.getToken().equals(Token.FLOAT)
//                || actualToken.getToken().equals(Token.SEMICOLON)
//                || actualToken.getToken().equals(Token.RETURN)) {
//            stmt();
//            stmtList();
//        }
//    }
//
//    private static void stmt() {
//        if (actualToken.getToken().equals(Token.FOR)) {
//            forStmt();
//        } else if (actualToken.getToken().equals(Token.PRINT)
//                || (actualToken.getToken().equals(Token.SCAN))) {
//            ioStmt();
//        } else if (actualToken.getToken().equals(Token.WHILE)) {
//            whileStmt();
//        } else if (actualToken.getToken().equals(Token.NOT)
//                || actualToken.getToken().equals(Token.SUM)
//                || actualToken.getToken().equals(Token.SUBTRACTION)
//                || actualToken.getToken().equals(Token.NUM_INT)
//                || actualToken.getToken().equals(Token.NUM_FLOAT)
//                || actualToken.getToken().equals(Token.IDENT)
//                || actualToken.getToken().equals(Token.OPEN_PAR)) {
//            expr();
//            token_consume(Token.SEMICOLON);
//        } else if (actualToken.getToken().equals(Token.IF)) {
//            ifStmt();
//        } else if (actualToken.getToken().equals(Token.OPEN_BRACKET)) {
//            bloco();
//        } else if (actualToken.getToken().equals(Token.BREAK)) {
//            token_consume(Token.BREAK);
//            token_consume(Token.SEMICOLON);
//        } else if (actualToken.getToken().equals(Token.CONTINUE)) {
//            token_consume(Token.CONTINUE);
//        } else if (actualToken.getToken().equals(Token.INT)
//                || actualToken.getToken().equals(Token.FLOAT)) {
//            declaration();
//        } else if (actualToken.getToken().equals(Token.RETURN)) {
//            token_consume(Token.RETURN);
//            fator();
//            token_consume(Token.SEMICOLON);
//        } else {
//            token_consume(Token.SEMICOLON);
//        }
//
//    }
//
//    private static ResultReturn declaration() {
//        type();
//        identList();
//        token_consume(Token.SEMICOLON);
//    }
//
//    private static void identList() {
//        token_consume(Token.IDENT);
//        restoIdentList();
//    }
//
//    private static void restoIdentList() {
//        if (actualToken.getToken().equals(Token.COMMA)) {
//            token_consume(Token.COMMA);
//            token_consume(Token.IDENT);
//            restoIdentList();
//        }
//    }
//
//    private static void forStmt() {
//        token_consume(Token.FOR);
//        token_consume(Token.OPEN_PAR);
//        optExpr();
//        token_consume(Token.SEMICOLON);
//        optExpr();
//        token_consume(Token.SEMICOLON);
//        optExpr();
//        token_consume(Token.CLOSE_PAR);
//        stmt();
//    }
//
//    private static void optExpr() {
//        if (actualToken.getToken().equals(Token.NOT)
//                || actualToken.getToken().equals(Token.SUM)
//                || actualToken.getToken().equals(Token.SUBTRACTION)
//                || actualToken.getToken().equals(Token.NUM_INT)
//                || actualToken.getToken().equals(Token.NUM_FLOAT)
//                || actualToken.getToken().equals(Token.IDENT)
//                || actualToken.getToken().equals(Token.OPEN_PAR)) {
//            expr();
//        }
//
//    }
//
//    private static void ioStmt() {
//        if (actualToken.getToken().equals(Token.SCAN)) {
//            token_consume(Token.SCAN);
//            token_consume(Token.OPEN_PAR);
//            token_consume(Token.STR);
//            token_consume(Token.COMMA);
//            token_consume(Token.IDENT);
//            token_consume(Token.CLOSE_PAR);
//            token_consume(Token.SEMICOLON);
//        } else {
//            token_consume(Token.PRINT);
//            token_consume(Token.OPEN_PAR);
//            outList();
//            token_consume(Token.CLOSE_PAR);
//            token_consume(Token.SEMICOLON);
//        }
//
//    }
//
//    private static void outList() {
//        out();
//        restoOutList();
//    }
//
//    private static void out() {
//        switch (actualToken.getToken()) {
//            case STR:
//                token_consume(Token.STR);
//                break;
//            case IDENT:
//                token_consume(Token.IDENT);
//                break;
//            case NUM_INT:
//                token_consume(Token.NUM_INT);
//                break;
//            default:
//                token_consume(Token.NUM_FLOAT);
//                break;
//        }
//    }
//
//    private static void restoOutList() {
//        if (actualToken.getToken().equals(Token.COMMA)) {
//            token_consume(Token.COMMA);
//            out();
//            restoOutList();
//        }
//    }
//
//    private static void whileStmt() {
//        token_consume(Token.WHILE);
//        token_consume(Token.OPEN_PAR);
//        expr();
//        token_consume(Token.CLOSE_PAR);
//        stmt();
//    }
//
//    private static void ifStmt() {
//        token_consume(Token.IF);
//        token_consume(Token.OPEN_PAR);
//        expr();
//        token_consume(Token.CLOSE_PAR);
//        stmt();
//        elsePart();
//    }
//
//    private static void elsePart() {
//        if (actualToken.getToken().equals(Token.ELSE)) {
//            token_consume(Token.ELSE);
//            stmt();
//        }
//    }
//
//    private static void expr() {
//        atrib();
//    }
//
//    //WHEN RETURN FALSE, MEANS THATH CAN NOT SHOW ON THE LEFT SIDE
//    // value that can show on the left side of the '='
//    private static void atrib() {
//        boolean flag = or();
//        boolean flagAux = restoAtrib();
//        if (!flag && !flagAux) {
//            invalidAttribution(actualToken);
//
//        }
//    }
//
//    //arrumar reuslt
//    private static ResultReturn restoAtrib() {
//        ResultReturn r = new ResultReturn();
//        if (actualToken.getToken().equals(Token.RECEIVE)) {
//            token_consume(Token.RECEIVE);
//            atrib();
//            r.setLeftValue(false);
//        } else {
//            r.setLeftValue(true);
//        }
//        return r;
//    }
//
//    private static ResultReturn or() {
//        boolean flag = and();
//        boolean flagAux = restoOr();
//        return (flag && flagAux);
//
//    }
//
//    private static ResultReturn restoOr() {
//        if (actualToken.getToken().equals(Token.OR)) {
//            token_consume(Token.OR);
//            and();
//            restoOr();
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    private static ResultReturn and() {
//        boolean flag = not();
//        boolean flagAux = restoAnd();
//        return (flag && flagAux);
//    }
//
//    private static ResultReturn restoAnd() {
//        if (actualToken.getToken().equals(Token.AND)) {
//            token_consume(Token.AND);
//            not();
//            restoAnd();
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    private static ResultReturn not() {
//        if (actualToken.getToken().equals(Token.NOT)) {
//            token_consume(Token.NOT);
//            not();
//            return false;
//        } else {
//            return rel();
//        }
//    }
//
//    private static ResultReturn rel() {
//        boolean flag = add();
//        boolean flagAux = restoRel();
//        return (flag && flagAux);
//    }
//
//    private static ResultReturn restoRel() {
//        switch (actualToken.getToken()) {
//            case EQUAL:
//                token_consume(Token.EQUAL);
//                add();
//                return false;
//            case DIFFERENT:
//                token_consume(Token.DIFFERENT);
//                add();
//                return false;
//            case SMALLER:
//                token_consume(Token.SMALLER);
//                add();
//                return false;
//            case SMALLER_EQUAL:
//                token_consume(Token.SMALLER_EQUAL);
//                add();
//                return false;
//            case BIGGER:
//                token_consume(Token.BIGGER);
//                add();
//                return false;
//            case BIGGER_EQUAL:
//                token_consume(Token.BIGGER_EQUAL);
//                add();
//                return false;
//            default:
//                return true;
//        }
//    }
//
//    private static ResultReturn add() {
//        boolean flag = mult();
//        boolean flagAux = restoAdd();
//        return (flag && flagAux);
//    }
//
//    private static ResultReturn restoAdd() {
//        switch (actualToken.getToken()) {
//            case SUM:
//                token_consume(Token.SUM);
//                mult();
//                restoAdd();
//                return false;
//            case SUBTRACTION:
//                token_consume(Token.SUBTRACTION);
//                mult();
//                restoAdd();
//                return false;
//            default:
//                return true;
//        }
//    }
//
//    private static ResultReturn mult() {
//        boolean flag = uno();
//        boolean flagAux = restoMult();
//        return (flag && flagAux);
//    }
//
//    private static ResultReturn restoMult() {
//        switch (actualToken.getToken()) {
//            case MULT:
//                token_consume(Token.MULT);
//                uno();
//                restoMult();
//                return false;
//            case DIV:
//                token_consume(Token.DIV);
//                uno();
//                restoMult();
//                return false;
//            case MOD:
//                token_consume(Token.MOD);
//                uno();
//                restoMult();
//                return false;
//            default:
//                return true;
//        }
//    }
//
//    private static ResultReturn uno() {
//
//        switch (actualToken.getToken()) {
//            case SUM:
//                token_consume(Token.SUM);
//                uno();
//                return false;
//            case SUBTRACTION:
//                token_consume(Token.SUBTRACTION);
//                uno();
//                return false;
//            default:
//                return fator();
//
//        }
//    }
//
//    private static ResultReturn fator() {
//        switch (actualToken.getToken()) {
//            case NUM_INT:
//                token_consume(Token.NUM_INT);
//                return false;
//            case NUM_FLOAT:
//                token_consume(Token.NUM_FLOAT);
//                return false;
//            case IDENT:
//                token_consume(Token.IDENT);
//                return true;
//            default:
//                token_consume(Token.OPEN_PAR);
//                atrib();
//                token_consume(Token.CLOSE_PAR);
//                return false;
//        }
//
//    }
}
