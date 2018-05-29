package ecnu.compiling.compilingmate.lex.entity.tree;

public abstract class StateTreeNode {

    private Integer id;

    public StateTreeNode(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
