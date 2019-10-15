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
    private Map<String, Precedence> precedences = new HashMap<>();

    public List<String> getErrors() {
        return errors;
    }

    public static enum Precedence {
        //  优先级顺序 低 <- 高
        LOWEST, EQUALS, LESSGREATER, SUM, PRODUCT, PREFIX, CALL
    }

    public Parser(Lexer lexer) {
        this.lexer = lexer;

        nextToken();
        nextToken();

        // 注册优先级
        precedences.put(Token.EQ, Precedence.EQUALS);
        precedences.put(Token.NOT_EQ, Precedence.EQUALS);
        precedences.put(Token.LT, Precedence.LESSGREATER);
        precedences.put(Token.GT, Precedence.LESSGREATER);
        precedences.put(Token.PLUS, Precedence.SUM);
        precedences.put(Token.MINUS, Precedence.SUM );
        precedences.put(Token.SLASH, Precedence.PRODUCT);
        precedences.put(Token.ASTERISK, Precedence.PRODUCT);

        // 注册前置解析函数
        prefixParseFns.put(Token.IDENT, this::parseIdentifier);
        prefixParseFns.put(Token.INT, this::parseInteger);
        prefixParseFns.put(Token.BANG, this::parsePrefixExpression);
        prefixParseFns.put(Token.MINUS, this::parsePrefixExpression);

        // 注册中指表达式函数
        infixParseFns.put(Token.PLUS, this::parseInfixExpression);
        infixParseFns.put(Token.MINUS, this::parseInfixExpression);
        infixParseFns.put(Token.SLASH, this::parseInfixExpression);
        infixParseFns.put(Token.ASTERISK, this::parseInfixExpression);
        infixParseFns.put(Token.EQ, this::parseInfixExpression);
        infixParseFns.put(Token.NOT_EQ, this::parseInfixExpression);
        infixParseFns.put(Token.LT, this::parseInfixExpression);
        infixParseFns.put(Token.GT, this::parseInfixExpression);
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

        if ( !peekTokenIs(Token.SEMICOLON) ) {
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
            noPrefixParseFnError(curToken.type);
            return null;
        }

        Expression leftExpression = fn.apply();

        while ( !peekTokenIs(Token.SEMICOLON) && precedence.ordinal() < peekPrecedence().ordinal() ) {
            InfixParseFn infixParseFn = infixParseFns.get(peekToken.type);

            if (infixParseFn == null) {
                return leftExpression;
            }

            nextToken();

            leftExpression = infixParseFn.apply(leftExpression);
        }

        return leftExpression;
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

    private Expression parseInfixExpression(Expression left) {

        InfixExpression infixExpression = new InfixExpression();

        infixExpression.token = curToken;
        infixExpression.operator = curToken.literal;
        infixExpression.left = left;

        Precedence precedence = curPrecedence();
        nextToken();
        infixExpression.right = parseExpression(precedence);

        return infixExpression;
    }

    private Expression parsePrefixExpression() {
        PrefixExpression expression = new PrefixExpression();
        expression.token = curToken;
        expression.operator = curToken.literal;

        nextToken();

        expression.right = parseExpression(Precedence.PREFIX);

        return expression;
    }

    private Precedence peekPrecedence() {
        return precedences.getOrDefault(peekToken.type, Precedence.LOWEST);
    }

    private Precedence curPrecedence() {
        return precedences.getOrDefault(curToken.type, Precedence.LOWEST);
    }

}
