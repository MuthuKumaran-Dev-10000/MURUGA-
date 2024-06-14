package withrules;

import java.util.HashMap;
import java.util.Map;

public class Interpreter {
    private final Node ast;
    private final Map<String, Integer> variables = new HashMap<>();
    private final Map<String, FunctionDefinitionNode> functions = new HashMap<>();
    private final Map<String, String> stringVariables = new HashMap<>();

    public Interpreter(Node ast) {
        this.ast = ast;
    }

    public int interpret() {
        return interpretNode(ast);
    }

    private int interpretNode(Node node) {
        if (node instanceof BlockNode) {
            return interpretBlockNode((BlockNode) node);
        } else if (node instanceof ForLoopNode) {
            return interpretForLoopNode((ForLoopNode) node);
        } else if (node instanceof PrintNode) {
            return interpretPrintNode((PrintNode) node);
        } else if (node instanceof VariableDeclarationNode) {
            return interpretVariableDeclarationNode((VariableDeclarationNode) node);
        } else if (node instanceof ReturnNode) {
            return interpretReturnNode((ReturnNode) node);
        } else if (node instanceof FunctionDefinitionNode) {
            return interpretFunctionDefinitionNode((FunctionDefinitionNode) node);
        } else if (node instanceof NumberNode) {
            return ((NumberNode) node).getValue();
        } else if (node instanceof StringNode) {
            return 1;
        } else if (node instanceof BinaryOperationNode) {
            BinaryOperationNode binaryOperation = (BinaryOperationNode) node;
            if (binaryOperation.getOperator().equals("*")) {
                Node left = binaryOperation.getLeft();
                Node right = binaryOperation.getRight();
                if (left instanceof StringNode && right instanceof NumberNode) {
                    String value = ((StringNode) left).getValue();
                    int repeat = ((NumberNode) right).getValue();
                    value = processEscapeSequences(value); // Process escape sequences first
                    StringBuilder repeatedValue = new StringBuilder();
                    for (int i = 0; i < repeat; i++) {
                        repeatedValue.append(value);
                    }
                    System.out.print(repeatedValue.toString());
                    return 0; // Return value doesn't matter for print operation
                } else {
                    int leftValue = interpretNode(left);
                    int rightValue = interpretNode(right);
                    return leftValue * rightValue;
                }
            } else {
                int left = interpretNode(binaryOperation.getLeft());
                int right = interpretNode(binaryOperation.getRight());
                switch (binaryOperation.getOperator()) {
                    case "+":
                        return left + right;
                    case "-":
                        return left - right;
                    case "/":
                        return left / right;
                    default:
                        throw new RuntimeException("Unknown operator: " + binaryOperation.getOperator());
                }
            }
        } else if (node instanceof VariableNode) {
            return interpretVariableNode((VariableNode) node);
        }
        throw new RuntimeException("Unknown node type: " + node.getClass());
    }

    private int interpretBlockNode(BlockNode node) {
        int result = 0;
        for (Node statement : node.getStatements()) {
            result = interpretNode(statement);
        }
        return result;
    }

    private int interpretForLoopNode(ForLoopNode node) {
        int start = interpretNode(node.getStart());
        int end = interpretNode(node.getEnd());
        int result = 0;
        for (int i = start; i <= end; i++) {
            result = interpretNode(node.getBody());
        }
        return result;
    }

    private int interpretPrintNode(PrintNode node) {
        Node valueNode = node.getValue();
        if (valueNode instanceof StringNode) {
            String value = ((StringNode) valueNode).getValue();
            value = processEscapeSequences(value);
            System.out.print(value);
        } else {
            int value = interpretNode(valueNode);
            System.out.print(value);
        }
        return 0;
    }

    private int interpretVariableDeclarationNode(VariableDeclarationNode node) {
        String varName = node.getVarName();
        Node valueNode = node.getValue();
        if (valueNode instanceof StringNode) {
            String value = ((StringNode) valueNode).getValue();
            stringVariables.put(varName, value);
        } else if (valueNode instanceof NumberNode) {
            int value = ((NumberNode) valueNode).getValue();
            variables.put(varName, value);
        } else {
            throw new RuntimeException("Unsupported variable type");
        }
        return 0; // Return value doesn't matter for variable declaration
    }

    private int interpretReturnNode(ReturnNode node) {
        return interpretNode(node.getValue());
    }

    private int interpretFunctionDefinitionNode(FunctionDefinitionNode node) {
        functions.put(node.getFunctionName(), node);
        return 0;
    }

    private int interpretVariableNode(VariableNode node) {
        String varName = node.getName();
        if (variables.containsKey(varName)) {
            return variables.get(varName);
        } else if (stringVariables.containsKey(varName)) {
            return 1; // String variables are considered as having a value of 1 for now
        }
        throw new RuntimeException("Undefined variable: " + varName);
    }

    private String processEscapeSequences(String value) {
        return value.replace("_n", "\n").replace("_t", "\t").replace("_w", " ");
    }
}
