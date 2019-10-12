package com.xiongxin.app.lexer;

import java.util.HashMap;
import java.util.Map;

public class Token {

    public static String ILLEGAL = "ILLEGAL";
    public static String EOF = "EOF";
    // Identifiers + literals
    public static String IDENT = "IDENT";
    public static String INT = "INT";

    // Operators
    public static String ASSIGN = "=";
    public static String PLUS = "+";
    public static String MINUS = "-";
    public static String BANG = "!";
    public static String ASTERISK = "*";
    public static String SLASH = "/";

    public static String LT = "<";
    public static String GT = ">";

    // Delimiters
    public static String COMMA = ",";
    public static String SEMICOLON = ";";

    public static String LPAREN = "(";
    public static String RPAREN = ")";
    public static String LBRACE = "{";
    public static String RBARCE = "}";

    // keywords
    public static String FUNCTION = "FUNCTION";
    public static String LET = "LET";
    public static String TRUE = "TRUE";
    public static String FALSE = "FALSE";
    public static String IF = "IF";
    public static String ELSE = "ELSE";
    public static String RETURN = "RETURN";

    public static String EQ = "==";
    public static String NOT_EQ = "!=";

    public String type;
    public String literal;

    public static Map<String, String> keywords = new HashMap<>();

    static {
        keywords.put("fn", FUNCTION);
        keywords.put("let", LET);
        keywords.put("true", TRUE);
        keywords.put("false", FALSE);
        keywords.put("if", IF);
        keywords.put("else", ELSE);
        keywords.put("return", RETURN);
    }

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
