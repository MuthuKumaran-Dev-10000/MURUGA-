import java.util.HashMap;
import java.util.Map;

class Interpreter {
    private Node root;
    private Map<String, Integer> variables;

    public Interpreter(Node root) {
        this.root = root;
        this.variables = new HashMap<>();
    }

    public int interpret() {
        return evaluate(root);
    }

    private int evaluate(Node node) {
        if (node instanceof NumberNode) {
            return ((NumberNode) node).getValue();
        } else if (node instanceof VariableNode) {
            String name = ((VariableNode) node).getName();
            if (variables.containsKey(name)) {
                return variables.get(name);
            } else {
                throw new RuntimeException("Undefined variable: " + name);
            }
        } else if (node instanceof BinaryOperationNode) {
            BinaryOperationNode binOp = (BinaryOperationNode) node;
            int left = evaluate(binOp.getLeft());
            int right = evaluate(binOp.getRight());
            switch (binOp.getOperator()) {
                case "+":
                    return left + right;
                case "-":
                    return left - right;
                case "*":
                    return left * right;
                case "/":
                    return left / right;
                default:
                    throw new RuntimeException("Unknown operator: " + binOp.getOperator());
            }
        } else {
            throw new RuntimeException("Unknown node: " + node);
        }
    }
}
