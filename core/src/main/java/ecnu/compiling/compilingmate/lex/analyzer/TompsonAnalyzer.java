package ecnu.compiling.compilingmate.lex.analyzer;

import com.google.gson.Gson;
import ecnu.compiling.compilingmate.lex.dto.ReToNfaDto;
import ecnu.compiling.compilingmate.lex.entity.*;
import ecnu.compiling.compilingmate.lex.utils.LexUtils;
import ecnu.compiling.compilingmate.lex.exception.ConstructStateFailureException;
import ecnu.compiling.compilingmate.lex.exception.ParseFailureException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TompsonAnalyzer extends ReToNfaAnalyzer {

    private AtomicInteger stateNumberManager = new AtomicInteger();

    @Override
    public ReToNfaDto process(String input) {

        String suffixExpression = this.toSuffixExpression(input);

        if (StringUtils.isEmpty(suffixExpression)){
            throw new ParseFailureException(String.format(
                    "Fail to convert input expression into suffix expression. Input:[ %s ]", input));
        }

        Map<Integer, StateGraph> map = new HashMap<>();

        // 构造过程
        Stack<StateUnit> unitStack = new Stack<>();
        for (int i = 0; i < suffixExpression.length(); i++) {
            StateGraph unit = null;
            BranchNode node = new BranchNode();

            char character = suffixExpression.charAt(i);

            if (LexUtils.isNormalCharacter(character)){

                /**
                 * 一般字符 - a b c 等
                 */
                LeafNode leafNode = new LeafNode(character);
                node.addChild(leafNode);

                unit = this.generateSingle(character);

            } else if (LexUtils.isRepeatOrNone(character)){

                /**
                 * 特殊字符 - *
                 */
                LeafNode leafNode = new LeafNode(character);
                StateUnit toBeRepeat = unitStack.pop();

                node.addChild(toBeRepeat.getStateNode());
                node.addChild(leafNode);

                unit = this.repeatOrNone(toBeRepeat.getStateGraph());

            } else {

                /**
                 * 两目操作符
                 */
                StateUnit to = unitStack.pop();
                StateUnit from = unitStack.pop();

                node.addChild(from.getStateNode());
                node.addChild(to.getStateNode());

                if (LexUtils.isAnd(character)) {
                    /**
                     *  .操作
                     */
                    unit = this.and(from.getStateGraph(), to.getStateGraph());
                } else if (LexUtils.isOr(character)){
                    /**
                     *  |操作
                     */
                    unit = this.or(from.getStateGraph(), to.getStateGraph());
                } else {
                    throw new ConstructStateFailureException(input);
                }

            }

            unit.setRefId(i);
            node.setRefId(i);

            StateUnit graph = new StateUnit();
            graph.setStateNode(node);
            graph.setStateGraph(unit);

            unitStack.push(graph);
            map.put(i, unit);
        }

        if (unitStack.size() != 1){
            throw new ParseFailureException(String.format(
                    "Fail to construct correct NFA states because the unit stack is not empty.\n" +
                            "StateStack: [ %s ]\n" +
                            "Input: [%s]", new Gson().toJson(unitStack), input));
        }

        return new ReToNfaDto(unitStack.pop().getStateNode(), map);
    }


    /**
     * 中缀表达式 转 后缀表达式
     *
     * @param input
     * @return
     */
    private String toSuffixExpression(String input){
        if (StringUtils.isEmpty(input)){
            return "";
        }

        StringBuilder builder = new StringBuilder();

        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < input.length(); i++) {
            char character = input.charAt(i);
            if (LexUtils.isCharacter(character)){
                builder.append(character);
            } else if (LexUtils.isBracketStart(character) || LexUtils.isOperator(character)){
                stack.push(character);
            } else if (LexUtils.isBracketEnd(character)){
                Character tmp = stack.pop();
                while (!LexUtils.isBracketStart(tmp)){
                    builder.append(tmp);
                    tmp = stack.pop();
                }
            }
        }

        while (!stack.empty()){
            builder.append(stack.pop());
        }

        return builder.toString();
    }

    /**
     * 单个字符
     *
     * @param input
     * @return
     * @throws ConstructStateFailureException
     */
    private StateGraph generateSingle(Character input) throws ConstructStateFailureException {
        if (!LexUtils.isCharacter(input)){
            throw new ConstructStateFailureException(
                    String.format("Input is not a character when generate a single state.\n" +
                            "Input: [ %s ]", input));
        }

        State fromState = getNewState();
        State nextState = getNewState();

        fromState.addNext(input, nextState);

        nextState.setFinal(true);

        return new StateGraph(fromState, nextState);
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
        if (!isStateUnitAvailable(from) || !isStateUnitAvailable(to)){
            throw new ConstructStateFailureException(
                    String.format("StateUnit is not available.\n" +
                            "Input: from-[ %s ], to-[ %s ]",
                            new Gson().toJson(from), new Gson().toJson(to)));
        }

        State preFinal = from.getFinalState();
        State postSecond = to.getStartState().getNextState();
        if (postSecond != null) {
            preFinal.setFinal(false);
            preFinal.addNextWithEmptyInput(postSecond);
        } else {
            throw new ConstructStateFailureException(
                    String.format("StateUnit was internal broken.\n" +
                                    "Input: to-[ %s ]",
                            new Gson().toJson(to)));
        }

        return new StateGraph(from.getStartState(), to.getFinalState());
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
        if (!isStateUnitAvailable(from) || !isStateUnitAvailable(to)){
            throw new ConstructStateFailureException(
                    String.format("StateUnit is not available.\n" +
                                    "Input: from-[ %s ], to-[ %s ]",
                            new Gson().toJson(from), new Gson().toJson(to)));
        }

        State start = getNewState();
        State end = getNewState();

        State aLeft = from.getStartState();
        State aRight = from.getFinalState();

        State bLeft = to.getStartState();
        State bRight = to.getFinalState();

        start.addNextWithEmptyInput(aLeft);
        start.addNextWithEmptyInput(bLeft);

        aRight.setFinal(false);
        aRight.addNextWithEmptyInput(end);
        bRight.setFinal(false);
        bRight.addNextWithEmptyInput(end);

        end.setFinal(true);

        return new StateGraph(start, end);
    }

    /**
     *  *操作
     *
     * @param stateGraph
     * @return
     * @throws ConstructStateFailureException
     */
    private StateGraph repeatOrNone(StateGraph stateGraph) throws ConstructStateFailureException {
        if (!isStateUnitAvailable(stateGraph)){
            throw new ConstructStateFailureException(
                    String.format("StateUnit is not available.\n" +
                                    "Input: stateUnit-[ %s ]",
                            new Gson().toJson(stateGraph)));
        }

        State newStart = getNewState();
        State newEnd = getNewState();
        State oldStart = stateGraph.getStartState();
        State oldFinal = stateGraph.getFinalState();

        newStart.addNextWithEmptyInput(oldStart);
        newStart.addNextWithEmptyInput(newEnd);

        oldFinal.setFinal(false);
        oldFinal.addNextWithEmptyInput(newEnd);
        oldFinal.addNextWithEmptyInput(oldStart);
        newEnd.setFinal(true);

        return new StateGraph(newStart, newEnd);
    }



    private State getNewState(){
        return new State(stateNumberManager.incrementAndGet());
    }

    private boolean isStateUnitAvailable(StateGraph stateGraph){
        return stateGraph != null && stateGraph.getStartState() != null && stateGraph.getFinalState() != null;
    }

}
