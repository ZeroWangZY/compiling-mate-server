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
                        && !(items.getClosure().keySet().contains(lr0Item) // I中不包含该产生式
                        && items.getClosure().get(lr0Item)==0) //
                        ){
                    items.addProduction(lr0Item,0);
                    findClosure(items, lr0Item, t, allProds);
                }
            }
        }
    }

    public static Map<String, Set> getFollowSet(List<Production> productions, String start, List<String> ntList, List<String> t){
        Map<String, Set> followSet=new HashMap<>();
        for(String nt:ntList) {
            Set ntFollows;
            if (!followSet.containsKey(nt)) {
                followSet.put(nt, new HashSet());
            }
            ntFollows = followSet.get(nt);


            if (nt.equals(start)) ntFollows.add("$");
            else {
                List<String> nts=new ArrayList<>();
                for (Production p : productions) {
                    if (Arrays.asList(p.getRight()).contains(nt) && Arrays.asList(p.getRight()).indexOf(nt) < p.getRight().length - 1) { //nt不是最后一个,A->Bb, first(b)-->follow(B)
                        ntFollows.addAll(first(p.getRight()[Arrays.asList(p.getRight()).indexOf(nt) + 1], productions, t));
                    } else if (Arrays.asList(p.getRight()).indexOf(nt) == p.getRight().length - 1 && p.getLeft().equals(start)) { //nt是最后一个，S->xxxB, $加入followB
                        ntFollows.add("$");
                    } else if (p.getLeft().equals(nt) && ntList.contains(p.getRight()[p.getRight().length - 1])) { //nt是最后一个，B->xxxC, follow(B)加入follow(C)
                        nts.add(p.getRight()[p.getRight().length - 1]);
                    }
                }
                for(String fnt:nts){
                    if (!followSet.containsKey(fnt)) {
                        followSet.put(fnt, new HashSet());
                    }
                    followSet.get(fnt).addAll(ntFollows);
                }
            }
        }
        return followSet;
    }


//    public static List<String> follow(Map<String, List<String>> followSet,String nt,List<Production> productions, String start, List<String> t){
//        if(followSet.containsKey(nt)){
//            return followSet.get(nt);
//        }
//        else{
//            List<String> list=new ArrayList<>();
//            Set set = new HashSet();
//            if(nt.equals(start)) list.add("$");
//            for(Production p:productions){
//                if(Arrays.asList(p.getRight()).contains(nt) && Arrays.asList(p.getRight()).indexOf(nt)<p.getRight().length-1){ //nt不是最后一个,A->Bb, first(b)-->follow(B)
//                    set.addAll(first(p.getRight()[Arrays.asList(p.getRight()).indexOf(nt)+1],productions,t));
//                }
//                else if(Arrays.asList(p.getRight()).indexOf(nt)==p.getRight().length-1){ //nt是最后一个，A->xxxB, follow(A)-->follow(B)
//                    if(followSet.containsKey(p.getLeft())){
//                        set.addAll(followSet.get(p.getLeft()));
//                    }
//                    else {
//                        set.addAll(follow(followSet, p.getLeft(), productions, start, t));
//                    }
//                }
//            }
//            list.addAll(set);
//            followSet.put(nt,list);
//            return list;
//        }
//    }
//    public static List<String> follow(String nt, List<Production> productions, String start, List<String> t){ //未完成
//        List<String> list=new ArrayList<>();
//        Set set = new HashSet();
//        if(nt.equals(start)) list.add("$");
//        for(Production p:productions){
//            if(Arrays.asList(p.getRight()).contains(nt) && Arrays.asList(p.getRight()).indexOf(nt)<p.getRight().length-1){ //nt不是最后一个,A->Bb, first(b)-->follow(B)
//                set.addAll(first(p.getRight()[Arrays.asList(p.getRight()).indexOf(nt)+1],productions,t));
//            }
//            else if(Arrays.asList(p.getRight()).indexOf(nt)==p.getRight().length-1){ //nt是最后一个，A->xxxB, follow(A)-->follow(B)
//                set.addAll(follow(p.getLeft(),productions,start,t));
//            }
//        }
//        list.addAll(set);
//        return list;
//    }

    public static List<String> first(String symbol, List<Production> productions, List<String> t){
        List<String> first=new ArrayList<>();
        Set set = new  HashSet();
        if(t.contains(symbol)){  //first(t)={t}
            first.add(symbol);
        }
        else {  //first(nt)
            for (Production p : productions) {
                if (p.getLeft().equals(symbol)){
                    if(t.contains(p.getRight()[0])){
                        set.add(p.getRight()[0]);
                    }
                    else{
                        set.addAll(first(p.getRight()[0],productions,t));
                    }
                }
            }
        }
        first.addAll(set);
        return first;
    }
}
