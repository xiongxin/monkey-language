package com.xiongxin.app.ast;

import com.xiongxin.app.lexer.Token;

import java.util.List;
import java.util.stream.Collectors;

public class ArrayLiteral implements Expression {

    public Token token;
    public List<Expression> elements;

    @Override
    public void expressionNode() {

    }


    @Override
    public String tokenLiteral() {
        return token.literal;
    }


    @Override
    public String toString() {

        return "[" +
                elements.stream().map(Object::toString).collect(Collectors.joining(",")) +
                "]";
    }
}
