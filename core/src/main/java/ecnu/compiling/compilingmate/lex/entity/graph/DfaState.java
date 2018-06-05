package ecnu.compiling.compilingmate.lex.entity.graph;

import ecnu.compiling.compilingmate.lex.entity.token.Token;

import java.util.HashSet;
import java.util.Set;

public class DfaState extends State{

    private Set<NfaState> nfaStates;

    private Token nextInput;

    private NfaState nextState;

    public DfaState(){
        nfaStates = new HashSet<>();
        this.setEnd(true);
    }

    public void addNfaState(NfaState state){
        nfaStates.add(state);
    }

    public Set<NfaState> getNfaStates() {
        return nfaStates;
    }

    public Token getNextInput() {
        return nextInput;
    }

    public void setNextInput(Token nextInput) {
        this.nextInput = nextInput;
    }

    public NfaState getNextState() {
        return nextState;
    }

    public void setNextState(NfaState nextState) {
        this.nextState = nextState;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DfaState){
            DfaState other = (DfaState) obj;
            return nfaStates.equals(other.nfaStates);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return nfaStates.hashCode();
    }
}
