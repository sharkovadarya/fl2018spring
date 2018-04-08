package ru.spbau.group202.fl.sharkova.hw3lexer;

import java.util.ArrayList;

/**
 * This class is used for splitting an expression into tokens.
 * Supported tokens: natural numbers (with 0), +, -, *, /, ^, brackets, spaces.
 */
public class ArithmeticExpressionLexer {

    /**
     * This method returns a list of tokens from the given expression.
     * @param expression expression to be tokenized
     * @return list of tokens
     */
    public static ArrayList<Token> getTokenList(String expression) throws TokenizeException {
        ArrayList<Token> tokens = new ArrayList<>();

        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == ' ') {
                tokens.add(new Token(Token.WHITESPACE, " "));
            } else if (expression.charAt(i) == '(') {
                tokens.add(new Token(Token.OPEN_BRACKET, "("));
            } else if (expression.charAt(i) == ')') {
                tokens.add(new Token(Token.CLOSE_BRACKET, ")"));
            } else if (expression.charAt(i) == '+') {
                tokens.add(new Token(Token.PLUSMINUS, "+"));
            } else if (expression.charAt(i) == '-') {
                tokens.add(new Token(Token.PLUSMINUS, "-"));
            } else if (expression.charAt(i) == '*') {
                tokens.add(new Token(Token.MULTDIV, "*"));
            } else if (expression.charAt(i) == '/') {
                tokens.add(new Token(Token.MULTDIV, "/"));
            } else if (expression.charAt(i) == '^') {
                tokens.add(new Token(Token.POWER, "^"));
            } else if (Character.isDigit(expression.charAt(i))) {
                StringBuilder num = new StringBuilder();
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    num.append(expression.charAt(i++));
                }
                if (i == expression.length()) {
                    tokens.add(new Token(Token.NUMBER, num.toString()));
                    break;
                }

                char c = expression.charAt(i);
                if (c != ')' && c != '+' && c != '-' && c != '*' && c != '/' && c != '^' && c != ' ') {
                    throw new TokenizeException("Error at position " + i);
                } else {
                    tokens.add(new Token(Token.NUMBER, num.toString()));
                    i--;
                }
            } else {
                throw new TokenizeException("Error at position " + i);
            }
        }

        return tokens;
    }
}
