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
    private Token token;
    private Token prevToken;
    private ArrayList<Token> tokens;
    private ArrayList<String> result = new ArrayList<>();

    public ArithmeticExpressionParser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public ArrayList<String> parse() throws ParseException  {
        if (tokens.size() == 0) {
            throw new ParseException("Parsing error at position 0");
        }
        next();
        getE();
        if (token != null) { // if the expression has been fully parsed, next() returns null
            throw new ParseException("Parsing error at position " + token.position);
        }
        else {
            result.add("Successful parse");
            return result;
        }
    }

    private void getE() throws ParseException  {
        enter('E');
        getT();
        while (token != null && token.token == Token.PLUSMINUS) {
            next();
            getT();
        }
        if (token != null && token.token == Token.NUMBER) {
            throw new ParseException("Parsing error at position " + token.position);
        }
        leave('E');
    }

    private void getT() throws ParseException  {
        enter('T');
        getS();
        while (token != null && token.token == Token.MULTDIV) {
            next();
            getS();
        }
        leave('T');
    }

    private void getS() throws ParseException  {
        enter('S');
        getF();
        if (token != null && token.token == Token.POWER) {
            next();
            getS();
        }
        leave('S');
    }

    private void getF() throws ParseException {
        enter('F');
        if (token == null) {
            throw new ParseException("Parsing error at position " +
                    (prevToken.position + prevToken.sequence.length()));
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
                throw new ParseException("Parsing error at position " +
                        (token == null ? (prevToken.position + prevToken.sequence.length())
                                       : token.position));
            }
        }
        else {
            throw new ParseException("Parsing error at position " + token.position);
        }
        leave('F');
    }

    private void next() {
        prevToken = token;
        if (next >= tokens.size()) {
            token = null;
            return;
        }
        token = tokens.get(next++);
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