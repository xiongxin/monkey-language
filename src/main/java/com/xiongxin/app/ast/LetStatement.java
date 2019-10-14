package com.xiongxin.app.ast;

import com.xiongxin.app.lexer.Token;

public class LetStatement implements Statement {

    public Token token;  // the LET token
    public Identifier name; // 标识符
    public Expression value; // 表达式

    @Override
    public void statementNode() {

    }

    @Override
    public String tokenLiteral() {
        return token.literal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(token.literal);
        sb.append(" ");
        sb.append(name.toString());
        sb.append(" = ");

        if (value != null) {
            sb.append(value.toString());
        }

        sb.append(";");

        return sb.toString();
    }
}
