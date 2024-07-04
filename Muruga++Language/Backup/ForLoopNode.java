package withrules;

public class ForLoopNode implements Node {
    private final VariableNode variable;
    private final Node start;
    private final Node end;
    private final BlockNode body;

    public ForLoopNode(VariableNode variable, Node start, Node end, BlockNode body) {
        this.variable = variable;
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
