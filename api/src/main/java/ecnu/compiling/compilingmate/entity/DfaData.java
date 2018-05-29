package ecnu.compiling.compilingmate.entity;

import java.util.List;
import java.util.Map;

public class DfaData {

    private Graph dfa;

    private Map<Integer, List<Integer>> states;

    public Graph getDfa() {
        return dfa;
    }

    public void setDfa(Graph dfa) {
        this.dfa = dfa;
    }

    public Map<Integer, List<Integer>> getStates() {
        return states;
    }

    public void setStates(Map<Integer, List<Integer>> states) {
        this.states = states;
    }
}
