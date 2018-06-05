package ecnu.compiling.compilingmate.lex.entity;

public class Token implements Cloneable{

    private String content;

    private TokenKind tokenKind;

    public Token(String content) {
        this.content = content;
        this.tokenKind = TokenKind.UNKNOWN;
    }

    public Token(String content, TokenKind tokenKind) {
        this.content = content;
        this.tokenKind = tokenKind;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TokenKind getTokenKind() {
        return tokenKind;
    }

    public void setTokenKind(TokenKind tokenKind) {
        this.tokenKind = tokenKind;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Token){
            Token other = (Token) obj;
            return this.content.equals(other.content);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.content.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s [Type: %s]", content, tokenKind.name());
    }

    @Override
    public Token clone(){
        try {
            return (Token) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
