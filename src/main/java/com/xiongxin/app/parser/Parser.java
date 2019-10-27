package com.xiongxin.app.parser;

import com.xiongxin.app.ast.*;
import com.xiongxin.app.lexer.Lexer;
import com.xiongxin.app.lexer.Token;

import java.util.*;

/**
 * 解析器
 * 将Token解析成 ast 语法树
 */
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

    public enum Precedence {
        //  优先级顺序 低 <- 高
        LOWEST, EQUALS, LESSGREATER, SUM, PRODUCT, PREFIX, CALL, INDEX
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
        precedences.put(Token.LPAREN, Precedence.CALL);
        precedences.put(Token.LBRACKET, Precedence.INDEX);

        // 注册前置解析函数
        prefixParseFns.put(Token.IDENT, this::parseIdentifier);
        prefixParseFns.put(Token.INT, this::parseInteger);
        prefixParseFns.put(Token.BANG, this::parsePrefixExpression);
        prefixParseFns.put(Token.MINUS, this::parsePrefixExpression);
        prefixParseFns.put(Token.TRUE, this::parseBoolean);
        prefixParseFns.put(Token.FALSE, this::parseBoolean);
        prefixParseFns.put(Token.LPAREN, this::parseGroupExpression);
        prefixParseFns.put(Token.IF, this::parseIfExpression);
        prefixParseFns.put(Token.FUNCTION, this::parseFnExpression);
        prefixParseFns.put(Token.STRING, this::parseStringLiteral);
        prefixParseFns.put(Token.LBRACKET,this::parseArrayLiteral);  // 先解析数组字面量

        // 注册中指表达式函数
        infixParseFns.put(Token.PLUS, this::parseInfixExpression);
        infixParseFns.put(Token.MINUS, this::parseInfixExpression);
        infixParseFns.put(Token.SLASH, this::parseInfixExpression);
        infixParseFns.put(Token.ASTERISK, this::parseInfixExpression);
        infixParseFns.put(Token.EQ, this::parseInfixExpression);
        infixParseFns.put(Token.NOT_EQ, this::parseInfixExpression);
        infixParseFns.put(Token.LT, this::parseInfixExpression);
        infixParseFns.put(Token.GT, this::parseInfixExpression);
        infixParseFns.put(Token.LPAREN, this::parseCallExpression);
        infixParseFns.put(Token.LBRACKET, this::parseIndexExpression);  // 数组索引操作符
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

        if ( peekTokenIs(Token.SEMICOLON) ) {
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

        nextToken(); // eat =

        letStatement.value = parseExpression(Precedence.LOWEST);

        while ( peekTokenIs(Token.SEMICOLON) ) {
            nextToken();
        }


        return letStatement;
    }

    private ReturnStatement parseReturnStatement() {
        ReturnStatement returnStatement = new ReturnStatement();
        returnStatement.token = curToken;

        nextToken(); // eat return

        returnStatement.returnValue = parseExpression(Precedence.LOWEST);

        while ( peekTokenIs(Token.SEMICOLON) ) {
            nextToken(); // eat ;
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

    private Expression parseBoolean() {
        return new BooleanExpression(curToken, curTokenIs(Token.TRUE));
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

    private Expression parseGroupExpression() {
        nextToken();

        // 例如: (1 + 1)， 1 和 ) 的优先级相同(最低优先级)，表达式解析停止
        Expression expression = parseExpression(Precedence.LOWEST);

        if ( !expectPeek(Token.RPAREN) ) {
            return null;
        }

        return expression;
    }

    private Expression parseIfExpression() {
        IfExpression ifExpression = new IfExpression();
        ifExpression.token = curToken; // if

        if ( !expectPeek(Token.LPAREN) ) { // eat (
            return null;
        }

        nextToken(); // expression

        ifExpression.condition = parseExpression(Precedence.LOWEST);

        if ( !expectPeek(Token.RPAREN) ) { // eat )
            return null;
        }

        if ( !expectPeek(Token.LBRACE) ) { // eat {
            return null;
        }

        // curToken {
        ifExpression.consequence = parseBlockStatement();

        if (peekTokenIs(Token.ELSE)) {
            nextToken();

            if ( !expectPeek(Token.LBRACE) ) {
                return null;
            }

            ifExpression.alternative = parseBlockStatement();
        }

        return ifExpression;
    }

    private Expression parseFnExpression() {
        FunctionLiteral functionLiteral = new FunctionLiteral();
        functionLiteral.token = curToken;

        if ( !expectPeek(Token.LPAREN) ) {
            return null;
        }

        nextToken();

        while ( !curTokenIs(Token.RPAREN) ) {
            Identifier identifier = new Identifier(curToken, curToken.literal);
            functionLiteral.parameters.add(identifier);
            if ( peekTokenIs(Token.COMMA) ) { nextToken(); nextToken(); } // eat ,
            else {
                if ( !expectPeek(Token.RPAREN) ) {
                    return null;
                }
            }
        }

        if ( !expectPeek(Token.LBRACE) ) {
            return null;
        }

        functionLiteral.body = parseBlockStatement();

        return functionLiteral;
    }

    private List<Expression> parseCallArgsExpression() {
        List<Expression> args = new LinkedList<>();

        if ( peekTokenIs(Token.RPAREN) ) { // 空参数类型
            nextToken();

            return args;
        }

        nextToken();

        args.add(parseExpression(Precedence.LOWEST));

        while ( peekTokenIs(Token.COMMA) ) {
            nextToken(); // eat ,
            nextToken(); // identifier;
            args.add(parseExpression(Precedence.LOWEST));
        }

        if ( !expectPeek(Token.RPAREN) ) {
            return null;
        }

        return args;
    }

    private Expression parseCallExpression(Expression left) {
        CallExpression expression = new CallExpression();

        expression.token = curToken;
        expression.function = left;
        expression.arguments = parseCallArgsExpression();

        return expression;
    }

    private BlockStatement parseBlockStatement() {
        BlockStatement blockStatement = new BlockStatement();
        blockStatement.token = curToken;

        nextToken(); // eat {

        while (!curTokenIs(Token.RBARCE)) {
            Statement statement = parseStatement();
            if (statement != null) {
                blockStatement.statements.add(statement);
            }

            nextToken();
        }

        return blockStatement;
    }

    private ArrayLiteral parseArrayLiteral() {
        ArrayLiteral arrayLiteral = new ArrayLiteral();
        arrayLiteral.token = curToken;

        arrayLiteral.elements = parseArrayExpressionList();

        return arrayLiteral;
    }

    private List<Expression> parseArrayExpressionList() {
        List<Expression> list = new LinkedList<>();

        if (peekTokenIs(Token.RBRACKET)) {
            nextToken();
            return list;
        }

        nextToken();

        list.add(parseExpression(Precedence.LOWEST));

        while (peekTokenIs(Token.COMMA)) {
            nextToken();nextToken();// eat ,
            list.add(parseExpression(Precedence.LOWEST));
        }

        if (!expectPeek(Token.RBRACKET)) {  // eat ]
            return null;
        }

        return list;
    }

    private Expression parseIndexExpression(Expression left) {
        IndexExpression indexExpression = new IndexExpression();
        indexExpression.token = curToken;

        indexExpression.left = left;
        nextToken();
        indexExpression.index = parseExpression(Precedence.LOWEST);

        if (!expectPeek(Token.RBRACKET)) {
            return null;
        }

        return indexExpression;
    }

    private Expression parseStringLiteral() {
        return new StringLiteral(curToken, curToken.literal);
    }

    private Precedence peekPrecedence() {
        return precedences.getOrDefault(peekToken.type, Precedence.LOWEST);
    }

    private Precedence curPrecedence() {
        return precedences.getOrDefault(curToken.type, Precedence.LOWEST);
    }

}
