package ru.spbau.group202.fl.sharkova.hw4lexer.tokens;

/**
 * This class represents a L language keyword
 * which can be one of the following:
 * if, then, else, while, do, read, write.
 */
public class KeywordToken extends Token {

    public static final int IF = 0;
    public static final int THEN = 1;
    public static final int ELSE = 2;
    public static final int WHILE = 3;
    public static final int DO = 4;
    public static final int READ = 5;
    public static final int WRITE = 6;

    public KeywordToken(int lineNumber, int startPos, int endPos, int token, String sequence) {
        super(lineNumber, startPos, endPos, token, sequence);
    }

    @Override
    protected String getDescription() {
        switch (token) {
            case IF:    return "KW_If";
            case THEN:  return "KW_Then";
            case ELSE:  return "KW_Else";
            case WHILE: return "KW_While";
            case DO:    return "KW_Do";
            case READ:  return "KW_Read";
            case WRITE: return "KW_Write";
            default:    return "";
        }
    }

    @Override
    public String getToken() {
        return String.format(tokenTemplate, description, "", lineNumber, startPos, endPos);
    }
}
