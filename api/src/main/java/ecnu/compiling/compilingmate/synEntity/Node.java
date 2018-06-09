package ecnu.compiling.compilingmate.synEntity;

import ecnu.compiling.compilingmate.syntax.entity.Goto;
import ecnu.compiling.compilingmate.syntax.entity.LR0Items;
import ecnu.compiling.compilingmate.syntax.entity.LR1Items;

public class Node {
    private String id;
    private String relatedTo;
    private String linkInfo;
    private String text;
    private String[] productionLeft;
    private String[] productionRight;


    public Node(String id, Goto gotoUnit, LR0Items lr0Items){
        this.id=id;
        this.relatedTo=String.valueOf(gotoUnit.getBeginIndex());
        this.linkInfo=gotoUnit.getX();
        this.text=String.valueOf(gotoUnit.getEndIndex());
        this.productionLeft=lr0Items.getProductionLeft();
        this.productionRight=lr0Items.getProductionRight();
    }

    public Node(String id, Goto gotoUnit, LR1Items lr1Items){
        this.id=id;
        this.relatedTo=String.valueOf(gotoUnit.getBeginIndex());
        this.linkInfo=gotoUnit.getX();
        this.text=String.valueOf(gotoUnit.getEndIndex());
        this.productionLeft=lr1Items.getProductionLeft();
        this.productionRight=lr1Items.getProductionRight();
    }

    public Node(String id, String relatedTo, String linkInfo, String text, String[] productionLeft, String[] productionRight) {
        this.id = id;
        this.relatedTo = relatedTo;
        this.linkInfo = linkInfo;
        this.text = text;
        this.productionLeft = productionLeft;
        this.productionRight = productionRight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelatedTo() {
        return relatedTo;
    }

    public void setRelatedTo(String relatedTo) {
        this.relatedTo = relatedTo;
    }

    public String getLinkInfo() {
        return linkInfo;
    }

    public void setLinkInfo(String linkInfo) {
        this.linkInfo = linkInfo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getProductionLeft() {
        return productionLeft;
    }

    public void setProductionLeft(String[] productionLeft) {
        this.productionLeft = productionLeft;
    }

    public String[] getProductionRight() {
        return productionRight;
    }

    public void setProductionRight(String[] productionRight) {
        this.productionRight = productionRight;
    }
}
