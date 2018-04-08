package ru.spbau.group202.fl.sharkova.hw3lexer;

public class Token {
    public static final int WHITESPACE = 0;
    public static final int PLUSMINUS = 1;
    public static final int MULTDIV = 2;
    public static final int POWER = 3;
    public static final int OPEN_BRACKET = 4;
    public static final int CLOSE_BRACKET = 5;
    public static final int NUMBER = 6;

    public final int token;
    public final String sequence;

    public Token(int token, String sequence) {
        this.token = token;
        this.sequence = sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Token)) {
            return false;
        }

        Token t = (Token) o;
        return token == t.token && sequence.equals(t.sequence);
    }
}
