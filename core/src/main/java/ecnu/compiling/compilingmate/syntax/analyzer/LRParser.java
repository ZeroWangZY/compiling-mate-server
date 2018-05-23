package ecnu.compiling.compilingmate.syntax.analyzer;

import ecnu.compiling.compilingmate.syntax.entity.LR1Item;
import ecnu.compiling.compilingmate.syntax.entity.LR1Items;
import ecnu.compiling.compilingmate.syntax.entity.Production;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class LRParser implements BottomUpParser{
    @Override
    public List<LR1Items> constructItems(List<Production> productions,List<String> nt,List<String> t) {
        LR1Items i0=new LR1Items();
        i0.addItem(new LR1Item(productions.get(0),new String[]{"$"}));
        ListIterator<LR1Item> iterator=i0.getClosure().listIterator();
        while(iterator.hasNext()){
            findClosure(iterator.next(),i0,nt,t,productions);
        }
        return null;
    }

    public void findClosure(LR1Item item, LR1Items items,List<String> nt,List<String> t,List<Production> productions){
        if(nt.contains(item.getRight()[item.getPos()])){  // 光标指向非终结符A时，需要继续求闭包
            for(Production p:productions){
                if(p.getLeft().equals(item.getRight()[item.getPos()])){  // 所有A开始的产生式加入闭包
                    List<String> lhTemp=new ArrayList<>(); // 记录lf列表
                    if(Arrays.asList(p.getRight()).contains(item.getRight()[item.getPos()])){
                        //
                    }
                    LR1Item newItem=new LR1Item(p.getLeft(),p.getRight(),0,(String[])lhTemp.toArray());
                    items.addItem(newItem);
                }
            }
        }
    }
}
