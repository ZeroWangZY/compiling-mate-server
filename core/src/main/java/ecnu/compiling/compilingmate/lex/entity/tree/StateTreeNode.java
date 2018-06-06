package ecnu.compiling.compilingmate.lex.entity.tree;

import ecnu.compiling.compilingmate.lex.entity.token.Token;

public abstract class StateTreeNode {

    private Integer id;

    private Token token;

    public StateTreeNode(Integer id, Token token) {
        this.id = id;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
