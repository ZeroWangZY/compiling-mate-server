package ecnu.compiling.compilingmate.lex.analyzer.nfa;

import com.google.gson.Gson;
import ecnu.compiling.compilingmate.lex.constants.LexConstants;
import ecnu.compiling.compilingmate.lex.dto.ReToNfaDto;
import ecnu.compiling.compilingmate.lex.entity.*;
import ecnu.compiling.compilingmate.lex.entity.graph.NfaState;
import ecnu.compiling.compilingmate.lex.entity.graph.StateGraph;
import ecnu.compiling.compilingmate.lex.entity.token.Token;
import ecnu.compiling.compilingmate.lex.entity.tree.BranchNode;
import ecnu.compiling.compilingmate.lex.entity.tree.LeafNode;
import ecnu.compiling.compilingmate.lex.exception.ConstructStateFailureException;
import ecnu.compiling.compilingmate.lex.exception.ParseFailureException;
import ecnu.compiling.compilingmate.lex.policy.rule.DefaultReRule;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TompsonAnalyzer extends ReToNfaAnalyzer {

    private AtomicInteger stateNumberManager = new AtomicInteger();
    
    private DefaultReRule defaultReRule;

    private Gson GSON = new Gson();

    @Override
    public ReToNfaDto process(List<Token> tokenList) {
        
        defaultReRule = (DefaultReRule) this.getRule();

        List<Token> suffixExpression = this.toSuffixExpression(tokenList);

        if (CollectionUtils.isEmpty(suffixExpression)){
            throw new ParseFailureException(String.format(
                    "Fail to convert input expression into suffix expression. Input:[ %s ]", GSON.toJson(tokenList)));
        }

        List<StateGraph> graphs = new ArrayList<>();

        // 构造过程
        Stack<StateUnit> unitStack = new Stack<>();
        for (int i = 0; i < suffixExpression.size(); i++) {
            StateGraph graph = null;
            Token character = suffixExpression.get(i);
            BranchNode node = new BranchNode(i, character);

            if (defaultReRule.isNormalCharacter(character)){

                /**
                 * 一般字符 - a b c 等
                 */
                LeafNode leafNode = new LeafNode(character);
                node.addChild(leafNode);

                graph = this.generateSingle(character);

            } else if (defaultReRule.isRepeatOrNone(character)){

                /**
                 * 特殊字符 - *
                 */
                LeafNode leafNode = new LeafNode(character);
                StateUnit toBeRepeat = unitStack.pop();

                node.addChild(toBeRepeat.getStateNode());
                node.addChild(leafNode);

                graph = this.repeatOrNone(toBeRepeat.getStateGraph());

            } else {

                /**
                 * 两目操作符
                 */
                StateUnit to = unitStack.pop();
                StateUnit from = unitStack.pop();

                node.addChild(from.getStateNode());
                node.addChild(to.getStateNode());

                if (defaultReRule.isAnd(character)) {
                    /**
                     *  .操作
                     */
                    graph = this.and(from.getStateGraph(), to.getStateGraph());
                } else if (defaultReRule.isOr(character)){
                    /**
                     *  |操作
                     */
                    graph = this.or(from.getStateGraph(), to.getStateGraph());
                } else {
                    throw new ConstructStateFailureException(GSON.toJson(tokenList));
                }

            }

            graph.setRefId(i);

            StateUnit unit = new StateUnit();
            unit.setStateNode(node);
            unit.setStateGraph(graph);

            unitStack.push(unit);
            graphs.add(graph);
        }

        if (unitStack.size() != 1){
            throw new ParseFailureException(String.format(
                    "Fail to construct correct NFA states because the unit stack is not empty.\n" +
                            "StateStack: [ %s ]\n" +
                            "Input: [%s]", GSON.toJson(unitStack), GSON.toJson(unitStack)));
        }

        return new ReToNfaDto(unitStack.pop().getStateNode(), graphs);
    }


    /**
     * 中缀表达式 转 后缀表达式
     * todo 只能用default
     *
     * @param input
     * @return
     */
    private List<Token> toSuffixExpression(List<Token> input){
        if (CollectionUtils.isEmpty(input)){
            return null;
        }

        List<Token> result = new ArrayList<>();

        Stack<Token> stack = new Stack<>();
        for (int i = 0; i < input.size(); i++) {
            Token character = input.get(i);
            if (defaultReRule.isCharacter(character)){
                result.add(character);
            } else if (defaultReRule.isBracketEnd(character)){
                Token tmp = stack.pop();
                while (!defaultReRule.isBracketStart(tmp)){
                    result.add(tmp);
                    tmp = stack.pop();
                }
            } else if (defaultReRule.isOperator(character)){
                stack.push(character);
            }
        }

        while (!stack.empty()){
            result.add(stack.pop());
        }

        return result;
    }

    /**
     * 单个字符
     *
     * @param input
     * @return
     * @throws ConstructStateFailureException
     */
    private StateGraph generateSingle(Token input) throws ConstructStateFailureException {
        StateGraph graph = new StateGraph();

        if (!defaultReRule.isCharacter(input)){
            throw new ConstructStateFailureException(
                    String.format("Input is not a character when generate a single state.\n" +
                            "Input: [ %s ]", input));
        }

        NfaState fromState = getNewState();
        NfaState nextState = getNewState();

        fromState.addNext(input, nextState);

        fromState.setStart(true);
        fromState.setEnd(false);
        nextState.setStart(false);
        nextState.setEnd(true);

        graph.addState(fromState);
        graph.addState(nextState);
        graph.addEdge(fromState.getId(), nextState.getId(), input);
        graph.setStartState(fromState);
        graph.setFinalState(nextState);

        return graph;
    }

    /**
     * 两个状态做与操作（.）
     *
     * @param from
     * @param to
     * @return
     * @throws ConstructStateFailureException
     */
    private StateGraph and(StateGraph from, StateGraph to) throws ConstructStateFailureException {
        StateGraph graph = new StateGraph();
        if (!isStateUnitAvailable(from) || !isStateUnitAvailable(to)){
            throw new ConstructStateFailureException(
                    String.format("StateUnit is not available.\n" +
                            "Input: from-[ %s ], to-[ %s ]",
                            GSON.toJson(from), GSON.toJson(to)));
        }

        // deep copy 所有的节点和边
        StateGraph fromCopy = from.clone();
        StateGraph toCopy = to.clone();
        
        graph.addAll(fromCopy);
        graph.addAll(toCopy);

        // and操作
        NfaState preFinal = fromCopy.getFinalState();
        NfaState postStart = toCopy.getStartState();
        if (preFinal == null || postStart == null) {
            throw new ConstructStateFailureException(
                    String.format("StateUnit was internal broken.\n" +
                                    "Input: to-[ %s ]",
                            GSON.toJson(to)));
        }

        preFinal.setEnd(false);
        if (postStart.getNextInput() != null){
            preFinal.addNext(postStart.getNextInput(), postStart.getNextState(postStart.getNextInput()));
            graph.addEdge(preFinal.getId(), postStart.getNextState(postStart.getNextInput()).getId(), postStart.getNextInput());
        }
        if (!CollectionUtils.isEmpty(postStart.getNextStatesWhenEmptyInput())) {
            postStart.getNextStatesWhenEmptyInput().forEach(state -> {
                preFinal.addNextWithEmptyInput(state);
                graph.addEdge(preFinal.getId(), state.getId(), LexConstants.EMPTY);
            });

        }

        graph.deleteState(toCopy.getStartState());

        graph.setStartState(fromCopy.getStartState());
        graph.setFinalState(toCopy.getFinalState());

        return graph;
    }

    /**
     * 两个状态做或操作（|）
     *
     * @param from
     * @param to
     * @return
     * @throws ConstructStateFailureException
     */
    private StateGraph or(StateGraph from, StateGraph to) throws ConstructStateFailureException {
        StateGraph graph = new StateGraph();

        if (!isStateUnitAvailable(from) || !isStateUnitAvailable(to)){
            throw new ConstructStateFailureException(
                    String.format("StateUnit is not available.\n" +
                                    "Input: from-[ %s ], to-[ %s ]",
                            GSON.toJson(from), GSON.toJson(to)));
        }

        // deep copy 所有的节点和边
        StateGraph fromCopy = from.clone();
        StateGraph toCopy = to.clone();

        NfaState start = getNewState();
        start.setStart(true);
        NfaState end = getNewState();
        end.setEnd(true);

        NfaState aLeft = fromCopy.getStartState();
        NfaState aRight = fromCopy.getFinalState();

        NfaState bLeft = toCopy.getStartState();
        NfaState bRight = toCopy.getFinalState();

        start.addNextWithEmptyInput(aLeft);
        aLeft.setStart(false);
        start.addNextWithEmptyInput(bLeft);
        bLeft.setStart(false);

        aRight.setEnd(false);
        aRight.addNextWithEmptyInput(end);
        bRight.setEnd(false);
        bRight.addNextWithEmptyInput(end);

        graph.addAll(fromCopy);
        graph.addAll(toCopy);
        graph.addState(start);
        graph.addState(end);
        graph.setStartState(start);
        graph.setFinalState(end);

        graph.addEdge(start.getId(), aLeft.getId(), LexConstants.EMPTY);
        graph.addEdge(start.getId(), bLeft.getId(), LexConstants.EMPTY);
        graph.addEdge(aRight.getId(), end.getId(), LexConstants.EMPTY);
        graph.addEdge(bRight.getId(), end.getId(), LexConstants.EMPTY);

        return graph;
    }

    /**
     *  *操作
     *
     * @param stateGraph
     * @return
     * @throws ConstructStateFailureException
     */
    private StateGraph repeatOrNone(StateGraph stateGraph) throws ConstructStateFailureException {
        StateGraph graph = stateGraph.clone();

        if (!isStateUnitAvailable(stateGraph)){
            throw new ConstructStateFailureException(
                    String.format("StateUnit is not available.\n" +
                                    "Input: stateUnit-[ %s ]",
                            GSON.toJson(stateGraph)));
        }

        NfaState newStart = getNewState();
        NfaState newEnd = getNewState();
        NfaState oldStart = graph.getStartState();
        NfaState oldEnd = graph.getFinalState();

        newStart.setStart(true);
        newEnd.setEnd(true);
        oldStart.setStart(false);
        oldEnd.setEnd(false);

        newStart.addNextWithEmptyInput(oldStart);
        newStart.addNextWithEmptyInput(newEnd);

        oldEnd.addNextWithEmptyInput(newEnd);
        oldEnd.addNextWithEmptyInput(oldStart);

        graph.addState(newStart);
        graph.addState(newEnd);
        graph.setStartState(newStart);
        graph.setFinalState(newEnd);

        graph.addEdge(newStart.getId(), oldStart.getId(), LexConstants.EMPTY);
        graph.addEdge(newStart.getId(), newEnd.getId(), LexConstants.EMPTY);
        graph.addEdge(oldEnd.getId(), newEnd.getId(), LexConstants.EMPTY);
        graph.addEdge(oldEnd.getId(), oldStart.getId(), LexConstants.EMPTY);

        return graph;
    }



    private NfaState getNewState(){
        return new NfaState(stateNumberManager.incrementAndGet());
    }

    private boolean isStateUnitAvailable(StateGraph stateGraph){
        return stateGraph != null && stateGraph.getStartState() != null && stateGraph.getFinalState() != null;
    }

}
