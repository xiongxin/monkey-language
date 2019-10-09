package com.xiongxin.app.lexing;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class LexerTest
{

    private class Expected {
        public String expectedType;
        public String expectLiteral;

        public Expected(String expectedType, String expectLiteral) {
            this.expectedType = expectedType;
            this.expectLiteral = expectLiteral;
        }
    }

    @Test
    public void testNextToken() {
        String input = "=+(){},;";

        List<Expected> tests = new ArrayList<>();

        tests.add(new Expected(Token.ASSIGN, "="));
        tests.add(new Expected(Token.PLUS, "+"));
        tests.add(new Expected(Token.LPAREN, "("));
        tests.add(new Expected(Token.RPAREN, ")"));
        tests.add(new Expected(Token.LBRACE, "{"));
        tests.add(new Expected(Token.RBARCE, "}"));
        tests.add(new Expected(Token.COMMA, ","));
        tests.add(new Expected(Token.SEMICOLON, ";"));

        Lexer lexer = new Lexer(input);

        tests.forEach( expected -> {
            Token token = lexer.nextToken();
            assertEquals("token equal 1", expected.expectedType , token.type );
            assertEquals("token equal 2", expected.expectLiteral, token.literal );
        });
    }



    @Test
    public void testMoreToken() {
        String input = "let five = 5;" +
                "let ten = 10;" +
                "let add = fn(x, y) {" +
                "x + y" +
                "};" +
                "let result = add(five, ten);";

        List<Expected> tests = new ArrayList<>();
    }
}
