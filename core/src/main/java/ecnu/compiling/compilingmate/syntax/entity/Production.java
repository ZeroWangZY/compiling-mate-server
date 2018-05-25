package ecnu.compiling.compilingmate.syntax.entity;

import java.util.Arrays;
import java.util.Objects;

public class Production {
    protected String left;
    protected String[] right;

    public Production(String left, String[] right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        Production other=(Production)obj;
        if(!other.left.equals(left))
            return false;
        else if(!Arrays.equals(other.right,right)){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode(){
        return Objects.hash(left,right);
    }

    @Override
    public String toString(){
        String s=left+"->";
        for(int i=0;i<right.length;i++){
            s+=right[i];
        }
        return s;
    }


    public String getLeft() {
        return left;
    }

    public String[] getRight() {
        return right;
    }

}
