package ecnu.compiling.compilingmate.lex.entity;

public class LeafNode extends StateNode {
    private Character token;

    public LeafNode(Character token) {
        this.token = token;
    }

    public Character getToken() {
        return token;
    }

    public void setToken(Character token) {
        this.token = token;
    }
}
