package ecnu.compiling.compilingmate.lex.policy.rule;

import ecnu.compiling.compilingmate.lex.constants.LexConstants;
import ecnu.compiling.compilingmate.lex.entity.ReTokenType;
import ecnu.compiling.compilingmate.lex.entity.Token;

import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * 单个字母或数字为一个token
 */
public class DefaultReRule extends Rule{

    public DefaultReRule(){
        super();
        for (ReTokenType tokenType : ReTokenType.values()){
            switch (tokenType.getTokenKind()){
                case OPERATOR:
                    this.addOperator(tokenType.getValue(), tokenType.getTokenKind());
                    break;
                case SPECIAL_VALUE:
                    this.addSpecialCharacter(tokenType.getValue(), tokenType.getTokenKind());
                case VALUE:
                    this.addCharacter(tokenType.getValue(), tokenType.getTokenKind());
            }
        }
    }

    @Override
    protected boolean matchKey(String key, String token){
        return Pattern.matches(key, token);
    }

    public boolean isAnd(Token input){
        if (input == null) return false;

        return this.isAnd(input.getContent());
    }

    public boolean isOr(Token input){
        if (input == null) return false;

        return this.isOr(input.getContent());
    }

    public boolean isRepeatOrNone(Token input){
        if (input == null) return false;

        return this.isRepeatOrNone(input.getContent());
    }


    public boolean isBracket(Token input){
        return this.isBracketStart(input) || this.isBracketEnd(input);
    }

    public boolean isBracketStart(Token input){
        return this.isBracketStart(input.getContent());
    }

    public boolean isBracketEnd(Token input){
        return this.isBracketEnd(input.getContent());
    }

    public boolean isAnd(String input){
        return LexConstants.Operator.AND.getValue().equals(input);
    }

    public boolean isOr(String input){
        return LexConstants.Operator.OR.getValue().equals(input);
    }

    public boolean isRepeatOrNone(String input){
        return LexConstants.SpecialToken.REPEAT_OR_NONE.getValue().equals(input);
    }

    public boolean isBracket(String input){
        return this.isBracketStart(input) || this.isBracketEnd(input);
    }

    public boolean isBracketStart(String input){
        return LexConstants.Operator.BRACKET_START.getValue().equals(input);
    }

    public boolean isBracketEnd(String input){
        return LexConstants.Operator.BRACKET_END.getValue().equals(input);
    }

    public boolean isBracketLegal(List<Token> input){

        Stack<Integer> bracketStartIndexes = new Stack<>();
        for (int i = 0; i < input.size(); i++) {
            if (this.isBracketStart(input.get(i))){
                bracketStartIndexes.push(i);
            } else if (this.isBracketEnd(input.get(i))){
                if (bracketStartIndexes.isEmpty()){
                    return false;
                }

                bracketStartIndexes.pop();
            }
        }

        return bracketStartIndexes.isEmpty();
    }


}
