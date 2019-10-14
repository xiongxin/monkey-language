package com.xiongxin.app.ast;

import com.xiongxin.app.lexer.Token;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class AstTest {

    @Test
    public void testString() {
        Program program = new Program();
        List<Statement> statements = new ArrayList<>();

        LetStatement letStatement = new LetStatement();

        letStatement.token = new Token(Token.LET, "let");
        letStatement.name = new Identifier(new Token(Token.IDENT, "myvar"), "myvar");
        letStatement.value = new Identifier(new Token(Token.IDENT, "anotherVar"), "anotherVar");

        statements.add(letStatement);

        program.statements = statements;

        System.out.println(program);

        assertEquals("program equals","let myvar = anotherVar;", program.toString());
    }


}
