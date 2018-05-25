package ecnu.compiling.compilingmate.syntax.analyzer;

import ecnu.compiling.compilingmate.syntax.entity.Goto;
import ecnu.compiling.compilingmate.syntax.entity.LR1Item;
import ecnu.compiling.compilingmate.syntax.entity.LR1Items;
import ecnu.compiling.compilingmate.syntax.entity.Production;

import java.util.*;

import static ecnu.compiling.compilingmate.syntax.utils.Utils.*;

public class LRParser implements BottomUpParser{
    @Override
    public List<LR1Items> constructItems(List<Production> productions,List<String> nt,List<String> t) {
        List<LR1Items> itemsList=new ArrayList<>(); // 已找到的items
        List<Goto> gotoList=new ArrayList<>();
        Queue<LR1Items> waitingItems=new LinkedList<>(); //BFS，用队列记录待寻找closure的项
        /**
         * init item 0
         */
        LR1Items i0=new LR1Items();
        i0.addItem(new LR1Item(productions.get(0),0,new String[]{"$"}));
        //find closure
        ListIterator<LR1Item> iterator = i0.getClosure().listIterator();
        while (iterator.hasNext()) {
            findClosure(iterator.next(), iterator,i0, nt, t, productions);
        }
        itemsList.add(i0);
        waitingItems.offer(i0);

        while(!waitingItems.isEmpty()) {
            LR1Items items=waitingItems.poll();
            System.out.println(items);
            /**
             * find next temp items
             */
            Map<String, LR1Items> temp = new LinkedHashMap<>(); // < NT,从当前items可达的items >
            for (LR1Item i : items.getClosure()) {
                if(i.getPos()<i.getRight().length) { // 未读完
                    if (!temp.containsKey(i.getRight()[i.getPos()])) {
                        temp.put(i.getRight()[i.getPos()], new LR1Items());
                    }
                    temp.get(i.getRight()[i.getPos()]).addItem(new LR1Item(i,i.getPos()+1,i.getLookHead()));
                }
            }

            for (Map.Entry<String, LR1Items> entry : temp.entrySet()) {  // 遍历temp items，找到完整闭包
                ListIterator<LR1Item> itr = entry.getValue().getClosure().listIterator();
                while (itr.hasNext()) {
                    findClosure(itr.next(), itr,entry.getValue(), nt, t, productions);
                }
                if (!itemsList.contains(entry.getValue())) {
                    itemsList.add(entry.getValue());
                    waitingItems.offer(entry.getValue());
                }
                gotoList.add(new Goto(itemsList.indexOf(items), itemsList.indexOf(entry.getValue()), entry.getKey()));
            }

        }
        System.out.println(gotoList);
        return itemsList;
    }

    public void findClosure(LR1Item item, ListIterator<LR1Item> closure,LR1Items lr1Items,List<String> nt,List<String> t,List<Production> productions){
        boolean[] ntIsIncluded=new boolean[nt.size()];
        findClosure(item,closure,lr1Items,nt,ntIsIncluded,t,productions);
    }

    /**
     * 对于入参item，若下一个是ntB:
     若出现B已经存在于items的left，更新所有left=B的lookhead;
     否则找到所有B->r，统一设置它们的lookhead；
     对新成员B->r递归findClosure
     */
    public void findClosure(LR1Item item, ListIterator<LR1Item> closure,LR1Items lr1Items,List<String> nt,boolean[] ntIsIncluded,List<String> t,List<Production> productions) {
        if (item.getPos() == item.getRight().length)
            return;
        String ntB = item.getRight()[item.getPos()]; // A→α·Bβ,a 中的B
        if (nt.contains(ntB)) {  // 光标指向ntB
            List<String> lhTemp = new ArrayList<>(); // 记录A→α·Bβ,a计算出的lookHead列表
            for (String b : t) {
                boolean isLookHead = false;
                if (item.getPos() < item.getRight().length - 1 && first(item.getRight()[item.getPos() + 1], productions, t).contains(b)) {
                    isLookHead = true;
                }
                for (String lh : item.getLookHead()) {
                    if (first(lh, productions, t).contains(b)) {
                        isLookHead = true;
                        break;
                    }
                }
                if (isLookHead) {
                    lhTemp.add(b);
                }
            }
            if (!ntIsIncluded[nt.indexOf(ntB)]) { // B是新加入闭包的left
                ntIsIncluded[nt.indexOf(ntB)]=true;
                List<Production> newItemList = new ArrayList<>();
                for (Production p : productions) {
                    if (p.getLeft().equals(ntB)) {
                        newItemList.add(p);
                    }
                }

                // B是新加入闭包的left,所有B开始的产生式加入闭包
                List<LR1Item> waitList=new ArrayList<>();
                for (Production p :newItemList){
                    LR1Item newItem = new LR1Item(p.getLeft(), p.getRight(), 0, lhTemp.toArray(new String[lhTemp.size()]));
                    closure.add(newItem);
                    waitList.add(newItem);
                }

                for(LR1Item item1:waitList){
                    findClosure(item1, closure,lr1Items, nt,ntIsIncluded, t, productions);
                }

            }
            else{
                List<LR1Item> list=lr1Items.getItemsByLeft(ntB);  // 已存在的B=left
                if(list.size()!=0){
                    Set set = new HashSet();
                    set.addAll(Arrays.asList(list.get(0).getLookHead()));
                    set.addAll(lhTemp);
                    List<String> newLfList=new ArrayList<>();
                    newLfList.addAll(set);
                    for(LR1Item lr1Item:list){
                        lr1Item.setLookHead(newLfList.toArray(new String[newLfList.size()]));
                    }
                }
            }
        }
    }

//
//
//    public void setLookHeadList(List<LR1Item> closure,List<String> nt,List<String> t,List<Production> productions){
//        Map<String, List<String>> lhMap=new LinkedHashMap<>();
//        for(LR1Item lr1Item:closure) {
//            if(lr1Item.getPos()<lr1Item.getRight().length ){
//                String tempB=lr1Item.getRight()[lr1Item.getPos()];
//                if(nt.contains(tempB)){ // A->x.By, a
//                    if(!lhMap.keySet().contains(lr1Item.getRight()[lr1Item.getPos()])){
//                        lhMap.put(tempB,new ArrayList<>());
//                    }
//                    for (String b : t) {
//                        boolean isLookHead = false;
//                        if (lr1Item.getPos() < lr1Item.getRight().length - 1 &&
//                                first(lr1Item.getRight()[lr1Item.getPos() + 1], productions, t).contains(b)) {
//                            isLookHead = true;
//                        }
//                        for (String lh : lr1Item.getLookHead()) {
//                            if (first(lh, productions, t).contains(b)) {
//                                isLookHead = true;
//                                break;
//                            }
//                        }
//                        if (isLookHead) {
//                            lhMap.get(tempB).add(b);
//                        }
//                    }
//                }
//            }
//        }
//        for(LR1Item lr1Item:closure){
//            List<String> lfs=lhMap.get(lr1Item.getLeft());
//            lr1Item.setLookHead(lfs.toArray(new String[lfs.size()]));
//        }
//    }
}
