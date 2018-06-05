package ecnu.compiling.compilingmate.service;

import ecnu.compiling.compilingmate.synEntity.Node;
import ecnu.compiling.compilingmate.synEntity.ParseTable;
import ecnu.compiling.compilingmate.synEntity.TreeStep;
import ecnu.compiling.compilingmate.syntax.analyzer.LRParser;
import ecnu.compiling.compilingmate.syntax.analyzer.SLRParser;
import ecnu.compiling.compilingmate.syntax.entity.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("syntaxParsingService")
public class SyntaxParsingServiceImpl implements SyntaxParsingService{
    @Override
    public Map<String,Object> getSlrParsingOutput(int type){ //0:slr,1:lr
        //入参
//        java.util.List<Production> productions = new ArrayList<>();
//        java.util.List<String> t=Arrays.asList("id","+","*","(",")","$");
//        List<String> nt=Arrays.asList("E","T","F");
//        productions.add(new Production("E'", new String[]{"E"}));
//        productions.add(new Production("E", new String[]{"E", "+", "T"}));
//        productions.add(new Production("E", new String[]{"T"}));
//        productions.add(new Production("T", new String[]{"T", "*", "F"}));
//        productions.add(new Production("T", new String[]{"F"}));
//        productions.add(new Production("F", new String[]{"(", "E", ")"}));
//        productions.add(new Production("F", new String[]{"id"}));
//        String startSymbol="E'";

        java.util.List<Production> productions = new ArrayList<>();
        List<String> nt=Arrays.asList(new String[]{"S","L","R"});
        List<String>  t=Arrays.asList(new String[]{"=","*","id","$"});

        productions.add(new Production("S'",new String[]{"S"}));
        productions.add(new Production("S",new String[]{"L","=","R"}));
        productions.add(new Production("S",new String[]{"R"}));
        productions.add(new Production("L",new String[]{"*","R"}));
        productions.add(new Production("L",new String[]{"id"}));
        productions.add(new Production("R",new String[]{"L"}));

        String startSymbol="S'";


        Map<String,Object> data=new HashMap<>();
        List<Goto> gotoList=new ArrayList<>();
        Map<String,Object> resultMap;
        ParsingTable parsingTable;
        if(type==0) {
            resultMap = new SLRParser().parse(productions, nt, t, startSymbol, gotoList);
            parsingTable = (ParsingTable) resultMap.get("parsingTable");
            data.put("treeSteps", getLr0TreeSteps(productions, nt, t, "E'", gotoList, (List<LR0Items>) resultMap.get("itemsList")));
        }
        else{
            resultMap = new LRParser().parse(productions, nt, t, startSymbol, gotoList);
            parsingTable = (ParsingTable) resultMap.get("parsingTable");
            data.put("treeSteps", getLr1TreeSteps(productions, nt, t, "S'", gotoList, (List<LR1Items>) resultMap.get("itemsList")));
        }

        data.put("symbols",parsingTable.getSymbolList().toArray());
        ParseTable parseTable=new ParseTable();
        parseTable.setTable(parsingTable.getTableStrs());
        parseTable.setConflictList(parsingTable.getConflictList());
        data.put("parseTable",parseTable);
        return data;
    }



    public TreeStep[] getLr0TreeSteps(List<Production> productions, List<String> nt, List<String> t,String startSymbol,List<Goto> gotoList, List<LR0Items> lr0ItemsList){
        TreeStep[] treeSteps=new TreeStep[gotoList.size()+1];
        //init I0
        treeSteps[0]=new TreeStep("add",new Node("0",null,null,"0",lr0ItemsList.get(0).getProductionLeft(),lr0ItemsList.get(0).getProductionRight()));

        for(int i=0;i<gotoList.size();i++){
            Node node=new Node(String.valueOf(i)+1,gotoList.get(i),lr0ItemsList.get(gotoList.get(i).getEndIndex()));
            treeSteps[i+1]=new TreeStep("add",node);
        }
        return treeSteps;
    }

    public TreeStep[] getLr1TreeSteps(List<Production> productions, List<String> nt, List<String> t,String startSymbol,List<Goto> gotoList, List<LR1Items> lr1ItemsList){
        TreeStep[] treeSteps=new TreeStep[gotoList.size()+1];
        //init I0
        treeSteps[0]=new TreeStep("add",new Node("0",null,null,"0",lr1ItemsList.get(0).getProductionLeft(),lr1ItemsList.get(0).getProductionRight()));
        for(int i=0;i<gotoList.size();i++){
            Node node=new Node(String.valueOf(i+1),gotoList.get(i),lr1ItemsList.get(gotoList.get(i).getEndIndex()));
            treeSteps[i+1]=new TreeStep("add",node);
        }
        return treeSteps;
    }


}
