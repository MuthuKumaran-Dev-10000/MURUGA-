package withrules;

import java.util.List;

abstract class Node {}

class BlockNode extends Node {
    private List<Node> statements;

    public BlockNode(List<Node> statements) {
        this.statements = statements;
    }

    public List<Node> getStatements() {
        return statements;
    }
}

class VariableDeclarationNode extends Node {
    private String varName;
    private Node value;

    public VariableDeclarationNode(String varName, Node value) {
        this.varName = varName;
        this.value = value;
    }

    public String getVarName() {
        return varName;
    }

    public Node getValue() {
        return value;
    }
}

class PrintNode extends Node {
    private Node value;

    public PrintNode(Node value) {
        this.value = value;
    }

    public Node getValue() {
        return value;
    }
}

class ForLoopNode extends Node {
    private Node start;
    private Node end;
    private BlockNode body;

    public ForLoopNode(Node start, Node end, BlockNode body) {
        this.start = start;
        this.end = end;
        this.body = body;
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public BlockNode getBody() {
        return body;
    }
}

class ReturnNode extends Node {
    private Node value;

    public ReturnNode(Node value) {
        this.value = value;
    }

    public Node getValue() {
        return value;
    }
}

class FunctionDefinitionNode extends Node {
    private String functionName;
    private List<String> parameters;
    private BlockNode body;
    private Node returnValue;

    public FunctionDefinitionNode(String functionName, List<String> parameters, BlockNode body, Node returnValue) {
        this.functionName = functionName;
        this.parameters = parameters;
        this.body = body;
        this.returnValue = returnValue;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public BlockNode getBody() {
        return body;
    }

    public Node getReturnValue() {
        return returnValue;
    }
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

class StringNode extends Node {
    private String value;

    public StringNode(String value) {
        this.value = value;
    }

    public String getValue() {
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
