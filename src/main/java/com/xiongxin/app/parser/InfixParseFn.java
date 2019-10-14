package com.xiongxin.app.parser;

import com.xiongxin.app.ast.Expression;

@FunctionalInterface
public interface InfixParseFn {

    Expression apply(Expression expression);
}
