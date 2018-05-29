package ecnu.compiling.compilingmate.lex.entity.graph;

import ecnu.compiling.compilingmate.lex.entity.Token;

public class Edge implements Cloneable{
    private Integer from;
    private Integer to;
    private Token tag;

    public Edge(Integer from, Integer to, Token tag) {
        this.from = from;
        this.to = to;
        this.tag = tag;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Token getTag() {
        return tag;
    }

    public void setTag(Token tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge){
            Edge other = (Edge) obj;
            return from.equals(other.from) &&
                    to.equals(other.to) &&
                    tag.equals(other.tag);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return tag.hashCode() + from + to;
    }

    @Override
    public Edge clone(){
        Edge copy = null;
        try {
            copy = (Edge) super.clone();
            copy.tag = this.tag.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return copy;
    }
}
