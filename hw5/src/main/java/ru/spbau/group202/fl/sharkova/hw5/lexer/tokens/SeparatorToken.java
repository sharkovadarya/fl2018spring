package ru.spbau.group202.fl.sharkova.hw5.lexer.tokens;

/**
 * This class represents a L language separator.
 */
public class SeparatorToken extends Token {

    public static final int OPEN_BRACKET = 0;
    public static final int CLOSE_BRACKET = 1;
    public static final int SEMICOLON = 2;
    public static final int COMMA = 3;
    public static final int OPEN_CURLY_BRACKET = 4;
    public static final int CLOSE_CURLY_BRACKET = 5;

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
            case OPEN_CURLY_BRACKET: return "OpenCrlBr";
            case CLOSE_CURLY_BRACKET: return "ClCrlBr";
            default: return "";
        }
    }

    @Override
    public String getToken() {
        return String.format(tokenTemplate, description, "", lineNumber, startPos, endPos);
    }
}
