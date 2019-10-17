package com.xiongxin.app.ast;

import com.xiongxin.app.lexer.Token;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        StringBuilder sb = new StringBuilder();

        sb.append(token.literal);
        sb.append("(");
        parameters.forEach( params -> {
            sb.append(params.toString());
            sb.append(",");
        } );
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(") ");
        sb.append(body.toString());

        return sb.toString();
    }
}
