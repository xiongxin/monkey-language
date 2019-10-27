package com.xiongxin.app.ast;

import com.xiongxin.app.lexer.Token;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CallExpression implements Expression {
    public Token token;
    public Expression function; // identifier
    public List<Expression> arguments = new LinkedList<>();

    @Override
    public void expressionNode() {

    }

    @Override
    public String tokenLiteral() {
        return token.literal;
    }

    @Override
    public String toString() {

        return function.toString() +
                "(" +
                arguments.stream().map(Object::toString).collect(Collectors.joining(",")) +
                ") ";
    }
}
