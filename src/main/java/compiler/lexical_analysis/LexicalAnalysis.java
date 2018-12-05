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

    public static String contentFile = null;
    public static Current current = new Current();
    private static int state = 1;
    private static char readChar;
    public static File file;
    private static int cursor = -1;

    public static void main(String[] args) throws IOException {
        file = new File("src\\main\\java\\compiler\\input\\test").getCanonicalFile();
        System.out.println(file);
        contentFile = loadArq(file.toPath());
        System.out.println(contentFile);
        while (current.token != Token.EOF) {
            getToken();
            System.out.println(current.toString());
        }

    }

    private static char getChar() {
        cursor++;
        if (cursor >= contentFile.length()) {
            return '$';
        }
        current.column++;
        return contentFile.charAt(cursor);
    }

    private static void removeChar() {
        cursor--;
        current.column--;
        if (current.lexeme.length() > 0)
            current.lexeme = current.lexeme.substring(0, current.getLexeme().length() - 1);
    }

    private static Current tokenAtualize(Token token) {

        current.token = token;
        removeChar();

        switch (current.lexeme) {
            case "print":
                current.token = Token.PRINT;
                break;
            case "scan":
                current.token = Token.SCAN;
                break;
            case "for":
                current.token = Token.FOR;
                break;
            case "while":
                current.token = Token.WHILE;
                break;
            case "if":
                current.token = Token.IF;
                break;
            case "else":
                current.token = Token.ELSE;
                break;
            case "break":
                current.token = Token.BREAK;
                break;
            case "continue":
                current.token = Token.CONTINUE;
                break;
            case "return":
                current.token = Token.RETURN;
                break;
            case "int":
                current.token = Token.INT;
                break;
            case "float":
                current.token = Token.FLOAT;
                break;

        }

        return current;
    }


    public static Current getToken() {
        state = 1;
        current.lexeme = "";

        while (true) {
            readChar = getChar();

            //eof
            if (readChar != '$') {
                current.lexeme = String.format("%s%s", current.getLexeme(), readChar);
            }

            if (readChar == '\n') {
                current.lexeme = current.getLexeme().substring(0, current.getLexeme().length() - 1);
                current.line++;
                current.column = 1;
            } else if (state == 1) {

                if (readChar == ' ' || readChar == '\t') {
                    current.lexeme = current.getLexeme().substring(0, current.getLexeme().length() - 1);
                } else if (readChar == '$') {
                    return tokenAtualize(Token.EOF);

                } else if (Character.isAlphabetic(readChar)) {
                    state = 2;
                } else if (Character.isDigit(readChar)) {
                    state = 4;
                } else if (readChar == '%') {
                    state = 9;
                } else if (readChar == '/') {
                    state = 10;
                } else if (readChar == '-') {
                    state = 11;
                } else if (readChar == '+') {
                    state = 12;
                } else if (readChar == '=') {
                    state = 13;
                } else if (readChar == ',') {
                    state = 14;
                } else if (readChar == ';') {
                    state = 15;
                } else if (readChar == '(') {
                    state = 16;
                } else if (readChar == ')') {
                    state = 17;
                } else if (readChar == '*') {
                    state = 18;
                } else if (readChar == '"') {
                    state = 21;
                } else if (readChar == '&') {
                    state = 24;
                } else if (readChar == '|') {
                    state = 26;
                } else if (readChar == '!') {
                    state = 28;
                } else if (readChar == '>') {
                    state = 31;
                } else if (readChar == '<') {
                    state = 33;
                } else if (readChar == '{') {
                    state = 35;
                } else if (readChar == '}') {
                    state = 36;
                } else {
                    state = -1;
                }

            } else if (state == 2) {
                //Token Identifier
                if (Character.isAlphabetic(readChar) ||
                        Character.isDigit(readChar) && readChar != '$') {
                    state = 2;
                } else {
                    removeChar();
                    state = 3;
                }
            } else if (state == 3) {
                return tokenAtualize(Token.IDENT);
            } else if (state == 4) {
                //Token Number
                if (Character.isDigit(readChar)) {
                    state = 4;
                } else if (readChar == '.') {
                    state = 6;
                } else if (Character.isAlphabetic(readChar) && readChar != '$') {
                    state = 2;
                } else {
                    removeChar();
                    state = 5;
                }
            } else if (state == 5) {
                return tokenAtualize(Token.NUM_INT);
            } else if (state == 6) {
                if (Character.isDigit(readChar)) {
                    state = 7;
                } else {
                    removeChar();
                    state = -1;
                }
            } else if (state == 7) {
                if (Character.isDigit(readChar)) {
                    state = 7;
                } else {
                    removeChar();
                    state = 23;
                }
            }


//            else if (state == 8) {
//
            else if (state == 9) {
                return tokenAtualize(Token.MOD);
            } else if (state == 10) {
                if (readChar == '/') {
                    //line comment
                    state = -2;
                } else if (readChar == '*') {
                    //block comment
                    state = -3;
                } else {
                    return tokenAtualize(Token.DIV);
                }
            } else if (state == 11) {
                return tokenAtualize(Token.SUBTRACTION);
            } else if (state == 12) {
                return tokenAtualize(Token.SUM);
            } else if (state == 13) {
                if (readChar == '=') {
                    state = 30;
                } else {
                    return tokenAtualize(Token.RECEIVE);
                }
            } else if (state == 14) {
                return tokenAtualize(Token.COMMA);
            } else if (state == 15) {
                return tokenAtualize(Token.SEMICOLON);
            } else if (state == 16) {
                return tokenAtualize(Token.OPEN_PAR);
            } else if (state == 17) {
                return tokenAtualize(Token.CLOSE_PAR);
            } else if (state == 18) {
                if (readChar == '*') {
                    state = 20;
                } else {
                    removeChar();
                    state = 19;
                }
            } else if (state == 19) {
                return tokenAtualize(Token.MULT);
            } else if (state == 20) {
                return tokenAtualize(Token.POTENCY);
            } else if (state == 21) {
                //STRING
                if (readChar == '\\') {
                    state = 39;
                } else if (readChar == '"') {
                    state = 22;
                } else {
                    state = 21;
                }

            } else if (state == 22) {
                current.lexeme = current.getLexeme().substring(1, current.getLexeme().length() - 1);
                return tokenAtualize(Token.STR);
            } else if (state == 23) {
                return tokenAtualize(Token.NUM_FLOAT);
            } else if (state == 24) {
                if (readChar == '&') {
                    state = 25;
                } else {
                    state = -1;
                }
            } else if (state == 25) {
                return tokenAtualize(Token.AND);
            } else if (state == 26) {
                if (readChar == '|') {
                    state = 27;
                } else {
                    state = -1;
                }
            } else if (state == 27) {
                return tokenAtualize(Token.OR);
            } else if (state == 28) {
                if (readChar == '=') {
                    state = 29;
                } else {
                    return tokenAtualize(Token.NOT);
                }
            } else if (state == 29) {
                return tokenAtualize(Token.DIFFERENT);
            } else if (state == 30) {
                return tokenAtualize(Token.EQUAL);
            } else if (state == 31) {
                if (readChar == '=') {
                    state = 32;
                } else {
                    return tokenAtualize(Token.BIGGER);
                }
            } else if (state == 32) {
                return tokenAtualize(Token.BIGGER_EQUAL);
            } else if (state == 33) {
                if (readChar == '=') {
                    state = 34;
                } else {
                    return tokenAtualize(Token.SMALLER);
                }
            } else if (state == 34) {
                return tokenAtualize(Token.SMALLER_EQUAL);
            } else if (state == 35) {
                return tokenAtualize(Token.OPEN_BRACKET);
            } else if (state == 36) {
                return tokenAtualize(Token.CLOSE_BRACKET);
            } else if (state == -2) {
                while (readChar != '\r') {
                    readChar = getChar();
                    if (readChar == '$') {
                        break;
                    }
                }
                current.line++;
                current.column = 1;
                current.lexeme = current.getLexeme().substring(0, current.getLexeme().length() - 3);
                state = 1;
            } else if (state == -3) {
                if (readChar != '*' && readChar != '$') {
                    state = -3;
                } else {
                    state = -4;
                }

            } else if (state == 39) {
                if (readChar != 'n') {
                    current.lexeme = current.getLexeme().substring(0, current.getLexeme().length() - 2) + "\n";

                } else {
                    current.lexeme = current.getLexeme().substring(0, current.getLexeme().length() - 2) + "\t";

                }

                state = 21;
            } else if (state == -4) {
                if (readChar == '/' || readChar == '$') {
                    current.lexeme = current.getLexeme().substring(current.getLexeme().length() - 2, current.getLexeme().length() - 2);
                    state = 1;
                } else if (readChar == '*') {
                    state = -4;
                } else {
                    state = -3;
                }
            } else if (state == -1) {
                //ERROR STATE
                removeChar();

                return tokenAtualize(Token.ERROR);
            }

        }
    }

    public static String loadArq(Path name) throws IOException {
        return new String(Files.readAllBytes(name));
    }


}
