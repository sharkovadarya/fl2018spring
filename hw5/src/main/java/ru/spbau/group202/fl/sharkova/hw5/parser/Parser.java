package ru.spbau.group202.fl.sharkova.hw5.parser;

import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;
import ru.spbau.group202.fl.sharkova.hw5.lexer.tokens.*;

import java.util.List;

public class Parser {
    private List<Token> tokens;
    private Token t;
    private TreeNode<ASTNode> tree = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.PROGRAM));

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public TreeNode<ASTNode> parse() throws ParseException {
        int i = 0;
        t = tokens.get(i);
        while (true) {
            int def = parseFunctionDefinition(i, tree);
            if (def == -1) {
                t = tokens.get(i);
                break;
            }

            i = next(def);
            if (i == -1) {
                break;
            }
        }

        while (i != -1 && i < tokens.size()) {
            i = next(parseStatement(i, tree));
        }

        return tree;
    }

    private void reportParsingError(Token t) throws ParseException {
        throw new ParseException("Parsing error at: line " + t.lineNumber + ", position " + t.startPos);
    }

    private int parseFunctionDefinition(int i, TreeNode<ASTNode> parent) throws ParseException {
        TreeNode<ASTNode> functionDefinition = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.FUNCTION_DEFINITION));

        if (!(t instanceof IdentifierToken)) {
            return -1;
        }

        addToken(functionDefinition);

        TreeNode<ASTNode> functionArguments = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.FUNCTION_ARGUMENTS));

        i = next(i);
        if (!(t instanceof SeparatorToken && t.token == SeparatorToken.OPEN_BRACKET)) {
            return -1;
        }

        while (true) {
            Token lastToken = t;
            i = next(i);

            if ((t instanceof SeparatorToken && t.token == SeparatorToken.CLOSE_BRACKET)) {
                break;
            }

            if (t instanceof IdentifierToken) {
                addToken(functionArguments);
            }

            if (!(t instanceof IdentifierToken ||
                    (t instanceof SeparatorToken && t.token == SeparatorToken.COMMA))) {
                return -1;
            }

            if (lastToken instanceof IdentifierToken && t instanceof IdentifierToken ||
                    lastToken instanceof SeparatorToken && t instanceof SeparatorToken) {
                reportParsingError(t);
            }
        }

        functionDefinition.add(functionArguments);

        TreeNode<ASTNode> functionBody = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.FUNCTION_BODY));

        i = parseBlock(i, functionBody);
        if (i == -1) {
            // this is a function call
            return -1;
        }
        if (!(t instanceof SeparatorToken && t.token == SeparatorToken.CLOSE_CURLY_BRACKET)) {
            reportParsingError(t);
        }

        functionDefinition.add(functionBody);
        parent.add(functionDefinition);

        return i;
    }

    private int parseBlock(int i, TreeNode<ASTNode> parent) throws ParseException {
        i = next(i);
        if (!(t instanceof SeparatorToken && t.token == SeparatorToken.OPEN_CURLY_BRACKET)) {
            return -1;
        }

        return parseUntilEndOfBlock(i, parent);
    }

    private int parseStatement(int i, TreeNode<ASTNode> parent) throws ParseException {
        TreeNode<ASTNode> statement = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.STATEMENT));

        if (t instanceof IdentifierToken) {
            TreeNode<ASTNode> cur = new ArrayMultiTreeNode<>(new ASTNode(t, ASTNode.TOKEN));
            statement.add(cur);

            i = next(i);
            if (i == -1) {
                reportParsingError(t);
            }

            if (t instanceof OperatorToken && t.token == OperatorToken.ASSIGN) {
                addToken(statement);
                i = parseExpression(i, statement);
                i = parseSemicolon(i, statement);
                parent.add(statement);
                return i;
            } else if (t instanceof SeparatorToken && t.token == SeparatorToken.OPEN_BRACKET) {
                TreeNode<ASTNode> functionCall = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.FUNCTION_CALL));
                TreeNode<ASTNode> functionArguments = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.FUNCTION_ARGUMENTS));
                while (true) {
                    if (i + 1 < tokens.size()) {
                        Token t1 = tokens.get(i + 1);
                        if (t1 instanceof SeparatorToken && t1.token == SeparatorToken.CLOSE_BRACKET) {
                            i = next(i);
                            break;
                        }
                    }
                    i = parseExpression(i, functionArguments);
                    i = next(i);
                    if (t instanceof SeparatorToken) {
                        if (t.token == SeparatorToken.COMMA) {
                            continue;
                        } else if (t.token == SeparatorToken.CLOSE_BRACKET) {
                            break;
                        } else {
                            reportParsingError(t);
                        }
                    } else {
                        reportParsingError(t);
                        break;
                    }
                }

                statement.remove(cur);
                functionCall.add(cur);
                functionCall.add(functionArguments);
                statement.add(functionCall);
                i = parseSemicolon(i, statement);
            }
        } else if (t instanceof KeywordToken) {
            if (t.token == KeywordToken.WRITE) {
                i = parseWrite(i, statement);
            } else if (t.token == KeywordToken.READ) {
                i = parseRead(i, statement);
            } else if (t.token == KeywordToken.WHILE) {
                i = parseWhile(i, statement);
            } else if (t.token == KeywordToken.IF) {
                i = parseIf(i, statement);
            }
        } else {
            reportParsingError(t);
        }
        parent.add(statement);
        return i;
    }

    private int parseExpression(int i, TreeNode<ASTNode> parent) throws ParseException {

        i = next(i);
        ArithmeticExpressionParser arithmParser = new ArithmeticExpressionParser(tokens, i, parent);
        return Math.max(arithmParser.parse() - 1, -1);
    }

    private int parseWrite(int i, TreeNode<ASTNode> parent) throws ParseException {
        TreeNode<ASTNode> writeStatement = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.OPERATOR));
        addToken(writeStatement);

        i = next(i);
        if (!(t instanceof SeparatorToken && t.token == SeparatorToken.OPEN_BRACKET)) {
            reportParsingError(t);
        }

        i = next(parseExpression(i, writeStatement));
        if (i == -1 || !(t instanceof SeparatorToken && t.token == SeparatorToken.CLOSE_BRACKET)) {
            reportParsingError(t);
        }

        parent.add(writeStatement);
        int res =  parseSemicolon(i, parent);
        if (res == -1) {
            parent.remove(writeStatement);
        }

        return res;
    }

    private int parseRead(int i, TreeNode<ASTNode> parent) throws ParseException {
        TreeNode<ASTNode> readStatement = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.OPERATOR));
        addToken(readStatement);

        i = next(i);
        if (i == -1 || !(t instanceof SeparatorToken && t.token == SeparatorToken.OPEN_BRACKET)) {
            reportParsingError(t);
        }

        i = next(i);
        if (i == -1 || !(t instanceof IdentifierToken)) {
            reportParsingError(t);
        }
        addToken(readStatement);

        i = next(i);
        if (i == -1 || !(t instanceof SeparatorToken && t.token == SeparatorToken.CLOSE_BRACKET)) {
            reportParsingError(t);
        }

        parent.add(readStatement);
        int res = parseSemicolon(i, parent);
        if (res == -1) {
            parent.remove(readStatement);
        }

        return res;
    }

    private int parseWhile(int i, TreeNode<ASTNode> parent) throws ParseException {
        TreeNode<ASTNode> whileStatement = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.OPERATOR));
        addToken(whileStatement);

        i = next(parseCondition(i, whileStatement));
        if (i == -1 || !(t instanceof KeywordToken && t.token == KeywordToken.DO)) {
            reportParsingError(t);
        }

        i = next(i);
        if (i == -1 || !(t instanceof SeparatorToken && t.token == SeparatorToken.OPEN_CURLY_BRACKET)) {
            reportParsingError(t);
        }

        int res = parseUntilEndOfBlock(i, whileStatement);
        if (res != -1) {
            parent.add(whileStatement);
        }

        return res;
    }

    private int parseIf(int i, TreeNode<ASTNode> parent) throws ParseException {
        TreeNode<ASTNode> ifStatement = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.OPERATOR));
        addToken(ifStatement);

        i = next(parseCondition(i, ifStatement));
        TreeNode<ASTNode> thenBlock = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.THEN_BLOCK));
        if (!(t instanceof KeywordToken && t.token == KeywordToken.THEN)) {
            reportParsingError(t);
        } else {
            addToken(thenBlock);
        }

        i = next(i);
        if (!(t instanceof SeparatorToken && t.token == SeparatorToken.OPEN_CURLY_BRACKET)) {
            reportParsingError(t);
        }

        i = parseUntilEndOfBlock(i, thenBlock);
        TreeNode<ASTNode> elseBlock = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.ELSE_BLOCK));
        i = next(i);
        if ((t instanceof KeywordToken && t.token == KeywordToken.ELSE)) {
            addToken(elseBlock);

            i = next(i);
            if (!(t instanceof SeparatorToken && t.token == SeparatorToken.OPEN_CURLY_BRACKET)) {
                reportParsingError(t);
            }

            int res = parseUntilEndOfBlock(i, elseBlock);
            if (res != -1) {
                ifStatement.add(thenBlock);
                ifStatement.add(elseBlock);
                parent.add(ifStatement);
            }

            return res;
        }

        // the tree can't be exactly the same as there's no 'else' keyword token
        // omitting 'else' is a form of syntactic sugar
        ifStatement.add(thenBlock);
        ifStatement.add(elseBlock);
        parent.add(ifStatement);
        return i - 1;
    }

    private int parseUntilEndOfBlock(int i, TreeNode<ASTNode> parent) throws ParseException {
        while (true) {
            i = next(i);
            if (t instanceof SeparatorToken && t.token == SeparatorToken.CLOSE_CURLY_BRACKET) {
                return i;
            }
            i = parseStatement(i, parent);
        }
    }

    private int parseCondition(int i, TreeNode<ASTNode> parent) throws ParseException {
        TreeNode<ASTNode> condition = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.CONDITION));

        i = next(i);
        if (!(t instanceof SeparatorToken && t.token == SeparatorToken.OPEN_BRACKET)) {
            reportParsingError(t);
        }

        i = next(parseExpression(i, condition));
        if ((t instanceof SeparatorToken && t.token == SeparatorToken.CLOSE_BRACKET)) {
            parent.add(condition);
            return i;
        }

        reportParsingError(t);
        return -1;
    }

    private int parseSemicolon(int i, TreeNode<ASTNode> parent) throws ParseException {
        i = next(i);
        if (i == -1) {
            reportParsingError(t);
        }
        if (t instanceof SeparatorToken && t.token == SeparatorToken.SEMICOLON) {
            addToken(parent);
            return i;
        }

        reportParsingError(t);
        return -1;
    }

    private int next(int i) {
        i++;
        if (i == -1 || i >= tokens.size()) {
            return -1;
        }

        t = tokens.get(i);
        return i;
    }

    private void addToken(TreeNode<ASTNode> parent) {
        parent.add(new ArrayMultiTreeNode<>(new ASTNode(t, ASTNode.TOKEN)));
    }

    class ArithmeticExpressionParser {
        private int next;
        private Token token;
        private List<Token> tokens;
        private TreeNode<ASTNode> parent;

        public ArithmeticExpressionParser(List<Token> tokens, int pos, TreeNode<ASTNode> parent) {
            this.tokens = tokens;
            this.parent = parent;
            next = pos;
        }

        public int parse() throws ParseException {
            if (tokens.size() == 0) {
                return -1;
            }
            next();
            getE(parent);
            if (!(token instanceof SeparatorToken &&
                    (token.token == SeparatorToken.COMMA ||
                     token.token == SeparatorToken.SEMICOLON ||
                     token.token == SeparatorToken.CLOSE_BRACKET))) {
                reportParsingError(t);
            }
            else {
                return next - 1;
            }

            return -1;
        }

        private void getE(TreeNode<ASTNode> parent) throws ParseException  {

            TreeNode<ASTNode> newE = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.EXPRESSION));
            getET(newE);

            boolean usedOp = false;
            while ((token instanceof OperatorToken) &&
                    isRelationOperator(token)) {
                newE.add(new ArrayMultiTreeNode<>(new ASTNode(token, ASTNode.TOKEN)));
                usedOp = true;
                next();
                getET(newE);
            }

            if (!usedOp) {
                for (TreeNode<ASTNode> child : newE.subtrees()) {
                    parent.add(child);
                }
            } else {
                parent.add(newE);
            }

            if (token instanceof LiteralToken && token.token == LiteralToken.NUMBER) {
                reportParsingError(t);
            }
        }

        private void getET(TreeNode<ASTNode> node) throws ParseException {
            TreeNode<ASTNode> newET = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.EXPRESSION));
            getT(newET);

            boolean usedOp = false;
            while (token instanceof OperatorToken &&
                   (token.token == OperatorToken.PLUS ||
                    token.token == OperatorToken.MINUS)) {
                newET.add(new ArrayMultiTreeNode<>(new ASTNode(token, ASTNode.TOKEN)));
                usedOp = true;
                next();
                getT(newET);
            }

            if (!usedOp) {
                for (TreeNode<ASTNode> child : newET.subtrees()) {
                    node.add(child);
                }
            } else {
                node.add(newET);
            }
        }

        private void getT(TreeNode<ASTNode> node) throws ParseException  {
            TreeNode<ASTNode> newT = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.EXPRESSION));
            getF(newT);

            boolean usedOp = false;
            while (token instanceof OperatorToken &&
                    (token.token == OperatorToken.MUL ||
                     token.token == OperatorToken.DIV ||
                     token.token == OperatorToken.REM ||
                     token.token == OperatorToken.AND ||
                     token.token == OperatorToken.OR)) {
                newT.add(new ArrayMultiTreeNode<>(new ASTNode(token, ASTNode.TOKEN)));
                usedOp = true;
                next();
                getF(newT);
            }

            if (!usedOp) {
                for (TreeNode<ASTNode> child : newT.subtrees()) {
                    node.add(child);
                }
            } else {
                node.add(newT);
            }

        }

        private void getF(TreeNode<ASTNode> node) throws ParseException {
            if (token == null) {
                reportParsingError(t);
            }

            if (token instanceof LiteralToken && token.token == LiteralToken.NUMBER) {
                node.add(new ArrayMultiTreeNode<>(new ASTNode(token, ASTNode.TOKEN)));
                next();
            } else if (token instanceof IdentifierToken) {
                Token old = token;
                int oldPos = next;

                TreeNode<ASTNode> functionCall = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.FUNCTION_CALL));
                functionCall.add(new ArrayMultiTreeNode<>(new ASTNode(token, ASTNode.TOKEN)));
                TreeNode<ASTNode> functionArguments = new ArrayMultiTreeNode<>(new ASTNode(ASTNode.FUNCTION_ARGUMENTS));
                next();
                if (token instanceof SeparatorToken && token.token == SeparatorToken.OPEN_BRACKET) {
                    next();
                    while (true) {
                        if (token instanceof SeparatorToken && token.token == SeparatorToken.COMMA) {
                            next();
                            continue;
                        }

                        if (token instanceof SeparatorToken && token.token == SeparatorToken.CLOSE_BRACKET) {
                            next();
                            functionCall.add(functionArguments);
                            node.add(functionCall);
                            return;
                        }

                        getE(functionArguments);
                        if (next == -1) {
                            reportParsingError(token);
                        }
                    }
                } else {
                    token = old;
                    next = oldPos;
                    // casual identifier
                    node.add(new ArrayMultiTreeNode<>(new ASTNode(token, ASTNode.TOKEN)));
                    next();
                }
            } else if (token instanceof SeparatorToken && token.token == SeparatorToken.OPEN_BRACKET) {
                next();
                getE(node);
                if (token instanceof SeparatorToken && token.token == SeparatorToken.CLOSE_BRACKET) {
                    next();
                }
                else {
                    reportParsingError(t);
                }
            }
            else {
                reportParsingError(t);
            }
        }

        private void next() {
            if (next >= tokens.size()) {
                token = null;
                return;
            }
            token = tokens.get(next++);
        }

        private boolean isRelationOperator(Token token) {
            return token.token == OperatorToken.EQ || token.token == OperatorToken.NEQ ||
                    token.token == OperatorToken.LT || token.token == OperatorToken.LTE ||
                    token.token == OperatorToken.GT || token.token == OperatorToken.GTE;
        }

    }

}
