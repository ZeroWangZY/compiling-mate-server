package ecnu.compiling.compilingmate.syntax.entity;

public class TableUnit {
    protected int number;
    protected int type; // 0:r, 1:s, 2:acc, 3:conflicted, -1:未设置
    public TableUnit(){
        number=0;
        type=-1;
    }

    public TableUnit(String actionStr){
        number=0;
        type=-1;
        if(actionStr.length()!=0) {
            if (actionStr.equals("acc")) {
                type = 2;
            } else if (actionStr.substring(0, 1).equals("s")) {
                type = 1;
                number = Integer.valueOf(actionStr.substring(1));
            } else if (actionStr.substring(0, 1).equals("r")) {
                type = 0;
                number = Integer.valueOf(actionStr.substring(1));
            }
            else{
                type=1;
                number=Integer.valueOf(actionStr);
            }
        }
    }
    public int getNumber() {
        return number;
    }

    public int getType() {
        return type;
    }
}
