package ecnu.compiling.compilingmate.lex.entity.tree;

import ecnu.compiling.compilingmate.lex.entity.Token;

public class LeafNode extends StateTreeNode {
    private Token token;
    public LeafNode(Token token) {
        super(-1);
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
