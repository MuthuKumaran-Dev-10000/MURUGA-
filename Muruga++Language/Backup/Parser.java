package withrules;

import java.util.ArrayList;
import java.util.List;

class Parser {
    private List<Token> tokens;
    private int position;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.position = 0;
    }

    public Node parse() {
        List<Node> statements = new ArrayList<>();
        while (position < tokens.size()) {
            statements.add(parseStatement());
        }
        return new BlockNode(statements);
    }

    private Node parseStatement() {
        if (match("muruga")) {
            return parseVariableDeclaration();
        } else if (match("vel")) {
            return parsePrintStatement();
        } else if (match("arokara")) {
            return parseForLoop();
        } else if (match("sendruva")) {
            return parseReturnStatement();
        } else if (match("vetrivel")) {
            return parseFunctionDefinition();
        } else if (tokens.get(position).getType() == TokenType.IDENTIFIER) {
            return parseAssignmentOrExpression();
        } else {
            throw new RuntimeException("Unexpected token: " + tokens.get(position));
        }
    }

    private Node parseVariableDeclaration() {
        String varName = consume(TokenType.IDENTIFIER).getValue();
        consume(":");
        Node value = parseExpression();
        consume(";");
        return new VariableDeclarationNode(varName, value);
    }

    private Node parsePrintStatement() {
        consume("(");
        Node value = parseExpression();
        consume(")");
        consume(";");
        return new PrintNode(value);
    }

    private Node parseForLoop() {
        Node start = parseExpression();
        consume("..");
        Node end = parseExpression();
        consume("{");
        List<Node> body = new ArrayList<>();
        while (!match("}")) {
            if (position >= tokens.size()) {
                throw new RuntimeException("Unclosed block for 'arokara'");
            }
            body.add(parseStatement());
        }
        consume("}");
        return new ForLoopNode(start, end, new BlockNode(body));
    }

    private Node parseReturnStatement() {
        Node value = parseExpression();
        consume(";");
        return new ReturnNode(value);
    }
    private Node parseFunctionDefinition() {
        String functionName = consume(TokenType.IDENTIFIER).getValue();
        consume("(");
    
        // Parse function parameters
        List<String> parameters = new ArrayList<>();
        if (!match(")")) {
            do {
                parameters.add(consume(TokenType.IDENTIFIER).getValue());
            } while (match(","));
            consume(")");
        } else {
            consume(")");
        }
    
        // Parse function body
        consume("{");
        List<Node> body = new ArrayList<>();
        while (!match("sendruva")) {
            body.add(parseStatement());
        }
    
        // Ensure the function ends with a return statement
        if (body.isEmpty() || !(body.get(body.size() - 1) instanceof ReturnNode)) {
            throw new RuntimeException("Function must end with a return statement (sendruva).");
        }
    
        // Parse return expression
        Node returnValue = ((ReturnNode) body.get(body.size() - 1)).getExpression();
        consume(";"); // Consume the semicolon after the return statement
        consume("}");
    
        return new FunctionDefinitionNode(functionName, parameters, new BlockNode(body.subList(0, body.size() - 1)), returnValue);
    }
    
    
    
    
    
    
    
    
    private VariableDeclarationNode parseAssignmentOrExpression() {
        Node left = parseExpression();
        if (tokens.get(position).getValue().equals(":")) {
            // Variable redefinition
            position++; // Consume ':'
            Node value = parseExpression();
            consume(";");
            return new VariableDeclarationNode(((VariableNode) left).getName(), value);
        } else {
            // Regular assignment
            consume(";");
            // return new AssignmentNode((VariableNode) left, parseExpression());
        }
        return null;
    }

    private Node parseExpression() {
        return parseTerm();
    }

    private Node parseTerm() {
        Node left = parseFactor();
        while (match("+") || match("-")) {
            String operator = tokens.get(position - 1).getValue();
            Node right = parseFactor();
            left = new BinaryOperationNode(left, operator, right);
        }
        return left;
    }

    private Node parseFactor() {
        Node left = parsePrimary();
        while (match("*") || match("/")) {
            String operator = tokens.get(position - 1).getValue();
            Node right = parsePrimary();
            left = new BinaryOperationNode(left, operator, right);
        }
        return left;
    }

    private Node parsePrimary() {
        if (match(TokenType.NUMBER)) {
            return new NumberNode(Integer.parseInt(tokens.get(position - 1).getValue()));
        } else if (match(TokenType.STRING)) {
            return new StringNode(tokens.get(position - 1).getValue());
        } else if (match(TokenType.IDENTIFIER)) {
            return new VariableNode(tokens.get(position - 1).getValue());
        } else if (match("(")) {
            Node expression = parseExpression();
            consume(")");
            return expression;
        } else {
            throw new RuntimeException("Unexpected token: " + tokens.get(position));
        }
    }

    private boolean match(String expected) {
        if (position < tokens.size() && tokens.get(position).getValue().equals(expected)) {
            position++;
            return true;
        }
        return false;
    }

    private boolean match(TokenType type) {
        if (position < tokens.size() && tokens.get(position).getType() == type) {
            position++;
            return true;
        }
        return false;
    }

    private Token consume(TokenType type) {
        if (position < tokens.size() && tokens.get(position).getType() == type) {
            return tokens.get(position++);
        }
        throw new RuntimeException("Expected token type: " + type + " but found: " + tokens.get(position));
    }

    private void consume(String value) {
        if (position < tokens.size() && tokens.get(position).getValue().equals(value)) {
            position++;
        } else {
            throw new RuntimeException("Expected token: " + value + " but found: " + tokens.get(position));
        }
    }
}
