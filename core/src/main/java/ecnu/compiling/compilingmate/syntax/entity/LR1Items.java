package ecnu.compiling.compilingmate.syntax.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LR1Items {
    protected List<LR1Item> closure;

    public LR1Items(){
        closure=new ArrayList<>();
    }
    public void addItem(LR1Item item){
        closure.add(item);
    }

    public List<LR1Item> getClosure() {
        return closure;
    }


    public List<LR1Item> getItemsByLeft(String left){
        List<LR1Item> result=new ArrayList<>();
        for(LR1Item i:closure){
            if(i.getLeft().equals(left)){
                result.add(i);
            }
        }
        return result;
    }


    @Override
    public String toString(){
        return closure.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LR1Items other = (LR1Items) obj;
        List<LR1Item> otherClo=other.getClosure();
        if(otherClo.size()!=closure.size()){
            return false;
        }
        for(LR1Item item : closure){
            if(!otherClo.contains(item)){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode(){
        return Objects.hash(closure);
    }
}
