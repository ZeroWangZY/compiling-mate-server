package ecnu.compiling.compilingmate.lex.entity.tree;

import ecnu.compiling.compilingmate.lex.entity.Token;

public class LeafNode extends StateTreeNode {

    public LeafNode(Token token) {
        super(-1, token);
    }
}
