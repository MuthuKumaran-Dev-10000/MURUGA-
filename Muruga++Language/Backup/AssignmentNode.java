package withrules;
import java.util.Objects;

public class AssignmentNode implements Node {
    private VariableNode variable;
    private Node value;

    public AssignmentNode(VariableNode variable, Node value) {
        this.variable = variable;
        this.value = value;
    }

    public VariableNode getVariable() {
        return variable;
    }

    public Node getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "AssignmentNode{" +
                "variable=" + variable +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignmentNode that = (AssignmentNode) o;
        return Objects.equals(variable, that.variable) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable, value);
    }

    
    // public void visit() {
    //     System.out.println("Visiting Assignment Node");
    // }
}
