package ecnu.compiling.compilingmate.lex.entity.tree;

import java.util.ArrayList;
import java.util.List;

public class BranchNode extends StateTreeNode {
    private List<StateTreeNode> children;

    public BranchNode(Integer id){
        super(id);
        this.children = new ArrayList<>();
    }

    public void addChild(StateTreeNode child){
        children.add(child);
    }

}
