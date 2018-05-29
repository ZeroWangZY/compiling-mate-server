package ecnu.compiling.compilingmate.lex.dto;

import ecnu.compiling.compilingmate.lex.entity.tree.StateTreeNode;
import ecnu.compiling.compilingmate.lex.entity.graph.StateGraph;

import java.util.List;
import java.util.Map;

/**
 * 用于和前端交互的实体
 */
public class ReToNfaDto {
    /**
     * 树状图根节点
     */
    private StateTreeNode root;
    /**
     * 节点编号和对应图的映射关系
     */
    private List<StateGraph> graphs;

    public ReToNfaDto(StateTreeNode root, List<StateGraph> graphs) {
        this.root = root;
        this.graphs = graphs;
    }

    public StateTreeNode getRoot() {
        return root;
    }

    public void setRoot(StateTreeNode root) {
        this.root = root;
    }

    public List<StateGraph> getGraphs() {
        return graphs;
    }

    public void setGraphs(List<StateGraph> graphs) {
        this.graphs = graphs;
    }
}
