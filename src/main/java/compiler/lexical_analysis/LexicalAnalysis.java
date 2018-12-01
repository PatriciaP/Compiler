/*
 * IFMG - COMPILERS - 2018
 * Lexical Analysis
 */
package compiler.lexical_analysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Patricia Pieroni The Lexical Analysis reads the source file,
 * character by character, taking the tokens as established by the grammar
 */
public class LexicalAnalysis {

    private static char caractere;
    private static int cursor = 0;
    private static int state = 1;
    private static int flag = 0;
    private static int aux = 0;
    private static String contentFile;
    public static File file;
    private static Current current = new Current();

    public static void main(String[] args) throws IOException {
        file = new File("src\\main\\java\\compiler\\input\\test").getCanonicalFile();
        System.out.println(file);
        contentFile = loadArq(file.toPath());
        System.out.println(contentFile);
        while (current.token != Token.EOF) {
            getToken();
        }

    }

    private static char getChar() {
        return contentFile.charAt(cursor);
    }

    private static void updateManipulation() {
        cursor++;
        if (flag == 0) {
            current.column = 1;
        }
        if (flag > 0) {
            current.column = current.column + aux;
        }
        aux = 1;
        current.lexeme = String.format("%s%s", current.lexeme, caractere);
    }

    private static void goToState(int num) {
        cursor++;
        state = num;
        current.lexeme = String.format("%s%s", current.lexeme, caractere);
    }

    public static Current getToken() {
        String lexemeAux;
        state = 1;
        current.lexeme = "";
        current.token = null;
        while (current.token == null) {
            if (cursor >= contentFile.length()) {
                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.EOF);
            } else {

                caractere = getChar();

                switch (state) {
                    // ERROR STATE
                    case -1:
                        current.lexeme = "";
                        return LexicalAnalysis.updateCurrent(current.line, current.column, Token.ERROR);

                    case 1:
                        if ((contentFile.charAt(cursor) == '\n') || (contentFile.charAt(cursor) == '\r')) {
                            current.lexeme = String.format("%s%s", current.lexeme, caractere);
                            cursor++;
                            if (current.lexeme.contains(System.getProperty("line.separator"))) {
                                current.line++;
                                flag = 0;
                                current.lexeme = "";
                            }
                        } else {
                            if (caractere == ' ' || caractere == '\t') {
                                cursor++;
                                current.column++;
                                continue;
                            }
                            if (Character.isAlphabetic(caractere)) {
                                goToState(2);
                            } else if (Character.isDigit(caractere)) {
                                goToState(3);
                            } else if (caractere == '"') {
                                goToState(5);
                            } else if (caractere == '(') {
                                updateManipulation();
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.OPEN_PAR);
                            } else if (caractere == ')') {
                                updateManipulation();
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.CLOSE_PAR);
                            } else if (caractere == '{') {
                                updateManipulation();
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.OPEN_BRACKET);
                            } else if (caractere == '}') {
                                updateManipulation();
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.CLOSE_BRACKET);
                            } else if (caractere == ';') {
                                updateManipulation();
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.SEMICOLON);
                            } else if (caractere == ',') {
                                updateManipulation();
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.COMMA);
                            } else if (caractere == '+') {
                                updateManipulation();
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.SUM);
                            } else if (caractere == '*') {
                                goToState(7);
                            } else if (caractere == '/') {
                                goToState(6);
                            } else if (caractere == '%') {
                                updateManipulation();
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.MOD);
                            } else if (caractere == '=') {
                                updateManipulation();
                                if (contentFile.charAt(cursor) == '=') {
                                    cursor++;
                                    current.lexeme += '=';
                                    return LexicalAnalysis.updateCurrent(current.line, current.column, Token.EQUAL);
                                } else {
                                    return LexicalAnalysis.updateCurrent(current.line, current.column, Token.RECEIVE);
                                }
                            } else if (caractere == '!') {
                                updateManipulation();
                                if (contentFile.charAt(cursor) == '=') {
                                    cursor++;
                                    current.lexeme += '=';
                                    return LexicalAnalysis.updateCurrent(current.line, current.column, Token.DIFFERENT);
                                } else {
                                    return LexicalAnalysis.updateCurrent(current.line, current.column, Token.NOT);
                                }
                            } else if (caractere == '>') {
                                updateManipulation();
                                if (contentFile.charAt(cursor) == '=') {
                                    cursor++;
                                    current.lexeme += '=';
                                    return LexicalAnalysis.updateCurrent(current.line, current.column, Token.BIGGER_EQUAL);
                                } else {
                                    return LexicalAnalysis.updateCurrent(current.line, current.column, Token.BIGGER);
                                }
                            } else if (caractere == '<') {
                                updateManipulation();
                                if (contentFile.charAt(cursor) == '=') {
                                    cursor++;
                                    current.lexeme += '=';
                                    return LexicalAnalysis.updateCurrent(current.line, current.column, Token.SMALLER_EQUAL);
                                } else {
                                    return LexicalAnalysis.updateCurrent(current.line, current.column, Token.SMALLER);
                                }
                            } else if (cursor > contentFile.length()) {
                                state = 9;
                            }
                        }
                        break;

                    // state verification of tokens: IDENT,KEYWORDS
                    case 2:
                        if (current.lexeme.matches("[a-zA-Z]+[\\d]*") && Character.isAlphabetic(caractere)) {
                            goToState(2);
                        } else {

                            state = 9;
                        }
                        break;

                    // state verification of number token (int)
                    case 3:
                        if (Character.isDigit(caractere)) {
                            goToState(3);
                        } else if (caractere == '.') {
                            goToState(4);
                        } else if (Character.isAlphabetic(caractere)) {
                            state = -1;
                            current.lexeme = String.format("%s%s", current.lexeme, caractere);
                        } else if (!Character.isDigit(caractere) && !(caractere == '.')) {
                            if (current.lexeme.matches("(\\d)+")) {
                                current.column += aux;
                                aux = current.lexeme.length();
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.NUM_INT);
                            }
                        }
                        break;
                    //state verification of number token (float)
                    case 4:
                        if (Character.isDigit(caractere)) {
                            goToState(4);
                        } else if (caractere == '.') {
                            state = -1;
                        } else {
                            if (current.lexeme.matches("(\\d)+[.](\\d)+")) {
                                current.column += aux;
                                aux = current.lexeme.length();
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.NUM_FLOAT);
                            }
                        }
                        break;

                    // state verification of string token
                    case 5:
                        if (caractere == '"') {
                            goToState(9);
                        } else if (current.lexeme.matches("\".*")) {
                            goToState(5);
                        }
                        break;

                    // state verification of code comments or div token
                    case 6:
                        if (contentFile.charAt(cursor) == '*') {
                            while (!current.lexeme.endsWith("*/")) {
                                if (current.lexeme.endsWith(System.getProperty("line.separator"))) {
                                    current.line++;
                                    flag = 0;
                                }
                                cursor++;
                                current.lexeme = String.format("%s%s", current.lexeme, caractere);
                                caractere = getChar();
                            }
                            current.lexeme = "";
                            state = 1;
                            break;
                        } else if (caractere == '/') {
                            while (!current.lexeme.endsWith(System.getProperty("line.separator"))) {
                                cursor++;
                                caractere = getChar();
                                current.lexeme = String.format("%s%s", current.lexeme, caractere);
                            }
                            current.lexeme = "";
                            current.line++;
                            flag = 0;
                            state = 1;
                        } else {
                            cursor++;
                            current.column += aux;
                            current.lexeme = String.format("%s%s", current.lexeme, caractere);
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.DIV);
                        }

                        break;

                    // state verification of potency (**) or just multiply  7
                    case 7:
                        if (caractere == '*') {
                            cursor++;
                            current.column += aux;
                            current.lexeme = String.format("%s%s", current.lexeme, caractere);
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.POTENCY);
                        } else {
                            cursor++;
                            current.column += aux;
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.MULT);
                        }


                        //Estado final retorno de tokens: ident, str
                    case 9:
                        if (flag == 0) {
                            current.column = 1;
                        }
                        if (flag > 0) {
                            current.column = current.column + aux;
                        }
                        flag++;

                        if (current.lexeme.equals("print")) {
                            aux = current.lexeme.length();
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.PRINT);
                        } else if (current.lexeme.equals("scan")) {
                            aux = current.lexeme.length();
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.SCAN);
                        } else if (current.lexeme.equals("return")) {
                            aux = current.lexeme.length();
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.RETURN);
                        } else if (current.lexeme.equals("int")) {
                            aux = current.lexeme.length();
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.INT);
                        } else if (current.lexeme.equals("float")) {
                            aux = current.lexeme.length();
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.FLOAT);
                        } else if (current.lexeme.equals("break")) {
                            aux = current.lexeme.length();
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.BREAK);
                        } else if (current.lexeme.equals("continue")) {
                            aux = current.lexeme.length();
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.CONTINUE);
                        } else if (current.lexeme.equals("for")) {
                            aux = current.lexeme.length();
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.FOR);
                        } else if (current.lexeme.equals("while")) {
                            aux = current.lexeme.length();
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.WHILE);
                        } else if (current.lexeme.equals("if")) {
                            aux = current.lexeme.length();
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.IF);
                        } else if (current.lexeme.equals("else")) {
                            aux = current.lexeme.length();
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.ELSE);
                        } else if (current.lexeme.equals("break")) {
                            aux = current.lexeme.length();
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.BREAK);
                        } else if (current.lexeme.matches("[a-zA-Z]+[\\d]*")) {
                            aux = current.lexeme.length();
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.IDENT);
                        } else if (current.lexeme.matches("\".*\"")) {
                            aux = current.lexeme.length() - 1;
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.STR);
                        } else if (current.lexeme.matches("\\z")) {
                            aux = current.lexeme.length();
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.EOF);
                        } else {
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.ERROR);
                        }

                }
            }
        }

        return LexicalAnalysis.updateCurrent(current.line, current.column, Token.EOF);
    }

    private static String loadArq(Path name) throws IOException {
        return new String(Files.readAllBytes(name));
    }

    private static Current updateCurrent(int line, int column, Token token) {

        current.line = line;
        current.column = column;
        current.token = token;
        System.out.println(current.toString());
        return current;
    }

}
