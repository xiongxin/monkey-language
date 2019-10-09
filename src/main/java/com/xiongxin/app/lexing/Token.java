package com.xiongxin.app.lexing;

public class Token {

    public static String ILLEGAL = "ILLEGAL";
    public static String EOF = "EOF";
    // Identifiers + literals
    public static String IDENT = "IDENT";
    public static String INT = "INT";

    // Operators
    public static String ASSIGN = "=";
    public static String PLUS = "+";

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

    public String type;
    public String literal;

    public Token(String tokenType, String valueOf) {
        this.type = tokenType;
        this.literal = valueOf;
    }
}
