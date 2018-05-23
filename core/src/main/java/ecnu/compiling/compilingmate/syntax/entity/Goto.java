package ecnu.compiling.compilingmate.syntax.entity;

/**
 * goto(I.X)关系类
 */
public class Goto {
    private int beginIndex;
    private int endIndex;
    private String x;

    public Goto(int beginIndex, int endIndex, String x) {
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        this.x = x;
    }

    @Override
    public String toString(){
        return "Item"+beginIndex+"--"+x+"->"+endIndex;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public String getX() {
        return x;
    }

}
