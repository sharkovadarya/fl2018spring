package ru.spbau.group202.fl.sharkova.hw4lexer.lexer.tokens;

/**
 * This class represents an identifier token.
 * Identifiers consist of latin lowercase letters, digits and underscores,
 * first symbol is always either a letter or an underscore.
 */
public class IdentifierToken extends Token {

    public static final int IDENTIFIER = 0;

    public IdentifierToken(int lineNumber, int startPos, int endPos, int token, String sequence) {
        super(lineNumber, startPos, endPos, token, sequence);
    }

    @Override
    protected String getDescription() {
        return "Ident";
    }

    @Override
    public String getToken() {
        return String.format(tokenTemplate, description, "\"" + sequence + "\", ", lineNumber, startPos, endPos);
    }
}
