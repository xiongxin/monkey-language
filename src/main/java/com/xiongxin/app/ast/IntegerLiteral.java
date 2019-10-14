package com.xiongxin.app.ast;

import com.xiongxin.app.lexer.Token;

public class IntegerLiteral implements Expression {

    public Token token; // INT
    public Integer value;

    @Override
    public void expressionNode() {

    }

    @Override
    public String tokenLiteral() {
        return token.literal;
    }

    @Override
    public String toString() {
        return token.literal;
    }
}
