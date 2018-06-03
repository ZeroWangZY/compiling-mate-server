package ecnu.compiling.compilingmate.syntax.analyzer;

import ecnu.compiling.compilingmate.syntax.entity.*;
import ecnu.compiling.compilingmate.syntax.utils.Utils;

import java.util.*;

import static ecnu.compiling.compilingmate.syntax.utils.Utils.getFollowSet;

public class SLRParser implements BottomUpParser<LR0Items>{
    public Map<String,Object> parse(List<Production> productions, List<String> nt, List<String> t,String startSymbol, List<Goto> gotoList){
        List<LR0Items> lr0ItemsList=constructItems(productions,nt,t,startSymbol,gotoList);
        ParsingTable parsingTable=constructParsingTable(t,nt,lr0ItemsList,gotoList,productions,startSymbol);
        parsingTable.printTable();
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("itemsList",lr0ItemsList);
        resultMap.put("parsingTable",parsingTable);
        String[] input = {"id","*","id","+","id"};
        searchTable(productions,input,parsingTable);
        return resultMap;
    }

    @Override
    public List<LR0Items> constructItems(List<Production> productions, List<String> nt, List<String> t,String startSymbol, List<Goto> gotoList) {
        String[] input = {"id","*","id","+","id"};
        //计算items
        List<LR0Items> itemList = new ArrayList<>(); //记录最终items
        //设置初始状态I0
        LR0Items i0=new LR0Items();
        for(Production p:productions){
            i0.addProduction(new LR0Item(p.getLeft(),p.getRight(),0),0);
        }
        itemList.add(i0);
        Queue<LR0Items> waitingItems=new LinkedList<>(); //BFS，用队列记录待寻找closure的项
        waitingItems.offer(i0);

        while(!waitingItems.isEmpty()) {
            LR0Items curItem=waitingItems.poll();
            // 遍历当前items中的Production
            Map<String, LR0Items> temp = new LinkedHashMap<>(); // < NT,从当前item开始的gotoItemTemp >
            for (LR0Item p : curItem.getClosure().keySet()) {
                if(curItem.getClosure().get(p)<p.getRight().length) {
                    if (!temp.containsKey(p.getRight()[curItem.getClosure().get(p)])) {
                        temp.put(p.getRight()[curItem.getClosure().get(p)], new LR0Items());
                    }
                    temp.get(p.getRight()[curItem.getClosure().get(p)]).addProduction(new LR0Item(p.getLeft(),p.getRight(),p.getPos()+1), curItem.getClosure().get(p) + 1);
                }
            }

            // find goto(I,X)，得到当前items可达的items
            for (Map.Entry<String, LR0Items> entry : temp.entrySet()) {
                Utils.findClosure(entry.getValue(), t, productions);
                if (!itemList.contains(entry.getValue())) {
                    itemList.add(entry.getValue());
                    waitingItems.offer(entry.getValue());
                }
                gotoList.add(new Goto(itemList.indexOf(curItem), itemList.indexOf(entry.getValue()), entry.getKey()));
            }
        }
        for(LR0Items lr0Item:itemList){
            System.out.println(lr0Item);
        }
        System.out.println(gotoList);
        return itemList;
    }

    @Override
    public ParsingTable constructParsingTable(List<String> t, List<String> nt, List<LR0Items> itemsList, List<Goto> gotoList, List<Production> productions, String startSymbol) {
        System.out.println("--------------follow集--------------");
        Map<String, Set> followSet = getFollowSet(productions, startSymbol, nt, t);
        for (Map.Entry<String, Set> entry : followSet.entrySet()) {
            System.out.println("follow(" + entry.getKey() + "):" + entry.getValue());
        }

        ParsingTable actionTable = new ParsingTable(t.size() + nt.size(), itemsList.size(), t, nt);
        for (LR0Items item : itemsList) {
            //shift j
            for (Goto gotoUnit : gotoList) {
                if (gotoUnit.getBeginIndex() == itemsList.indexOf(item)) {
                    //conflict判断在setTable中处理
                    actionTable.setTable(gotoUnit.getEndIndex(), 1, itemsList.indexOf(item), gotoUnit.getX());
                }
            }
            //reduce
            for (LR0Item p : item.getClosure().keySet()) {
                if (p.getPos() == p.getRight().length && !p.getLeft().equals(startSymbol)) { // A->a.
                    for (String terminal : t) {
                        if (followSet.get(p.getLeft()).contains(terminal)) {
                            actionTable.setTable(productions.indexOf(new Production(p.getLeft(), p.getRight())), 0, itemsList.indexOf(item), terminal);
                        }
                    }
                }
                //accept
                if (p.getLeft().equals(startSymbol) && p.getRight().length==p.getPos()) {
                    actionTable.setTable(0, 2, itemsList.indexOf(item), "$");
                }
            }
        }
        return actionTable;
    }



    public void searchTable(List<Production> productions,String[] input,ParsingTable actionTable){
        //        System.out.println("---------------search table-----------------");
        Stack<String> stack=new Stack<>();
        String[] myInput=new String[input.length+1];
        System.arraycopy(input,0,myInput,0,input.length);
        myInput[input.length]="$";
        stack.push("0");
        int pointer=0; //input光标
        while(true){
            TableUnit action=actionTable.getUnit(myInput[pointer],Integer.valueOf(stack.peek()));
            System.out.print("stack:"+stack.toString()+"     ");
            if(action.getType()==0){ //reduce
                System.out.println("action:"+"reduce by "+ productions.get(action.getNumber()).toString());
                for(int i=0;i<productions.get(action.getNumber()).getRight().length * 2 ;i++){
                    stack.pop(); //弹出产生式右边
                }
                TableUnit gotoUnit=actionTable.getUnit(productions.get(action.getNumber()).getLeft(),Integer.valueOf(stack.peek()));
                stack.push(productions.get(action.getNumber()).getLeft());
                stack.push(String.valueOf(gotoUnit.getNumber()));
            }
            else if(action.getType()==1){ //shift
                System.out.println("action:"+"shift "+action.getNumber());
                stack.push(myInput[pointer]);
                stack.push(String.valueOf(action.getNumber()));
                pointer++;
            }
            else if(action.getType()==2){
                System.out.println("accept");
                break;
            }
            else{
                System.out.println("error");
                break;
            }
        }
    }
}
