package ecnu.compiling.compilingmate.synEntity;

import java.util.List;

public class RequestDto {
    String startSymbol;
    List<ProductionDto> productions;
    int type; // 0:SLR, 1:LR

    public String getStartSymbol() {
        return startSymbol;
    }

    public void setStartSymbol(String startSymbol) {
        this.startSymbol = startSymbol;
    }

    public List<ProductionDto> getProductions() {
        return productions;
    }

    public void setProductions(List<ProductionDto> productions) {
        this.productions = productions;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
