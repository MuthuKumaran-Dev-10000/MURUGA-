package withrules;

import java.util.HashMap;
import java.util.Map;

public class Interpreter {
    private final Node ast;
    private final Map<String, Object> variables = new HashMap<>();
    private final Map<String, FunctionDefinitionNode> functions = new HashMap<>();
    private final Map<String, String> stringVariables = new HashMap<>();

    public Interpreter(Node ast) {
        this.ast = ast;
    }

    public Object interpret() {
        return interpretNode(ast);
    }

    private Object interpretNode(Node node) {
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
            return ((StringNode) node).getValue();
        } else if (node instanceof BinaryOperationNode) {
            BinaryOperationNode binaryOperation = (BinaryOperationNode) node;
            if (binaryOperation.getOperator().equals("*")) {
                Node left = binaryOperation.getLeft();
                Node right = binaryOperation.getRight();
                Object leftValue = interpretNode(left);
                Object rightValue = interpretNode(right);
                
                // Handle multiplication with string repetition
                if (leftValue instanceof String && rightValue instanceof Integer) {
                    String value = (String) leftValue;
                    int repeat = (int) rightValue;
                    value = processEscapeSequences(value); // Process escape sequences first
                    StringBuilder repeatedValue = new StringBuilder();
                    for (int i = 0; i < repeat; i++) {
                        repeatedValue.append(value);
                    }
                    return repeatedValue.toString();
                } else if (leftValue instanceof Integer && rightValue instanceof Integer) {
                    // Perform integer multiplication
                    int leftInt = (int) leftValue;
                    int rightInt = (int) rightValue;
                    return leftInt * rightInt;
                } else {
                    throw new RuntimeException("Unsupported operation: " + leftValue.getClass().getSimpleName() + " * " + rightValue.getClass().getSimpleName());
                }
            } else {
                int left = (int) interpretNode(binaryOperation.getLeft()); // Cast to int
                int right = (int) interpretNode(binaryOperation.getRight()); // Cast to int
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

    private Object interpretBlockNode(BlockNode node) {
        Object result = "";
        for (Node statement : node.getStatements()) {
            result = interpretNode(statement);
        }
        return result;
    }

    private Object interpretForLoopNode(ForLoopNode node) {
        int start = (int) interpretNode(node.getStart());
        int end = (int) interpretNode(node.getEnd());
        Object result = "";
        for (int i = start; i <= end; i++) {
            result = interpretNode(node.getBody());
        }
        return result;
    }

    private Object interpretPrintNode(PrintNode node) {
        Node valueNode = node.getValue();
        if (valueNode instanceof StringNode) {
            String value = (String) interpretNode(valueNode);
            value = processEscapeSequences(value);
            System.out.print(value);
            return ""; // Return empty string for print operation
        } else {
            Object value = interpretNode(valueNode);
            System.out.print(value);
            return ""; // Return empty string for print operation
        }
    }

    private Object interpretVariableDeclarationNode(VariableDeclarationNode node) {
        String varName = node.getVarName();
        Node valueNode = node.getValue();

        if (valueNode instanceof NumberNode) {
            int value = ((NumberNode) valueNode).getValue();
            variables.put(varName, value);
        } else if (valueNode instanceof StringNode) {
            String value = ((StringNode) valueNode).getValue();
            value = processEscapeSequences(value); // Process escape sequences for string variables
            variables.put(varName, value);
        } else if (valueNode instanceof VariableNode) {
            String referencedVar = ((VariableNode) valueNode).getName();
            if (variables.containsKey(referencedVar)) {
                Object value = variables.get(referencedVar);
                variables.put(varName, value);
            } else if (stringVariables.containsKey(referencedVar)) {
                String value = stringVariables.get(referencedVar);
                stringVariables.put(varName, value);
            } else {
                throw new RuntimeException("Undefined variable: " + referencedVar);
            }
        } else {
            throw new RuntimeException("Unsupported variable type");
        }

        return ""; // Return empty string for variable declaration
    }

    private int interpretReturnNode(ReturnNode node) {
        return (int) interpretNode(node.getValue());
    }

    private Object interpretFunctionDefinitionNode(FunctionDefinitionNode node) {
        functions.put(node.getFunctionName(), node);
        return ""; // Return empty string for function definition
    }

    private Object interpretVariableNode(VariableNode node) {
        String varName = node.getName();
        if (variables.containsKey(varName)) {
            Object value = variables.get(varName);
            return value;
        } else if (stringVariables.containsKey(varName)) {
            String value = stringVariables.get(varName);
            return value;
        } else {
            throw new RuntimeException("Undefined variable: " + varName);
        }
    }

    private String processEscapeSequences(String value) {
        return value.replace("_n", "\n").replace("_t", "\t").replace("_w", " ");
    }
}
