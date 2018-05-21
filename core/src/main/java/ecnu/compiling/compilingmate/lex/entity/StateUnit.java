package ecnu.compiling.compilingmate.lex.entity;

public class StateUnit {
    private State startState;
    private State finalState;

    public StateUnit(State startState, State finalState) {
        this.startState = startState;
        this.finalState = finalState;
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

    @Override
    public String toString() {
        return new StringBuilder("from-").append(startState.getId()).append("  to=").append(finalState.getId()).toString();
    }
}
