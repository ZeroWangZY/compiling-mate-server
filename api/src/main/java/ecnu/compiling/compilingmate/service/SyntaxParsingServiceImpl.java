package ecnu.compiling.compilingmate.service;

import ecnu.compiling.compilingmate.synEntity.*;
import ecnu.compiling.compilingmate.syntax.analyzer.LL1Parser;
import ecnu.compiling.compilingmate.syntax.analyzer.LRParser;
import ecnu.compiling.compilingmate.syntax.analyzer.SLRParser;
import ecnu.compiling.compilingmate.syntax.entity.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("syntaxParsingService")
public class SyntaxParsingServiceImpl implements SyntaxParsingService{
    @Override
    public Map<String,Object> getParsingOutput(RequestDto requestDto){ //0:slr,1:lr
        //入参
//        java.util.List<Production> productions = new ArrayList<>();



        ProductionList productionList=new ProductionList(requestDto.getProductions(),requestDto.getStartSymbol());
        List<Production> productions=productionList.getProductions();
        List<String> t=productionList.getTs();
        List<String> nt=productionList.getNts();

        String startSymbol=requestDto.getStartSymbol()+"'";


        Map<String,Object> data=new HashMap<>();
        List<Goto> gotoList=new ArrayList<>();
        Map<String,Object> resultMap;
        ParsingTable parsingTable;
        if(requestDto.getType()==0) {
            resultMap = new SLRParser().parse(productions, nt, t, startSymbol, gotoList);
            parsingTable = (ParsingTable) resultMap.get("parsingTable");
            data.put("treeSteps", getLr0TreeSteps(productions, nt, t, startSymbol, gotoList, (List<LR0Items>) resultMap.get("itemsList")));
        }
        else{
            resultMap = new LRParser().parse(productions, nt, t, startSymbol, gotoList);
            parsingTable = (ParsingTable) resultMap.get("parsingTable");
            data.put("treeSteps", getLr1TreeSteps(productions, nt, t, startSymbol, gotoList, (List<LR1Items>) resultMap.get("itemsList")));
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
            Node node=new Node(String.valueOf(i+1),gotoList.get(i),lr0ItemsList.get(gotoList.get(i).getEndIndex()));
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


    @Override
    public List<Production> productionProcess(List<ProductionDto> productionDtos,String startSymbol){
        List<Production> productions=new ArrayList<>();
        productions.add(new Production(startSymbol+"'",new String[]{startSymbol}));
        for(ProductionDto productionDto:productionDtos){
            productions.add(new Production(productionDto.getLeft(),productionDto.getRightStrs()));
        }
        return productions;
    }

    @Override
    public Map<String,Object> getActionOutput(ActionRequestDto actionRequestDto){
        Map<String,Object> data=new HashMap<>();
        ProductionList productionList=new ProductionList(actionRequestDto.getProductions(),actionRequestDto.getStartSymbol());

        String[][] actionResult=new SLRParser().searchTable(productionList.getProductions(),
                actionRequestDto.getInput().split("\\s"),actionRequestDto.getTable(),Arrays.asList(actionRequestDto.getSymbols()));
        data.put("actionResult",actionResult);
        return data;
    }


    public Map<String,Object> getParsingLL1Output(String startSymbol, List<Map<String,String>> productions, String type){
        Map<String,Object> data=new HashMap<>();
        try {
            LL1Parser parser = new LL1Parser(startSymbol,productions);
            List<String> terminals=parser.getTerminals();
            List<String> nonTerminals=parser.getNonterminals();
            Map<String,List<String>> firstSet=parser.getFirstSet();
            Map<String,List<String>> followSet=parser.getFollowSet();
            List<List<Map<String,Object>>> parseTable=parser.getParseTable();
            List<String> rowIndex=parser.getRowIndex();
            List<String> colIndex=parser.getColIndex();
            data.put("firsts",firstSet);
            data.put("follows",followSet);
            data.put("terminals",terminals);
            data.put("nonTerminals",nonTerminals);
            data.put("parseTable",parseTable);
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }
}
