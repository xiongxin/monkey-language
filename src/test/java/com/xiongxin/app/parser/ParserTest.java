package com.xiongxin.app.parser;

import com.xiongxin.app.ast.*;
import com.xiongxin.app.lexer.Lexer;
import com.xiongxin.app.lexer.Token;
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
}
