package ecnu.compiling.compilingmate.lex.entity;

import java.util.ArrayList;
import java.util.List;

public class BranchNode extends StateNode {
    private Integer refId;
    private List<StateNode> children;

    public BranchNode(){
        this.children = new ArrayList<>();
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public void addChild(StateNode child){
        children.add(child);
    }

    public Integer getId() {
        return refId;
    }
}
