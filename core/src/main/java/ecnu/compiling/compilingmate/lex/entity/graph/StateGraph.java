package ecnu.compiling.compilingmate.lex.entity.graph;

import ecnu.compiling.compilingmate.lex.constants.LexConstants;
import ecnu.compiling.compilingmate.lex.entity.Token;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

public class StateGraph implements Cloneable {

    private Set<State> states;

    private List<Edge> edges;

    private Integer refId;
    private State startState;
    private State finalState;

    public StateGraph() {
        states = new HashSet<>();
        edges = new ArrayList<>();
    }

    public void addAll(StateGraph graph){
        for (State state : graph.states) {
            this.addState(state);
        }

        for (Edge edge : graph.edges) {
            this.addEdge(edge);
        }
    }

    public void addState(State state){
        states.add(state);
    }

    public void addEdge(Integer from, Integer to, Token tag){
        edges.add(new Edge(from, to, tag));
    }

    public void addEdge(Edge edge){
        edges.add(edge);
    }

    public void deleteState(State state){
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

    public State getStartState() {
        return startState;
    }

    public void setStartState(State startState) {
        this.startState = startState;
    }

    public State getFinalState() {
        return finalState;
    }

    public void setFinalState(State finalState) {
        this.finalState = finalState;
    }

    public Set<State> getStates() {
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
            for (State state : this.states) {
                State newState = state.clone();
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

            for (State state : copy.states) {
                if (state.getNextInput() != null) {
                    State fakeNext = state.getNextState(state.getNextInput());

                    for (State nextState : copy.states){
                        if (nextState.getId().equals(fakeNext.getId())){
                            state.addNext(state.getNextInput(), nextState);
                            break;
                        }
                    }
                } else if (!CollectionUtils.isEmpty(state.getNextStatesWhenEmptyInput())){
                    for (State fakeNext : state.getNextStatesWhenEmptyInput()) {
                        for (State nextState : copy.states){
                            if (nextState.getId().equals(fakeNext.getId())){
                                state.addNextWithEmptyInput(nextState);
                                break;
                            }
                        }
                    }
                }

            }

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return copy;
    }
}
