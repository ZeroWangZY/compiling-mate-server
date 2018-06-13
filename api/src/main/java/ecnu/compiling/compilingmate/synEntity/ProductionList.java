package ecnu.compiling.compilingmate.synEntity;

import ecnu.compiling.compilingmate.syntax.entity.Production;
import ecnu.compiling.compilingmate.syntax.exception.SyntaxInputException;

import java.util.ArrayList;
import java.util.List;

public class ProductionList {
    List<Production> productions;
    List<String> nts;
    List<String> ts;

    public ProductionList(List<ProductionDto> productionDtos,String startSymbol) {
        List<Production> productions=new ArrayList<>();
        productions.add(new Production(startSymbol+"'",new String[]{startSymbol}));
        for(ProductionDto productionDto:productionDtos){
            productions.add(new Production(productionDto.getLeft(),productionDto.getRightStrs()));
        }
        this.productions=productions;
        setNtsAndTs(productionDtos);
        startCheck(startSymbol);
    }

    public void setNtsAndTs(List<ProductionDto> productionDtos){
        List<String> nts=new ArrayList<>();
        List<String> ts=new ArrayList<>();
        for(ProductionDto p:productionDtos){
            if(!nts.contains(p.getLeft())){
                nts.add(p.getLeft());
            }
        }
        for(ProductionDto p:productionDtos){
            for(String s:p.getRightStrs()){
                if(!nts.contains(s)){
                    ts.add(s);
                }
            }
        }
        ts.add("$");
        this.ts=ts;
        this.nts=nts;
    }

    public void startCheck(String startSymbol){
        if(!nts.contains(startSymbol)){
            throw new SyntaxInputException("start symbol not exists");
        }
    }

    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> productions) {
        this.productions = productions;
    }

    public List<String> getNts() {
        return nts;
    }

    public void setNts(List<String> nts) {
        this.nts = nts;
    }

    public List<String> getTs() {
        return ts;
    }

    public void setTs(List<String> ts) {
        this.ts = ts;
    }
}
