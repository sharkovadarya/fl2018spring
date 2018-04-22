package ru.spbau.group202.fl.sharkova.hw4lexer.tokens;

/**
 * This class represents a L language token.
 */
abstract public class Token {

    public final int lineNumber;
    public final int startPos;
    public final int endPos;
    public final int token;
    public final String sequence;
    public final String description;

    protected static final String tokenTemplate = "%s(%s%d, %d, %d)";

    public Token(int lineNumber, int startPos, int endPos, int token, String sequence) {
        this.lineNumber = lineNumber;
        this.startPos = startPos;
        this.endPos = endPos;
        this.token = token;
        this.sequence = sequence;
        this.description = getDescription();
    }

    abstract protected String getDescription();

    abstract public String getToken();
}
