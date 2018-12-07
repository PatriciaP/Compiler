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
import compiler.virtual_machine.Variables;
import compiler.virtual_machine.VirtualMachine;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Patricia Pieroni
 */
public class Parser {

    private static Current current = new Current();

    // name and type
    private static HashMap<String, String> symbolsTable = new HashMap<>();
    private static List<String> idList = new ArrayList<>();
    //Block Scope
    private static int actualBlock = 0;


    public static void main(String[] args) throws RuntimeException, IOException {
        LexicalAnalysis.file = new File("/home/renan/IdeaProjects/compiler/src/main/java/compiler/input/test").getCanonicalFile();
        LexicalAnalysis.contentFile = LexicalAnalysis.loadArq(LexicalAnalysis.file.toPath());
        System.out.println(LexicalAnalysis.contentFile);

        List<ResultReturn> code = Collections.singletonList(Parser.parser());

        for (int i = 0; i < code.size(); i++) {
            System.out.println(code);
        }
    }


    //token validation
    private static String token_consume(Token token) {
        System.out.println("Token expected " + token + " Token received " + current.getToken());
        if (current.getToken().equals(token)) {
            current = LexicalAnalysis.getToken();
            return current.getLexeme();
        } else {
            try {
                throw Errors.SyntaticError(token,current);
            } catch (RuntimeException RuntimeException) {
                RuntimeException.printStackTrace();
            }
            return null;
        }
    }


    public static ResultReturn parser() throws RuntimeException {
        current = LexicalAnalysis.getToken();
        ResultReturn rFunction = function();
        token_consume(Token.EOF);
        return rFunction;
    }


    /*GRAMATIC MINI C*/
    private static ResultReturn function() throws RuntimeException {
        ResultReturn resultReturn;
        type();
        token_consume(Token.IDENT);
        token_consume(Token.OPEN_PAR);
        argList();
        token_consume(Token.CLOSE_PAR);
        resultReturn = bloco(null,null);
        return resultReturn;
    }


    private static void argList() throws RuntimeException {

        if (current.getToken().equals(Token.INT)
                || current.getToken().equals(Token.FLOAT)) {
            arg();
            restoArgList();
        }

    }


    private static void arg() throws RuntimeException {
        int type = type();
        Current actualToken = current;
        token_consume(Token.IDENT);

        //variable level
        if(actualBlock != 1){
            actualBlock = 1;
        }
        addIdVar(actualToken, type);
        actualBlock = 0;
    }

    private static void restoArgList() throws RuntimeException {
        if (current.getToken().equals(Token.COMMA)) {
            token_consume(Token.COMMA);
            argList();
        }
    }

    private static int type() throws RuntimeException {
        if (current.getToken().equals(Token.FLOAT)) {
            token_consume(Token.FLOAT);
            return 1;
        } else {
            token_consume(Token.INT);
            return 0;
        }
    }

    private static ResultReturn bloco(String begin, String end) throws RuntimeException {
        ResultReturn resultReturn;
        token_consume(Token.OPEN_BRACKET);
        actualBlock++;
        resultReturn = stmtList(begin, end);
        freeBlock();
        actualBlock--;
        token_consume(Token.CLOSE_BRACKET);

        return resultReturn;

    }

    private static ResultReturn stmtList(String begin, String end) throws RuntimeException {

        ResultReturn resultReturn = new ResultReturn();
        if (current.getToken().equals(Token.NOT)
                || current.getToken().equals(Token.OPEN_BRACKET)
                || current.getToken().equals(Token.SUM)
                || current.getToken().equals(Token.SUBTRACTION)
                || current.getToken().equals(Token.SEMICOLON)
                || current.getToken().equals(Token.IDENT)
                || current.getToken().equals(Token.NUM_FLOAT)
                || current.getToken().equals(Token.NUM_INT)
                || current.getToken().equals(Token.BREAK)
                || current.getToken().equals(Token.CONTINUE)
                || current.getToken().equals(Token.FOR)
                || current.getToken().equals(Token.IF)
                || current.getToken().equals(Token.PRINT)
                || current.getToken().equals(Token.SCAN)
                || current.getToken().equals(Token.WHILE)
                || current.getToken().equals(Token.RETURN)) {
            ResultReturn rStmt = stmt(begin, end);
            ResultReturn rStmtList = stmtList(begin, end);
            resultReturn.setListQuadruple(Quadruple.concatenateLists(Objects.requireNonNull(rStmt).getListQuadruple(), rStmtList.getListQuadruple()));
        } else if (current.getToken().equals(Token.FLOAT) || current.getToken().equals(Token.INT) ) {
            ResultReturn rD = declaration();
            ResultReturn rS = stmtList(begin, end);
            resultReturn.setListQuadruple(Quadruple.concatenateLists(rD.getListQuadruple(),rS.getListQuadruple()));;
        }

        return resultReturn;
    }

    private static ResultReturn stmt(String begin, String end) throws RuntimeException {
        ResultReturn resultReturn = new ResultReturn();
        if (current.getToken().equals(Token.FOR)) {
            resultReturn =  forStmt();
        } else if (current.getToken().equals(Token.PRINT)
                || (current.getToken().equals(Token.SCAN))) {
            resultReturn = ioStmt();
        } else if (current.getToken().equals(Token.WHILE)) {
            resultReturn = whileStmt();
        } else if (current.getToken().equals(Token.NOT)
                || current.getToken().equals(Token.SUM)
                || current.getToken().equals(Token.SUBTRACTION)
                || current.getToken().equals(Token.NUM_INT)
                || current.getToken().equals(Token.NUM_FLOAT)
                || current.getToken().equals(Token.IDENT)
                || current.getToken().equals(Token.OPEN_PAR)) {
            resultReturn = expr();
            token_consume(Token.SEMICOLON);
        } else if (current.getToken().equals(Token.IF)) {
            resultReturn = ifStmt();
        } else if (current.getToken().equals(Token.OPEN_BRACKET)) {
            resultReturn = bloco(begin, end);
        } else if (current.getToken().equals(Token.BREAK)) {

            if(begin == null){
                throw Errors.SemanticError("Iválid declaration of 'BREAK'", current);
            }
            token_consume(Token.BREAK);
            List<Quadruple> quadruples = new ArrayList<>();
            quadruples.add(new Quadruple("jump",end,null,null));
            resultReturn.setListQuadruple(quadruples);
            token_consume(Token.SEMICOLON);
        } else if (current.getToken().equals(Token.CONTINUE)) {
            if(begin == null){
                throw Errors.SemanticError("Iválid declaration of 'CONTINUE'", current);
            }
            token_consume(Token.CONTINUE);
            List<Quadruple> quadruples = new ArrayList<>();
            quadruples.add(new Quadruple("jump",begin,null,null));
            resultReturn.setListQuadruple(quadruples);
            token_consume(Token.SEMICOLON);

        }  else {
            token_consume(Token.SEMICOLON);
        }
        return resultReturn;

    }

    private static ResultReturn declaration() throws RuntimeException {
        ResultReturn resultReturn;
        int typeVar = type();
        int numberVariablesBefore;
        if(VirtualMachine.lenghtVariables - 1 > 0){
            numberVariablesBefore = VirtualMachine.lenghtVariables;
        } else {
            numberVariablesBefore = 0;
        }
        resultReturn = identList(typeVar);
        token_consume(Token.SEMICOLON);
        return resultReturn;
    }

    private static ResultReturn identList(int typeVar) throws RuntimeException {
        ResultReturn resultReturn;
        Current actual = current;
        String lexeme  = token_consume(Token.IDENT);
        addIdVar(actual, typeVar);
        resultReturn = restoIdentList(typeVar);
        List<Quadruple> list = new ArrayList<>();
        list.add(new Quadruple("=",searchVarScope(lexeme),"0",null));
        resultReturn.setListQuadruple(Quadruple.concatenateLists(resultReturn.getListQuadruple(),list));
        return resultReturn;
    }

    private static ResultReturn restoIdentList(int varType) throws RuntimeException {
        ResultReturn resultReturn = new ResultReturn();
        if (current.getToken().equals(Token.COMMA)) {
            token_consume(Token.COMMA);
            Current actual = current;
            String lexeme  = token_consume(Token.IDENT);
            addIdVar(actual, varType);
            resultReturn = restoIdentList(varType);
            List<Quadruple> list = new ArrayList<>();
            list.add(new Quadruple("=",searchVarScope(lexeme),"0",null));
            resultReturn.setListQuadruple(Quadruple.concatenateLists(resultReturn.getListQuadruple(),list));

        }

        return resultReturn;
    }



    private static ResultReturn forStmt() throws RuntimeException {
        ResultReturn rOEAttribution, rOEComparation, rOEIncrementation,
                auxComparation = new ResultReturn(), rStmt;
        token_consume(Token.FOR);
        token_consume(Token.OPEN_PAR);

        rOEAttribution = optExpr();
        token_consume(Token.SEMICOLON);

        rOEComparation = optExpr();
        auxComparation.setListQuadruple(rOEComparation.getListQuadruple());
        token_consume(Token.SEMICOLON);

        rOEIncrementation = optExpr();
        token_consume(Token.CLOSE_PAR);

        String labelReturn = Labels.createLabel();
        String labelCont = Labels.createLabel();
        String labelExit = Labels.createLabel();

        rStmt = stmt(labelCont, labelExit);

        rOEAttribution.setListQuadruple(Quadruple.concatenateLists(rOEAttribution.getListQuadruple(), rOEComparation.getListQuadruple()));
        rOEAttribution.listQuadruple.add(new Quadruple(Token.IF.getDescription(), rOEComparation.getNameResult(), labelReturn, labelExit));
        rOEAttribution.listQuadruple.add(new Quadruple("label", labelReturn, null, null));
        rOEAttribution.setListQuadruple(Quadruple.concatenateLists(rOEAttribution.getListQuadruple(),rStmt.getListQuadruple()));
        rOEAttribution.listQuadruple.add(new Quadruple("label", labelCont, null, null));
        rOEAttribution.setListQuadruple(Quadruple.concatenateLists(rOEAttribution.getListQuadruple(),rOEIncrementation.getListQuadruple()));
        rOEAttribution.setListQuadruple(Quadruple.concatenateLists(rOEAttribution.getListQuadruple(),auxComparation.getListQuadruple()));
        rOEAttribution.listQuadruple.add(new Quadruple("if", rOEAttribution.getNameResult(), labelReturn, labelExit));
        rOEAttribution.listQuadruple.add(new Quadruple("label", labelExit, null, null));

        return rOEAttribution;
    }


    private static ResultReturn optExpr() throws RuntimeException {
        if (current.getToken().equals(Token.NOT)
                || current.getToken().equals(Token.SUM)
                || current.getToken().equals(Token.SUBTRACTION)
                || current.getToken().equals(Token.NUM_INT)
                || current.getToken().equals(Token.NUM_FLOAT)
                || current.getToken().equals(Token.IDENT)
                || current.getToken().equals(Token.OPEN_PAR)) {
            return expr();
        } else {
            return new ResultReturn();
        }
    }

    private static ResultReturn ioStmt() throws RuntimeException {
        ResultReturn resultReturn = new ResultReturn();
        if (current.getToken().equals(Token.SCAN)) {
            token_consume(Token.SCAN);
            token_consume(Token.OPEN_PAR);

            //remove char ", str before the scan
            String str = token_consume(Token.STR);
            token_consume(Token.COMMA);
            Current actual = current;
            token_consume(Token.IDENT);

            if(searchVarScope(actual.getLexeme()) == null){
                throw Errors.SemanticError("Undeclared Variable : "+actual.getLexeme(),actual);
            }
            resultReturn.listQuadruple.add(new Quadruple("call","scan",str,searchVarScope(actual.getLexeme())));

            token_consume(Token.CLOSE_PAR);
            token_consume(Token.SEMICOLON);

        } else {
            token_consume(Token.PRINT);
            token_consume(Token.OPEN_PAR);
            resultReturn = outList();
            token_consume(Token.CLOSE_PAR);
            token_consume(Token.SEMICOLON);
        }

        return resultReturn;

    }


    private static ResultReturn outList() throws RuntimeException {
        ResultReturn rO, rOL;
        rO = out();
        rOL = restoOutList();

        rO.setListQuadruple(Quadruple.concatenateLists(rO.getListQuadruple(),rOL.getListQuadruple()));
        rO.setNameResult(rOL.getNameResult());

        return rO;
    }


    private static ResultReturn out() throws RuntimeException {
        ResultReturn resultReturn = new ResultReturn();
        String lexeme;
        Quadruple quadruple;
        switch (current.getToken()) {
            case STR:
                lexeme = token_consume(Token.STR);
                quadruple = new Quadruple("call",Token.PRINT.getDescription(), current.getLexeme(), null);
                resultReturn.setNameResult(lexeme);
            case NUM_FLOAT:
                lexeme = token_consume(Token.FLOAT);
                quadruple = new Quadruple("call",Token.PRINT.getDescription(), current.getLexeme(), null);
                resultReturn.setNameResult(lexeme);
            case NUM_INT:
                lexeme = token_consume(Token.INT);
                quadruple = new Quadruple("call",Token.PRINT.getDescription(), current.getLexeme(), null);
                resultReturn.setNameResult(lexeme);
            default:
                Current actual = current;
                token_consume(Token.IDENT);
                if(searchVarScope(actual.getLexeme()) == null){
                    throw Errors.SemanticError("Undeclared Variable :"+actual.getLexeme(),actual);
                }
                quadruple = new Quadruple("call",Token.PRINT.getDescription(), searchVarScope(actual.getLexeme()), null);
                resultReturn.setNameResult(searchVarScope(actual.getLexeme()));


        }

        resultReturn.listQuadruple.add(quadruple);
        return resultReturn;

    }

    private static ResultReturn restoOutList() throws RuntimeException {
        ResultReturn resultReturn = new ResultReturn(), resultReturn1= new ResultReturn();
        if (current.getToken().equals(Token.COMMA)) {
            token_consume(Token.COMMA);
            resultReturn = out();
            resultReturn1 = restoOutList();
            resultReturn.setListQuadruple(Quadruple.concatenateLists(resultReturn.getListQuadruple(),resultReturn1.getListQuadruple()));
        }
        return resultReturn;

    }

    private static ResultReturn whileStmt() throws RuntimeException {
        ResultReturn resultReturn, resultReturn1, resultReturn2 = new ResultReturn();
        token_consume(Token.WHILE);
        token_consume(Token.OPEN_PAR);

        resultReturn = expr();
        token_consume(Token.CLOSE_PAR);

        resultReturn2.setListQuadruple(resultReturn.getListQuadruple());

        String labelReturn = Labels.createLabel();
        String labelCont = Labels.createLabel();
        String labelExit = Labels.createLabel();

        resultReturn1 = stmt(labelCont,labelExit);

        resultReturn.listQuadruple.add(new Quadruple(Token.IF.getDescription(),
                resultReturn.getNameResult(), labelReturn, labelExit));
        resultReturn.listQuadruple.add(new Quadruple("label",
                labelExit, null, null));
        resultReturn.setListQuadruple(Quadruple.concatenateLists(resultReturn.getListQuadruple(), resultReturn1.getListQuadruple()));
        resultReturn.listQuadruple.add(new Quadruple("label", labelCont, null, null));
        resultReturn.setListQuadruple(Quadruple.concatenateLists(resultReturn.getListQuadruple(), resultReturn2.getListQuadruple()));
        resultReturn.listQuadruple.add(new Quadruple(Token.IF.getDescription(),
                resultReturn.getNameResult(), labelReturn, labelExit));
        resultReturn.listQuadruple.add(new Quadruple("label",
                labelExit, null, null));

        return resultReturn;

    }

    private static ResultReturn ifStmt() throws RuntimeException {
        ResultReturn aux, aux2, aux3;
        token_consume(Token.IF);
        token_consume(Token.OPEN_PAR);

        String labTrue = Labels.createLabel();
        String labFalse = Labels.createLabel();

        aux = expr();
        aux.listQuadruple.add(new Quadruple("if",aux.getNameResult(),labTrue, labFalse));
        token_consume(Token.CLOSE_PAR);

        aux.listQuadruple.add(new Quadruple("label",labTrue,null, null));
        aux2 = stmt(null,null);
        aux.setListQuadruple(Quadruple.concatenateLists(aux.getListQuadruple(),aux2.getListQuadruple()));
        aux3 = elsePart();
        Quadruple quadruple = new Quadruple("label",labTrue,null,null);
        if (!aux3.getListQuadruple().isEmpty()){
            String labExit  = Labels.createLabel();
            aux.listQuadruple.add(new Quadruple("jump",labExit,null,null));
            aux.listQuadruple.add(quadruple);
            aux.setListQuadruple(Quadruple.concatenateLists(aux.getListQuadruple(),aux3.getListQuadruple()));
            aux.listQuadruple.add(new Quadruple("label",labExit,null,null));
            aux.setNameResult(aux3.getNameResult());

        }else{
            aux.setNameResult(aux2.getNameResult());
            aux.listQuadruple.add(quadruple);
        }
        return aux;

    }

    private static ResultReturn elsePart() throws RuntimeException {
        ResultReturn aux = new ResultReturn();
        if (current.getToken().equals(Token.ELSE)) {
            token_consume(Token.ELSE);
            aux = stmt(null,null);
        }

        return aux;
    }

    // dont have left value
    private static ResultReturn expr() throws RuntimeException {
        return atrib();
    }


    private static ResultReturn atrib() throws RuntimeException {
        ResultReturn aux, aux2;

        aux = or();
        aux2 = restoAtrib(aux.getNameResult());

        if (!aux.isLeftValue() && !aux.isLeftValue()) {
            throw Errors.SemanticError("Invalid Attribution", current);
        }

        aux.setListQuadruple(Quadruple.concatenateLists(aux.getListQuadruple(),aux2.getListQuadruple()));
        aux.setNameResult(aux2.getNameResult());
        return aux;

    }

    private static ResultReturn restoAtrib(String value) throws RuntimeException {
        ResultReturn aux = new ResultReturn();
        if (current.getToken().equals(Token.RECEIVE)) {
            token_consume(Token.RECEIVE);
            aux = atrib();
            aux.getListQuadruple().add(new Quadruple(Token.RECEIVE.getDescription(), value, aux.getNameResult(), null));
        } else {
            aux.setLeftValue(true);
            aux.setNameResult(value);
        }

        return aux;
    }

    private static ResultReturn or() throws RuntimeException {
        ResultReturn aux, aux2;
        aux = and();
        aux2 = restoOr(aux.getNameResult());
        aux.setListQuadruple(Quadruple.concatenateLists(aux.getListQuadruple(),aux2.getListQuadruple()));
        aux.setNameResult(aux2.getNameResult());
        aux.setLeftValue(aux.isLeftValue() & aux2.isLeftValue());
        return aux;

    }

    private static ResultReturn restoOr(String value) throws RuntimeException {
        ResultReturn aux = new ResultReturn(), aux2;
        if (current.getToken().equals(Token.OR)) {
            token_consume(Token.OR);
            aux = and();
            aux2 = restoOr(aux.getNameResult());
            String temp = TemporaryVariable.createTemp(actualBlock);
            aux.listQuadruple.add(new Quadruple(Token.OR.getDescription(), temp, value, aux.getNameResult()));
            aux.setListQuadruple(Quadruple.concatenateLists(aux.getListQuadruple(),aux2.getListQuadruple()));
            aux.setNameResult(temp);
        } else {
            aux.setLeftValue(true);
            aux.setNameResult(value);
        }
        return aux;
    }

    private static ResultReturn and() throws RuntimeException {
        ResultReturn aux, aux2;

        aux = not();
        aux2 = restoAnd(aux.getNameResult());
        aux.setListQuadruple(Quadruple.concatenateLists(aux.getListQuadruple(),aux2.getListQuadruple()));
        aux.setNameResult(aux2.getNameResult());
        aux.setLeftValue(aux.isLeftValue() & aux2.isLeftValue());

        return aux;
    }

    private static ResultReturn restoAnd(String value) throws RuntimeException {
        ResultReturn aux = new ResultReturn(), aux2;

        if (current.getToken().equals(Token.AND)) {
            token_consume(Token.AND);
            aux = not();
            aux2 = restoAnd(aux.getNameResult());
            String temp = TemporaryVariable.createTemp(actualBlock);
            aux.listQuadruple.add(new Quadruple(Token.AND.getDescription(), temp, value, aux.getNameResult()));
            aux.setListQuadruple(Quadruple.concatenateLists(aux.getListQuadruple(),aux2.getListQuadruple()));
            aux.setNameResult(temp);
        } else {
            aux.setLeftValue(true);
            aux.setNameResult(value);
        }

        return aux;
    }

    private static ResultReturn not() throws RuntimeException {
        ResultReturn resultReturn;
        if (current.getToken().equals(Token.NOT)) {
            token_consume(Token.NOT);
            resultReturn = not();
            String temp = TemporaryVariable.createTemp(actualBlock);
            resultReturn.listQuadruple.add(new Quadruple(Token.NOT.getDescription(), temp, resultReturn.getNameResult(), null));
            return resultReturn;
        } else {
            return rel(); //didn't change
        }
    }

    private static ResultReturn rel() throws RuntimeException {
        ResultReturn aux, aux2;
        aux = add();
        aux2 = restoRel(aux.getNameResult());

        aux.setListQuadruple(Quadruple.concatenateLists(aux.getListQuadruple(),aux2.getListQuadruple()));
        aux.setNameResult(aux2.getNameResult());
        aux.setLeftValue(aux.isLeftValue() & aux2.isLeftValue());

        return aux;

    }


    private static ResultReturn restoRel(String value) throws RuntimeException {
        ResultReturn aux = new ResultReturn();
        switch (current.getToken()) {
            case EQUAL:
                String temp = TemporaryVariable.createTemp(actualBlock);
                token_consume(Token.EQUAL);
                aux = add();
                aux.listQuadruple.add(new Quadruple(Token.EQUAL.getDescription(), temp, value, aux.getNameResult()));
                aux.setNameResult(temp);
                aux.setLeftValue(false);
            case DIFFERENT:
                String temp1 = TemporaryVariable.createTemp(actualBlock);
                token_consume(Token.DIFFERENT);
                aux = add();
                aux.listQuadruple.add(new Quadruple(Token.DIFFERENT.getDescription(), temp1, value, aux.getNameResult()));
                aux.setNameResult(temp1);
                aux.setLeftValue(false);
            case SMALLER:
                String temp2 = TemporaryVariable.createTemp(actualBlock);
                token_consume(Token.SMALLER);
                aux = add();
                aux.listQuadruple.add(new Quadruple(Token.SMALLER.getDescription(), temp2, value, aux.getNameResult()));
                aux.setNameResult(temp2);
                aux.setLeftValue(false);
            case SMALLER_EQUAL:
                String temp3 = TemporaryVariable.createTemp(actualBlock);
                token_consume(Token.SMALLER_EQUAL);
                aux = add();
                aux.listQuadruple.add(new Quadruple(Token.SMALLER_EQUAL.getDescription(), temp3, value, aux.getNameResult()));
                aux.setNameResult(temp3);
                aux.setLeftValue(false);
            case BIGGER:
                String temp4 = TemporaryVariable.createTemp(actualBlock);
                token_consume(Token.BIGGER);
                aux = add();
                aux.listQuadruple.add(new Quadruple(Token.BIGGER.getDescription(), temp4, value, aux.getNameResult()));
                aux.setNameResult(temp4);
                aux.setLeftValue(false);
            case BIGGER_EQUAL:
                String temp5 = TemporaryVariable.createTemp(actualBlock);
                token_consume(Token.BIGGER_EQUAL);
                aux = add();
                aux.listQuadruple.add(new Quadruple(Token.BIGGER_EQUAL.getDescription(), temp5, value, aux.getNameResult()));
                aux.setNameResult(temp5);
                aux.setLeftValue(false);

            default:
                aux.setLeftValue(true);
                aux.setNameResult(value);
        }

        return aux;
    }

    private static ResultReturn add() throws RuntimeException {
        ResultReturn aux = new ResultReturn(), aux2;
        aux = mult();
        aux2 = restoAdd(aux.getNameResult());
        aux.setListQuadruple(Quadruple.concatenateLists(aux.getListQuadruple(),aux2.getListQuadruple()));
        aux.setNameResult(aux2.getNameResult());
        aux.setLeftValue(aux.isLeftValue() & aux2.isLeftValue());

        return aux;
    }

    private static ResultReturn restoAdd(String value) throws RuntimeException {
        ResultReturn aux = new ResultReturn(), aux2;
        switch (current.getToken()) {
            case SUM:
                token_consume(Token.SUM);
                aux = mult();
                String temp = TemporaryVariable.createTemp(actualBlock);
                aux2 = restoAdd(temp);
                aux.listQuadruple.add(new Quadruple(Token.SUM.getDescription(), temp, value, aux.getNameResult()));
                aux.setListQuadruple(Quadruple.concatenateLists(aux.getListQuadruple(),aux2.getListQuadruple()));
                aux.setNameResult(aux2.getNameResult());
                aux.setLeftValue(false);

            case SUBTRACTION:
                token_consume(Token.SUBTRACTION);
                aux = mult();
                String temp2 = TemporaryVariable.createTemp(actualBlock);
                aux2 = restoAdd(temp2);
                aux.listQuadruple.add(new Quadruple(Token.SUBTRACTION.getDescription(), temp2, value, aux.getNameResult()));
                aux.setListQuadruple(Quadruple.concatenateLists(aux.getListQuadruple(),aux2.getListQuadruple()));
                aux.setNameResult(aux2.getNameResult());
                aux.setLeftValue(false);
            default:
                aux.setLeftValue(true);
                aux.setNameResult(value);
        }

        return aux;
    }

    private static ResultReturn mult() throws RuntimeException {
        ResultReturn aux, aux2;
        aux = uno();
        aux2 = restoMult(aux.getNameResult());

        aux.setListQuadruple(Quadruple.concatenateLists(aux.getListQuadruple(),aux2.getListQuadruple()));
        aux.setNameResult(aux2.getNameResult());
        aux.setLeftValue(aux.isLeftValue() & aux.isLeftValue());

        return aux;

    }

    private static ResultReturn restoMult(String value) throws RuntimeException {
        ResultReturn aux = new ResultReturn();
        switch (current.getToken()) {
            case MULT:
                token_consume(Token.MULT);
                aux = uno();
                String temp = TemporaryVariable.createTemp(actualBlock);
                aux.listQuadruple.add(new Quadruple(Token.MULT.getDescription(), temp, value, aux.getNameResult()));
                aux.setNameResult(temp);
                aux.setLeftValue(false);
            case DIV:
                token_consume(Token.DIV);
                aux = uno();
                String temp2 = TemporaryVariable.createTemp(actualBlock);
                aux.listQuadruple.add(new Quadruple(Token.DIV.getDescription(), temp2, value, aux.getNameResult()));
                aux.setNameResult(temp2);
                aux.setLeftValue(false);
            case MOD:
                token_consume(Token.MOD);
                aux = uno();
                String temp3 = TemporaryVariable.createTemp(actualBlock);
                aux.listQuadruple.add(new Quadruple(Token.MOD.getDescription(), temp3, value, aux.getNameResult()));
                aux.setNameResult(temp3);
                aux.setLeftValue(false);
            default:
                aux.setLeftValue(true);
                aux.setNameResult(value);
        }

        return aux;
    }

    private static ResultReturn uno() throws RuntimeException {
        ResultReturn aux;
        switch (current.getToken()) {
            case SUM:
                token_consume(Token.SUM);
                aux = uno();
                aux.setLeftValue(false);
            case SUBTRACTION:
                token_consume(Token.SUBTRACTION);
                aux = uno();
                aux.listQuadruple.add(new Quadruple("-",aux.getNameResult(),"0",aux.getNameResult()));
                aux.setLeftValue(false);
            default:
                aux = fator();

        }

        return aux;
    }

    private static ResultReturn fator() throws RuntimeException {
        ResultReturn aux = new ResultReturn();
        switch (current.getToken()) {
            case OPEN_PAR:
                token_consume(Token.OPEN_PAR);
                aux = atrib();
                token_consume(Token.CLOSE_PAR);
                aux.setLeftValue(false);
            case NUM_FLOAT:
                String temp = TemporaryVariable.createTemp(actualBlock);
                String lexeme = token_consume(Token.NUM_FLOAT);
                aux.listQuadruple.add(new Quadruple(Token.RECEIVE.getDescription(), temp, lexeme, null));
                aux.setNameResult(temp);
                aux.setLeftValue(false);
            case IDENT:
                String lex = token_consume(Token.IDENT);
                Boolean flag = true;
                for (int i = 0; i < VirtualMachine.lenghtVariables ; i++) {
                    if(VirtualMachine.variablesList.get(i).varId.equals(lex)){
                        flag = false;
                        break;
                    }
                }

                if (flag){
                    throw Errors.SemanticError("Undeclared Variable :"+lex,current);
                }
                aux.setNameResult(searchVarScope(lex));
                //return true if IDENT
                aux.setLeftValue(true);

            default:
                //todo verification
                String temp2 = TemporaryVariable.createTemp(actualBlock);
                String lexem = token_consume(Token.NUM_INT);
                aux.listQuadruple.add(new Quadruple(Token.RECEIVE.getDescription(), temp2, lexem, null));
                aux.setNameResult(temp2);
                aux.setLeftValue(false);
        }

        return aux;

    }



    private static void addIdVar(Current actualToken, int type) {

        if(VirtualMachine.lenghtVariables == 0){
            VirtualMachine.variablesList = new ArrayList<>();
            VirtualMachine.lenghtVariables = 1;
            VirtualMachine.variablesList.add(new Variables(actualToken.getLexeme(),type,actualBlock));
        } else {
            for (int i = 0; i < VirtualMachine.lenghtVariables ; i++) {
                if(VirtualMachine.variablesList.get(i).level == actualBlock){
                    throw Errors.SemanticError("Multiple Declaration of Variables",current);
                }

            }
            VirtualMachine.variablesList.add(new Variables(actualToken.getLexeme(),type,actualBlock));
        }
    }

    private static String searchVarScope(String var) {

        String variable;
        for (int i = VirtualMachine.lenghtVariables-1; i>=0 ; i--) {
            if(VirtualMachine.variablesList.get(i).varId.equals(var)){
                return var;
            }

        }

        return null;
    }

    //free all the variables and temps created on the current block
    private static void freeBlock() {

        int cont = 0;

        while (cont < VirtualMachine.lenghtVariables){
            if(VirtualMachine.variablesList.get(cont).level == actualBlock &&
                    actualBlock != 1){
                VirtualMachine.variablesList.remove(cont);
            }
            cont++;
        }


    }




}
