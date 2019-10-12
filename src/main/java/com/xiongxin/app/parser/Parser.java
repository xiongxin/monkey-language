package com.xiongxin.app.parser;

import com.xiongxin.app.lexer.Lexer;
import com.xiongxin.app.lexer.Token;

public class Parser {

    private Lexer lexer;
    private Token curToken = null;
    private Token peekToken = null;

    public Parser(Lexer lexer) {
        this.lexer = lexer;

        nextToken();
        nextToken();
    }

    private void nextToken() {
        curToken = peekToken;
        peekToken = lexer.nextToken();
    }
}
