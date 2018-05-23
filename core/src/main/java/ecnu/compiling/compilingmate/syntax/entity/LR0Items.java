package ecnu.compiling.compilingmate.syntax.entity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class LR0Items {
    protected Map<LR0Item,Integer> closure;

    public LR0Items(){
        closure=new LinkedHashMap<>();
        closure=new LinkedHashMap<>();
    }

    public void addProduction(LR0Item p, int pos){
        closure.put(p,pos);
    }

    public Map<LR0Item,Integer> getClosure(){
        return closure;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LR0Items other = (LR0Items) obj;
        Map<LR0Item,Integer> otherClo=other.getClosure();
        if(otherClo.size()!=closure.size()){
            return false;
        }
        for(Map.Entry<LR0Item,Integer> entry : closure.entrySet()){
            if(!otherClo.containsKey(entry.getKey())){
                return false;
            }
            else if(otherClo.get(entry.getKey()) != entry.getValue()){
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
