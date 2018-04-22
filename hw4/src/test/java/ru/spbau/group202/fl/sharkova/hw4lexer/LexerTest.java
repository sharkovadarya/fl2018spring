package ru.spbau.group202.fl.sharkova.hw4lexer;

import org.junit.Test;

import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testEmptyExpression() throws LexerException {
        String expr = "";
        assertEquals("", Lexer.getTokens(expr));
    }

    @Test
    public void testIdentifiers() throws LexerException {
        String expr = "identifier";
        assertEquals("Ident(\"identifier\", 0, 0, 9)", Lexer.getTokens(expr));

        expr = "_ident";
        assertEquals("Ident(\"_ident\", 0, 0, 5)", Lexer.getTokens(expr));

        expr = "_ident19";
        assertEquals("Ident(\"_ident19\", 0, 0, 7)", Lexer.getTokens(expr));

        expr = "true_";
        assertEquals("Ident(\"true_\", 0, 0, 4)", Lexer.getTokens(expr));

        expr = "if1";
        assertEquals("Ident(\"if1\", 0, 0, 2)", Lexer.getTokens(expr));

        expr = "doo";
        assertEquals("Ident(\"doo\", 0, 0, 2)", Lexer.getTokens(expr));
    }

    @Test
    public void testNumbers() throws LexerException {
        String expr = "30";
        assertEquals("Num(30, 0, 0, 1)", Lexer.getTokens(expr));

        expr = "21.34";
        assertEquals("Num(21.34, 0, 0, 4)", Lexer.getTokens(expr));

        expr = ".6876";
        assertEquals("Num(0.6876, 0, 0, 4)", Lexer.getTokens(expr));

        expr = "34.e2";
        assertEquals("Num(3400.0, 0, 0, 4)", Lexer.getTokens(expr));

        expr = "6.022137e+23f";
        assertEquals("Num(6.022137E23, 0, 0, 12)", Lexer.getTokens(expr));
    }

    @Test
    public void testBooleanLiterals() throws LexerException {
        String expr = "true";
        assertEquals("Bool(true, 0, 0, 3)", Lexer.getTokens(expr));

        expr = "  false ";
        assertEquals("Bool(false, 0, 2, 6)", Lexer.getTokens(expr));
    }

    @Test
    public void testKeywords() throws LexerException {
        String expr = " if\n";
        assertEquals("KW_If(0, 1, 2)", Lexer.getTokens(expr));

        expr = "then";
        assertEquals("KW_Then(0, 0, 3)", Lexer.getTokens(expr));

        expr = "else\t";
        assertEquals("KW_Else(0, 0, 3)", Lexer.getTokens(expr));

        expr = " \n while\r\n";
        assertEquals("KW_While(1, 1, 5)", Lexer.getTokens(expr));

        expr = "\r\n\r\ndo";
        assertEquals("KW_Do(2, 0, 1)", Lexer.getTokens(expr));

        expr = " read  \t ";
        assertEquals("KW_Read(0, 1, 4)", Lexer.getTokens(expr));

        expr = "write\r";
        assertEquals("KW_Write(0, 0, 4)", Lexer.getTokens(expr));
    }

    @Test
    public void testOperators() throws LexerException {
        String expr = "+";
        assertEquals("Op(Plus, 0, 0, 0)", Lexer.getTokens(expr));

        expr = "%";
        assertEquals("Op(Rem, 0, 0, 0)", Lexer.getTokens(expr));

        expr = "==";
        assertEquals("Op(Eq, 0, 0, 1)", Lexer.getTokens(expr));

        expr = ">";
        assertEquals("Op(Gt, 0, 0, 0)", Lexer.getTokens(expr));

        expr = "||";
        assertEquals("Op(Or, 0, 0, 1)", Lexer.getTokens(expr));
    }

    @Test
    public void testArithmeticExpression() throws LexerException {
        String expr = "12 + 34 - (7.4 / 2.1) * 25 % 3";
        String expected = "Num(12, 0, 0, 1); Op(Plus, 0, 3, 3); Num(34, 0, 5, 6); " +
                "Op(Minus, 0, 8, 8); OpenBr(0, 10, 10); Num(7.4, 0, 11, 13); " +
                "Op(Div, 0, 15, 15); Num(2.1, 0, 17, 19); ClBr(0, 20, 20); " +
                "Op(Mul, 0, 22, 22); Num(25, 0, 24, 25); Op(Rem, 0, 27, 27); " +
                "Num(3, 0, 29, 29)";
        assertEquals(expected, Lexer.getTokens(expr));
    }

    @Test
    public void testExpressionWithKeywords() throws LexerException {
        String expr = "read x; if y + 1 == x then write y else write x";
        String expected = "KW_Read(0, 0, 3); Ident(\"x\", 0, 5, 5); Semicolon(0, 6, 6); " +
                "KW_If(0, 8, 9); Ident(\"y\", 0, 11, 11); Op(Plus, 0, 13, 13); " +
                "Num(1, 0, 15, 15); Op(Eq, 0, 17, 18); Ident(\"x\", 0, 20, 20); " +
                "KW_Then(0, 22, 25); KW_Write(0, 27, 31); Ident(\"y\", 0, 33, 33); " +
                "KW_Else(0, 35, 38); KW_Write(0, 40, 44); Ident(\"x\", 0, 46, 46)";
        assertEquals(expected, Lexer.getTokens(expr));

        expr = "read read read_\nread3 read readx read\treadt";
        expected = "KW_Read(0, 0, 3); KW_Read(0, 5, 8); Ident(\"read_\", 0, 10, 14); " +
                "Ident(\"read3\", 1, 0, 4); KW_Read(1, 6, 9); Ident(\"readx\", 1, 11, 15); " +
                "KW_Read(1, 17, 20); Ident(\"readt\", 1, 22, 26)";
        assertEquals(expected, Lexer.getTokens(expr));
    }

    @Test
    public void testLogicalExpression() throws LexerException {
        String expr = "true || (false && true)";
        String expected = "Bool(true, 0, 0, 3); Op(Or, 0, 5, 6); OpenBr(0, 8, 8); " +
                "Bool(false, 0, 9, 13); Op(And, 0, 15, 16); Bool(true, 0, 18, 21); " +
                "ClBr(0, 22, 22)";
        assertEquals(expected, Lexer.getTokens(expr));
    }

    @Test
    public void testComparisonOperators() throws LexerException {
        String expr = "(1 > 2) && (2 <= 3.1) || 23 != 24";
        String expected = "OpenBr(0, 0, 0); Num(1, 0, 1, 1); Op(Gt, 0, 3, 3); " +
                "Num(2, 0, 5, 5); ClBr(0, 6, 6); Op(And, 0, 8, 9); " +
                "OpenBr(0, 11, 11); Num(2, 0, 12, 12); Op(Lte, 0, 14, 15); " +
                "Num(3.1, 0, 17, 19); ClBr(0, 20, 20); Op(Or, 0, 22, 23); " +
                "Num(23, 0, 25, 26); Op(Not eq, 0, 28, 29); Num(24, 0, 31, 32)";
        assertEquals(expected, Lexer.getTokens(expr));

        expr = "(1 < 2) || (2 >= 3.1) && 23 == 24";
        expected = "OpenBr(0, 0, 0); Num(1, 0, 1, 1); Op(Lt, 0, 3, 3); " +
                "Num(2, 0, 5, 5); ClBr(0, 6, 6); Op(Or, 0, 8, 9); " +
                "OpenBr(0, 11, 11); Num(2, 0, 12, 12); Op(Gte, 0, 14, 15); " +
                "Num(3.1, 0, 17, 19); ClBr(0, 20, 20); Op(And, 0, 22, 23); " +
                "Num(23, 0, 25, 26); Op(Eq, 0, 28, 29); Num(24, 0, 31, 32)";
        assertEquals(expected, Lexer.getTokens(expr));
    }

    @Test
    public void testSeparators() throws LexerException {
        String expr = "x, y; (z)";
        String expected = "Ident(\"x\", 0, 0, 0); Comma(0, 1, 1); Ident(\"y\", 0, 3, 3); " +
                "Semicolon(0, 4, 4); OpenBr(0, 6, 6); Ident(\"z\", 0, 7, 7); ClBr(0, 8, 8)";
        assertEquals(expected, Lexer.getTokens(expr));
    }

    @Test
    public void testIllegalOperator() {
        String expr = "x ^ 6";
        try {
            Lexer.getTokens(expr);
            fail();
        } catch (LexerException e) {
            assertEquals("Unable to tokenize expression at line 0, position 2", e.getMessage());
        }

        expr = "x = 1";
        try {
            Lexer.getTokens(expr);
            fail();
        } catch (LexerException e) {
            assertEquals("Unable to tokenize expression at line 0, position 2", e.getMessage());
        }

        expr = "x & 1";
        try {
            Lexer.getTokens(expr);
            fail();
        } catch (LexerException e) {
            assertEquals("Unable to tokenize expression at line 0, position 2", e.getMessage());
        }
    }

    @Test
    public void testIncorrectIdentifier() {
        String expr = "Identifier";
        try {
            Lexer.getTokens(expr);
            fail();
        } catch (LexerException e) {
            assertEquals("Unable to tokenize expression at line 0, position 0", e.getMessage());
        }

        expr = "1dentifier";
        try {
            Lexer.getTokens(expr);
            fail();
        } catch (LexerException e) {
            assertEquals("Unable to tokenize expression at line 0, position 2", e.getMessage());
        }

        expr = "ident|fier";
        try {
            Lexer.getTokens(expr);
            fail();
        } catch (LexerException e) {
            assertEquals("Unable to tokenize expression at line 0, position 5", e.getMessage());
        }
    }


}