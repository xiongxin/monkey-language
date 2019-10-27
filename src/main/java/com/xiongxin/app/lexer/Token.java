package com.xiongxin.app.lexer;

import java.util.HashMap;
import java.util.Map;

public class Token {

    public final static String ILLEGAL = "ILLEGAL";
    public final static String EOF = "EOF";
    // Identifiers + literals
    public final static String IDENT = "IDENT";
    public final static String INT = "INT";

    // Operators
    public final static String ASSIGN = "=";
    public final static String PLUS = "+";
    public final static String MINUS = "-";
    public final static String BANG = "!";
    public final static String ASTERISK = "*";
    public final static String SLASH = "/";

    public final static String LT = "<";
    public final static String GT = ">";

    // Delimiters
    public final static String COMMA = ",";
    public final static String SEMICOLON = ";";

    public final static String LPAREN = "(";
    public final static String RPAREN = ")";
    public final static String LBRACE = "{";
    public final static String RBARCE = "}";
    public final static String LBRACKET = "[";
    public final static String RBRACKET = "]";

    // keywords
    public final static String FUNCTION = "FUNCTION";
    public final static String LET = "LET";
    public final static String TRUE = "TRUE";
    public final static String FALSE = "FALSE";
    public final static String IF = "IF";
    public final static String ELSE = "ELSE";
    public final static String RETURN = "RETURN";

    public final static String EQ = "==";
    public final static String NOT_EQ = "!=";

    // 数据类型
    public final static String STRING = "STRING";

    public String type;
    public String literal;

    private static Map<String, String> keywords = new HashMap<String, String>(){{
        put("fn", FUNCTION);
        put("let", LET);
        put("true", TRUE);
        put("false", FALSE);
        put("if", IF);
        put("else", ELSE);
        put("return", RETURN);
    }};

    public static String lookupIdent(String ident) {
        return keywords.getOrDefault(ident, IDENT);
    }

    public Token(String tokenType, String valueOf) {
        this.type = tokenType;
        this.literal = valueOf;
    }

    public Token() {}

    @Override
    public String toString() {
        return "Token{" +
                "type='" + type + '\'' +
                ", literal='" + literal + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        Token otherToken;
        otherToken = (Token) obj;

        return otherToken.type.equals(type);
    }
}
