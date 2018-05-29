package ecnu.compiling.compilingmate.lex.dto;

import ecnu.compiling.compilingmate.lex.entity.graph.DfaState;
import ecnu.compiling.compilingmate.lex.entity.graph.Edge;

import java.util.List;
import java.util.Set;

public class NfaToDfaDto {
    private List<Edge> edges;
    private Set<DfaState> dStates;

    public NfaToDfaDto(List<Edge> edges, Set<DfaState> dStates) {
        this.edges = edges;
        this.dStates = dStates;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public Set<DfaState> getdStates() {
        return dStates;
    }

    public void setdStates(Set<DfaState> dStates) {
        this.dStates = dStates;
    }
}
