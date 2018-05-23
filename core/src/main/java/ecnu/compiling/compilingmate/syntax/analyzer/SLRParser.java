package ecnu.compiling.compilingmate.syntax.analyzer;

import ecnu.compiling.compilingmate.syntax.entity.*;
import ecnu.compiling.compilingmate.syntax.utils.Utils;

import java.util.*;

import static ecnu.compiling.compilingmate.syntax.utils.Utils.follow;

public class SLRParser {
    public void parsing(String[] input, List<ProductionLR0> productions,List<String> t,List<String> ntList) {

        //计算items
        List<Item> itemList = new ArrayList<>(); //记录最终items
        List<Goto> gotoList=new ArrayList<>(); //goto(begin,end,X)
        //设置初始状态I0
        Item i0=new Item();
        for(ProductionLR0 p:productions){
            i0.addProduction(new ProductionLR0(p.left,p.right,0),0);
        }
        itemList.add(i0);
        Queue<Item> waitingItems=new LinkedList<>(); //BFS，用队列记录待识别的项
        waitingItems.offer(i0);

        while(!waitingItems.isEmpty()) {
            Item curItem=waitingItems.poll();
            //遍历当前item中的Prod
            Map<String, Item> temp = new LinkedHashMap<>(); //NT,从当前item开始的gotoItemTemp
            for (ProductionLR0 p : curItem.getClosure().keySet()) {
                if(curItem.getClosure().get(p)<p.right.length) {
                    if (!temp.containsKey(p.right[curItem.getClosure().get(p)])) {
                        temp.put(p.right[curItem.getClosure().get(p)], new Item());
                    }
                    temp.get(p.right[curItem.getClosure().get(p)]).addProduction(new ProductionLR0(p.left,p.right,p.pos+1), curItem.getClosure().get(p) + 1);
                }
            }

            //得到goto(I,X)，当前item可达的items
            for (Map.Entry<String, Item> entry : temp.entrySet()) {
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
        for(Item item:itemList){
            //shift j
            for(Goto gotoUnit:gotoList){
                if(gotoUnit.getBeginIndex()==itemList.indexOf(item)){
                    actionTable.setTable(gotoUnit.getEndIndex(),1,itemList.indexOf(item),gotoUnit.getX());
                }
            }
            //reduce
            for(ProductionLR0 p:item.getClosure().keySet()){
                if(p.pos==p.right.length && !p.left.equals("E'")){
                    List<String> follow=follow(p.left,productions,"E'",t);
                    for(String terminal:t){
                        if(follow.contains(terminal)){
                            actionTable.setTable(productions.indexOf(new ProductionLR0(p.left,p.right,0)),0,itemList.indexOf(item),terminal);
                        }
                    }
                }
            }
            //accept
            if(item.getClosure().containsKey(new ProductionLR0(productions.get(0).left,productions.get(0).right,1 ))){
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
            if(action.type==0){ //reduce
                System.out.println("action:"+"reduce by "+ productions.get(action.number).toString());
                for(int i=0;i<productions.get(action.number).right.length * 2 ;i++){
                    stack.pop(); //弹出产生式右边
                }
                TableUnit gotoUnit=actionTable.getUnit(productions.get(action.number).left,Integer.valueOf(stack.peek()));
                stack.push(productions.get(action.number).left);
                stack.push(String.valueOf(gotoUnit.number));
            }
            else if(action.type==1){ //shift
                System.out.println("action:"+"shift "+action.number);
                stack.push(myInput[pointer]);
                stack.push(String.valueOf(action.number));
                pointer++;
            }
            else if(action.type==2){
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
