package ecnu.compiling.compilingmate.lex.entity.tree;

import ecnu.compiling.compilingmate.lex.entity.Token;

import java.util.ArrayList;
import java.util.List;

public class BranchNode extends StateTreeNode {
    private List<StateTreeNode> children;

    public BranchNode(Integer id, Token token){
        super(id, token);
        this.children = new ArrayList<>();
    }

    public void addChild(StateTreeNode child){
        children.add(child);
    }

    public List<StateTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<StateTreeNode> children) {
        this.children = children;
    }
}
