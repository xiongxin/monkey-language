package com.xiongxin.app.ast;

import com.xiongxin.app.lexer.Token;

public class Identifier implements Expression {
    public Token token; // the IDENT token
    public String value;

    @Override
    public void expressionNode() {

    }

    @Override
    public String tokenLiteral() {
        return token.literal;
    }
}
