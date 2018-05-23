package ecnu.compiling.compilingmate.utils;

import ecnu.compiling.compilingmate.entity.Item;
import ecnu.compiling.compilingmate.entity.ProductionLR0;

import java.util.*;

public class Utils {
    public static void findClosure(Item item, List<String> t, List<ProductionLR0> allProds){
        for(ProductionLR0 p:item.getClosure().keySet()){
            if(item.getClosure().get(p)<p.right.length) {
                findClosure(item, p, t, allProds);
            }
        }
    }

    public static void findClosure(Item item,ProductionLR0 production,List<String> t,List<ProductionLR0> allProds){
        if(!t.contains(production.right[item.getClosure().get(production)])){  //是非终结符
            for(ProductionLR0 p:allProds){
                if(p.left.equals(production.right[item.getClosure().get(production)]) && !(item.getClosure().keySet().contains(p)&&item.getClosure().get(p)==0)){
                    item.addProduction(new ProductionLR0(p.left,p.right,0),0);
                    findClosure(item, p, t, allProds);
                }
            }
        }
    }

    public static List<String> follow(String nt,List<ProductionLR0> productions,String start,List<String> t){ //未完成
        List<String> list=new ArrayList<>();
        Set set = new HashSet();
        if(nt.equals(start)) list.add("$");
        for(ProductionLR0 p:productions){
            if(Arrays.asList(p.right).contains(nt) && Arrays.asList(p.right).indexOf(nt)<p.right.length-1){ //nt不是最后一个,A->Bb, first(b)-->follow(B)
                set.addAll(first(p.right[Arrays.asList(p.right).indexOf(nt)+1],productions,t));
            }
            else if(Arrays.asList(p.right).indexOf(nt)==p.right.length-1){ //nt是最后一个，A->xxxB, follow(A)-->follow(B)
                set.addAll(follow(p.left,productions,start,t));
            }
        }
        list.addAll(set);
        return list;
    }

    public static List<String> first(String symbol,List<ProductionLR0> productions, List<String> t){
        List<String> first=new ArrayList<>();
        Set set = new  HashSet();
        if(t.contains(symbol)){
            first.add(symbol);
        }
        else {
            for (ProductionLR0 p : productions) {
                if (p.left.equals(symbol) && t.contains(p.right[0])) {
                    set.add(p.right[0]);
                }
            }
        }
        first.addAll(set);
        return first;
    }
}
