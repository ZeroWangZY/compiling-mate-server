package ecnu.compiling.compilingmate.syntax;

import ecnu.compiling.compilingmate.syntax.analyzer.SLRParser;
import ecnu.compiling.compilingmate.syntax.entity.*;
import org.junit.Test;

import java.util.*;

import static ecnu.compiling.compilingmate.syntax.utils.Utils.first;
import static ecnu.compiling.compilingmate.syntax.utils.Utils.getFollowSet;

public class SLRParserTest {
    SLRParser slrParser=new SLRParser();
    @Test
    public void accept(){
//        String[] input = {"id","*","id","+","id"};
        java.util.List<Production> productions = new ArrayList<>();
        java.util.List<String> t=Arrays.asList("id","+","*","(",")","$");
        List<String> nt=Arrays.asList("E","T","F");
        //初始化productions,添加E'->E
        productions.add(new Production("E'", new String[]{"E"}));
        productions.add(new Production("E", new String[]{"E", "+", "T"}));
        productions.add(new Production("E", new String[]{"T"}));
        productions.add(new Production("T", new String[]{"T", "*", "F"}));
        productions.add(new Production("T", new String[]{"F"}));
        productions.add(new Production("F", new String[]{"(", "E", ")"}));
        productions.add(new Production("F", new String[]{"id"}));

        List<Goto> gotoList=new ArrayList<>();

        List<LR0Items> itemsList=slrParser.constructItems(productions,nt,t,"E'",gotoList);
//
//        Map<String,List<String>> followSet=getFollowSet(productions,"S'",Arrays.asList(nt),Arrays.asList(t));
//        for(Map.Entry<String,List<String>> entry:followSet.entrySet()){
//            System.out.println("follow("+entry.getKey()+"):"+entry.getValue());
//        }

//        List<String> firstS=first("S",productions,Arrays.asList(t));
//        System.out.println(firstS);
//        List<String> firstL=first("L",productions,Arrays.asList(t));
//        System.out.println(firstL);
//        List<String> firstR=first("R",productions,Arrays.asList(t));
//        System.out.println(firstR);
//        Map<String,Set> followSets=getFollowSet(productions,"S'",nt,t);
//
//        for(Map.Entry<String,Set> entry:followSets.entrySet()){
//            System.out.println(entry.getKey()+":"+entry.getValue());
    }

    @Test
    public void notSlr(){
        java.util.List<Production> productions = new ArrayList<>();

        productions.add(new Production("S'",new String[]{"S"}));
        productions.add(new Production("S",new String[]{"L","=","R"}));
        productions.add(new Production("S",new String[]{"R"}));
        productions.add(new Production("L",new String[]{"*","R"}));
        productions.add(new Production("L",new String[]{"id"}));
        productions.add(new Production("R",new String[]{"L"}));

        List<String> nt=Arrays.asList(new String[]{"S","L","R"});
        List<String>  t=Arrays.asList(new String[]{"=","*","id","$"});

        List<Goto> gotoList=new ArrayList<>();
        List<LR0Items> itemsList=slrParser.constructItems(productions,nt,t,"S'",gotoList);
    }

    @Test
    public void parseTest(){
        java.util.List<Production> productions = new ArrayList<>();
        java.util.List<String> t=Arrays.asList("id","+","*","(",")","$");
        List<String> nt=Arrays.asList("E","T","F");
        //初始化productions,添加E'->E
        productions.add(new Production("E'", new String[]{"E"}));
        productions.add(new Production("E", new String[]{"E", "+", "T"}));
        productions.add(new Production("E", new String[]{"T"}));
        productions.add(new Production("T", new String[]{"T", "*", "F"}));
        productions.add(new Production("T", new String[]{"F"}));
        productions.add(new Production("F", new String[]{"(", "E", ")"}));
        productions.add(new Production("F", new String[]{"id"}));

        List<Goto> gotoList=new ArrayList<>();


       slrParser.parse(productions,nt,t,"E'",gotoList);
    }

    @Test
    public void inputProcessing(){
        java.util.List<Production> productions = new ArrayList<>();
//        java.util.List<String> t=Arrays.asList("id","+","*","(",")","$");
//        List<String> nt=Arrays.asList("E","T","F");
        //初始化productions,添加E'->E
        //productions.add(new Production("E'", new String[]{"E"}));
        productions.add(new Production("E", new String[]{"E", "+", "T"}));
        productions.add(new Production("E", new String[]{"T"}));
        productions.add(new Production("T", new String[]{"T", "*", "F"}));
        productions.add(new Production("T", new String[]{"F"}));
        productions.add(new Production("F", new String[]{"(", "E", ")"}));
        productions.add(new Production("F", new String[]{"id"}));
        String start="E";

        List<String> nts=new ArrayList<>();
        List<String> ts=new ArrayList<>();
        for(Production p:productions){
            if(!nts.contains(p.getLeft())){
                nts.add(p.getLeft());
            }
        }
        for(Production p:productions){
            for(String s:p.getRight()){
                if(!nts.contains(s)){
                    ts.add(s);
                }
            }
        }
        ts.add("$");
        System.out.println(ts);
        System.out.println(nts);
    }



}
