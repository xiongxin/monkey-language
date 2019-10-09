package com.xiongxin.app.lexing;

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
        Token token;

        switch (ch) {
            case '=':
                token = newToken(Token.ASSIGN, ch);
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
            case Character.MIN_VALUE:
                token = newToken(Token.EOF, Character.MIN_VALUE);
                break;
            default:
                token = newToken(Token.ILLEGAL, ch);
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
}
