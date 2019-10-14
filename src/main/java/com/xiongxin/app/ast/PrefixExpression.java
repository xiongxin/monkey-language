package com.xiongxin.app.ast;

import com.xiongxin.app.lexer.Token;

public class PrefixExpression implements Expression {
    public Token token;
    public String operator;
    public Expression right;


    @Override
    public void expressionNode() {

    }

    @Override
    public String tokenLiteral() {
        return token.literal;
    }

    @Override
    public String toString() {

        return "(" +
                    operator +
                    right.toString() +
                ")";
    }
}
