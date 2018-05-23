package ecnu.compiling.compilingmate.syntax.entity;

public class TableUnit {
    protected int number;
    protected int type; // 0:r, 1:s, 2:acc, -1:未设置
    public TableUnit(){
        number=0;
        type=-1;
    }
    public int getNumber() {
        return number;
    }

    public int getType() {
        return type;
    }
}
