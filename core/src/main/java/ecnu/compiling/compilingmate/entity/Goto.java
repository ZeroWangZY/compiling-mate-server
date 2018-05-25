package ecnu.compiling.compilingmate.entity;

public class Goto {
    public int beginIndex;
    public int endIndex;
    public String x;

    public Goto(int beginIndex, int endIndex, String x) {
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        this.x = x;
    }


    @Override
    public String toString(){
        return "Item"+beginIndex+"--"+x+"->"+endIndex;
    }

}
