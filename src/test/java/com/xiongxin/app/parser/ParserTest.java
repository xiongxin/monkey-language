package com.xiongxin.app.parser;

import com.xiongxin.app.ast.LetStatement;
import com.xiongxin.app.ast.Program;
import com.xiongxin.app.ast.ReturnStatement;
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
}
