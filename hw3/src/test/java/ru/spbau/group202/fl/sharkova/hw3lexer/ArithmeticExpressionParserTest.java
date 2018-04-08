package ru.spbau.group202.fl.sharkova.hw3lexer;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ArithmeticExpressionParserTest {
    /*
     * The following tests succeed if no exception was thrown,
     * meaning the expression was successfully parsed.
     */

    @Test
    public void testParseOneNumber() throws TokenizeException, ParseException {
        String expr = "46386436";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        p.parse();
    }

    @Test
    public void testParseOneNumberBracketed() throws TokenizeException, ParseException {
        String expr = "( ( 67 )          )";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        p.parse();
    }

    @Test
    public void testParseAdditionSubtraction() throws TokenizeException, ParseException {
        String expr = "5632 + 7843 - 333 + 238";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        p.parse();
    }

    @Test
    public void testParseAdditionSubtractionWithBrackets() throws TokenizeException, ParseException {
        String expr = "5632+ (   7843) - (333 +  238)";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        p.parse();
    }

    @Test
    public void testParseMultiplicationDivision() throws TokenizeException, ParseException {
        String expr = "30 * 59 / 77";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        p.parse();
    }

    @Test
    public void testParseMultiplicationDivisionWithBrackets() throws TokenizeException, ParseException {
        String expr = "(116 / 59) * 77 * ((116)   ) / (59 * 77)";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        p.parse();
    }

    @Test
    public void testParsePower() throws TokenizeException, ParseException {
        String expr = "30 ^ 59";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        p.parse();
    }

    @Test
    public void testParsePowerWithBrackets() throws TokenizeException, ParseException {
        String expr = "(30) ^(78^54)";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        p.parse();
    }

    @Test
    public void testParseExpressionWithAllOperationsAndBrackets()
            throws TokenizeException, ParseException {
        String[] expressions = new String[]{"0 + 13 * 42 - 7 / 0", "(0 + 13) * ((42 - 7) / 0)",
                "1 - 2 - 3 - (5 - 6)", "13", "(((((13)))))",
                "42 ^ 24 - 156 * 123", "(42 ^ (24 - 156) * 123)"};
        for (String expr : expressions) {
            ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
            ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
            p.parse();
        }
    }

    /*
     * The following tests succeed when an exception is thrown,
     * meaning it was impossible to parse the expression.
     */

    @Test
    public void testParseAdditionNoLeftArgument() throws TokenizeException {
        String expr = " + 30";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        try {
            p.parse();
            fail();
        } catch (ParseException e) {
            assertEquals("Parsing error at position 0", e.getMessage());
        }
    }

    @Test
    public void testParseSubtractionNoRightArgument() throws TokenizeException {
        String expr = "30 - ";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        try {
            p.parse();
            fail();
        } catch (ParseException e) {
            assertEquals( "Parsing error at position 4", e.getMessage());
        }
    }

    @Test
    public void testParseMultiplicationNoLeftArgument() throws TokenizeException {
        String expr = "*30";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        try {
            p.parse();
            fail();
        } catch (ParseException e) {
            assertEquals("Parsing error at position 0", e.getMessage());
        }
    }

    @Test
    public void testParseDivisionNoRightArgument() throws TokenizeException {
        String expr = "2/    ";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        try {
            p.parse();
            fail();
        } catch (ParseException e) {
            assertEquals("Parsing error at position 2", e.getMessage());
        }
    }

    @Test
    public void testParsePowerNoLeftArgument() throws TokenizeException {
        String expr = "^6";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        try {
            p.parse();
            fail();
        } catch (ParseException e) {
            assertEquals("Parsing error at position 0", e.getMessage());
        }
    }

    @Test
    public void testParsePowerNoRightArgument() throws TokenizeException {
        String expr = "30 ^";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        try {
            p.parse();
            fail();
        } catch (ParseException e) {
            assertEquals("Parsing error at position 4", e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testParseMismatchedClosingBracket() throws TokenizeException, ParseException {
        String expr = "21 + 30)";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        p.parse();
    }

    @Test(expected = ParseException.class)
    public void testParseMismatchedClosingBracketLongerExpression()
            throws TokenizeException, ParseException {
        String expr = "(2 /(21 + 30) / 0))";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        p.parse();
    }

    @Test(expected = ParseException.class)
    public void testParseMismatchedOpeningBracket() throws TokenizeException, ParseException {
        String expr = "(30 - 21";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        p.parse();
    }

    @Test(expected = ParseException.class)
    public void testParseMismatchedOpeningBracketLongerExpression()
            throws TokenizeException, ParseException {
        String expr = "21 + (13 / (30 - 21 ^(55 - 1)  )";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        p.parse();
    }

    @Test(expected = ParseException.class)
    public void testParseTwoNumbersWithoutOperator() throws TokenizeException, ParseException {
        String expr = "30 89";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        p.parse();
    }

    @Test(expected = ParseException.class)
    public void testParseTwoNumbersWithoutOperatorLongerExpression()
            throws TokenizeException, ParseException {
        String expr = "21 + 2221^(30 89) / 992 - 1";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        p.parse();
    }

    @Test(expected = ParseException.class)
    public void testParseTwoOperatorsInARow() throws TokenizeException, ParseException {
        String expr = "50 * -89";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        p.parse();
    }

    @Test(expected = ParseException.class)
    public void testParseTwoOperatorsInARowLongerExpression() throws TokenizeException, ParseException {
        String expr = "12 / 0 * 23 / (50) * /89 - 89";
        ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(expr);
        ArithmeticExpressionParser p = new ArithmeticExpressionParser(tokens);
        p.parse();
    }

}