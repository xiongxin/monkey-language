package com.xiongxin.app.parser;

import com.xiongxin.app.ast.*;
import com.xiongxin.app.lexer.Lexer;
import com.xiongxin.app.lexer.Token;
import jdk.internal.util.xml.impl.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {


    private void checkParserError(Parser parser) {
        if (parser.getErrors().size() == 0) {
            return;
        }

        parser.getErrors().forEach(System.out::println);
    }

    @Test
    public void testLetStatements() {
        String input = "let x = 5; \n" +
                "let y = 10; \n" +
                "let foobar = 838383; \n";

        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Program program = parser.parseProgram();

        checkParserError(parser);
        assertNotNull("program", program);
        assertEquals("program size", 3, program.statements.size());

        List<String> tests = Arrays.asList("x", "y", "foobar");

        for (int i = 0; i < tests.size(); i++) {
            LetStatement statement = (LetStatement) program.statements.get(i);
            assertEquals(statement.name.value, tests.get(i));
        }
    }

    @Test
    public void testReturnStatements() {
        String input = "return 5; \n" +
                "return 10; \n" +
                "return 99999; \n";

        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Program program = parser.parseProgram();

        checkParserError(parser);
        assertNotNull("program", program);
        assertEquals("program size", 3, program.statements.size());

        program.statements.forEach(statement -> {
            System.out.println(((ReturnStatement)statement).toString());
            assertEquals(((ReturnStatement)statement).token.type, Token.RETURN);
        });
    }

    @Test
    public void testIdentifierExpression() {
        String input = "foobar";
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Program program = parser.parseProgram();

        checkParserError(parser);

        assertEquals("statement len", 1, program.statements.size());
        ExpressionStatement statement = (ExpressionStatement) program.statements.get(0);

        Identifier identifier = (Identifier) statement.expression;

        assertEquals("identifier value", "foobar", identifier.value);

    }


    @Test
    public void testIntegerExpression() {
        String input = "11";
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Program program = parser.parseProgram();

        checkParserError(parser);

        assertEquals("statement length", 1, program.statements.size());
        ExpressionStatement statement = (ExpressionStatement) program.statements.get(0);
        IntegerLiteral integerLiteral = (IntegerLiteral) statement.expression;

        assertEquals("integer value", new Integer(11), integerLiteral.value);
    }

    @Test
    public void testPrecedencs() {
        Parser.Precedence precedence1 = Parser.Precedence.LOWEST;
        Parser.Precedence precedence2 = Parser.Precedence.CALL;
        System.out.println( precedence1.name());
        System.out.println(precedence2.ordinal());
    }

    private static class PrefixTest {
        public String input;
        public String operator;
        public Integer integerValue;

        public PrefixTest(String input, String operator, Integer integerValue) {
            this.input = input;
            this.operator = operator;
            this.integerValue = integerValue;
        }
    }

    @Test
    public void testParsingPrefixExpression() {
        List<PrefixTest> prefixTests = Arrays.asList(
                new PrefixTest("!5", "!", 5),
                new PrefixTest("-15", "-", 15),
                new PrefixTest("-1", "-", 15)
        );

        prefixTests.forEach( prefixTest -> {
            Lexer lexer = new Lexer(prefixTest.input);
            Parser parser = new Parser(lexer);
            Program program = parser.parseProgram();

            checkParserError(parser);

            assertEquals("statement length", 1, program.statements.size());
            ExpressionStatement statement = (ExpressionStatement) program.statements.get(0);
            PrefixExpression prefixExpression = (PrefixExpression) statement.expression;
            System.out.println(prefixExpression);
            assertEquals("operator",prefixTest.operator, prefixExpression.operator);
            assertEquals("integerValue", prefixTest.integerValue, ((IntegerLiteral) prefixExpression.right).value);
        });
    }

    private static class InfixTest {
        public String input;
        public Integer leftValue;
        public String operator;
        public Integer rightValue;

        public InfixTest(String input, Integer leftValue, String operator, Integer rightValue) {
            this.input = input;
            this.leftValue = leftValue;
            this.operator = operator;
            this.rightValue = rightValue;
        }
    }

    @Test
    public void testParsingInfixExpressions() {
        List<InfixTest> infixTests = Arrays.asList(
                new InfixTest(" 5 + 5", 5, "+", 5),
                new InfixTest(" 5 - 5", 5, "-", 5),
                new InfixTest(" 5 / 5", 5, "/", 5),
                new InfixTest(" 5 > 5", 5, ">", 5),
                new InfixTest(" 5 < 5", 5, "<", 5),
                new InfixTest(" 5 == 5", 5, "==", 5),
                new InfixTest(" 5 != 5", 5, "!=", 5)
        );

        infixTests.forEach( infixTest -> {
            Lexer lexer = new Lexer(infixTest.input);
            Parser parser = new Parser(lexer);
            Program program = parser.parseProgram();

            checkParserError(parser);

            assertEquals("statement", 1, program.statements.size());
            ExpressionStatement statement = (ExpressionStatement) program.statements.get(0);
            InfixExpression expression = (InfixExpression) statement.expression;
            System.out.println(expression);
            assertEquals("left", infixTest.leftValue, ( (IntegerLiteral) expression.left).value);
            assertEquals("operator",infixTest.operator, expression.operator);
            assertEquals("right", infixTest.leftValue, ( (IntegerLiteral) expression.right).value);
        } );
    }


    private static class PrecedenceTest {
        public String input;
        public String expected;

        public PrecedenceTest(String input, String expected) {
            this.input = input;
            this.expected = expected;
        }
    }


    @Test
    public void testOperatorPrecedenceParsing() {
        List<PrecedenceTest> tests = Arrays.asList(
                new PrecedenceTest("-a * b", "((-a) * b)"),
                new PrecedenceTest("!-a", "(!(-a))")
        );

        tests.forEach( precedenceTest -> {
            Lexer lexer = new Lexer(precedenceTest.input);
            Parser parser = new Parser(lexer);
            Program program = parser.parseProgram();

            checkParserError(parser);

            assertEquals("statement", 1, program.statements.size());
            ExpressionStatement statement = (ExpressionStatement) program.statements.get(0);
            assertEquals("expected", precedenceTest.expected, statement.expression.toString());
        } );
    }

    @Test
    public void testBooleanExpression() {
        String input = "false";
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Program program = parser.parseProgram();

        checkParserError(parser);

        assertEquals("statement length", 1, program.statements.size());
        ExpressionStatement statement = (ExpressionStatement) program.statements.get(0);
        BooleanExpression booleanExpression = (BooleanExpression) statement.expression;

        assertEquals("'boolean'", false, booleanExpression.value);
    }

    @Test
    public void testGroupExpression() {
        List<PrecedenceTest> tests = Arrays.asList(
                new PrecedenceTest("1 + ( 2 + 3 ) + 4", "((1 + (2 + 3)) + 4)")
                ,new PrecedenceTest("(5 + 5) * 2", "((5 + 5) * 2)")
        );

        tests.forEach( precedenceTest -> {
            Lexer lexer = new Lexer(precedenceTest.input);
            Parser parser = new Parser(lexer);
            Program program = parser.parseProgram();

            checkParserError(parser);

            //assertEquals("statement", 1, program.statements.size());
            ExpressionStatement statement = (ExpressionStatement) program.statements.get(0);
            assertEquals("expected", precedenceTest.expected, statement.expression.toString());
        } );
    }

    @Test
    public void testIfExpression() {
        String input = "if ( x < y ) { let a = x + y; return a; } else { x - y }";

        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Program program = parser.parseProgram();
        ExpressionStatement statement = (ExpressionStatement) program.statements.get(0);
        IfExpression expression = (IfExpression) statement.expression;
        System.out.println(expression.toString());
        assertEquals("statement length", 1, program.statements.size());

        checkParserError(parser);
    }

    @Test
    public void testFuExpression() {
        String input = "fn(x, y, z) {};";

        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Program program = parser.parseProgram();
        ExpressionStatement statement = (ExpressionStatement) program.statements.get(0);
        FunctionLiteral expression = (FunctionLiteral) statement.expression;
        System.out.println(expression.toString());
        assertEquals("statement length", 1, program.statements.size());

        checkParserError(parser);
    }

    @Test
    public void testCallExpression() {
        String input = "add(a, b, 1, 2 * 3, 4 + 5, add(6, 7 * 8))";

        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Program program = parser.parseProgram();
        assertEquals("statement length", 1, program.statements.size());
        checkParserError(parser);
        ExpressionStatement statement = (ExpressionStatement) program.statements.get(0);
        CallExpression expression = (CallExpression) statement.expression;

        System.out.println(expression.toString());

        checkParserError(parser);
    }

    @Test
    public void testCallExpression1() {
        String input = "idenity(5)";

        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Program program = parser.parseProgram();
        assertEquals("statement length", 1, program.statements.size());
        checkParserError(parser);
        ExpressionStatement statement = (ExpressionStatement) program.statements.get(0);
        CallExpression expression = (CallExpression) statement.expression;

        System.out.println(expression.toString());

        checkParserError(parser);
    }
}
