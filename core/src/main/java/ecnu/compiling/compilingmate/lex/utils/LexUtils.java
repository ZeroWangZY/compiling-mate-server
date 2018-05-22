package ecnu.compiling.compilingmate.lex.utils;

import ecnu.compiling.compilingmate.lex.constants.LexConstants;
import ecnu.compiling.compilingmate.lex.entity.State;
import org.apache.commons.lang3.StringUtils;

import java.util.Stack;

public final class LexUtils {

    public static boolean isCharacter(Character input){
        if (input == null) return false;

        return !isOperator(input) && !isBracket(input);
    }

    public static boolean isNormalCharacter(Character input){
        return isCharacter(input) && !isSpecialCharacter(input);
    }

    public static boolean isSpecialCharacter(Character input){
        for (LexConstants.SpecialCharacter sp : LexConstants.SpecialCharacter.values()) {
            if (input == sp.getValue()){
                return true;
            }
        }

        return false;
    }

    public static boolean isOperator(Character input){
        return isAnd(input) ||
                isOr(input);
    }

    public static boolean isAnd(Character input){
        if (input == null) return false;

        return LexConstants.Operator.AND.equals(input);
    }

    public static boolean isOr(Character input){
        if (input == null) return false;

        return LexConstants.Operator.OR.equals(input);
    }

    public static boolean isRepeatOrNone(Character input){
        if (input == null) return false;

        return LexConstants.SpecialCharacter.REPEAT_OR_NONE.equals(input);
    }

    public static boolean isEmptyChar(Character input){
        if (input == null) return false;

        return LexConstants.SpecialCharacter.EMPTY.equals(input);
    }

    public static boolean isBracket(Character input){
        return isBracketStart(input) || isBracketEnd(input);
    }

    public static boolean isBracketStart(Character input){
        return LexConstants.Operator.BRACKET_START.equals(input);
    }

    public static boolean isBracketEnd(Character input){
        return LexConstants.Operator.BRACKET_END.equals(input);
    }

    public static boolean isBracketLegal(String input){

        if (StringUtils.containsNone(input, LexConstants.Operator.BRACKET_START.getValue(), LexConstants.Operator.BRACKET_END.getValue())){
            return true;
        }

        Stack<Integer> bracketStartIndexes = new Stack<>();
        for (int i = 0; i < input.length(); i++) {
            if (LexConstants.Operator.BRACKET_START.equals(input.charAt(i))){
                bracketStartIndexes.push(i);
            } else if (LexConstants.Operator.BRACKET_END.equals(input.charAt(i))){
                if (bracketStartIndexes.isEmpty()){
                    return false;
                }

                bracketStartIndexes.pop();
            }
        }

        return bracketStartIndexes.isEmpty();
    }

    public static void printNFA(State start){

    }
}
