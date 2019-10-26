package com.xiongxin.app.ast;

import com.xiongxin.app.lexer.Token;

public class StringLiteral implements Expression {

    public Token token;
    public String value;

    public StringLiteral(Token token, String value) {
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
