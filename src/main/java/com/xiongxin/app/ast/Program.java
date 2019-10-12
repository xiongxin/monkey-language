package com.xiongxin.app.ast;

/**
 * Root node of ast
 */
public class Program implements Node {

    public Statement[] statements;


    @Override
    public String tokenLiteral() {
        if (statements.length > 0) {
            return statements[0].tokenLiteral();
        } else {
            return "";
        }
    }
}
