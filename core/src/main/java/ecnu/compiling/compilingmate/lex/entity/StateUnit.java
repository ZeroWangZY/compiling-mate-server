package ecnu.compiling.compilingmate.lex.entity;

public class StateUnit {
    private StateNode stateNode;
    private StateGraph stateGraph;

    public StateNode getStateNode() {
        return stateNode;
    }

    public void setStateNode(StateNode stateNode) {
        this.stateNode = stateNode;
    }

    public StateGraph getStateGraph() {
        return stateGraph;
    }

    public void setStateGraph(StateGraph stateGraph) {
        this.stateGraph = stateGraph;
    }
}
