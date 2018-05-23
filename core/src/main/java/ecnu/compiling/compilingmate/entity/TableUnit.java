package ecnu.compiling.compilingmate.entity;

public class TableUnit {
    public int number;
    public int type; // 0:r, 1:s, 2:acc, -1:未设置
    public TableUnit(){
        number=0;
        type=-1;
    }
}
