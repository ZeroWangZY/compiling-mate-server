package ecnu.compiling.compilingmate.lex.analyzer;

import ecnu.compiling.compilingmate.lex.utils.LexUtils;
import ecnu.compiling.compilingmate.lex.entity.State;
import ecnu.compiling.compilingmate.lex.entity.StateUnit;
import ecnu.compiling.compilingmate.lex.exception.IllegalInputException;
import ecnu.compiling.compilingmate.lex.exception.ParseFailureException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TompsonAnalyzer extends ReToNfaAnalyzer {

    private AtomicInteger stateNumberManager = new AtomicInteger();

    @Override
    public StateUnit process(String input) {
        // todo catch the exceptions
        String suffixExpression = this.toSuffixExpression(input);

        if (StringUtils.isEmpty(suffixExpression)){
            return new StateUnit(null, null);
        }

        Stack<StateUnit> unitStack = new Stack<>();
        for (int i = 0; i < suffixExpression.length(); i++) {
            char character = suffixExpression.charAt(i);

            if (LexUtils.isNormalCharacter(character)){

                /**
                 * 一般字符 - a b c 等
                 */
                unitStack.push(this.generateSingle(character));

            } else if (LexUtils.isRepeatOrNone(character)){

                /**
                 * 特殊字符 - *
                 */
                unitStack.push(this.repeatOrNone(unitStack.pop()));

            } else {

                /**
                 * 两目操作符
                 */
                StateUnit to = unitStack.pop();
                StateUnit from = unitStack.pop();

                if (LexUtils.isAnd(character)) {
                    /**
                     *  .操作
                     */
                    unitStack.push(this.and(from, to));
                } else if (LexUtils.isOr(character)){
                    /**
                     *  |操作
                     */
                    unitStack.push(this.or(from, to));
                } else {
                    throw new IllegalInputException(input);
                }

            }
        }

        if (unitStack.size() != 1){
            throw new ParseFailureException(input);
        }

        return unitStack.pop();
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
     * @throws IllegalInputException
     */
    private StateUnit generateSingle(Character input) throws IllegalInputException{
        // and 操作必须后跟字符
        if (!LexUtils.isCharacter(input)){
            throw new IllegalInputException(input);
        }

        State fromState = getNewState();
        State nextState = getNewState();

        fromState.addNext(input, nextState);

        nextState.setFinal(true);

        return new StateUnit(fromState, nextState);
    }

    /**
     * 两个状态做与操作（.）
     *
     * @param from
     * @param to
     * @return
     * @throws IllegalInputException
     */
    private StateUnit and(StateUnit from, StateUnit to) throws IllegalInputException{
        if (!isStateUnitAvailable(from) || !isStateUnitAvailable(to)){
            throw  new IllegalInputException(from, to);
        }

        State preFinal = from.getFinalState();
        State postSecond = to.getStartState().getNextState();
        if (postSecond != null) {
            preFinal.setFinal(false);
            preFinal.addNextWithEmptyInput(postSecond);
        } else {
            throw new ParseFailureException(from, to);
        }

        return new StateUnit(from.getStartState(), to.getFinalState());
    }

    /**
     * 两个状态做或操作（|）
     *
     * @param from
     * @param to
     * @return
     * @throws IllegalInputException
     */
    private StateUnit or(StateUnit from, StateUnit to) throws IllegalInputException{
        if (!isStateUnitAvailable(from) || !isStateUnitAvailable(to)){
            throw  new IllegalInputException(from, to);
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

        return new StateUnit(start, end);
    }

    /**
     *  *操作
     *
     * @param stateUnit
     * @return
     * @throws IllegalInputException
     */
    private StateUnit repeatOrNone(StateUnit stateUnit) throws IllegalInputException{
        if (!isStateUnitAvailable(stateUnit)){
            throw  new IllegalInputException(stateUnit);
        }

        State newStart = getNewState();
        State newEnd = getNewState();
        State oldStart = stateUnit.getStartState();
        State oldFinal = stateUnit.getFinalState();

        newStart.addNextWithEmptyInput(oldStart);
        newStart.addNextWithEmptyInput(newEnd);

        oldFinal.setFinal(false);
        oldFinal.addNextWithEmptyInput(newEnd);
        oldFinal.addNextWithEmptyInput(oldStart);
        newEnd.setFinal(true);

        return new StateUnit(newStart, newEnd);
    }



    private State getNewState(){
        return new State(stateNumberManager.incrementAndGet());
    }

    private boolean isStateUnitAvailable(StateUnit stateUnit){
        return stateUnit != null && stateUnit.getStartState() != null && stateUnit.getFinalState() != null;
    }

}
