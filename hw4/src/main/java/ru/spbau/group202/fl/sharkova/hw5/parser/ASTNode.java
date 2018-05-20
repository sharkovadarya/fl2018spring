package ru.spbau.group202.fl.sharkova.hw4lexer.parser;

import ru.spbau.group202.fl.sharkova.hw4lexer.lexer.tokens.Token;

public class ASTNode {
    public static final String PROGRAM = "program";
    public static final String FUNCTION_DEFINITION = "function definition";
    public static final String FUNCTION_ARGUMENTS = "function arguments";
    public static final String FUNCTION_BODY = "function body";
    public static final String STATEMENT = "statement";
    public static final String EXPRESSION = "expression";
    public static final String FUNCTION_CALL = "function call";
    public static final String OPERATOR = "operator";
    public static final String TOKEN = "token";
    public static final String CONDITION = "condition";
    public static final String THEN_BLOCK  = "then";
    public static final String ELSE_BLOCK = "else";


    private Token token;
    private String description;

    public ASTNode(Token token, String description) {
        this.token = token;
        this.description = description;
    }

    public ASTNode(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return token == null ? description : description + " " + token.getToken();
    }
}
