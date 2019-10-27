package com.xiongxin.app.lexer;

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
        String input = "let five = 5; " +
                "let ten = 10; \n" +
                "let add = fn(x, y) { \n" +
                "x + y \n" +
                "}; \n" +
                "let result = add(five, ten); " +
                "!-/*5;\n" +
                "5 < 10 > 5;\n" +
                "if ( 5 < 10) { \n" +
                "return true; \n " +
                "} else {" +
                "return false; \n" +
                "} \n" +
                "10 == 10; \n" +
                "10 != 9; \n" +
                "\"foobar\"" +
                "\"foo bar\"" +
                "[1, 2]";
        List<Expected> tests = new ArrayList<>();

        tests.add(new Expected(Token.LET, "let"));
        tests.add(new Expected(Token.IDENT, "five"));
        tests.add(new Expected(Token.ASSIGN, "="));
        tests.add(new Expected(Token.INT, "5"));
        tests.add(new Expected(Token.SEMICOLON, ";"));
        tests.add(new Expected(Token.LET, "let"));
        tests.add(new Expected(Token.IDENT, "ten"));
        tests.add(new Expected(Token.ASSIGN, "="));
        tests.add(new Expected(Token.INT, "10"));
        tests.add(new Expected(Token.SEMICOLON, ";"));
        tests.add(new Expected(Token.LET, "let"));
        tests.add(new Expected(Token.IDENT, "add"));
        tests.add(new Expected(Token.ASSIGN, "="));
        tests.add(new Expected(Token.FUNCTION, "fn"));
        tests.add(new Expected(Token.LPAREN, "("));
        tests.add(new Expected(Token.IDENT, "x"));
        tests.add(new Expected(Token.COMMA, ","));
        tests.add(new Expected(Token.IDENT, "y"));
        tests.add(new Expected(Token.RPAREN, ")"));
        tests.add(new Expected(Token.LBRACE, "{"));
        tests.add(new Expected(Token.IDENT, "x"));
        tests.add(new Expected(Token.PLUS, "+"));
        tests.add(new Expected(Token.IDENT, "y"));
        tests.add(new Expected(Token.RBARCE, "}"));
        tests.add(new Expected(Token.SEMICOLON, ";"));
        tests.add(new Expected(Token.LET, "let"));
        tests.add(new Expected(Token.IDENT, "result"));
        tests.add(new Expected(Token.ASSIGN, "="));
        tests.add(new Expected(Token.IDENT, "add"));
        tests.add(new Expected(Token.LPAREN, "("));
        tests.add(new Expected(Token.IDENT, "five"));
        tests.add(new Expected(Token.COMMA, ","));
        tests.add(new Expected(Token.IDENT, "ten"));
        tests.add(new Expected(Token.RPAREN, ")"));
        tests.add(new Expected(Token.SEMICOLON, ";"));
        tests.add(new Expected(Token.BANG, "!"));
        tests.add(new Expected(Token.MINUS, "-"));
        tests.add(new Expected(Token.SLASH, "/"));
        tests.add(new Expected(Token.ASTERISK, "*"));
        tests.add(new Expected(Token.INT, "5"));
        tests.add(new Expected(Token.SEMICOLON, ";"));
        tests.add(new Expected(Token.INT, "5"));
        tests.add(new Expected(Token.LT, "<"));
        tests.add(new Expected(Token.INT, "10"));
        tests.add(new Expected(Token.GT, ">"));
        tests.add(new Expected(Token.INT, "5"));
        tests.add(new Expected(Token.SEMICOLON, ";"));
        tests.add(new Expected(Token.IF, "if"));
        tests.add(new Expected(Token.LPAREN, "("));
        tests.add(new Expected(Token.INT, "5"));
        tests.add(new Expected(Token.LT, "<"));
        tests.add(new Expected(Token.INT, "10"));
        tests.add(new Expected(Token.RPAREN, ")"));
        tests.add(new Expected(Token.LBRACE, "{"));
        tests.add(new Expected(Token.RETURN, "return"));
        tests.add(new Expected(Token.TRUE, "true"));
        tests.add(new Expected(Token.SEMICOLON, ";"));
        tests.add(new Expected(Token.RBARCE, "}"));
        tests.add(new Expected(Token.ELSE, "else"));
        tests.add(new Expected(Token.LBRACE, "{"));
        tests.add(new Expected(Token.RETURN, "return"));
        tests.add(new Expected(Token.FALSE, "false"));
        tests.add(new Expected(Token.SEMICOLON, ";"));
        tests.add(new Expected(Token.RBARCE, "}"));
        tests.add(new Expected(Token.INT, "10"));
        tests.add(new Expected(Token.EQ, "=="));
        tests.add(new Expected(Token.INT, "10"));
        tests.add(new Expected(Token.SEMICOLON, ";"));
        tests.add(new Expected(Token.INT, "10"));
        tests.add(new Expected(Token.NOT_EQ, "!="));
        tests.add(new Expected(Token.INT, "9"));
        tests.add(new Expected(Token.SEMICOLON, ";"));
        tests.add(new Expected(Token.STRING, "foobar"));
        tests.add(new Expected(Token.STRING, "foo bar"));
        tests.add(new Expected(Token.LBRACKET, "["));
        tests.add(new Expected(Token.INT, "1"));
        tests.add(new Expected(Token.COMMA, ","));
        tests.add(new Expected(Token.INT, "2"));
        tests.add(new Expected(Token.RBRACKET, "]"));
        tests.add(new Expected(Token.EOF, String.valueOf(Character.MIN_VALUE)));


        Lexer lexer = new Lexer(input);

        tests.forEach( expected -> {
            Token token = lexer.nextToken();
            assertEquals("token equal: " + token.type, expected.expectedType , token.type );
            assertEquals("literal equal: " + token.literal, expected.expectLiteral, token.literal );
        });
    }
}
