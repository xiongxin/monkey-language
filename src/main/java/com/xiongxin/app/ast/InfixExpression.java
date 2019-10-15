package com.xiongxin.app.ast;

import com.xiongxin.app.lexer.Token;

public class InfixExpression implements Expression {

    public Token token;
    public Expression left;
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
                left.toString() +
                " " +
                operator +
                " " +
                right.toString() +
                ")";
    }
}
