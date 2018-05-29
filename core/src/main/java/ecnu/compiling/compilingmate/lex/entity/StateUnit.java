package ecnu.compiling.compilingmate.lex.entity;

import ecnu.compiling.compilingmate.lex.entity.graph.StateGraph;
import ecnu.compiling.compilingmate.lex.entity.tree.StateTreeNode;

public class StateUnit {
    private StateTreeNode stateNode;
    private StateGraph stateGraph;

    public StateTreeNode getStateNode() {
        return stateNode;
    }

    public void setStateNode(StateTreeNode stateNode) {
        this.stateNode = stateNode;
    }

    public StateGraph getStateGraph() {
        return stateGraph;
    }

    public void setStateGraph(StateGraph stateGraph) {
        this.stateGraph = stateGraph;
    }
}
