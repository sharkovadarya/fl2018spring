package ru.spbau.group202.fl.sharkova.hw4lexer.tokens;

import ru.spbau.group202.fl.sharkova.hw4lexer.LexerException;

/**
 * This class represents a L language literal
 * which is either a floating point number or a boolean value.
 */
public class LiteralToken extends Token {

    public static final int NUMBER = 0;
    public static final int TRUE = 1;
    public static final int FALSE = 2;

    private Double doubleValue;
    private Integer intValue;
    private Boolean boolValue;

    public LiteralToken(int lineNumber, int startPos, int endPos, int token, String sequence) throws LexerException {
        super(lineNumber, startPos, endPos, token, sequence);
        switch (token) {
            case NUMBER:
                doubleValue = Double.parseDouble(sequence);
                try {
                    intValue = Integer.parseInt(sequence);
                } catch (NumberFormatException e) {
                    intValue = null;
                }
                boolValue = null;
                break;
            case TRUE:
                doubleValue = null;
                boolValue = true;
                break;
            case FALSE:
                doubleValue = null;
                boolValue = false;
                break;
             default: throw new LexerException("Unable to tokenize expression");
        }
    }

    @Override
    protected String getDescription() {
        if (token == NUMBER) {
            return "Num";
        } else if (token == TRUE || token == FALSE) {
            return "Bool";
        }

        return "";
    }

    @Override
    public String getToken() {
        if (token == NUMBER) {
            if (intValue != null) {
                return String.format(tokenTemplate, description, intValue + ", ", lineNumber, startPos, endPos);
            }
            return String.format(tokenTemplate, description, doubleValue + ", ", lineNumber, startPos, endPos);
        }

        return String.format(tokenTemplate, description, boolValue + ", ",
                lineNumber, startPos, endPos);
    }
}
