package com.xiongxin.app.ast;

import com.xiongxin.app.lexer.Token;

// We can add it to the Statements array of Program's statements
public class ExpressionStatement implements Statement {

    public Token token; // the first token of the expression
    public Expression expression;

    @Override
    public void statementNode() {

    }

    @Override
    public String tokenLiteral() {
        return token.literal;
    }

    @Override
    public String toString() {
        return expression.toString();
    }
}
