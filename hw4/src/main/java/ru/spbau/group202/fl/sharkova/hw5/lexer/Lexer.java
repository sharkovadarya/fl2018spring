package ru.spbau.group202.fl.sharkova.hw4lexer.lexer;

import ru.spbau.group202.fl.sharkova.hw4lexer.lexer.tokens.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class tokenizes a L language expression.
 */
public class Lexer {

    private static final Pattern numberRegex = Pattern.compile("^[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)([eE][-+]?[0-9]+)?[fFdD]?");
    private static final Pattern keywordRegex = Pattern.compile("^(if|then|else|while|do|read|write)");
    private static final Pattern boolRegex = Pattern.compile("^(true|false)");
    private static final Pattern identifierRegex = Pattern.compile("^[_a-z][_a-z0-9]*");

    private static final List<String> spaceSymbols = Arrays.asList(" ", "\t", "\r", "\n", "\r\n", "\f");

    private static int linePos = 0;

    public static String getTokens(String input) throws LexerException {
        return tokenize(input).stream().map(Token::getToken).collect(Collectors.joining("; "));
    }

    public static List<Token> getTokensList(String input) throws LexerException {
        return tokenize(input);
    }

    private static ArrayList<Token> tokenize(String input) throws LexerException {
        int lineNumber = 0;
        linePos = 0;
        ArrayList<Token> tokens = new ArrayList<>();
        for (int i = 0; i < input.length(); i++, linePos++) {
            char c = input.charAt(i);
            if (spaceSymbols.contains("" + c)) {
                if (c == '\n' || (c == '\r' && i + 1 < input.length() && input.charAt(i + 1) == '\n')) {
                    lineNumber++;
                    linePos = -1;
                    if (c == '\r' && i + 1 < input.length() && input.charAt(i + 1) == '\n') {
                        i++;
                    }
                }
            } else {
                switch (c) {
                    case '(':
                        tokens.add(new SeparatorToken(lineNumber, linePos, linePos, SeparatorToken.OPEN_BRACKET, "("));
                        break;
                    case ')':
                        tokens.add(new SeparatorToken(lineNumber, linePos, linePos, SeparatorToken.CLOSE_BRACKET, ")"));
                        break;
                    case '{':
                        tokens.add(new SeparatorToken(lineNumber, linePos, linePos, SeparatorToken.OPEN_CURLY_BRACKET, "{"));
                        break;
                    case '}':
                        tokens.add(new SeparatorToken(lineNumber, linePos, linePos, SeparatorToken.CLOSE_CURLY_BRACKET, "}"));
                        break;
                    case ';':
                        tokens.add(new SeparatorToken(lineNumber, linePos, linePos, SeparatorToken.SEMICOLON, ";"));
                        break;
                    case ',':
                        tokens.add(new SeparatorToken(lineNumber, linePos, linePos, SeparatorToken.COMMA, ","));
                        break;
                    // operators
                    case '+':
                        tokens.add(new OperatorToken(lineNumber, linePos, linePos, OperatorToken.PLUS, "+"));
                        break;
                    case '-':
                        tokens.add(new OperatorToken(lineNumber, linePos, linePos, OperatorToken.MINUS, "-"));
                        break;
                    case '*':
                        tokens.add(new OperatorToken(lineNumber, linePos, linePos, OperatorToken.MUL, "*"));
                        break;
                    case '/':
                        tokens.add(new OperatorToken(lineNumber, linePos, linePos, OperatorToken.DIV, "/"));
                        break;
                    case '%':
                        tokens.add(new OperatorToken(lineNumber, linePos, linePos, OperatorToken.REM, "%"));
                        break;
                    case '=': {
                            if (i + 1 < input.length() && input.charAt(i + 1) == '=') {
                                tokens.add(new OperatorToken(lineNumber, linePos, linePos + 1, OperatorToken.EQ, "=="));
                                i++;
                                linePos++;
                            } else {
                                throw new LexerException("Unable to tokenize expression at line "
                                        + lineNumber + ", position " + linePos);
                            }
                        }
                        break;
                    case '!': {
                            if (i + 1 < input.length() && input.charAt(i + 1) == '=') {
                                tokens.add(new OperatorToken(lineNumber, linePos, linePos + 1, OperatorToken.NEQ, "!="));
                                i++;
                                linePos++;
                            } else {
                                throw new LexerException("Unable to tokenize expression at line "
                                        + lineNumber + ", position " + linePos);
                            }
                        }
                        break;
                    case '<': {
                            if (i + 1 < input.length() && input.charAt(i + 1) == '=') {
                                tokens.add(new OperatorToken(lineNumber, linePos, linePos + 1, OperatorToken.LTE, "<="));
                                i++;
                                linePos++;
                            } else {
                                tokens.add(new OperatorToken(lineNumber, linePos, linePos, OperatorToken.LT, "<"));
                            }
                        }
                        break;
                    case '>': {
                            if (i + 1 < input.length() && input.charAt(i + 1) == '=') {
                                tokens.add(new OperatorToken(lineNumber, linePos, linePos + 1, OperatorToken.GTE, ">="));
                                i++;
                                linePos++;
                            } else {
                                tokens.add(new OperatorToken(lineNumber, linePos, linePos, OperatorToken.GT, ">"));
                            }
                        }
                        break;
                    case '&': {
                            if (i + 1 < input.length() && input.charAt(i + 1) == '&') {
                                tokens.add(new OperatorToken(lineNumber, linePos, linePos + 1, OperatorToken.AND, "&&"));
                                i++;
                                linePos++;
                            } else {
                                throw new LexerException("Unable to tokenize expression at line "
                                        + lineNumber + ", position " + linePos);
                            }
                        }
                        break;
                    case '|': {
                            if (i + 1 < input.length() && input.charAt(i + 1) == '|') {
                                tokens.add(new OperatorToken(lineNumber, linePos, linePos + 1, OperatorToken.OR, "||"));
                                i++;
                                linePos++;
                            } else {
                                throw new LexerException("Unable to tokenize expression at line "
                                        + lineNumber + ", position " + linePos);
                            }
                        }
                        break;
                    case ':': {
                            if (i + 1 < input.length() && input.charAt(i + 1) == '=') {
                                tokens.add(new OperatorToken(lineNumber, linePos, linePos + 1, OperatorToken.ASSIGN, ":="));
                                i++;
                                linePos++;
                            } else {
                                throw new LexerException("Unable to tokenize expression at line "
                                        + lineNumber + ", position " + linePos);
                            }
                        }
                        break;
                    default: {
                            String inputFromPos = input.substring(i);

                            Matcher numberMatcher = numberRegex.matcher(inputFromPos);
                            Matcher booleanMatcher = boolRegex.matcher(inputFromPos);


                            if (numberMatcher.find()) {
                                String extractedNumber = numberMatcher.group();
                                if (i + extractedNumber.length() < input.length()) {
                                    c = input.charAt(i + extractedNumber.length());
                                    if (Character.isAlphabetic(c) || Character.isDigit(c) || c == '_') {
                                        throw new LexerException("Unable to tokenize expression at line "
                                                + lineNumber + ", position " + (linePos + extractedNumber.length()));
                                    }

                                }
                                tokens.add(new LiteralToken(lineNumber, linePos, linePos + extractedNumber.length() - 1,
                                           LiteralToken.NUMBER, extractedNumber));
                                i += extractedNumber.length() - 1;
                                linePos += extractedNumber.length() - 1;
                                continue;
                            }

                            int res = matchKeywordLiteralOrIdentifier(booleanMatcher, input, tokens, lineNumber, i, false);
                            if (res != 0) {
                                i += res - 1;
                                linePos += res - 1;
                                continue;
                            }

                            res = matchKeywordLiteralOrIdentifier(keywordRegex.matcher(inputFromPos), input, tokens, lineNumber, i, true);
                            if (res != 0) {
                                i += res - 1;
                                linePos += res - 1;
                                continue;
                            }

                            res = matchIdentifier(identifierRegex.matcher(inputFromPos), tokens, lineNumber, i);
                            if (res != 0) {
                                i += res - 1;
                                linePos += res - 1;
                            } else {
                                // no acceptable option
                                throw new LexerException("Unable to tokenize expression at line "
                                        + lineNumber + ", position " + linePos);
                            }

                        }
                }

            }
        }

        return tokens;
    }

    private static int matchKeywordLiteralOrIdentifier(Matcher matcher, String input,
                                                       ArrayList<Token> tokens, int lineNumber,
                                                       int pos, boolean matchKeyword) throws LexerException {
        if (!matcher.find()) {
            return 0;
        }

        String matched = matcher.group();
        if (pos + matched.length() < input.length()) {
            char c = input.charAt(pos + matched.length());
            if (Character.isAlphabetic(c) || Character.isDigit(c) || c == '_') {
                Matcher identifierMatcher = identifierRegex.matcher(input.substring(pos));
                return matchIdentifier(identifierMatcher, tokens, lineNumber, pos);
            }
        }

        int token;
        if (!matchKeyword) {
            token = matched.equals("true") ? LiteralToken.TRUE : LiteralToken.FALSE;

            tokens.add(new LiteralToken(lineNumber, linePos, linePos + matched.length() - 1, token, matched));
            return matched.length();
        }

        switch (matched) {
            case "if":
                token = KeywordToken.IF;
                break;
            case "then":
                token = KeywordToken.THEN;
                break;
            case "else":
                token = KeywordToken.ELSE;
                break;
            case "while":
                token = KeywordToken.WHILE;
                break;
            case "do":
                token = KeywordToken.DO;
                break;
            case "read":
                token = KeywordToken.READ;
                break;
            case "write":
                token = KeywordToken.WRITE;
                break;
            default:
                throw new LexerException("Unable to tokenize expression at line "
                        + lineNumber + ", position " + linePos);
        }

        tokens.add(new KeywordToken(lineNumber, linePos, linePos + matched.length() - 1, token, matched));
        return matched.length();
    }

    private static int matchIdentifier(Matcher identifierMatcher, ArrayList<Token> tokens, int lineNumber, int pos) {
        if (!identifierMatcher.find()) {
            return 0;
        }

        String identifier = identifierMatcher.group();
        tokens.add(new IdentifierToken(lineNumber, linePos, linePos + identifier.length() - 1,
                IdentifierToken.IDENTIFIER, identifier));
        return identifier.length();
    }
}
