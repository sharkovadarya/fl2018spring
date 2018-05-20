package ru.spbau.group202.fl.sharkova.hw4lexer.parser;

import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;
import org.junit.Test;
import ru.spbau.group202.fl.sharkova.hw4lexer.lexer.Lexer;
import ru.spbau.group202.fl.sharkova.hw4lexer.lexer.LexerException;
import ru.spbau.group202.fl.sharkova.hw4lexer.lexer.tokens.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testFunctionDefinition() throws IOException, LexerException, ParseException {
        TreeNode<ASTNode> actual = getTree("fun_def.txt");
        TreeNode<ASTNode> expected = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.PROGRAM));
        TreeNode<ASTNode> functionDefinition = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.FUNCTION_DEFINITION));
        functionDefinition.add(new ArrayMultiTreeNode<>(new ASTNode(
                new IdentifierToken(0, 0, 7, IdentifierToken.IDENTIFIER,"function"),
                ASTNode.TOKEN)));
        TreeNode<ASTNode> functionArguments = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.FUNCTION_ARGUMENTS));
        functionDefinition.add(functionArguments);
        TreeNode<ASTNode> functionBody = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.FUNCTION_BODY));
        TreeNode<ASTNode> writeStatement = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.STATEMENT));
        TreeNode<ASTNode> operator = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.OPERATOR));
        operator.add(new ArrayMultiTreeNode<>(new ASTNode(
                new KeywordToken(2, 0, 4, KeywordToken.WRITE, "write"),
                ASTNode.TOKEN)));
        operator.add(new ArrayMultiTreeNode<>(new ASTNode(
                new IdentifierToken(2, 6, 6, IdentifierToken.IDENTIFIER,"x"),
                ASTNode.TOKEN)));
        writeStatement.add(operator);
        writeStatement.add(new ArrayMultiTreeNode<>(new ASTNode(
                new SeparatorToken(2, 8, 8, SeparatorToken.SEMICOLON, ";"),
                ASTNode.TOKEN)));
        functionBody.add(writeStatement);
        functionDefinition.add(functionBody);
        expected.add(functionDefinition);

        // somehow equals() is not overloaded for this library so we have to use strings
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testWriteStatement() throws IOException, LexerException, ParseException {
        TreeNode<ASTNode> actual = getTree("write.txt");
        TreeNode<ASTNode> expected = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.PROGRAM));
        TreeNode<ASTNode> writeStatement1 = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.STATEMENT));
        TreeNode<ASTNode> operator1 = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.OPERATOR));
        operator1.add(new ArrayMultiTreeNode<>(new ASTNode(
                new KeywordToken(0, 0, 4, KeywordToken.WRITE, "write"),
                ASTNode.TOKEN)));
        operator1.add(new ArrayMultiTreeNode<>(new ASTNode(
                new IdentifierToken(0, 6, 6, IdentifierToken.IDENTIFIER, "x"),
                ASTNode.TOKEN)));
        writeStatement1.add(operator1);
        writeStatement1.add(new ArrayMultiTreeNode<>(new ASTNode(
                new SeparatorToken(0, 8, 8, SeparatorToken.SEMICOLON, ";"),
                ASTNode.TOKEN)));
        TreeNode<ASTNode> writeStatement2 = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.STATEMENT));
        TreeNode<ASTNode> operator2 = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.OPERATOR));
        operator2.add(new ArrayMultiTreeNode<>(new ASTNode(
                new KeywordToken(1, 0, 4, KeywordToken.WRITE, "write"),
                ASTNode.TOKEN)));
        operator2.add(new ArrayMultiTreeNode<>(new ASTNode(
                new LiteralToken(1, 6, 7, LiteralToken.NUMBER, "30"),
                ASTNode.TOKEN)));
        writeStatement2.add(operator2);
        writeStatement2.add(new ArrayMultiTreeNode<>(new ASTNode(
                new SeparatorToken(1, 9, 9, SeparatorToken.SEMICOLON, ";"),
                ASTNode.TOKEN)));
        expected.add(writeStatement1);
        expected.add(writeStatement2);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAssignment() throws IOException, LexerException, ParseException {
        TreeNode<ASTNode> actual = getTree("assign.txt");
        TreeNode<ASTNode> expected = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.PROGRAM));

        TreeNode<ASTNode> assignment1 = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.STATEMENT));
        assignment1.add(new ArrayMultiTreeNode<>(new ASTNode(
                new IdentifierToken(0, 0, 0, IdentifierToken.IDENTIFIER, "x"),
                ASTNode.TOKEN)));
        assignment1.add(new ArrayMultiTreeNode<>(new ASTNode(
                new OperatorToken(0, 2, 3, OperatorToken.ASSIGN, ":="),
                ASTNode.TOKEN)));
        assignment1.add(new ArrayMultiTreeNode<>(
                new ASTNode(new LiteralToken(0, 5, 6, LiteralToken.NUMBER, "30"),
                ASTNode.TOKEN)));
        assignment1.add(new ArrayMultiTreeNode<>(new ASTNode(
                new SeparatorToken(0, 7, 7, SeparatorToken.SEMICOLON, ";"),
                ASTNode.TOKEN)));
        TreeNode<ASTNode> assignment2 = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.STATEMENT));
        assignment2.add(new ArrayMultiTreeNode<>(new ASTNode(
                new IdentifierToken(1, 0, 0, IdentifierToken.IDENTIFIER, "x"),
                ASTNode.TOKEN)));
        assignment2.add(new ArrayMultiTreeNode<>(new ASTNode(
                new OperatorToken(1, 2, 3, OperatorToken.ASSIGN, ":="),
                ASTNode.TOKEN)));
        assignment2.add(new ArrayMultiTreeNode<>(
                new ASTNode(new LiteralToken(1, 8, 9, LiteralToken.NUMBER, "30"),
                        ASTNode.TOKEN)));
        assignment2.add(new ArrayMultiTreeNode<>(new ASTNode(
                new SeparatorToken(1, 13, 13, SeparatorToken.SEMICOLON, ";"),
                ASTNode.TOKEN)));

        expected.add(assignment1);
        expected.add(assignment2);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testDefinitionAndStatement() throws IOException, LexerException, ParseException {
        TreeNode<ASTNode> actual = getTree("def_stmt.txt");
        TreeNode<ASTNode> expected = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.PROGRAM));

        TreeNode<ASTNode> functionDefinition = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.FUNCTION_DEFINITION));
        functionDefinition.add(new ArrayMultiTreeNode<>(new ASTNode(
                new IdentifierToken(0, 0, 7, IdentifierToken.IDENTIFIER,"function"),
                ASTNode.TOKEN)));
        TreeNode<ASTNode> functionArguments = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.FUNCTION_ARGUMENTS));
        functionArguments.add(new ArrayMultiTreeNode<>(new ASTNode(
                new IdentifierToken(0, 9, 12, IdentifierToken.IDENTIFIER, "arg1"),
                ASTNode.TOKEN)));
        functionDefinition.add(functionArguments);
        TreeNode<ASTNode> functionBody = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.FUNCTION_BODY));
        TreeNode<ASTNode> stmt = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.STATEMENT));
        TreeNode<ASTNode> call = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.FUNCTION_CALL));
        call.add(new ArrayMultiTreeNode<>(new ASTNode(
                new IdentifierToken(2, 0, 8, IdentifierToken.IDENTIFIER, "function1"),
                ASTNode.TOKEN)));
        TreeNode<ASTNode> args = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.FUNCTION_ARGUMENTS));
        args.add(new ArrayMultiTreeNode<>(new ASTNode(
                new IdentifierToken(2, 10, 13, IdentifierToken.IDENTIFIER, "arg1"),
                ASTNode.TOKEN)));
        call.add(args);
        stmt.add(call);
        stmt.add(new ArrayMultiTreeNode<>(new ASTNode(
                new SeparatorToken(2, 15, 15, SeparatorToken.SEMICOLON, ";"),
                ASTNode.TOKEN)));
        functionBody.add(stmt);
        functionDefinition.add(functionBody);
        expected.add(functionDefinition);

        TreeNode<ASTNode> assignment = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.STATEMENT));
        assignment.add(new ArrayMultiTreeNode<>(new ASTNode(
                new IdentifierToken(4, 0, 0, IdentifierToken.IDENTIFIER, "x"),
                ASTNode.TOKEN)));
        assignment.add(new ArrayMultiTreeNode<>(new ASTNode(
                new OperatorToken(4, 2, 3, OperatorToken.ASSIGN, ":="),
                ASTNode.TOKEN)));
        TreeNode<ASTNode> call2 = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.FUNCTION_CALL));
        call2.add(new ArrayMultiTreeNode<>(new ASTNode(
                new IdentifierToken(4, 5, 12, IdentifierToken.IDENTIFIER, "function"),
                ASTNode.TOKEN)));
        TreeNode<ASTNode> args2 = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.FUNCTION_ARGUMENTS));
        args2.add(new ArrayMultiTreeNode<>(new ASTNode(
                new IdentifierToken(4, 14, 17, IdentifierToken.IDENTIFIER, "arg1"),
                ASTNode.TOKEN)));
        call2.add(args2);
        assignment.add(call2);
        assignment.add(new ArrayMultiTreeNode<>(new ASTNode(
                new SeparatorToken(4, 19, 19, SeparatorToken.SEMICOLON, ";"),
                ASTNode.TOKEN)));
        expected.add(assignment);


        assertEquals(expected.toString(), actual.toString());
    }

    // the next test is used for manual testing
    // it outputs the tree for the user to observe themselves

    @Test
    public void testAllConstructions() throws IOException, LexerException, ParseException {
        TreeNode<ASTNode> tree = getTree("all.txt");
        System.out.println(tree);
    }

    // correctness of messages will not be checked
    // as the messages are formed from tokens
    // and correctness of tokens has been checked in previous h/w
    // also each test contains only one error

    @Test(expected = ParseException.class)
    public void testNoSemicolon() throws LexerException, ParseException {
        testInput("write(x)");
    }

    @Test(expected = ParseException.class)
    public void testIncorrectArithmeticExpression() throws LexerException, ParseException {
        testInput("x := 32 - 90 * -12");
    }

    @Test(expected = ParseException.class)
    public void testMalformedWhile() throws LexerException, ParseException {
        testInput("while (x == 30) {write(x);}");
    }

    @Test(expected = ParseException.class)
    public void testMalformedIfCondition() throws LexerException, ParseException {
        testInput("if (if (x) then {function1(x);} else {function2(x);}) then {function1(x);} else {function2(x);}");
    }

    @Test(expected = ParseException.class)
    public void testMalformedIf() throws LexerException, ParseException {
        testInput("if (x == 30) then {x := 31;}");
    }

    @Test(expected = ParseException.class)
    public void testMalformedFunctionCall() throws LexerException, ParseException {
        testInput("function1(arg1 arg2, arg3);");
    }

    @Test(expected = ParseException.class)
    public void testNoMatchingCurlyBrace() throws LexerException, ParseException {
        testInput("function1(arg1){write(x);");
    }

    @Test(expected = ParseException.class)
    public void testNoOpenCurlyBrace() throws LexerException, ParseException {
        testInput("function1(arg1)write(x);}");

    }

    @Test(expected = ParseException.class)
    public void testMalformedAssignment() throws LexerException, ParseException {
        testInput("x := while (y != z) do {function1(y, z);}");
    }

    // the 'while' itself is correct
    @Test
    public void testWhile() throws LexerException, ParseException {
        testInput("while (y != z) do {function1(y, z);}");
    }

    @Test(expected = ParseException.class)
    public void testIncorrectReadArgument() throws LexerException, ParseException {
        testInput("read(30);");
    }

    private TreeNode<ASTNode> getTree(String file1) throws IOException, LexerException, ParseException {
        File f = new File("src/test/resources/" + file1);
        byte[] encoded = Files.readAllBytes(f.toPath());
        String input = new String(encoded);
        Parser parser = new Parser(Lexer.getTokensList(input));
        return parser.parse();
    }

    private void testInput(String input) throws LexerException, ParseException {
        Parser parser = new Parser(Lexer.getTokensList(input));
        parser.parse();
    }

}