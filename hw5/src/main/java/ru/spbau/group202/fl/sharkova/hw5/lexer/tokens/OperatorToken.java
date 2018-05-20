package ru.spbau.group202.fl.sharkova.hw5.lexer.tokens;

/**
 * This class represents a L language operator.
 */
public class OperatorToken extends Token {

    public static final int PLUS = 0;
    public static final int MINUS = 1;
    public static final int MUL = 2;
    public static final int DIV = 3;
    public static final int REM = 4;
    public static final int EQ = 5;
    public static final int NEQ = 6;
    public static final int LT = 7;
    public static final int LTE = 8;
    public static final int GT = 9;
    public static final int GTE = 10;
    public static final int AND = 11;
    public static final int OR = 12;
    public static final int ASSIGN = 13;

    public OperatorToken(int lineNumber, int startPos, int endPos, int token, String sequence) {
        super(lineNumber, startPos, endPos, token, sequence);
    }

    @Override
    protected String getDescription() {
        return "Op";
    }

    @Override
    public String getToken() {
        String str = "";
        switch (token) {
            case PLUS:
                str = "Plus";
                break;
            case MINUS:
                str = "Minus";
                break;
            case MUL:
                str = "Mul";
                break;
            case DIV:
                str = "Div";
                break;
            case REM:
                str = "Rem";
                break;
            case EQ:
                str = "Eq";
                break;
            case NEQ:
                str = "Not eq";
                break;
            case LT:
                str = "Lt";
                break;
            case LTE:
                str = "Lte";
                break;
            case GT:
                str = "Gt";
                break;
            case GTE:
                str = "Gte";
                break;
            case AND:
                str = "And";
                break;
            case OR:
                str = "Or";
                break;
            case ASSIGN:
                str = "Assign";
                break;
        }

        return String.format(tokenTemplate, description, str + ", ", lineNumber, startPos, endPos);
    }
}
