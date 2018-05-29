package ecnu.compiling.compilingmate.lex.entity;

public class Token implements Cloneable{

    private String content;

    public Token(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        return content;
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
