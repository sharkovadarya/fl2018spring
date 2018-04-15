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
        expression = expression.replaceAll("\n", "");

        for (int i = 0; i < expression.length(); i++) {
            if (Character.isWhitespace(expression.charAt(i))) {
                continue;
            } else if (expression.charAt(i) == '(') {
                tokens.add(new Token(Token.OPEN_BRACKET, "(", i));
            } else if (expression.charAt(i) == ')') {
                tokens.add(new Token(Token.CLOSE_BRACKET, ")", i));
            } else if (expression.charAt(i) == '+') {
                tokens.add(new Token(Token.PLUSMINUS, "+", i));
            } else if (expression.charAt(i) == '-') {
                tokens.add(new Token(Token.PLUSMINUS, "-", i));
            } else if (expression.charAt(i) == '*') {
                tokens.add(new Token(Token.MULTDIV, "*", i));
            } else if (expression.charAt(i) == '/') {
                tokens.add(new Token(Token.MULTDIV, "/", i));
            } else if (expression.charAt(i) == '^') {
                tokens.add(new Token(Token.POWER, "^", i));
            } else if (Character.isDigit(expression.charAt(i))) {
                StringBuilder num = new StringBuilder();
                int pos = i;
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    num.append(expression.charAt(i++));
                }
                if (i == expression.length()) {
                    tokens.add(new Token(Token.NUMBER, num.toString(), pos));
                    break;
                }

                char c = expression.charAt(i);
                if (c != ')' && c != '+' && c != '-' && c != '*' && c != '/' && c != '^' && c != ' ') {
                    throw new TokenizeException("Error at position " + i);
                } else {
                    tokens.add(new Token(Token.NUMBER, num.toString(), pos));
                    i--;
                }
            } else {
                throw new TokenizeException("Error at position " + i);
            }
        }

        return tokens;
    }
}
