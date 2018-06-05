package ecnu.compiling.compilingmate.lex.entity.tree;

import ecnu.compiling.compilingmate.lex.entity.Token;

import java.util.ArrayList;
import java.util.List;

public class TokenizedPhrase {
    private Integer seqNo;

    private List<Token> tokens;

    public TokenizedPhrase(Integer seqNo) {
        this.seqNo = seqNo;
        this.tokens = new ArrayList<>();
    }

    public TokenizedPhrase(Integer seqNo, List<Token> tokens) {
        this.seqNo = seqNo;
        this.tokens = new ArrayList<>();
        this.tokens.addAll(tokens);
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public void addToken(Token token) {
        this.tokens.add(token);
    }

    public List<Token> getTokens(){
        return this.tokens;
    }
}
