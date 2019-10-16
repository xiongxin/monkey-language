package com.xiongxin.app.ast;


import com.xiongxin.app.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement implements Statement {
    public Token token;
    public List<Statement> statements = new ArrayList<>();

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
        statements.forEach(sb::append);
        return sb.toString();
    }
}
