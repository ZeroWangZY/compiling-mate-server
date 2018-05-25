package ecnu.compiling.compilingmate.entity;

import java.util.Arrays;
import java.util.Objects;

public class ProductionLR0 extends Production {
    public int pos;
    public ProductionLR0(String left, String[] right,int pos) {
        super(left,right);
        this.pos=pos;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProductionLR0 other=(ProductionLR0) obj;
        if(!other.left.equals(left))
            return false;
        else if(other.pos!=this.pos)
            return false;
        else if(!Arrays.equals(other.right,right)){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode(){
        return Objects.hash(left,right,pos);
    }

    @Override
    public String toString(){
        String s=left+"->";
        for(int i=0;i<right.length;i++){
            s+=right[i];
        }
        return s;
    }

}
