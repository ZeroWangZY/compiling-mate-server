package ecnu.compiling.compilingmate.syntax.entity;

import java.util.ArrayList;
import java.util.List;

public class LR1Items {
    protected List<LR1Item> closure;

    public LR1Items(){
        closure=new ArrayList<>();
    }
    public void addItem(LR1Item item){
        closure.add(item);
    }

    public List<LR1Item> getClosure() {
        return closure;
    }
}
