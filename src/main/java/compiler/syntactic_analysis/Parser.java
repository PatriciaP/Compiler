/*
 * IFMG - COMPILERS - 2018
 * Syntactic Analysis
 */
package compiler.syntactic_analysis;

import compiler.exceptions.Errors;
import compiler.lexical_analysis.Current;
import compiler.lexical_analysis.LexicalAnalysis;
import compiler.lexical_analysis.Token;
import compiler.virtual_machine.Quadruple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Patricia Pieroni
 */
public class Parser {


    public static Current actualToken;

    // name and type
    public static HashMap<String,Token> symbolsTable;
    private static List<String> idList;
    //Block Scope
    private static int actualBlock = -1;


    //token validation
    public static void token_consume(Token token) throws Throwable {
        System.out.println("Token expected " + token + " Token received " + actualToken.getToken());
        if (actualToken.getToken().equals(token)) {
            actualToken = LexicalAnalysis.getToken();
        } else {
            throw Errors.SyntaticError(token);
        }
    }

    private static String formatVarName(String var) {
        return "v_" + actualBlock + "_" + var;
    }

    // verifies if exists the variable in the scopes witch smaller or equal id
    private static Boolean searchVarScope(String var) {

        int index = var.indexOf('_', 2);
        int blockId = Integer.parseInt(var.substring(2, index));
        String varName = var.substring(index + 1);


        for (String name : symbolsTable.keySet()) {
            int indexV = name.indexOf('_', 2);
            int blockIdV = Integer.parseInt(name.substring(2, indexV));
            String varNameV = name.substring(indexV + 1);

            if (varNameV.equals(varName) && blockIdV <= blockId) {
                return true;
            }
        }

        return false;
    }

    //return the variable
    private static String getVarScope(String var) {

        int index = var.indexOf('_', 2);
        int blockId = Integer.parseInt(var.substring(2, index));
        String varName = var.substring(index + 1);

        for (String name : symbolsTable.keySet()) {
            int indexV = name.indexOf('_', 2);
            int blockIdV = Integer.parseInt(name.substring(2, indexV));
            String varNameV = name.substring(indexV + 1);

            if (varNameV.equals(varName) && blockIdV <= blockId) {
                return name;
            }
        }

        return var;
    }

    //free all the variables and temps created on the current block
    private static void freeBlock() {

        List<String> freeVars = new ArrayList<>();

        for (String name : symbolsTable.keySet()) {
            int index = name.indexOf('_', 2);
            int blockId = Integer.parseInt(name.substring(2, index));
            String varName = name.substring(index + 1);

            if (blockId == actualBlock) {
                freeVars.add(name);
            }

            //deleting the variables in the free list
            for (String nameVar : symbolsTable.keySet()) {
                for (String freeVar : freeVars) {
                    if (nameVar.equals(freeVar)) {
                        symbolsTable.remove(nameVar);
                    }
                }
            }

        }
    }


    public static List<Quadruple> parser() throws Throwable {
        List<Quadruple> quad;
        actualToken = LexicalAnalysis.getToken();
        quad = function();
        token_consume(actualToken.getToken());
        token_consume(Token.EOF);
        quad.add(new Quadruple("stop"));
        return quad;

    }


    /*GRAMATIC MINI C*/
    private static List<Quadruple> function() throws Throwable {
        List<Quadruple> quad;
        type();
        token_consume(Token.IDENT);
        token_consume(Token.OPEN_PAR);
        quad = argList();
        token_consume(Token.CLOSE_PAR);
        quad.add(bloco());
        return quad;

    }
//

//

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
private static void bloco(String begin, String end) {
    token_consume(Token.OPEN_BRACKET);
    stmtList();
    token_consume(Token.CLOSE_BRACKET);

}

    private static ResultReturn argList() throws Throwable {

        if (actualToken.getToken().equals(Token.INT)
                || actualToken.getToken().equals(Token.FLOAT)) {
            ResultReturn rArg = arg();
            ResultReturn rResto = restoArgList();

            return new ResultReturn(Quadruple.concatenateLists(rArg.getListQuadruple(), rResto.getListQuadruple()));
        } else {
            return new ResultReturn(new ArrayList<Quadruple>());
        }

    }

    private static ResultReturn restoArgList() throws Throwable {
        if (actualToken.getToken().equals(Token.COMMA)) {
            token_consume(Token.COMMA);
            argList();
            return new ResultReturn(new ArrayList<Quadruple>());
        } else {
            return new ResultReturn(new ArrayList<Quadruple>());
        }
    }


    private static ResultReturn arg() throws Throwable {
        type();
        token_consume(Token.IDENT);
        return new ResultReturn(new ArrayList<Quadruple>());
    }

    private static ResultReturn stmtList(String begin, String end) throws Throwable {

        if (actualToken.getToken().equals(Token.FOR)
                || actualToken.getToken().equals(Token.SCAN)
                || actualToken.getToken().equals(Token.PRINT)
                || actualToken.getToken().equals(Token.WHILE)
                || actualToken.getToken().equals(Token.SUM)
                || actualToken.getToken().equals(Token.NOT)
                || actualToken.getToken().equals(Token.SUBTRACTION)
                || actualToken.getToken().equals(Token.NUM_INT)
                || actualToken.getToken().equals(Token.NUM_FLOAT)
                || actualToken.getToken().equals(Token.IDENT)
                || actualToken.getToken().equals(Token.OPEN_PAR)
                || actualToken.getToken().equals(Token.IF)
                || actualToken.getToken().equals(Token.OPEN_BRACKET)
                || actualToken.getToken().equals(Token.BREAK)
                || actualToken.getToken().equals(Token.CONTINUE)
                || actualToken.getToken().equals(Token.INT)
                || actualToken.getToken().equals(Token.FLOAT)
                || actualToken.getToken().equals(Token.SEMICOLON)
                || actualToken.getToken().equals(Token.RETURN)) {
            ResultReturn rStmt = stmt(begin, end);
            ResultReturn rStmtList = stmtList(begin, end);

            return new ResultReturn(Quadruple.concatenateLists(Objects.requireNonNull(rStmt).getListQuadruple(), rStmtList.getListQuadruple()));
        } else {
            return null;
        }
    }


    //TODO: VERIFICAR RETURN
    private static ResultReturn stmt(String begin, String end) throws Throwable {
        if (actualToken.getToken().equals(Token.FOR)) {
            return forStmt();
        } else if (actualToken.getToken().equals(Token.PRINT)
                || (actualToken.getToken().equals(Token.SCAN))) {
            ioStmt();
        } else if (actualToken.getToken().equals(Token.WHILE)) {
            return whileStmt();
        } else if (actualToken.getToken().equals(Token.NOT)
                || actualToken.getToken().equals(Token.SUM)
                || actualToken.getToken().equals(Token.SUBTRACTION)
                || actualToken.getToken().equals(Token.NUM_INT)
                || actualToken.getToken().equals(Token.NUM_FLOAT)
                || actualToken.getToken().equals(Token.IDENT)
                || actualToken.getToken().equals(Token.OPEN_PAR)) {
            ResultReturn rExpr = expr();
            token_consume(Token.SEMICOLON);
            return new ResultReturn(rExpr.getListQuadruple());
        } else if (actualToken.getToken().equals(Token.IF)) {
            return ifStmt(begin, end);
        } else if (actualToken.getToken().equals(Token.OPEN_BRACKET)) {
            return bloco(begin, end);
        } else if (actualToken.getToken().equals(Token.BREAK)) {
            return operationBreak(end);
        } else if (actualToken.getToken().equals(Token.CONTINUE)) {
            return operationContinue(begin);
        } else if (actualToken.getToken().equals(Token.INT)
                || actualToken.getToken().equals(Token.FLOAT)) {
            return declaration();
        } else if (actualToken.getToken().equals(Token.RETURN)) {
            token_consume(Token.RETURN);
            fator();
            token_consume(Token.SEMICOLON);
        } else {
            token_consume(Token.SEMICOLON);
            return null;
        }

    }

    private static ResultReturn forStmt() throws Throwable {
        token_consume(Token.FOR);
        token_consume(Token.OPEN_PAR);
        ResultReturn rOEAttribution = optExpr();
        token_consume(Token.SEMICOLON);
        ResultReturn rOEComparation = optExpr();
        token_consume(Token.SEMICOLON);
        ResultReturn rOEIncrementation = optExpr();
        token_consume(Token.CLOSE_PAR);

        String labelBegin = Labels.createLabel();
        String labelEnd = Labels.createLabel();

        ResultReturn rStmt = stmt(labelBegin, labelEnd);

        List<Quadruple> codeList = new ArrayList<>();
        codeList = Quadruple.concatenateLists(codeList, Objects.requireNonNull(rOEAttribution).getListQuadruple());
        codeList.add(new Quadruple("label", labelBegin, null, null));
        codeList = Quadruple.concatenateLists(codeList, Objects.requireNonNull(rOEComparation).getListQuadruple());
        codeList.add(new Quadruple(Token.IF.getDescription(), rOEComparation.getNameResult(), null, labelEnd));
        codeList = Quadruple.concatenateLists(codeList,
                Quadruple.concatenateLists(rStmt.getListQuadruple(), Objects.requireNonNull(rOEIncrementation).getListQuadruple()));
        codeList.add(new Quadruple("jump", labelBegin, null, null));
        codeList.add(new Quadruple("label", labelEnd, null, null));

        return new ResultReturn(codeList);


    }


    private static ResultReturn ioStmt() throws Throwable {
        if (actualToken.getToken().equals(Token.SCAN)) {
            token_consume(Token.SCAN);
            token_consume(Token.OPEN_PAR);

            //remove char "
            String str = actualToken.getLexeme().substring(1,
                    actualToken.getLexeme().length() - 1);

            token_consume(Token.STR);
            token_consume(Token.COMMA);

            String localVar = Parser.formatVarName(actualToken.getLexeme());
            localVar = Parser.getVarScope(localVar);

            token_consume(Token.IDENT);
            token_consume(Token.CLOSE_PAR);
            token_consume(Token.SEMICOLON);

            List<Quadruple> listQ = new ArrayList<>();
            listQ.add(new Quadruple(Token.SCAN.getDescription(), localVar, str, null));

            return new ResultReturn(listQ);
        } else {
            token_consume(Token.PRINT);
            token_consume(Token.OPEN_PAR);
            ResultReturn rOL = outList();
            token_consume(Token.CLOSE_PAR);
            token_consume(Token.SEMICOLON);
            return new ResultReturn(rOL);
        }

    }

    private static ResultReturn whileStmt() throws Throwable {
        token_consume(Token.WHILE);
        token_consume(Token.OPEN_PAR);
        ResultReturn rExpr = expr();
        token_consume(Token.CLOSE_PAR);

        String labelBegin = Labels.createLabel();
        String labelEnd = Labels.createLabel();

        ResultReturn rStmt = stmt(labelBegin, labelEnd);

        List<Quadruple> codeList = new ArrayList<>();

        codeList.add(new Quadruple("jump", labelBegin, null, null));
        codeList = Quadruple.concatenateLists(codeList, rExpr.getListQuadruple());
        codeList.add(new Quadruple(Token.IF.getDescription(), rExpr.getNameResult(), null, labelEnd));
        codeList = Quadruple.concatenateLists(codeList, rStmt.getListQuadruple());
        codeList.add(new Quadruple("jump", labelBegin, null, null));
        codeList.add(new Quadruple("label", labelEnd, null, null));

        return new ResultReturn(codeList);

    }

    private static ResultReturn ifStmt(String begin, String end) throws Throwable {
        token_consume(Token.IF);
        token_consume(Token.OPEN_PAR);
        ResultReturn rExpr = expr();
        token_consume(Token.CLOSE_PAR);

        String labelTrue = Labels.createLabel();
        String labelFalse = Labels.createLabel();

        ResultReturn rStmt = stmt(begin, end);
        ResultReturn rElse = elsePart(begin, end);

        List<Quadruple> codeList = new ArrayList<>();

        codeList = Quadruple.concatenateLists(codeList, expr().getListQuadruple());
        codeList.add(new Quadruple(Token.IF.getDescription(), rExpr.getNameResult(), null, labelFalse));
        codeList = Quadruple.concatenateLists(codeList, rStmt.getListQuadruple());
        codeList.add(new Quadruple("jump", labelTrue, null, null));
        codeList.add(new Quadruple("label", labelFalse, null, null));
        codeList = Quadruple.concatenateLists(codeList, Objects.requireNonNull(rElse).getListQuadruple());
        codeList.add(new Quadruple("label", labelTrue, null, null));

        return new ResultReturn(codeList);

    }

    private static ResultReturn elsePart(String begin, String end) throws Throwable {
        if (actualToken.getToken().equals(Token.ELSE)) {
            token_consume(Token.ELSE);
            return stmt(begin, end);
        } else {
            //empty
            return null;
        }
    }

    private static ResultReturn operationBreak(String end) throws Throwable {

        if (end != null) {
            token_consume(Token.BREAK);
            token_consume(Token.SEMICOLON);
            List<Quadruple> listQuad = new ArrayList<>();
            listQuad.add(new Quadruple("jump", end));
            return new ResultReturn(listQuad);
        } else {
            token_consume(Token.BREAK);
            token_consume(Token.SEMICOLON);
            return null;
        }
    }


    private static ResultReturn operationContinue(String begin) throws Throwable {

        if (begin != null) {
            token_consume(Token.CONTINUE);
            token_consume(Token.SEMICOLON);
            List<Quadruple> listQuad = new ArrayList<>();
            listQuad.add(new Quadruple("jump", begin));
            return new ResultReturn(listQuad);
        } else {
            token_consume(Token.BREAK);
            token_consume(Token.SEMICOLON);
            return null;
        }
    }

    private static ResultReturn declaration() throws Throwable {
        List<Quadruple> list = new ArrayList<>();
        Token t = type();
        List<String> varList = identList();
        List<String> declarationList = new ArrayList<>();

        //for each id in the declaration list
        for (String aDeclarationList : declarationList) {

            String varName = Parser.formatVarName(aDeclarationList);

            //if id exists
            if (symbolsTable.containsKey(varName)) {
                throw Errors.SemanticError("Already declared variable.");
            } else {
                //define type
                Quadruple quadruple = new Quadruple(Token.RECEIVE.getDescription(), varName, "0");
                //add the variable in the symbol table

                if (t.getDescription().equals(Token.FLOAT.getDescription())) {
                    quadruple.setArg2("0.0");
                    symbolsTable.put(varName, Token.FLOAT);
                } else {
                    symbolsTable.put(varName, Token.INT);
                }

                list.add(quadruple);
            }
        }

        token_consume(Token.SEMICOLON);
        return new ResultReturn(list);
    }

    private static List<String> identList() throws Throwable {
        idList.add(actualToken.getLexeme());
        token_consume(Token.IDENT);
        restoIdentList(idList);
        return idList;
    }

    private static void restoIdentList(List<String> idList) throws Throwable {
        if (actualToken.getToken().equals(Token.COMMA)) {
            token_consume(Token.COMMA);
            idList.add(actualToken.getLexeme());
            token_consume(Token.IDENT);
            restoIdentList(idList);
        }
    }


    private static ResultReturn optExpr() throws Throwable {
        if (actualToken.getToken().equals(Token.NOT)
                || actualToken.getToken().equals(Token.SUM)
                || actualToken.getToken().equals(Token.SUBTRACTION)
                || actualToken.getToken().equals(Token.NUM_INT)
                || actualToken.getToken().equals(Token.NUM_FLOAT)
                || actualToken.getToken().equals(Token.IDENT)
                || actualToken.getToken().equals(Token.OPEN_PAR)) {
            return expr();
        } else {
            return null;
        }

    }


    private static ResultReturn outList() throws Throwable {
        ResultReturn rO = out();
        ResultReturn rOL = restoOutList();

        return new ResultReturn(Quadruple.concatenateLists(rO.getListQuadruple(),
                Objects.requireNonNull(rOL).getListQuadruple()));
    }

    private static ResultReturn restoOutList() throws Throwable {
        if (actualToken.getToken().equals(Token.COMMA)) {
            token_consume(Token.COMMA);
            ResultReturn rO = out();
            ResultReturn rOL = restoOutList();
            return new ResultReturn(Quadruple.concatenateLists(rO.getListQuadruple(),
                    Objects.requireNonNull(rOL).getListQuadruple()));
        } else {
            return null;
        }
    }


    private static ResultReturn out() throws Throwable {
        switch (actualToken.getToken()) {
            case STR:
                List<Quadruple> listStr = new ArrayList<>();
                listStr.add(new Quadruple(Token.PRINT.getDescription(),
                        null, actualToken.getLexeme(), null));
                token_consume(Token.STR);
                return new ResultReturn(listStr);
            case IDENT:
                String varName = Parser.formatVarName(actualToken.getLexeme());

                //search for the variable
                if (!Parser.searchVarScope(varName)) {
                    throw Errors.SemanticError("Undeclared variable " + actualToken.getLexeme());
                }

                varName = Parser.getVarScope(varName);
                List<Quadruple> listIdent = new ArrayList<>();
                listIdent.add(new Quadruple(Token.PRINT.getDescription(),
                        null, varName, null));
                token_consume(Token.IDENT);
                return new ResultReturn(listIdent);
            case NUM_INT:
                List<Quadruple> listInt = new ArrayList<>();
                listInt.add(new Quadruple(Token.PRINT.getDescription(),
                        null, actualToken.getLexeme(), null));
                token_consume(Token.NUM_INT);
                return new ResultReturn(listInt);
            default:
                List<Quadruple> list = new ArrayList<>();
                list.add(new Quadruple(Token.PRINT.getDescription(), null, actualToken.getLexeme(), null));
                token_consume(Token.NUM_FLOAT);
                //return the quadruple
                return new ResultReturn(list);
        }


    }


    // dont have left value
    private static ResultReturn expr() throws Throwable {
        ResultReturn rAtrib = atrib();
        return new ResultReturn(rAtrib.getListQuadruple(), rAtrib.getNameResult());
    }


    //TODO: VERIFICAR O RATRIB
    private static ResultReturn atrib() throws Throwable {
        ResultReturn rO = or();
        ResultReturn rAtrib = restoAtrib(rO.getNameResult());

        if (!rO.isLeftValue() && !rAtrib.isLeftValue()) {
            throw Errors.SemanticError("Invalid Attribution");
        } else {
            return new ResultReturn(false, Quadruple.concatenateLists(rO.getListQuadruple(),
                    rAtrib.getListQuadruple()), rO.getNameResult());
        }
    }

    private static ResultReturn restoAtrib(String value) throws Throwable {
        if (actualToken.getToken().equals(Token.RECEIVE)) {
            token_consume(Token.RECEIVE);
            ResultReturn rAtrib = atrib();
            String temp = TemporaryVariable.createTemp(actualBlock);
            List<Quadruple> list = new ArrayList<>();
            list.add(new Quadruple(Token.RECEIVE.getDescription(), value, rAtrib.getNameResult()));
            return new ResultReturn(false, list, rAtrib.getNameResult());
        } else {
            return new ResultReturn(true, null, value);
        }
    }

    private static ResultReturn or() throws Throwable {
        ResultReturn rA = and();
        ResultReturn rO = restoOr(rA.getNameResult());
        if (rA.isLeftValue() && rO.isLeftValue()) {
            return new ResultReturn(true, Quadruple.concatenateLists(rA.getListQuadruple(),
                    rO.getListQuadruple()), rO.getNameResult());
        } else {
            return new ResultReturn(false, Quadruple.concatenateLists(rA.getListQuadruple(),
                    rO.getListQuadruple()), rO.getNameResult());
        }

    }

    private static ResultReturn restoOr(String value) throws Throwable {
        if (actualToken.getToken().equals(Token.OR)) {
            token_consume(Token.OR);
            ResultReturn rA = and();
            ResultReturn rO = restoOr(value);
            String temp = TemporaryVariable.createTemp(actualBlock);
            List<Quadruple> list = new ArrayList<>();
            list.add(new Quadruple(Token.OR.getDescription(), temp, value, rA.getNameResult()));
            return new ResultReturn(false, Quadruple.concatenateLists(list, rO.getListQuadruple()), temp);
        } else {
            return new ResultReturn(true, null, value);
        }
    }

    private static ResultReturn and() throws Throwable {
        ResultReturn rN = not();
        ResultReturn rA = restoAnd(rN.getNameResult());
        if (rN.isLeftValue() && rA.isLeftValue()) {
            return new ResultReturn(true, Quadruple.concatenateLists(rN.getListQuadruple(),
                    rA.getListQuadruple()), rA.getNameResult());
        } else {
            return new ResultReturn(false, Quadruple.concatenateLists(rN.getListQuadruple(),
                    rA.getListQuadruple()), rA.getNameResult());
        }
    }

    private static ResultReturn restoAnd(String value) throws Throwable {
        if (actualToken.getToken().equals(Token.AND)) {
            token_consume(Token.AND);
            ResultReturn rN = not();
            ResultReturn rA = restoAnd(value);
            String temp = TemporaryVariable.createTemp(actualBlock);
            List<Quadruple> list = new ArrayList<>();
            list.add(new Quadruple(Token.AND.getDescription(), temp, value, rN.getNameResult()));
            return new ResultReturn(false, Quadruple.concatenateLists(list, rA.getListQuadruple()), temp);
        } else {
            return new ResultReturn(true, null, value);
        }
    }

    private static ResultReturn not() throws Throwable {
        if (actualToken.getToken().equals(Token.NOT)) {
            token_consume(Token.NOT);
            ResultReturn rNot = not();
            String temp = TemporaryVariable.createTemp(actualBlock);
            List<Quadruple> list = new ArrayList<>();
            list.add(new Quadruple(Token.NOT.getDescription(), temp, rNot.getNameResult()));
            return new ResultReturn(false, list, temp);
        } else {
            return rel();
        }
    }

    private static ResultReturn rel() throws Throwable {
        ResultReturn rAdd = add();
        ResultReturn rRR = restoRel(rAdd.getNameResult());

        if (rAdd.isLeftValue() && rRR.isLeftValue()) {
            return new ResultReturn(true, Quadruple.concatenateLists(rAdd.getListQuadruple(),
                    rRR.getListQuadruple()), rRR.getNameResult());
        } else {
            return new ResultReturn(false, Quadruple.concatenateLists(rAdd.getListQuadruple(),
                    rRR.getListQuadruple()), rRR.getNameResult());
        }

    }


    private static ResultReturn restoRel(String value) throws Throwable {
        switch (actualToken.getToken()) {
            case EQUAL:
                token_consume(Token.EQUAL);
                ResultReturn rAdd = add();
                String temp = TemporaryVariable.createTemp(actualBlock);
                List<Quadruple> list = new ArrayList<>();
                list.add(new Quadruple(Token.EQUAL.getDescription(), temp, value, rAdd.getNameResult()));
                return new ResultReturn(false, list, temp);
            case DIFFERENT:
                token_consume(Token.DIFFERENT);
                ResultReturn rDif = add();
                String tempDif = TemporaryVariable.createTemp(actualBlock);
                List<Quadruple> listDif = new ArrayList<>();
                listDif.add(new Quadruple(Token.DIFFERENT.getDescription(), tempDif, value, rDif.getNameResult()));
                return new ResultReturn(false, listDif, tempDif);
            case SMALLER:
                token_consume(Token.SMALLER);
                ResultReturn rSmall = add();
                String tempSmall = TemporaryVariable.createTemp(actualBlock);
                List<Quadruple> listSmall = new ArrayList<>();
                listSmall.add(new Quadruple(Token.SMALLER.getDescription(), tempSmall, value, rSmall.getNameResult()));
                return new ResultReturn(false, listSmall, tempSmall);
            case SMALLER_EQUAL:
                token_consume(Token.SMALLER_EQUAL);
                ResultReturn rSE = add();
                String tempSE = TemporaryVariable.createTemp(actualBlock);
                List<Quadruple> listSE = new ArrayList<>();
                listSE.add(new Quadruple(Token.SMALLER_EQUAL.getDescription(), tempSE, value, rSE.getNameResult()));
                return new ResultReturn(false, listSE, tempSE);
            case BIGGER:
                token_consume(Token.BIGGER);
                ResultReturn rBig = add();
                String tempBig = TemporaryVariable.createTemp(actualBlock);
                List<Quadruple> listBig = new ArrayList<>();
                listBig.add(new Quadruple(Token.BIGGER.getDescription(), tempBig, value, rBig.getNameResult()));
                return new ResultReturn(false, listBig, tempBig);
            case BIGGER_EQUAL:
                token_consume(Token.BIGGER_EQUAL);
                ResultReturn rBE = add();
                String tempBE = TemporaryVariable.createTemp(actualBlock);
                List<Quadruple> listBE = new ArrayList<>();
                listBE.add(new Quadruple(Token.BIGGER_EQUAL.getDescription(), tempBE, value, rBE.getNameResult()));
                return new ResultReturn(false, listBE, tempBE);

            default:
                return new ResultReturn(true, null, value);
        }
    }

    private static ResultReturn add() throws Throwable {
        ResultReturn rMult = mult();
        ResultReturn rAdd = restoAdd(rMult.getNameResult());

        if (rMult.isLeftValue() && rAdd.isLeftValue()) {
            return new ResultReturn(true, Quadruple.concatenateLists(rMult.getListQuadruple(),
                    rAdd.getListQuadruple()), rAdd.getNameResult());
        } else {
            return new ResultReturn(false, Quadruple.concatenateLists(rMult.getListQuadruple(),
                    rAdd.getListQuadruple()), rAdd.getNameResult());
        }

    }

    private static ResultReturn restoAdd(String value) throws Throwable {
        switch (actualToken.getToken()) {
            case SUM:
                token_consume(Token.SUM);
                ResultReturn rMult = mult();
                ResultReturn rAdd = restoAdd(value);
                String temp = TemporaryVariable.createTemp(actualBlock);
                List<Quadruple> listM = new ArrayList<>();
                listM.add(new Quadruple(Token.SUM.getDescription(), temp, value, rMult.getNameResult()));
                return new ResultReturn(false, Quadruple.concatenateLists(listM,
                        rAdd.getListQuadruple()), temp);
            case SUBTRACTION:
                token_consume(Token.SUBTRACTION);
                ResultReturn rMultSub = mult();
                ResultReturn rAddSub = restoAdd(value);
                String tempSub = TemporaryVariable.createTemp(actualBlock);
                List<Quadruple> listMSub = new ArrayList<>();
                listMSub.add(new Quadruple(Token.SUBTRACTION.getDescription(), tempSub, value, rMultSub.getNameResult()));
                return new ResultReturn(false, Quadruple.concatenateLists(listMSub,
                        rAddSub.getListQuadruple()), tempSub);
            default:
                return new ResultReturn(true, null, value);
        }
    }

    private static ResultReturn mult() throws Throwable {
        ResultReturn rUno = uno();
        ResultReturn rMult = restoMult(rUno.getNameResult());
        if (rUno.isLeftValue() && rMult.isLeftValue()) {
            return new ResultReturn(true, Quadruple.concatenateLists(rUno.getListQuadruple(), rMult.getListQuadruple()),
                    rMult.getNameResult());
        } else {
            return new ResultReturn(false, Quadruple.concatenateLists(rUno.getListQuadruple(), rMult.getListQuadruple()),
                    rMult.getNameResult());
        }

    }

    private static ResultReturn restoMult(String value) throws Throwable {
        switch (actualToken.getToken()) {
            case MULT:
                token_consume(Token.MULT);
                ResultReturn rUno = uno();
                ResultReturn rMult = restoMult(value);
                String temp = TemporaryVariable.createTemp(actualBlock);
                List<Quadruple> listU = new ArrayList<>();
                listU.add(new Quadruple(Token.MULT.getDescription(), temp, value, rUno.getNameResult()));
                return new ResultReturn(false, Quadruple.concatenateLists(listU, rMult.getListQuadruple()), temp);
            case DIV:
                token_consume(Token.DIV);
                ResultReturn rUnoDiv = uno();
                ResultReturn rMultDiv = restoMult(value);
                String tempDiv = TemporaryVariable.createTemp(actualBlock);
                List<Quadruple> listUDiv = new ArrayList<>();
                listUDiv.add(new Quadruple(Token.DIV.getDescription(), tempDiv, value, rUnoDiv.getNameResult()));
                return new ResultReturn(false, Quadruple.concatenateLists(listUDiv, rMultDiv.getListQuadruple()), tempDiv);

            case MOD:
                token_consume(Token.MOD);
                ResultReturn rUnoMod = uno();
                ResultReturn rMultMod = restoMult(value);
                String tempMod = TemporaryVariable.createTemp(actualBlock);
                List<Quadruple> listUMod = new ArrayList<>();
                listUMod.add(new Quadruple(Token.MOD.getDescription(), tempMod, value, rUnoMod.getNameResult()));
                return new ResultReturn(false, Quadruple.concatenateLists(listUMod, rMultMod.getListQuadruple()), tempMod);

            default:
                return new ResultReturn(true, null, value);
        }
    }

    private static ResultReturn uno() throws Throwable {

        switch (actualToken.getToken()) {
            case SUM:
                token_consume(Token.SUM);
                ResultReturn resultReturn = uno();
                String temp = TemporaryVariable.createTemp(actualBlock);
                List<Quadruple> list = new ArrayList<>();
                list.add(new Quadruple(Token.SUM.getDescription(), temp, "0", resultReturn.getNameResult()));
                return new ResultReturn(false, list, temp);
            case SUBTRACTION:
                token_consume(Token.SUBTRACTION);
                ResultReturn resultReturn1 = uno();
                String temp1 = TemporaryVariable.createTemp(actualBlock);
                List<Quadruple> list1 = new ArrayList<>();
                list1.add(new Quadruple(Token.SUBTRACTION.getDescription(), temp1, "0", resultReturn1.getNameResult()));

                return new ResultReturn(false, list1, temp1);
            default:
                return fator();

        }
    }

    private static ResultReturn fator() throws Throwable {
        switch (actualToken.getToken()) {
            case NUM_FLOAT:
                String temp = TemporaryVariable.createTemp(actualBlock);
                List<Quadruple> quadruple = new ArrayList<>();
                quadruple.add(new Quadruple(Token.RECEIVE.getDescription(), temp, actualToken.getLexeme()));
                token_consume(Token.NUM_FLOAT);
                return new ResultReturn(false, quadruple, temp);
            case IDENT:
                String varName = Parser.formatVarName(actualToken.getLexeme());

                List<Quadruple> quadrupleIdent = new ArrayList<>();
                //undeclared variables
                if (!Parser.searchVarScope(varName)) {
                    throw Errors.SemanticError("Undeclared variable " + actualToken.getLexeme());
                } else {
                    varName = getVarScope(varName);
                }

                quadrupleIdent.add(new Quadruple(Token.RECEIVE.getDescription(), varName, varName));

                token_consume(Token.IDENT);
                return new ResultReturn(true, null, varName);
            case OPEN_PAR:
                token_consume(Token.OPEN_PAR);
                ResultReturn rR = atrib();
                token_consume(Token.CLOSE_PAR);
                return new ResultReturn(false, rR.getListQuadruple(), rR.getNameResult());
            default:
                String temp2 = TemporaryVariable.createTemp(actualBlock);
                List<Quadruple> quadruple3 = new ArrayList<>();
                quadruple3.add(new Quadruple(Token.RECEIVE.getDescription(), temp2, actualToken.getLexeme()));
                token_consume(Token.NUM_INT);
                return new ResultReturn(false, quadruple3, temp2);
        }

    }
}
