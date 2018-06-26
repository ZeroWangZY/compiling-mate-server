package ecnu.compiling.compilingmate.lex.entity.graph;

import ecnu.compiling.compilingmate.lex.entity.token.Token;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

public class StateGraph implements Cloneable {

    private Set<NfaState> states;

    private List<Edge> edges;

    private Integer refId;
    private NfaState startState;
    private NfaState finalState;

    public StateGraph() {
        states = new HashSet<>();
        edges = new ArrayList<>();
    }

    public void addAll(StateGraph graph){
        for (NfaState state : graph.states) {
            this.addState(state);
        }

        for (Edge edge : graph.edges) {
            this.addEdge(edge);
        }
    }

    public void addState(NfaState state){
        states.add(state);
    }

    public void addEdge(Integer from, Integer to, Token tag){
        edges.add(new Edge(from, to, tag));
    }

    public void addEdge(Edge edge){
        edges.add(edge);
    }

    public void deleteState(NfaState state){
        states.remove(state);
        edges.removeIf(e -> e.getFrom().equals(state.getId()) ||
                e.getTo().equals(state.getId()));
    }

    public void deleteEdge(Edge edge){
        edges.remove(edge);
    }

    public void deleteEdge(Integer from, Integer to, Token tag){
        edges.removeIf(e -> e.getFrom().equals(from) &&
                e.getTo().equals(to) &&
                e.getTag().equals(tag));
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public NfaState getStartState() {
        return startState;
    }

    public void setStartState(NfaState startState) {
        this.startState = startState;
    }

    public NfaState getFinalState() {
        return finalState;
    }

    public void setFinalState(NfaState finalState) {
        this.finalState = finalState;
    }

    public Set<NfaState> getStates() {
        return states;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        return new StringBuilder("from-").append(startState.getId()).append("  to=").append(finalState.getId()).toString();
    }

    @Override
    public StateGraph clone(){
        StateGraph copy = null;
        try {
            copy = (StateGraph) super.clone();

            copy.states = new HashSet<>();
            for (NfaState state : this.states) {
                NfaState newState = state.clone();
                copy.states.add(newState);
                if (newState.isStart()){
                    copy.setStartState(newState);
                }
                if (newState.isEnd()){
                    copy.setFinalState(newState);
                }
            }

            copy.edges = new ArrayList<>();
            for (Edge edge : this.edges) {
                copy.edges.add(edge.clone());
            }

            for (NfaState state : copy.states) {
                if (state.getNextInput() != null) {
                    NfaState fakeNext = state.getNextState(state.getNextInput());

                    for (NfaState nextState : copy.states){
                        if (nextState.getId().equals(fakeNext.getId())){
                            state.addNext(state.getNextInput(), nextState);
                            break;
                        }
                    }
                } else if (!CollectionUtils.isEmpty(state.getNextStatesWhenEmptyInput())){
                    List<NfaState> tmpList = new ArrayList<>();
                    for (NfaState fakeNext : state.getNextStatesWhenEmptyInput()) {
                        for (NfaState nextState : copy.states){
                            if (nextState.getId().equals(fakeNext.getId())){
                                tmpList.add(nextState);
                                break;
                            }
                        }
                    }
                    state.getNextStatesWhenEmptyInput().clear();
                    state.getNextStatesWhenEmptyInput().addAll(tmpList);
                }

            }

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return copy;
    }
}
