package ecnu.compiling.compilingmate.synEntity;

import java.util.List;

/**
 * actionRequestDto
 *  symbols: ["+","*","(",")","id","$","E","T","F"]
 *  table: [["","","s4","","s5","","1","2","3"],["s6","","","","","acc","","",""],...]
 *  productions: (left:String,right:String[])
 *  input: String[]
 */
public class ActionRequestDto {
    String[] symbols;
    String[][] table;
    String input;
    List<ProductionDto> productions;
    String startSymbol;

    public String[] getSymbols() {
        return symbols;
    }

    public void setSymbols(String[] symbols) {
        this.symbols = symbols;
    }

    public String[][] getTable() {
        return table;
    }

    public void setTable(String[][] table) {
        this.table = table;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public List<ProductionDto> getProductions() {
        return productions;
    }

    public void setProductions(List<ProductionDto> productions) {
        this.productions = productions;
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public void setStartSymbol(String startSymbol) {
        this.startSymbol = startSymbol;
    }
}
