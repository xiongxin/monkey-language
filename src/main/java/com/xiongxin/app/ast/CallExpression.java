package com.xiongxin.app.ast;

import com.xiongxin.app.lexer.Token;

import java.util.LinkedList;
import java.util.List;

public class CallExpression implements Expression {
    public Token token;
    public Expression function;
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
        StringBuilder sb = new StringBuilder();

        sb.append(function.toString());
        sb.append("(");
        arguments.forEach( params -> {
            sb.append(params.toString());
            sb.append(",");
        } );
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(") ");

        return sb.toString();
    }
}
