package com.xiongxin.app.ast;

import com.xiongxin.app.lexer.Token;

public class IfExpression implements Expression {
    public Token token;
    public Expression condition;
    public BlockStatement consequence;
    public BlockStatement alternative;

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

        sb.append("if");
        sb.append(condition.toString());
        sb.append(" ");
        sb.append(consequence.toString());

        if (alternative != null) {
            sb.append(" else ");
            sb.append(alternative.toString());
        }

        return sb.toString();
    }
}
