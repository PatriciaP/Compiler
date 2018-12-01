/*
 * IFMG - COMPILERS - 2018
 * Lexical Analysis 
 */
package compiler.lexical_analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 *
 * @author Patricia Pieroni The Lexical Analysis reads the source file,
 * character by character, taking the tokens as established by the grammar
 */
public class LexicalAnalysis {

    public static char caractere;
    public static int cursor = 0, strLenght = 0, state = 1, flag = 0, aux = 0;
    public static String contentFile;
    public static File file;
    public static Current current = new Current();

//    public static void main(String[] args) throws IOException {
//        file = new File("C:\\Users\\patri\\Documents\\NetBeansProjects\\Compilers\\src\\input\\teste.txt").getAbsoluteFile();
//        contentFile = loadArq(file.getAbsoluteFile().toPath());
//       
//
//        System.out.println(contentFile);
//        while (current.token != Token.EOF) {
//            getToken();
//        }
//
//    }
    
    public static char getChar() {
        
        return contentFile.charAt(cursor);
    }

    public static Current getToken() {
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
                            current.lexeme = current.lexeme + caractere;
                            cursor++;
                            if (current.lexeme.contains(System.getProperty("line.separator"))) {
                                current.line++;
                                flag = 0;
                                current.lexeme="";
                            }
                        } else {
                            current.lexeme = "";
                            if (caractere == ' ' || caractere == '\t') {
                                cursor++;
                                current.column++;
                                continue;
                            }
                            if (Character.isAlphabetic(caractere)) {
                                state = 2;
                                cursor++;
                                current.lexeme = current.lexeme + caractere;
                            } else if (Character.isDigit(caractere)) {
                                cursor++;
                                state = 3;
                                current.lexeme = current.lexeme + caractere;
                            } else if (caractere == '"') {
                                cursor++;
                                state = 4;
                                current.lexeme = current.lexeme + caractere;
                            } else if (caractere == '(') {
                                cursor++;
                                current.column = current.column + aux;
                                aux = 1;
                                current.lexeme = current.lexeme + caractere;
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.OPEN_PAR);

                            } else if (caractere == ')') {
                                cursor++;
                                current.column = current.column + aux;
                                aux = 1;
                                current.lexeme = current.lexeme + caractere;
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.CLOSE_PAR);
                            } else if (caractere == '{') {
                                cursor++;
                                current.column = current.column + aux;
                                aux = 1;
                                current.lexeme = current.lexeme + caractere;
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.OPEN_BRACKET);
                            } else if (caractere == '}') {
                                cursor++;
                                current.column = current.column + aux;
                                aux = 1;
                                current.lexeme = current.lexeme + caractere;
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.CLOSE_BRACKET);
                            } else if (caractere == ';') {
                                cursor++;
                                current.column = current.column + aux;
                                aux = 1;
                                current.lexeme = current.lexeme + caractere;
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.SEMICOLON);
                            } else if (caractere == ',') {
                                cursor++;
                                current.column = current.column + aux;
                                aux = 1;
                                current.lexeme = current.lexeme + caractere;
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.COMMA);
                            } else if (caractere == '+') {
                                cursor++;
                                current.column = current.column + aux;
                                aux = 1;
                                current.lexeme = current.lexeme + caractere;
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.SUM);
                            } else if (caractere == '*') {
                                cursor++;
                                current.column = current.column + aux;
                                aux = 1;
                                current.lexeme = current.lexeme + caractere;
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.MULT);

                            } else if (caractere == '/') {
                                cursor++;
                                current.lexeme = current.lexeme + caractere;
                                state = 5;
                            } else if (caractere == '%') {
                                cursor++;
                                current.column = current.column + aux;
                                aux = 1;
                                current.lexeme = current.lexeme + caractere;
                                return LexicalAnalysis.updateCurrent(current.line, current.column, Token.MOD);
                            } else if (caractere == '=') {
                                cursor++;
                                current.column = current.column + aux;
                                aux = 1;
                                current.lexeme = current.lexeme + caractere;
                                if (contentFile.charAt(cursor) == '=') {
                                    cursor++;
                                    current.lexeme = current.lexeme + '*';
                                    return LexicalAnalysis.updateCurrent(current.line, current.column, Token.EQUAL);
                                } else {
                                    cursor++;
                                    return LexicalAnalysis.updateCurrent(current.line, current.column, Token.RECEIVE);
                                }
                            } else if (caractere == '!') {
                                cursor++;
                                current.column = current.column + aux;
                                aux = 1;
                                current.lexeme = current.lexeme + caractere;
                                if (contentFile.charAt(cursor) == '=') {
                                    cursor++;
                                    current.lexeme = current.lexeme + '=';
                                    return LexicalAnalysis.updateCurrent(current.line, current.column, Token.DIFFERENT);
                                } else {
                                    cursor++;
                                    return LexicalAnalysis.updateCurrent(current.line, current.column, Token.NOT);
                                }
                            } else if (caractere == '>') {
                                cursor++;
                                current.column = current.column + aux;
                                aux = 1;
                                current.lexeme = current.lexeme + caractere;
                                if (contentFile.charAt(cursor) == '=') {
                                    cursor++;
                                    current.lexeme = current.lexeme + '=';
                                    return LexicalAnalysis.updateCurrent(current.line, current.column, Token.BIGGER_EQUAL);
                                } else {
                                    cursor++;
                                    return LexicalAnalysis.updateCurrent(current.line, current.column, Token.BIGGER);
                                }
                            } else if (caractere == '<') {
                                cursor++;
                                current.column = current.column + aux;
                                aux = 1;
                                current.lexeme = current.lexeme + caractere;
                                if (contentFile.charAt(cursor) == '=') {
                                    cursor++;
                                    current.lexeme = current.lexeme + '=';
                                    return LexicalAnalysis.updateCurrent(current.line, current.column, Token.EQUAL);
                                } else {
                                    cursor++;
                                    return LexicalAnalysis.updateCurrent(current.line, current.column, Token.SMALLER_EQUAL);
                                }
                            } else if (cursor > contentFile.length()) {
                                state = 9;
                            }
                        }
                        break;

                    // ESTADO DE VERIFICACAO DOS TOKENS IDENT, OU KEYWORDS    
                    case 2:
                        if (current.lexeme.matches("[a-zA-Z]+[\\d]*")) {
                            cursor++;
                            state = 2;
                            current.lexeme = current.lexeme + caractere;
                        } else if (!Character.isAlphabetic(caractere) || !Character.isDigit(caractere)) {
                            cursor--;
                            current.lexeme = current.lexeme.substring(0, current.lexeme.length() - 1);
                            state = 9;
                        }
                        break;

                    // ESTADO VERIFICACAO DO TOKEN DE NUMERO
                    case 3:
                        if (Character.isDigit(caractere)) {
                            cursor++;
                            state = 3;
                            current.lexeme = current.lexeme + caractere;
                        } else if (caractere == '.') {
                            cursor++;
                            state = 8;
                            current.lexeme = current.lexeme + caractere;
                        } else if (Character.isAlphabetic(caractere)) {
                            state = -1;
                            current.lexeme = current.lexeme + caractere;
                        } else if (!Character.isDigit(caractere) && !(caractere == '.')) {
                            state = 9;

                        }
                        break;

                    // ESTADO VERIFICACAO TOKEN STR    
                    case 4:
                        current.lexeme = current.lexeme + caractere;
                        if (caractere == '"') {
                            state = 9;
                            cursor++;
                        } else if (current.lexeme.matches("\".*")) {
                            cursor++;
                            state = 4;
                        }
                        break;

                    case 5:
                        if (contentFile.charAt(cursor) == '*') {
                            while (!current.lexeme.endsWith("*/")) {
                                if (current.lexeme.contains(System.getProperty("line.separator"))) {
                                    current.line++;
                                    flag = 0;
                                }
                                cursor++;
                                current.lexeme = current.lexeme + caractere;
                                caractere = getChar();
                            }
                            current.lexeme = "";
                            state = 1;
                            break;
                        } else if (caractere == '/') {
                            while (!current.lexeme.endsWith(System.getProperty("line.separator"))) {
                                cursor++;
                                caractere = getChar();
                                current.lexeme = current.lexeme + caractere;
                            }
                            current.lexeme = "";
                            current.line++;
                            flag = 0;
                            state = 1;
                        } else {
                            cursor++;
                            current.lexeme = current.lexeme + caractere;
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.DIV);
                        }

                        break;

                    // ESTADO VERIFICACAO DO TOKEN DE NUMERO
                    case 8:
                        if (Character.isDigit(caractere)) {
                            cursor++;
                            state = 8;
                            current.lexeme = current.lexeme + caractere;
                        } else if (caractere == '.') {
                            state = -1;
                        } else {
                            state = 9;

                        }
                        break;

                    //Estado final retorno de tokens
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
                        } else if (current.lexeme.matches("(\\d)+[.](\\d)+")) {
                            aux = current.lexeme.length();
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.NUM_FLOAT);
                        } else if (current.lexeme.matches("(\\d)+")) {
                            aux = current.lexeme.length();
                            return LexicalAnalysis.updateCurrent(current.line, current.column, Token.NUM_INT);
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

    public static String loadArq(Path name) throws IOException {
        return new String(Files.readAllBytes(name));
    }

    public static Current updateCurrent(int line, int column, Token token) {

        current.line = line;
        current.column = column;
        current.token = token;
        //System.out.println(current.toString());
        return current;

    }
    
   

}
