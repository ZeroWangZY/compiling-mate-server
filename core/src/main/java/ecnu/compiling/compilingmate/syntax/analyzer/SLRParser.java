package ecnu.compiling.compilingmate.syntax.analyzer;

import ecnu.compiling.compilingmate.syntax.entity.*;
import ecnu.compiling.compilingmate.syntax.utils.Utils;

import java.util.*;

import static ecnu.compiling.compilingmate.syntax.utils.Utils.follow;

public class SLRParser {
    public void parsing(String[] input, List<LR0Item> productions, List<String> t, List<String> ntList) {

        //计算items
        List<LR0Items> itemList = new ArrayList<>(); //记录最终items
        List<Goto> gotoList=new ArrayList<>(); //goto(begin,end,X)
        //设置初始状态I0
        LR0Items i0=new LR0Items();
        for(LR0Item p:productions){
            i0.addProduction(new LR0Item(p.getLeft(),p.getRight(),0),0);
        }
        itemList.add(i0);
        Queue<LR0Items> waitingItems=new LinkedList<>(); //BFS，用队列记录待识别的项
        waitingItems.offer(i0);

        while(!waitingItems.isEmpty()) {
            LR0Items curItem=waitingItems.poll();
            //遍历当前item中的Prod
            Map<String, LR0Items> temp = new LinkedHashMap<>(); //NT,从当前item开始的gotoItemTemp
            for (LR0Item p : curItem.getClosure().keySet()) {
                if(curItem.getClosure().get(p)<p.getRight().length) {
                    if (!temp.containsKey(p.getRight()[curItem.getClosure().get(p)])) {
                        temp.put(p.getRight()[curItem.getClosure().get(p)], new LR0Items());
                    }
                    temp.get(p.getRight()[curItem.getClosure().get(p)]).addProduction(new LR0Item(p.getLeft(),p.getRight(),p.getPos()+1), curItem.getClosure().get(p) + 1);
                }
            }

            //得到goto(I,X)，当前item可达的items
            for (Map.Entry<String, LR0Items> entry : temp.entrySet()) {
                Utils.findClosure(entry.getValue(), t, productions);
                if (!itemList.contains(entry.getValue())) {
                    itemList.add(entry.getValue());
                    waitingItems.offer(entry.getValue());
                }
                gotoList.add(new Goto(itemList.indexOf(curItem), itemList.indexOf(entry.getValue()), entry.getKey()));
            }
        }


        //2.填表
        System.out.println("--------follow集--------");
        System.out.println("follow(E):"+follow("E",productions,"E'",t));
        System.out.println("follow(T):"+follow("T",productions,"E'",t));
        System.out.println("follow(F):"+follow("F",productions,"E'",t));

        ParsingTable actionTable=new ParsingTable(t.size()+ntList.size(),itemList.size(),t,ntList);
        for(LR0Items item:itemList){
            //shift j
            for(Goto gotoUnit:gotoList){
                if(gotoUnit.getBeginIndex()==itemList.indexOf(item)){
                    actionTable.setTable(gotoUnit.getEndIndex(),1,itemList.indexOf(item),gotoUnit.getX());
                }
            }
            //reduce
            for(LR0Item p:item.getClosure().keySet()){
                if(p.getPos()==p.getRight().length && !p.getLeft().equals("E'")){
                    List<String> follow=follow(p.getLeft(),productions,"E'",t);
                    for(String terminal:t){
                        if(follow.contains(terminal)){
                            actionTable.setTable(productions.indexOf(new LR0Item(p.getLeft(),p.getRight(),0)),0,itemList.indexOf(item),terminal);
                        }
                    }
                }
            }
            //accept
            if(item.getClosure().containsKey(new LR0Item(productions.get(0).getLeft(),productions.get(0).getRight(),1 ))){
                actionTable.setTable(0,2,itemList.indexOf(item),"$");
            }
        }
        actionTable.printTable();

        //3. 查表
        System.out.println("---------------search table-----------------");
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
