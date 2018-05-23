package ecnu.compiling.compilingmate.syntax.entity;

public class LR1Item extends LR0Item{
    protected String[] lookHead;
    public LR1Item(String left, String[] right, int pos,String[] lookHead) {
        super(left, right, pos);
        this.lookHead=lookHead;
    }

    public LR1Item(Production p,String[] lookHead) {
        super(p);
        this.lookHead=lookHead;
    }
}
