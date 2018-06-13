package ecnu.compiling.compilingmate.lex.entity.token;

import ecnu.compiling.compilingmate.lex.entity.token.Token;

import java.util.ArrayList;
import java.util.List;

public class TokenizedPhrase implements Comparable<TokenizedPhrase>{
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

    @Override
    public int compareTo(TokenizedPhrase o) {
        if (this.seqNo == null) return 0;
        if (o.seqNo == null) return 0;
        return this.seqNo = o.seqNo;
    }
}
