package com.xiongxin.app.ast;

import com.xiongxin.app.lexer.Token;

public class LetStatement implements Statement {

    public Token token;  // the LET token
    public Identifier name; // 标识符
    public Exception value; // 表达式

    @Override
    public void statementNode() {

    }

    @Override
    public String tokenLiteral() {
        return token.literal;
    }
}
