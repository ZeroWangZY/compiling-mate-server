package ecnu.compiling.compilingmate.lex.analyzer.dfa;

import ecnu.compiling.compilingmate.lex.dto.NfaToDfaDto;
import ecnu.compiling.compilingmate.lex.entity.graph.*;
import ecnu.compiling.compilingmate.lex.entity.token.Token;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SubsetConstructionAnalyzer extends NfaToDfaAnalyzer {

    @Override
    protected NfaToDfaDto process(StateGraph stateGraph){
        AtomicInteger counter = new AtomicInteger(-1);

        Set<DfaState> dStateSet = new HashSet<>();
        List<Edge> edges = new ArrayList<>();

        Set<Token> tokens = calculateTokens(stateGraph);

        Set<NfaState> startSet = new HashSet<>();
        startSet.add(stateGraph.getStartState());

        // 初始化s0
        DfaState s0 = this.calculateClosure(startSet, stateGraph);
        s0.setId(counter.incrementAndGet());
        s0.setStart(true);
        dStateSet.add(s0);

        Stack<DfaState> stack = new Stack<>();
        stack.push(s0);

        while (!stack.isEmpty()){
            DfaState currentClosure = stack.pop();
            System.out.println(currentClosure.getNfaStates().stream().map(NfaState::getId).collect(Collectors.toList()));
            tokens.forEach(token -> {
                Set<NfaState> moveSet = this.calculateMove(currentClosure.getNfaStates(), token);
                if (!CollectionUtils.isEmpty(moveSet)){
                    DfaState newDState = this.calculateClosure(moveSet, stateGraph);
                    if (!dStateSet.contains(newDState)){
                        newDState.setId(counter.incrementAndGet());
                        dStateSet.add(newDState);
                        stack.push(newDState);
                    } else {
                        for (DfaState ds : dStateSet) {
                            if (ds.equals(newDState)){
                                newDState = ds;
                                break;
                            }
                        }
                    }
                    edges.add(new Edge(currentClosure.getId(), newDState.getId(), token));
                    currentClosure.setEnd(false);
                }
            });
        }

        return new NfaToDfaDto(edges, dStateSet);
    }

    private Set<Token> calculateTokens(StateGraph stateGraph){
        return stateGraph.getEdges().stream().filter(edge -> !this.getRule().isEmptyCharacter(edge.getTag())).map(Edge::getTag).collect(Collectors.toSet());
    }

    private Set<NfaState> calculateMove(Set<NfaState> startStates, Token token){
        Set<NfaState> result = new HashSet<>();
        for (NfaState state : startStates) {
            NfaState next = state.getNextState(token);
            if (next != null){
                result.add(next);
            }
        }

        return result;
    }

    private DfaState calculateClosure(Set<NfaState> start, StateGraph stateGraph){
        DfaState dState = new DfaState();

        Set<NfaState> states = stateGraph.getStates();
        List<Edge> edges = stateGraph.getEdges();

        for (NfaState state : start) {
            Stack<NfaState> stack = new Stack<>();
            stack.push(state);
            while (!stack.isEmpty()){
                NfaState currentState = stack.pop();
                dState.addNfaState(currentState);
                List<Integer> ids = edges.stream()
                        .filter(edge ->
                                this.getRule().isEmptyCharacter(edge.getTag()) &&
                                        currentState.getId().equals(edge.getFrom()))
                        .mapToInt(Edge::getTo).boxed()
                        .collect(Collectors.toList());

                states.stream()
                        .filter(nextState -> ids.contains(nextState.getId()))
                        .forEach(nextState -> stack.push(nextState));
            }
        }

        return dState;
    }
}
