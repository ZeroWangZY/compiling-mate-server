package ecnu.compiling.compilingmate.syntax.utils;

import ecnu.compiling.compilingmate.syntax.entity.LR0Items;
import ecnu.compiling.compilingmate.syntax.entity.LR0Item;

import java.util.*;

public class Utils {
    public static void findClosure(LR0Items item, List<String> t, List<LR0Item> allProds){
        for(LR0Item p:item.getClosure().keySet()){
            if(item.getClosure().get(p)<p.getRight().length) {
                findClosure(item, p, t, allProds);
            }
        }
    }

    public static void findClosure(LR0Items item, LR0Item production, List<String> t, List<LR0Item> allProds){
        if(!t.contains(production.getRight()[item.getClosure().get(production)])){  //是非终结符
            for(LR0Item p:allProds){
                if(p.getLeft().equals(production.getRight()[item.getClosure().get(production)]) && !(item.getClosure().keySet().contains(p)&&item.getClosure().get(p)==0)){
                    item.addProduction(new LR0Item(p.getLeft(),p.getRight(),0),0);
                    findClosure(item, p, t, allProds);
                }
            }
        }
    }

    public static List<String> follow(String nt, List<LR0Item> productions, String start, List<String> t){ //未完成
        List<String> list=new ArrayList<>();
        Set set = new HashSet();
        if(nt.equals(start)) list.add("$");
        for(LR0Item p:productions){
            if(Arrays.asList(p.getRight()).contains(nt) && Arrays.asList(p.getRight()).indexOf(nt)<p.getRight().length-1){ //nt不是最后一个,A->Bb, first(b)-->follow(B)
                set.addAll(first(p.getRight()[Arrays.asList(p.getRight()).indexOf(nt)+1],productions,t));
            }
            else if(Arrays.asList(p.getRight()).indexOf(nt)==p.getRight().length-1){ //nt是最后一个，A->xxxB, follow(A)-->follow(B)
                set.addAll(follow(p.getLeft(),productions,start,t));
            }
        }
        list.addAll(set);
        return list;
    }

    public static List<String> first(String symbol, List<LR0Item> productions, List<String> t){
        List<String> first=new ArrayList<>();
        Set set = new  HashSet();
        if(t.contains(symbol)){
            first.add(symbol);
        }
        else {
            for (LR0Item p : productions) {
                if (p.getLeft().equals(symbol) && t.contains(p.getRight()[0])) {
                    set.add(p.getRight()[0]);
                }
            }
        }
        first.addAll(set);
        return first;
    }
}
