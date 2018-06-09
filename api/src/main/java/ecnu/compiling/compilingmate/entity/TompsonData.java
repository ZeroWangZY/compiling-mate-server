package ecnu.compiling.compilingmate.entity;

import ecnu.compiling.compilingmate.lex.entity.tree.BranchNode;
import ecnu.compiling.compilingmate.lex.entity.tree.StateTreeNode;

import java.util.Map;

public class TompsonData {
    private StateTreeNode reTree;

    private Map<Integer, Graph> nfaMap;

    public StateTreeNode getReTree() {
        return reTree;
    }

    public void setReTree(StateTreeNode reTree) {
        this.reTree = reTree;
    }

    public Map<Integer, Graph> getNfaMap() {
        return nfaMap;
    }

    public void setNfaMap(Map<Integer, Graph> nfaMap) {
        this.nfaMap = nfaMap;
    }
}
