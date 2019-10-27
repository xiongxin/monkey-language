package com.xiongxin.app.ast;

import com.xiongxin.app.lexer.Token;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionLiteral implements Expression {
    public Token token; // The fn token
    public List<Identifier> parameters = new LinkedList<>();
    public BlockStatement body;

    @Override
    public void expressionNode() {

    }

    @Override
    public String tokenLiteral() {
        return null;
    }


    @Override
    public String toString() {

        return token.literal +
                "(" +
                parameters.stream().map(Object::toString).collect(Collectors.joining(",")) +
                ") " +
                body.toString();
    }
}
