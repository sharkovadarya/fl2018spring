package ru.spbau.group202.fl.sharkova.hw4lexer.tokens;

/**
 * This class represents a L language separator.
 */
public class SeparatorToken extends Token {

    public static final int OPEN_BRACKET = 0;
    public static final int CLOSE_BRACKET = 1;
    public static final int SEMICOLON = 2;
    public static final int COMMA = 3;

    public SeparatorToken(int lineNumber, int startPos, int endPos, int token, String sequence) {
        super(lineNumber, startPos, endPos, token, sequence);
    }

    @Override
    protected String getDescription() {
        switch (token) {
            case OPEN_BRACKET: return "OpenBr";
            case CLOSE_BRACKET: return "ClBr";
            case SEMICOLON: return "Semicolon"; // example says 'colon' but it's actually a semi-colon
            case COMMA: return "Comma";
            default: return "";
        }
    }

    @Override
    public String getToken() {
        return String.format(tokenTemplate, description, "", lineNumber, startPos, endPos);
    }
}
