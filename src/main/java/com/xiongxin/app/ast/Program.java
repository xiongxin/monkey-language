package com.xiongxin.app.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Root node of ast
 */
public class Program implements Node {

    public List<Statement> statements = new ArrayList<>();


    @Override
    public String tokenLiteral() {
        if (statements.size() > 0) {
            return statements.get(0).tokenLiteral();
        } else {
            return "";
        }
    }
}
