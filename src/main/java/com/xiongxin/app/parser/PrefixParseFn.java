package com.xiongxin.app.parser;

import com.xiongxin.app.ast.Expression;
import jdk.nashorn.internal.objects.annotations.Function;

@FunctionalInterface
public interface PrefixParseFn {

    Expression apply();
}
