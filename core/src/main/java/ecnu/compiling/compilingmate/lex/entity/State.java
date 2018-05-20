package ecnu.compiling.compilingmate.lex.entity;

import java.util.ArrayList;
import java.util.List;

public class State {

    private Integer id;

    private Character nextInput;

    private State nextState;

    private List<State> nextStatesWhenEmptyInput;

    private boolean isFinal;

    public State(Integer id) {
        this.id = id;
        nextStatesWhenEmptyInput = new ArrayList<>();
    }

    public void addNext(Character input, State state){
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

    public Character getNextInput() {
        return nextInput;
    }

    public State getNextState(Character nextInput) {
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

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }
}
