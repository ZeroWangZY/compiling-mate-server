package ecnu.compiling.compilingmate.lex.entity.graph;

import ecnu.compiling.compilingmate.lex.entity.token.Token;

import java.util.ArrayList;
import java.util.List;

public class NfaState extends State implements Cloneable{

    private Token nextInput;

    private NfaState nextState;

    private List<NfaState> nextStatesWhenEmptyInput;

    public NfaState(Integer id) {
        super();
        this.setId(id);
        nextStatesWhenEmptyInput = new ArrayList<>();
    }

    public void addNext(Token input, NfaState state){
        nextInput = input;
        nextState = state;

        nextStatesWhenEmptyInput.clear();
    }

    public boolean addNextWithEmptyInput(NfaState input){
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

    public NfaState getNextState(Token nextInput) {
        if (nextInput.equals(this.nextInput)) {
            return nextState;
        }
        return null;
    }

    public NfaState getNextState() {
        if (nextStatesWhenEmptyInput.size() == 1){
            return nextStatesWhenEmptyInput.get(0);
        }
        return nextState;
    }

    public List<NfaState> getNextStatesWhenEmptyInput() {
        return nextStatesWhenEmptyInput;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NfaState){
            NfaState other = (NfaState) obj;
            return this.getId().equals(other.getId());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    @Override
    public NfaState clone(){
        NfaState copy = null;
        try {
            copy = (NfaState) super.clone();
            if (this.nextState != null) {
                copy.nextState = new NfaState(this.nextState.getId());
                copy.nextState.setEnd(this.nextState.isEnd());
                copy.nextState.setStart(this.nextState.isStart());
            }
            if (this.nextInput != null) {
                copy.nextInput = this.nextInput.clone();
            }
            copy.nextStatesWhenEmptyInput = new ArrayList<>();
            for (NfaState whenEmpty : this.nextStatesWhenEmptyInput) {
                NfaState next = new NfaState(whenEmpty.getId());
                next.setEnd(whenEmpty.isEnd());
                next.setStart(whenEmpty.isStart());
                copy.nextStatesWhenEmptyInput.add(next);
            }

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return copy;
    }
}
