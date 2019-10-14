package com.xiongxin.app.ast;

import com.xiongxin.app.lexer.Token;

public class ReturnStatement implements Statement {

    public Token token;  // the RETURN token
    public Exception returnValue; // 表达式

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

        if (returnValue != null) {
            sb.append(returnValue.toString());
        }

        sb.append(";");

        return sb.toString();
    }
}
