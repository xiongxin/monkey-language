package com.xiongxin.app.lexing;

import java.util.Arrays;

public class Lexer {

    public char[] input;
    // current position in input (points to current char)
    public int position; // 已经读取过的字符
    // current reading position in input(after current char)
    public int readPosition; // 将要读取的字符
    // current char under examination
    public char ch;

    public Lexer(String input) {
        this.input = input.toCharArray();

        readChar();
    }

    public Token nextToken() {
        Token token = new Token();

        skipWhitespace();

        switch (ch) {
            case '=':
                if (peekChar() == '=') {
                    readChar();

                    token.type = Token.EQ;
                    token.literal = "==";
                } else {
                    token = newToken(Token.ASSIGN, ch);
                }
                break;
            case ';':
                token = newToken(Token.SEMICOLON, ch);
                break;
            case '(':
                token = newToken(Token.LPAREN, ch);
                break;
            case ')':
                token = newToken(Token.RPAREN, ch);
                break;
            case '{':
                token = newToken(Token.LBRACE, ch);
                break;
            case '}':
                token = newToken(Token.RBARCE, ch);
                break;
            case ',':
                token = newToken(Token.COMMA, ch);
                break;
            case '+':
                token = newToken(Token.PLUS, ch);
                break;
            case '!':
                if (peekChar() == '=') {
                    readChar();
                    token.type = Token.NOT_EQ;
                    token.literal = "!=";
                } else {
                    token = newToken(Token.BANG, ch);
                }
                break;
            case '-':
                token = newToken(Token.MINUS, ch);
                break;
            case '/':
                token = newToken(Token.SLASH, ch);
                break;
            case '*':
                token = newToken(Token.ASTERISK, ch);
                break;
            case '<':
                token = newToken(Token.LT, ch);
                break;
            case '>':
                token = newToken(Token.GT, ch);
                break;
            case Character.MIN_VALUE:
                token = newToken(Token.EOF, Character.MIN_VALUE);
                break;
            default:
                if ( Character.isLetter(ch) ) {
                    token.literal = readIdentifier();
                    token.type = Token.lookupIdent(token.literal);

                    return token;
                } else if ( Character.isDigit(ch) ) {
                    token.literal = readNumber();
                    token.type = Token.INT;

                    return token;
                } else {
                    token = newToken(Token.ILLEGAL, ch);
                }

        }

        readChar();

        return token;
    }

    private Token newToken(String tokenType, char ch) {
        return new Token(tokenType, String.valueOf(ch));
    }

    public void readChar() {
        if (readPosition >= input.length) {
            ch = Character.MIN_VALUE;
        } else {
            ch = input[readPosition];
        }

        position = readPosition;
        readPosition += 1;
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(ch)) {
            readChar();
        }
    }

    private String readIdentifier() {
        int currentPosition = position;

        while ( Character.isLetter(ch) ) {
            readChar();
        }

        return String.valueOf(Arrays.copyOfRange(input, currentPosition, position));
    }

    private String readNumber() {
        int currentPosition = position;

        while ( Character.isDigit(ch) ) {
            readChar();
        }

        return String.valueOf(Arrays.copyOfRange(input, currentPosition, position));
    }

    private char peekChar() {
        if ( readPosition >= input.length ) {
            return Character.MIN_VALUE;
        } else {
            return input[readPosition];
        }
    }
}
