package ecnu.compiling.compilingmate.syntax.entity;

import java.util.*;

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

    @Override
    public String toString(){
        return closure.keySet().toString();
    }

    public String[] getProductionLeft(){
        List<String> lefts=new ArrayList<>();
        for(LR0Item item:closure.keySet()){
            lefts.add(item.getLeft());
        }
        return lefts.toArray(new String[lefts.size()]);
    }

    public String[] getProductionRight(){
        List<String> rights=new ArrayList<>();
        for(LR0Item item:closure.keySet()){
            rights.add(item.getLeft());
        }
        return rights.toArray(new String[rights.size()]);
    }
}
