package ecnu.compiling.compilingmate.lex.entity.graph;

import ecnu.compiling.compilingmate.lex.entity.Token;

import java.util.ArrayList;
import java.util.List;

public class State implements Cloneable{

    private Integer id;

    private boolean isStart;

    private boolean isEnd;

    private Token nextInput;

    private State nextState;

    private List<State> nextStatesWhenEmptyInput;


    public State(Integer id) {
        this.id = id;
        nextStatesWhenEmptyInput = new ArrayList<>();
    }

    public void addNext(Token input, State state){
        nextInput = input;
        nextState = state;

        nextStatesWhenEmptyInput.clear();
    }

    public boolean addNextWithEmptyInput(State input){
        if (nextStatesWhenEmptyInput.size() < 2){
            nextInput = null;
            nextState = null;
            nextStatesWhenEmptyInput.add(input);
            return true;
        }

        return false;
    }

    public boolean allowCharactorInput(){
        return nextInput != null && nextState != null;
    }

    public boolean allowEmptyInput(){
        return !allowCharactorInput() && nextStatesWhenEmptyInput.size() <= 2;
    }

    public Token getNextInput() {
        return nextInput;
    }

    public State getNextState(Token nextInput) {
        if (nextInput == this.nextInput) {
            return nextState;
        }
        return null;
    }

    public Integer getId() {
        return id;
    }

    public State getNextState() {
        if (nextStatesWhenEmptyInput.size() == 1){
            return nextStatesWhenEmptyInput.get(0);
        }
        return nextState;
    }

    public List<State> getNextStatesWhenEmptyInput() {
        return nextStatesWhenEmptyInput;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof State){
            State other = (State) obj;
            return id.equals(other.getId());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public State clone(){
        State copy = null;
        try {
            copy = (State) super.clone();
            if (this.nextState != null) {
                copy.nextState = new State(this.nextState.id);
                copy.nextState.isEnd = this.nextState.isEnd;
                copy.nextState.isStart = this.nextState.isStart;
            }
            if (this.nextInput != null) {
                copy.nextInput = this.nextInput.clone();
            }
            copy.nextStatesWhenEmptyInput = new ArrayList<>();
            for (State whenEmpty : this.nextStatesWhenEmptyInput) {
                State next = new State(whenEmpty.id);
                next.isEnd = whenEmpty.isEnd;
                next.isStart = whenEmpty.isStart;
                copy.nextStatesWhenEmptyInput.add(next);
            }

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return copy;
    }
}
