package ru.spbau.group202.fl.sharkova.hw3lexer;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ArithmeticExpressionLexerTest {

    @Test
    public void testTokenizeOneNumber() throws TokenizeException {
        String expr = "46386436";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        assertEquals(1, tokens.size());
        assertEquals(new Token(Token.NUMBER, "46386436"), tokens.get(0));
    }

    @Test
    public void testTokenize() throws TokenizeException {
        String expr = "(21^0 + 0) * ( 33-(1  -2))";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        assertEquals(new Token(Token.OPEN_BRACKET, "("), tokens.get(0));
        assertEquals(new Token(Token.NUMBER, "21"), tokens.get(1));
        assertEquals(new Token(Token.POWER, "^"), tokens.get(2));
        assertEquals(new Token(Token.NUMBER, "0"), tokens.get(3));
        assertEquals(new Token(Token.WHITESPACE, " "), tokens.get(4));
        assertEquals(new Token(Token.PLUSMINUS, "+"), tokens.get(5));
        assertEquals(new Token(Token.WHITESPACE, " "), tokens.get(6));
        assertEquals(new Token(Token.NUMBER, "0"), tokens.get(7));
        assertEquals(new Token(Token.CLOSE_BRACKET, ")"), tokens.get(8));
        assertEquals(new Token(Token.WHITESPACE, " "), tokens.get(9));
        assertEquals(new Token(Token.MULTDIV, "*"), tokens.get(10));
        assertEquals(new Token(Token.WHITESPACE, " "), tokens.get(11));
        assertEquals(new Token(Token.OPEN_BRACKET, "("), tokens.get(12));
        assertEquals(new Token(Token.WHITESPACE, " "), tokens.get(13));
        assertEquals(new Token(Token.NUMBER, "33"), tokens.get(14));
        assertEquals(new Token(Token.PLUSMINUS, "-"), tokens.get(15));
        assertEquals(new Token(Token.OPEN_BRACKET, "("), tokens.get(16));
        assertEquals(new Token(Token.NUMBER, "1"), tokens.get(17));
        assertEquals(new Token(Token.WHITESPACE, " "), tokens.get(18));
        assertEquals(new Token(Token.WHITESPACE, " "), tokens.get(19));
        assertEquals(new Token(Token.PLUSMINUS, "-"), tokens.get(20));
        assertEquals(new Token(Token.NUMBER, "2"), tokens.get(21));
        assertEquals(new Token(Token.CLOSE_BRACKET, ")"), tokens.get(22));
        assertEquals(new Token(Token.CLOSE_BRACKET, ")"), tokens.get(23));
    }


    /*
     * The following tests succeed if an exception is thrown,
     * meaning it was impossible to tokenize or parse the expression.
     */

    @Test(expected = TokenizeException.class)
    public void testExpressionWithLetter() throws TokenizeException {
        String expr = "35 + a";
        ArithmeticExpressionLexer.getTokenList(expr);
    }

    @Test(expected = TokenizeException.class)
    public void testExpressionWithUnrecognizableOperations() throws TokenizeException {
        String expr = "35 > 34";
        ArithmeticExpressionLexer.getTokenList(expr);
    }

    @Test(expected = TokenizeException.class)
    public void testExpressionWithDoubleNumbers() throws TokenizeException {
        String expr = "35 + 3.0";
        ArithmeticExpressionLexer.getTokenList(expr);
    }

}