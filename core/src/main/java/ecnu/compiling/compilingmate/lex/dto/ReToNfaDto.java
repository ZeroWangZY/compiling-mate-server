package ecnu.compiling.compilingmate.lex.dto;

import ecnu.compiling.compilingmate.lex.entity.StateNode;
import ecnu.compiling.compilingmate.lex.entity.StateGraph;

import java.util.Map;

/**
 * 用于和前端交互的实体
 */
public class ReToNfaDto {
    /**
     * 树状图根节点
     */
    private StateNode root;
    /**
     * 节点编号和对应图的映射关系
     */
    private Map<Integer, StateGraph> graphMap;

    public ReToNfaDto() {
    }

    public ReToNfaDto(StateNode root, Map<Integer, StateGraph> graphMap) {
        this.root = root;
        this.graphMap = graphMap;
    }

    public StateNode getRoot() {
        return root;
    }

    public void setRoot(StateNode root) {
        this.root = root;
    }

    public Map<Integer, StateGraph> getGraphMap() {
        return graphMap;
    }

    public void setGraphMap(Map<Integer, StateGraph> graphMap) {
        this.graphMap = graphMap;
    }
}
