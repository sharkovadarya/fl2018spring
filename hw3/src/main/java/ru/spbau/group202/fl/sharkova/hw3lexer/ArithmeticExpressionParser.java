package ru.spbau.group202.fl.sharkova.hw3lexer;

import java.util.ArrayList;

/** This class parses a list of token using the following grammar:
 *   P -> E
 *   E -> T {(+|-) T}
 *   T -> S {(*|/) S}
 *   S -> F ^ S | F
 *   F -> number | (E)
 */
public class ArithmeticExpressionParser {
    private int level;
    private int next;
    private int position;
    private int lastValidTokenPosition;
    private int lastValidTokenLength;
    private Token token;
    private ArrayList<Token> tokens;
    private ArrayList<String> result = new ArrayList<>();

    public ArithmeticExpressionParser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public ArrayList<String> parse() throws ParseException  {
        next();
        skipSpaces();
        getE();
        if (token != null) { // if the expression has been fully parsed, next() returns null
            throw new ParseException("Parsing error at position " + position);
        }
        else {
            result.add("Successful parse");
            return result;
        }
    }

    private void getE() throws ParseException  {
        enter('E');
        getT();
        while (token != null && token.token == Token.WHITESPACE) {
            next();
        }
        while (token != null && token.token == Token.PLUSMINUS) {
            next();
            getT();
        }
        if (token != null && token.token == Token.NUMBER) {
            throw new ParseException("Error at position " + lastValidTokenPosition);
        }
        leave('E');
    }

    private void getT() throws ParseException  {
        enter('T');
        getS();
        while (token != null && token.token == Token.WHITESPACE) {
            next();
        }
        while (token != null && token.token == Token.MULTDIV) {
            next();
            getS();
        }
        leave('T');
    }

    private void getS() throws ParseException  {
        enter('S');
        getF();
        while (token != null && token.token == Token.WHITESPACE) {
            next();
        }
        if (token != null && token.token == Token.POWER) {
            next();
            getS();
        }
        leave('S');
    }

    private void getF() throws ParseException {
        enter('F');
        while (token != null && token.token == Token.WHITESPACE) {
            next();
        }
        if (token == null) {
            throw new ParseException("Parsing error at position " + (lastValidTokenPosition + lastValidTokenLength - 1));
        }
        if (token.token == Token.NUMBER) {
            next();
            if (token == null) {
                return;
            }
        }
        else if (token.token == Token.OPEN_BRACKET) {
            next();
            getE();
            if (token != null && token.token == Token.CLOSE_BRACKET) {
                next();
            }
            else {
                throw new ParseException("Parsing error at position " + position);
            }
        }
        else {
            throw new ParseException("Parsing error at position " + (lastValidTokenPosition + lastValidTokenLength));
        }
        leave('F');
    }

    private void next() {
        if (next >= tokens.size()) {
            token = null;
            return;
        }

        if (token != null && token.token != Token.WHITESPACE) {
            lastValidTokenPosition = position;
            lastValidTokenLength = token.sequence.length();
        }
        token = tokens.get(next++);
        if (token != null) {
            if (token.token != Token.WHITESPACE) {
                lastValidTokenPosition = position;
            }
            position += token.sequence.length();
        }
    }

    private void skipSpaces() {
        while (next < tokens.size() && token.token == Token.WHITESPACE) {
            token = tokens.get(next++);
            if (next == tokens.size()) {
                token = null;
            }
        }
    }

    private void enter(char name) {
        StringBuilder sb = new StringBuilder();
        spaces(level++, sb);
        sb.append("-" + name + "    ");
        if (token != null) {
            sb.append("next: " + token.sequence);
        }
        result.add(sb.toString());
    }

    private void leave(char name) {
        StringBuilder sb = new StringBuilder();
        spaces(--level, sb);
        sb.append("-" + name + "    ");
        if (token != null) {
            sb.append("next: " + token.sequence);
        }
        result.add(sb.toString());

    }

    private void spaces(int localLevel, StringBuilder sb) {
        while (localLevel-- > 0) {
            sb.append("| ");
        }
    }
}