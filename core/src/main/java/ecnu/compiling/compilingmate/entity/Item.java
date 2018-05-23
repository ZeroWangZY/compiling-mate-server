package ecnu.compiling.compilingmate.entity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Item {
    Map<ProductionLR0,Integer> closure;
    public Item(){
        closure=new LinkedHashMap<>();
        closure=new LinkedHashMap<>();
    }

    public void addProduction(ProductionLR0 p,int pos){
        closure.put(p,pos);
    }

    public Map<ProductionLR0,Integer> getClosure(){
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
        Item other = (Item) obj;
        Map<ProductionLR0,Integer> otherClo=other.getClosure();
        if(otherClo.size()!=closure.size()){
            return false;
        }
        for(Map.Entry<ProductionLR0,Integer> entry : closure.entrySet()){
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
