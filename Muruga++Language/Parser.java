import java.util.List;

class Parser {
    private List<Token> tokens;
    private int position;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.position = 0;
    }

    public Node parse() {
        return parseExpression();
    }

    private Node parseExpression() {
        Node left = parseTerm();
        while (match("+") || match("-")) {
            String operator = tokens.get(position - 1).getValue();
            Node right = parseTerm();
            left = new BinaryOperationNode(left, operator, right);
        }
        return left;
    }

    private Node parseTerm() {
        Node left = parseFactor();
        while (match("*") || match("/")) {
            String operator = tokens.get(position - 1).getValue();
            Node right = parseFactor();
            left = new BinaryOperationNode(left, operator, right);
        }
        return left;
    }

    private Node parseFactor() {
        if (match(TokenType.NUMBER)) {
            return new NumberNode(Integer.parseInt(tokens.get(position - 1).getValue()));
        } else if (match(TokenType.IDENTIFIER)) {
            return new VariableNode(tokens.get(position - 1).getValue());
        } else if (match("(")) {
            Node expression = parseExpression();
            match(")");
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
}

abstract class Node {
}

class NumberNode extends Node {
    private int value;

    public NumberNode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

class VariableNode extends Node {
    private String name;

    public VariableNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class BinaryOperationNode extends Node {
    private Node left;
    private String operator;
    private Node right;

    public BinaryOperationNode(Node left, String operator, Node right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Node getLeft() {
        return left;
    }

    public String getOperator() {
        return operator;
    }

    public Node getRight() {
        return right;
    }
}
