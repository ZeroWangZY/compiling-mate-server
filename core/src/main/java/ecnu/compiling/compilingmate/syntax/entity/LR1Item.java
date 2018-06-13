package ecnu.compiling.compilingmate.syntax.entity;

import java.util.Arrays;
import java.util.Objects;

public class LR1Item extends LR0Item{
    protected String[] lookHead;
    public LR1Item(String left, String[] right, int pos,String[] lookHead) {
        super(left, right, pos);
        this.lookHead=lookHead;
    }

    public LR1Item(Production p,int pos,String[] lookHead) {
        super(p,pos);
        this.lookHead=lookHead;
    }

    @Override
    public String getRightStr(){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(super.getRightStr());
        stringBuilder.append(",");
        for(String l:lookHead){
            stringBuilder.append(l);
            stringBuilder.append("/");
        }
        return stringBuilder.substring(0,stringBuilder.length()-1);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LR1Item other=(LR1Item) obj;
        if(!other.left.equals(left))
            return false;
        else if(other.pos!=this.pos)
            return false;
        else if(!Arrays.equals(other.right,right)){
            return false;
        }
        else if(other.lookHead.length!=lookHead.length){
            return false;
        }
        else {
            for(String s:other.lookHead){
                if(!Arrays.asList(lookHead).contains(s)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode(){
        return Objects.hash(left,right,pos,lookHead);
    }

    @Override
    public String toString(){
        String s=super.toString()+",";
        for(String l:lookHead){
            s+=l;
        }
        return s;
    }

    public String[] getLookHead() {
        return lookHead;
    }

    public void setLookHead(String[] lookHead){
        this.lookHead=lookHead;
    }
}
