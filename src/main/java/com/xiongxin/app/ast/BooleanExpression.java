package com.xiongxin.app.ast;

import com.xiongxin.app.lexer.Token;

public class BooleanExpression implements Expression {
    public Token token;
    public Boolean value;

    public BooleanExpression(Token token, boolean value) {
        this.token = token;
        this.value = value;
    }

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
