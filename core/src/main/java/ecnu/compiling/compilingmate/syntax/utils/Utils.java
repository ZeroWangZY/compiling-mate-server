package ecnu.compiling.compilingmate.syntax.utils;

import ecnu.compiling.compilingmate.syntax.entity.LR0Items;
import ecnu.compiling.compilingmate.syntax.entity.LR0Item;
import ecnu.compiling.compilingmate.syntax.entity.Production;

import java.util.*;

public class Utils {

    public static void findClosure(LR0Items items, List<String> t, List<Production> allProds){
        for(LR0Item p:items.getClosure().keySet()){
            if(items.getClosure().get(p)<p.getRight().length) {
                findClosure(items, p, t, allProds);
            }
        }
    }

    public static void findClosure(LR0Items items, LR0Item production, List<String> t, List<Production> allProds){
        if(!t.contains(production.getRight()[items.getClosure().get(production)])){  //是非终结符
            for(Production p:allProds){
                LR0Item lr0Item=new LR0Item(p,0);
                if(p.getLeft().equals(production.getRight()[items.getClosure().get(production)]) // 是从B开始的产生式
                        && !(items.getClosure().keySet().contains(new LR0Item(p,0)) // I中不包含该产生式
                        && items.getClosure().get(new LR0Item(p,0))==0) //
                        ){
                    items.addProduction(new LR0Item(p,0),0);
                    findClosure(items, new LR0Item(p,0), t, allProds);
                }
            }
        }
    }

    public static List<String> follow(String nt, List<Production> productions, String start, List<String> t){ //未完成
        List<String> list=new ArrayList<>();
        Set set = new HashSet();
        if(nt.equals(start)) list.add("$");
        for(Production p:productions){
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

    public static List<String> first(String symbol, List<Production> productions, List<String> t){
        List<String> first=new ArrayList<>();
        Set set = new  HashSet();
        if(t.contains(symbol)){
            first.add(symbol);
        }
        else {
            for (Production p : productions) {
                if (p.getLeft().equals(symbol) && t.contains(p.getRight()[0])) {
                    set.add(p.getRight()[0]);
                }
            }
        }
        first.addAll(set);
        return first;
    }
}
