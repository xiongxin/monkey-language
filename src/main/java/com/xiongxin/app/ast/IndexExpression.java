package com.xiongxin.app.ast;

import com.xiongxin.app.lexer.Token;

public class IndexExpression implements Expression {

    public Token token; // [ token
    public Expression left;
    public Expression index;

    @Override
    public void expressionNode() {

    }

    @Override
    public String toString() {
        return "(" +
                left.toString() +
                "[" +
                index.toString() +
                "])";
    }

    @Override
    public String tokenLiteral() {
        return token.literal;
    }
}
