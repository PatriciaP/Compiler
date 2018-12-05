/*
 * IFMG - COMPILERS - 2018
 * Syntactic Analysis
 */
package compiler.syntactic_analysis;

import compiler.exceptions.Errors;
import compiler.lexical_analysis.LexicalAnalysis;
import compiler.lexical_analysis.Token;
import compiler.virtual_machine.Quadruple;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Patricia Pieroni
 */
public class Parser {


    // name and type
    private static HashMap<String, String> symbolsTable = new HashMap<>();
    private static List<String> idList = new ArrayList<>();
    //Block Scope
    private static int actualBlock = -1;


    public static void main(String[] args) throws RuntimeException, IOException {
        LexicalAnalysis.file = new File("src\\main\\java\\compiler\\input\\test").getCanonicalFile();
        LexicalAnalysis.contentFile = LexicalAnalysis.loadArq(LexicalAnalysis.file.toPath());
        System.out.println(LexicalAnalysis.contentFile);

        List<ResultReturn> code = Collections.singletonList(Parser.parser());

        for (int i = 0; i < code.size(); i++) {
            System.out.println(code);
        }
    }


    //token validation
    private static void token_consume(Token token) {
        System.out.println("Token expected " + token + " Token received " + LexicalAnalysis.current.getToken());
        if (LexicalAnalysis.current.getToken().equals(token)) {
            LexicalAnalysis.current = LexicalAnalysis.getToken();
        } else {
            try {
                throw Errors.SyntaticError(token);
            } catch (RuntimeException RuntimeException) {
                RuntimeException.printStackTrace();
            }
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

        //every vars and temps of actual block
        List<String> freeVars = new ArrayList<>();

        Iterator iterator = symbolsTable.keySet().iterator();
        while (iterator.hasNext()) {
            String name = (String) iterator.next();
            int index = name.indexOf('_', 2);
            int blockId = Integer.parseInt(name.substring(2, index));
            String varName = name.substring(index + 1);

            if (blockId == actualBlock) {
                freeVars.add(name);
            }
        }

        Iterator iteratorDel = symbolsTable.keySet().iterator();

        while (iteratorDel.hasNext()) {
            String nameVar = (String) iteratorDel.next();
            for (String freeVar : freeVars) {
                if (nameVar.equals(freeVar)) {
                    iteratorDel.remove();
                }
            }
        }

    }


    public static ResultReturn parser() throws RuntimeException {
        LexicalAnalysis.current = LexicalAnalysis.getToken();
        ResultReturn code = function();
        System.out.println(code.getListQuadruple());
        System.out.println();

        token_consume(LexicalAnalysis.current.getToken());
        token_consume(Token.EOF);
        code.getListQuadruple().add(new Quadruple("stop"));
        return code;

    }


    /*GRAMATIC MINI C*/
    private static ResultReturn function() throws RuntimeException {
        type();
        token_consume(Token.IDENT);
        token_consume(Token.OPEN_PAR);
        ResultReturn rArg = argList();
        token_consume(Token.CLOSE_PAR);
        ResultReturn rB = bloco(null, null);
        return new ResultReturn(Quadruple.concatenateLists(rArg.getListQuadruple(), rB.getListQuadruple()));

    }

    private static String type() throws RuntimeException {
        if (LexicalAnalysis.current.getToken().equals(Token.FLOAT)) {
            token_consume(Token.FLOAT);
            return Token.FLOAT.getDescription();
        } else {
            token_consume(Token.INT);
            return Token.INT.getDescription();
        }
    }

    //
    private static ResultReturn bloco(String begin, String end) throws RuntimeException {
        token_consume(Token.OPEN_BRACKET);
        actualBlock++;
        ResultReturn stmtL = stmtList(begin, end);
        freeBlock();
        actualBlock--;
        token_consume(Token.CLOSE_BRACKET);

        return stmtL;

    }

    private static ResultReturn argList() throws RuntimeException {

        if (LexicalAnalysis.current.getToken().equals(Token.INT)
                || LexicalAnalysis.current.getToken().equals(Token.FLOAT)) {
            ResultReturn rArg = arg();
            ResultReturn rResto = restoArgList();

            return new ResultReturn(Quadruple.concatenateLists(rArg.getListQuadruple(), rResto.getListQuadruple()));
        } else {
            return new ResultReturn(new ArrayList<Quadruple>());
        }

    }

    private static ResultReturn restoArgList() throws RuntimeException {
        if (LexicalAnalysis.current.getToken().equals(Token.COMMA)) {
            token_consume(Token.COMMA);
            argList();
            return new ResultReturn(new ArrayList<Quadruple>());
        } else {
            return new ResultReturn(new ArrayList<Quadruple>());
        }
    }


    private static ResultReturn arg() throws RuntimeException {
        type();
        token_consume(Token.IDENT);
        return new ResultReturn(new ArrayList<Quadruple>());
    }

    private static ResultReturn stmtList(String begin, String end) throws RuntimeException {

        if (LexicalAnalysis.current.getToken().equals(Token.NOT)
                || LexicalAnalysis.current.getToken().equals(Token.OPEN_BRACKET)
                || LexicalAnalysis.current.getToken().equals(Token.SUM)
                || LexicalAnalysis.current.getToken().equals(Token.SUBTRACTION)
                || LexicalAnalysis.current.getToken().equals(Token.SEMICOLON)
                || LexicalAnalysis.current.getToken().equals(Token.IDENT)
                || LexicalAnalysis.current.getToken().equals(Token.NUM_FLOAT)
                || LexicalAnalysis.current.getToken().equals(Token.NUM_INT)
                || LexicalAnalysis.current.getToken().equals(Token.BREAK)
                || LexicalAnalysis.current.getToken().equals(Token.CONTINUE)
                || LexicalAnalysis.current.getToken().equals(Token.FLOAT)
                || LexicalAnalysis.current.getToken().equals(Token.FOR)
                || LexicalAnalysis.current.getToken().equals(Token.IF)
                || LexicalAnalysis.current.getToken().equals(Token.INT)
                || LexicalAnalysis.current.getToken().equals(Token.PRINT)
                || LexicalAnalysis.current.getToken().equals(Token.SCAN)
                || LexicalAnalysis.current.getToken().equals(Token.WHILE)
                || LexicalAnalysis.current.getToken().equals(Token.OPEN_BRACKET)
                || LexicalAnalysis.current.getToken().equals(Token.RETURN)) {
            ResultReturn rStmt = stmt(begin, end);
            ResultReturn rStmtList = stmtList(begin, end);

            return new ResultReturn(Quadruple.concatenateLists(Objects.requireNonNull(rStmt).getListQuadruple(), rStmtList.getListQuadruple()));
        } else {
            //  } else if (LexicalAnalysis.current.getToken().equals(Token.INT)
            //                || LexicalAnalysis.current.getToken().equals(Token.FLOAT)) {
            //            return declaration();
            return new ResultReturn();
        }
    }


    private static ResultReturn stmt(String begin, String end) throws RuntimeException {
        if (LexicalAnalysis.current.getToken().equals(Token.FOR)) {
            return forStmt();
        } else if (LexicalAnalysis.current.getToken().equals(Token.PRINT)
                || (LexicalAnalysis.current.getToken().equals(Token.SCAN))) {
            return ioStmt();
        } else if (LexicalAnalysis.current.getToken().equals(Token.WHILE)) {
            return whileStmt();
        } else if (LexicalAnalysis.current.getToken().equals(Token.NOT)
                || LexicalAnalysis.current.getToken().equals(Token.SUM)
                || LexicalAnalysis.current.getToken().equals(Token.SUBTRACTION)
                || LexicalAnalysis.current.getToken().equals(Token.NUM_INT)
                || LexicalAnalysis.current.getToken().equals(Token.NUM_FLOAT)
                || LexicalAnalysis.current.getToken().equals(Token.IDENT)
                || LexicalAnalysis.current.getToken().equals(Token.OPEN_PAR)) {
            ResultReturn rExpr = expr();
            token_consume(Token.SEMICOLON);
            return new ResultReturn(rExpr.getListQuadruple());
        } else if (LexicalAnalysis.current.getToken().equals(Token.IF)) {
            return ifStmt(begin, end);
        } else if (LexicalAnalysis.current.getToken().equals(Token.OPEN_BRACKET)) {
            return bloco(begin, end);
        } else if (LexicalAnalysis.current.getToken().equals(Token.BREAK)) {
            return operationBreak(end);
        } else if (LexicalAnalysis.current.getToken().equals(Token.CONTINUE)) {
            return operationContinue(begin);
        } else if (LexicalAnalysis.current.getToken().equals(Token.INT)
                || LexicalAnalysis.current.getToken().equals(Token.FLOAT)) {
            return declaration();
        } else if (LexicalAnalysis.current.getToken().equals(Token.RETURN)) {
            token_consume(Token.RETURN);
            ResultReturn rF = fator();
            token_consume(Token.SEMICOLON);
            return new ResultReturn();
        } else {
            token_consume(Token.SEMICOLON);
            return null;
        }

    }

    private static ResultReturn forStmt() throws RuntimeException {
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
                Quadruple.concatenateLists(rStmt != null ? rStmt.getListQuadruple() : null, Objects.requireNonNull(rOEIncrementation).getListQuadruple()));
        codeList.add(new Quadruple("jump", labelBegin, null, null));
        codeList.add(new Quadruple("label", labelEnd, null, null));

        return new ResultReturn(codeList);


    }


    private static ResultReturn ioStmt() throws RuntimeException {
        if (LexicalAnalysis.current.getToken().equals(Token.SCAN)) {
            token_consume(Token.SCAN);
            token_consume(Token.OPEN_PAR);

            //remove char ", str before the scan
            String str = LexicalAnalysis.current.getLexeme().substring(1,
                    LexicalAnalysis.current.getLexeme().length() - 1);

            token_consume(Token.STR);
            token_consume(Token.COMMA);

            String localVar = Parser.formatVarName(LexicalAnalysis.current.getLexeme());
            localVar = Parser.getVarScope(localVar);

            token_consume(Token.IDENT);
            token_consume(Token.CLOSE_PAR);
            token_consume(Token.SEMICOLON);

            List<Quadruple> listQ = new ArrayList<>();
            listQ.add(new Quadruple(Token.SCAN.getDescription(), localVar, str));

            return new ResultReturn(listQ);
        } else {
            token_consume(Token.PRINT);
            token_consume(Token.OPEN_PAR);
            ResultReturn rOL = outList();
            token_consume(Token.CLOSE_PAR);
            token_consume(Token.SEMICOLON);
            return rOL;
        }

    }

    private static ResultReturn whileStmt() throws RuntimeException {
        token_consume(Token.WHILE);
        token_consume(Token.OPEN_PAR);
        ResultReturn rExpr = expr();
        token_consume(Token.CLOSE_PAR);

        String labelBegin = Labels.createLabel();
        String labelEnd = Labels.createLabel();

        ResultReturn rStmt = stmt(labelBegin, labelEnd);

        List<Quadruple> codeList = new ArrayList<>();

        codeList.add(new Quadruple("label", labelBegin, null, null));
        codeList = Quadruple.concatenateLists(codeList, rExpr.getListQuadruple());
        codeList.add(new Quadruple(Token.IF.getDescription(), rExpr.getNameResult(), null, labelEnd));
        codeList = Quadruple.concatenateLists(codeList, Objects.requireNonNull(rStmt).getListQuadruple());
        codeList.add(new Quadruple("jump", labelBegin, null, null));
        codeList.add(new Quadruple("label", labelEnd, null, null));

        return new ResultReturn(codeList);

    }

    private static ResultReturn ifStmt(String begin, String end) throws RuntimeException {
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
        codeList = Quadruple.concatenateLists(codeList, Objects.requireNonNull(rStmt).getListQuadruple());
        codeList.add(new Quadruple("jump", labelTrue, null, null));
        codeList.add(new Quadruple("label", labelFalse, null, null));
        codeList = Quadruple.concatenateLists(codeList, Objects.requireNonNull(rElse).getListQuadruple());
        codeList.add(new Quadruple("label", labelTrue, null, null));

        return new ResultReturn(codeList);

    }

    private static ResultReturn elsePart(String begin, String end) throws RuntimeException {
        if (LexicalAnalysis.current.getToken().equals(Token.ELSE)) {
            token_consume(Token.ELSE);
            return stmt(begin, end);
        } else {
            //empty
            return new ResultReturn();
        }
    }

    private static ResultReturn operationBreak(String end) throws RuntimeException {

        if (end != null) {
            token_consume(Token.BREAK);
            token_consume(Token.SEMICOLON);
            List<Quadruple> listQuad = new ArrayList<>();
            listQuad.add(new Quadruple("jump", end));
            return new ResultReturn(listQuad);
        } else {
            token_consume(Token.BREAK);
            token_consume(Token.SEMICOLON);
            return new ResultReturn();
        }
    }


    private static ResultReturn operationContinue(String begin) throws RuntimeException {

        if (begin != null) {
            token_consume(Token.CONTINUE);
            token_consume(Token.SEMICOLON);
            List<Quadruple> listQuad = new ArrayList<>();
            listQuad.add(new Quadruple("jump", begin));
            return new ResultReturn(listQuad);
        } else {
            token_consume(Token.BREAK);
            token_consume(Token.SEMICOLON);
            return new ResultReturn();
        }
    }

    private static ResultReturn declaration() throws RuntimeException {
        List<Quadruple> list = new ArrayList<>();
        String t = type();
        List<String> varList = identList();
        List<String> declarationList = new ArrayList<>();

        //for each id in the list
        for (String aDeclarationList : varList) {

            String varName = Parser.formatVarName(aDeclarationList);

            //if id exists
            if (symbolsTable.containsKey(varName)) {
                throw Errors.SemanticError("Variable already declared.");
            } else {
                //define type
                Quadruple quadruple = new Quadruple(Token.RECEIVE.getDescription(), varName, "0");
                //add the variable in the symbol table

                if (t.equals(Token.FLOAT.getDescription())) {
                    quadruple.setArg2("0.0");
                    symbolsTable.put(varName, "float");
                } else {
                    symbolsTable.put(varName, "int");
                }

                list.add(quadruple);
            }
        }

        token_consume(Token.SEMICOLON);
        return new ResultReturn(list);
    }

    private static List<String> identList() throws RuntimeException {
        idList.add(LexicalAnalysis.current.getLexeme());
        token_consume(Token.IDENT);
        restoIdentList(idList);
        return idList;
    }

    private static void restoIdentList(List<String> idList) throws RuntimeException {
        if (LexicalAnalysis.current.getToken().equals(Token.COMMA)) {
            token_consume(Token.COMMA);
            idList.add(LexicalAnalysis.current.getLexeme());
            token_consume(Token.IDENT);
            restoIdentList(idList);
        }
    }


    private static ResultReturn optExpr() throws RuntimeException {
        if (LexicalAnalysis.current.getToken().equals(Token.NOT)
                || LexicalAnalysis.current.getToken().equals(Token.SUM)
                || LexicalAnalysis.current.getToken().equals(Token.SUBTRACTION)
                || LexicalAnalysis.current.getToken().equals(Token.NUM_INT)
                || LexicalAnalysis.current.getToken().equals(Token.NUM_FLOAT)
                || LexicalAnalysis.current.getToken().equals(Token.IDENT)
                || LexicalAnalysis.current.getToken().equals(Token.OPEN_PAR)) {
            return expr();
        } else {
            return new ResultReturn();
        }

    }


    private static ResultReturn outList() throws RuntimeException {
        ResultReturn rO = out();
        ResultReturn rOL = restoOutList();

        return new ResultReturn(Quadruple.concatenateLists(rO.getListQuadruple(),
                Objects.requireNonNull(rOL).getListQuadruple()));
    }

    private static ResultReturn restoOutList() throws RuntimeException {
        if (LexicalAnalysis.current.getToken().equals(Token.COMMA)) {
            token_consume(Token.COMMA);
            ResultReturn rO = out();
            ResultReturn rOL = restoOutList();
            return new ResultReturn(Quadruple.concatenateLists(rO.getListQuadruple(),
                    Objects.requireNonNull(rOL).getListQuadruple()));
        } else {
            return new ResultReturn();
        }
    }


    private static ResultReturn out() throws RuntimeException {
        switch (LexicalAnalysis.current.getToken()) {
            case STR:
                List<Quadruple> listStr = new ArrayList<>();
                listStr.add(new Quadruple(Token.PRINT.getDescription(),
                        null, LexicalAnalysis.current.getLexeme(), null));
                token_consume(Token.STR);
                return new ResultReturn(listStr);
            case IDENT:
                String varName = Parser.formatVarName(LexicalAnalysis.current.getLexeme());

                //search for the variable
                if (!Parser.searchVarScope(varName)) {
                    throw Errors.SemanticError("Undeclared variable " + LexicalAnalysis.current.getLexeme());
                } else {
                    varName = Parser.getVarScope(varName);
                }


                List<Quadruple> listIdent = new ArrayList<>();
                listIdent.add(new Quadruple(Token.PRINT.getDescription(),
                        null, varName, null));
                token_consume(Token.IDENT);
                return new ResultReturn(listIdent);
            case NUM_INT:
                List<Quadruple> listInt = new ArrayList<>();
                listInt.add(new Quadruple(Token.PRINT.getDescription(),
                        null, LexicalAnalysis.current.getLexeme(), null));
                token_consume(Token.NUM_INT);
                return new ResultReturn(listInt);
            default:

                List<Quadruple> list = new ArrayList<>();
                list.add(new Quadruple(Token.PRINT.getDescription(), null, LexicalAnalysis.current.getLexeme(), null));
                token_consume(Token.NUM_FLOAT);
                //return the quadruple
                return new ResultReturn(list);
        }


    }


    // dont have left value
    private static ResultReturn expr() throws RuntimeException {
        return atrib();
    }


    //TODO: VERIFICAR O RATRIB
    private static ResultReturn atrib() throws RuntimeException {
        ResultReturn rO = or();
        ResultReturn rAtrib = restoAtrib(rO.getNameResult());

        if (!rO.isLeftValue() && !rAtrib.isLeftValue()) {
            throw Errors.SemanticError("Invalid Attribution");
        }

        return new ResultReturn(false, Quadruple.concatenateLists(rO.getListQuadruple(),
                rAtrib.getListQuadruple()), rO.getNameResult());

    }

    private static ResultReturn restoAtrib(String value) throws RuntimeException {
        if (LexicalAnalysis.current.getToken().equals(Token.RECEIVE)) {
            token_consume(Token.RECEIVE);
            ResultReturn rAtrib = atrib();
            List<Quadruple> list = new ArrayList<>();
            list.add(new Quadruple(Token.RECEIVE.getDescription(), value, rAtrib.getNameResult()));
            return new ResultReturn(false, list, rAtrib.getNameResult());
        } else {
            return new ResultReturn(true, null, value);
        }
    }

    private static ResultReturn or() throws RuntimeException {
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

    private static ResultReturn restoOr(String value) throws RuntimeException {
        if (LexicalAnalysis.current.getToken().equals(Token.OR)) {
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

    private static ResultReturn and() throws RuntimeException {
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

    private static ResultReturn restoAnd(String value) throws RuntimeException {
        if (LexicalAnalysis.current.getToken().equals(Token.AND)) {
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

    private static ResultReturn not() throws RuntimeException {
        if (LexicalAnalysis.current.getToken().equals(Token.NOT)) {
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

    private static ResultReturn rel() throws RuntimeException {
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


    private static ResultReturn restoRel(String value) throws RuntimeException {
        switch (LexicalAnalysis.current.getToken()) {
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

    private static ResultReturn add() throws RuntimeException {
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

    private static ResultReturn restoAdd(String value) throws RuntimeException {
        switch (LexicalAnalysis.current.getToken()) {
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

    private static ResultReturn mult() throws RuntimeException {
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

    private static ResultReturn restoMult(String value) throws RuntimeException {
        switch (LexicalAnalysis.current.getToken()) {
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

    private static ResultReturn uno() throws RuntimeException {

        switch (LexicalAnalysis.current.getToken()) {
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

    private static ResultReturn fator() throws RuntimeException {
        switch (LexicalAnalysis.current.getToken()) {
            case NUM_FLOAT:
                String temp = TemporaryVariable.createTemp(actualBlock);
                String lexeme = LexicalAnalysis.current.getLexeme();
                token_consume(Token.NUM_FLOAT);
                List<Quadruple> quadruple = new ArrayList<>();
                quadruple.add(new Quadruple(Token.RECEIVE.getDescription(), temp, lexeme, null));
                return new ResultReturn(false, quadruple, temp);
            case IDENT:
                String varName = Parser.formatVarName(LexicalAnalysis.current.getLexeme());

                List<Quadruple> quadrupleIdent = new ArrayList<>();
                //undeclared variables
                if (!Parser.searchVarScope(varName)) {
                    throw Errors.SemanticError("Undeclared variable " + LexicalAnalysis.current.getLexeme());
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
                //todo verification
                String temp2 = TemporaryVariable.createTemp(actualBlock);
                List<Quadruple> quadruple3 = new ArrayList<>();
                String lexInt = LexicalAnalysis.current.getLexeme();
                token_consume(Token.NUM_INT);
                quadruple3.add(new Quadruple(Token.RECEIVE.getDescription(), temp2, lexInt));

                return new ResultReturn(false, quadruple3, temp2);
        }

    }
}
