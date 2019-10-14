package com.xiongxin.app.parser;

import com.xiongxin.app.ast.*;
import com.xiongxin.app.lexer.Lexer;
import com.xiongxin.app.lexer.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

    private Lexer lexer;
    private Token curToken = null;
    private Token peekToken = null;
    private List<String> errors = new ArrayList<>();

    private Map<String, PrefixParseFn> prefixParseFns = new HashMap<>();
    private Map<String, InfixParseFn> infixParseFns = new HashMap<>();

    public List<String> getErrors() {
        return errors;
    }

    public static enum Precedence {
        LOWEST, EQUALS, LESSGREATER, SUM, PRODUCT, PREFIX, CALL
    }

    public Parser(Lexer lexer) {
        this.lexer = lexer;

        nextToken();
        nextToken();

        // 注册前置解析函数
        prefixParseFns.put(Token.IDENT, this::parseIdentifier);
        prefixParseFns.put(Token.INT, this::parseInteger);
    }

    private void nextToken() {
        curToken = peekToken;
        peekToken = lexer.nextToken();
    }

    public Program parseProgram() {
        Program program = new Program();

        while ( !curToken.type.equalsIgnoreCase(Token.EOF) ) {
            Statement statement = parseStatement();

            if (statement != null) {
                program.statements.add(statement);
            }

            nextToken();
        }

        return program;
    }


    private Statement parseStatement() {
        switch (curToken.type) {
            case Token.LET:
                return parseLetStatement();
            case Token.RETURN:
                return parseReturnStatement();
            default:
                return parseExpressionStatement();
        }
    }

    private ExpressionStatement parseExpressionStatement() {
        ExpressionStatement statement = new ExpressionStatement();
        statement.token = curToken;

        statement.expression = parseExpression(Precedence.LOWEST);

        if (peekTokenIs(Token.SEMICOLON)) {
            nextToken();
        }

        return statement;
    }


    private LetStatement parseLetStatement() {
        LetStatement letStatement = new LetStatement();
        letStatement.token = curToken;

        if (!expectPeek(Token.IDENT)) {
            return null;
        }

        letStatement.name = new Identifier(curToken, curToken.literal);

        if ( !expectPeek(Token.ASSIGN) ) {
            return null;
        }

        // TODO: We're skipping the expression until we
        // encounter a semicolon
        while ( !curTokenIs(Token.SEMICOLON) && !curTokenIs(Token.EOF) ) {
            nextToken();
        }


        return letStatement;
    }

    private ReturnStatement parseReturnStatement() {
        ReturnStatement returnStatement = new ReturnStatement();
        returnStatement.token = curToken;

        nextToken();

        // TODO: We're skipping the expression until we
        // encounter a semicolon
        while ( !curTokenIs(Token.SEMICOLON) && !curTokenIs(Token.EOF) ) {
            nextToken();
        }

        return returnStatement;
    }

    private boolean curTokenIs(String tokenType) {
        return curToken.type.equalsIgnoreCase(tokenType);
    }

    private boolean peekTokenIs(String tokenType) {
        return peekToken.type.equals(tokenType);
    }

    private boolean expectPeek(String tokeType) {
        if (peekTokenIs(tokeType)) {
            nextToken();
            return true;
        } else {
            peekError(tokeType);
            return false;
        }
    }

    private void peekError(String tokenType) {
        String msg = String.format("expected next token to be %s, got %s instead",
                tokenType, peekToken.type);

        errors.add(msg);
    }

    private Expression parseExpression(Precedence precedence) {
        PrefixParseFn fn = prefixParseFns.get(curToken.type);
        if (fn == null) {
            return null;
        }

        return fn.apply();
    }

    private Expression parseIdentifier() {
        return new Identifier(curToken, curToken.literal);
    }

    private void noPrefixParseFnError(String tokenType) {
        String msg = String.format("no prefix parse function for %s found", tokenType);

        errors.add(msg);
    }

    private Expression parseInteger() {
        IntegerLiteral integerLiteral = new IntegerLiteral();
        integerLiteral.token = curToken;

        try {
            integerLiteral.value = Integer.valueOf(curToken.literal);
        } catch (NumberFormatException e) {
            String msg = String.format("could not parse %s as integer", curToken.literal);
            errors.add(msg);
        }

        return integerLiteral;
    }
}
