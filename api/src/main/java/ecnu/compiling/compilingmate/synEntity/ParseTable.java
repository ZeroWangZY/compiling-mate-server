package ecnu.compiling.compilingmate.synEntity;

import ecnu.compiling.compilingmate.syntax.entity.Conflict;

import java.util.List;

public class ParseTable {
    String[][] table;
    List<Conflict> conflictList;

    public String[][] getTable() {
        return table;
    }

    public List<Conflict> getConflictList() {
        return conflictList;
    }

    public void setTable(String[][] table) {
        this.table = table;
    }

    public void setConflictList(List<Conflict> conflictList) {
        this.conflictList = conflictList;
    }
}
