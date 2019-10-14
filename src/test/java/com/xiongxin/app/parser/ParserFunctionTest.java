package com.xiongxin.app.parser;

import com.xiongxin.app.ast.*;
import com.xiongxin.app.lexer.Lexer;
import com.xiongxin.app.lexer.Token;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserFunctionTest {

    @Test
    public void testLetStatements() {
        PrefixParseFn  fn = () -> new Identifier(new Token(Token.IDENT, "ab"), "abc");
        Expression expression = fn.apply();

        System.out.println(expression);
    }
}
