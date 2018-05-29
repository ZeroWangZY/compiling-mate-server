package ecnu.compiling.compilingmate.lex.policy.scanner;

import ecnu.compiling.compilingmate.lex.constants.LexConstants;
import ecnu.compiling.compilingmate.lex.entity.Token;
import ecnu.compiling.compilingmate.lex.policy.rule.DefaultReRule;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class DefaultReScanner extends AbstractScanner{

    public DefaultReScanner() {
        super(new DefaultReRule());
    }

    @Override
    protected List<String> breakInput(String input){
        if (StringUtils.isBlank(input)){
            throw new EmptyStackException();
        }

        char[] chars = this.preProcess(input).toCharArray();

        List<String> results = new ArrayList<>();

        for (char character : chars) {
            results.add(String.valueOf(character));
        }

        return results;
    }

    /**
     * 1、删去所有空格
     * 2、补充省略的and符号
     *
     * @param input
     * @return
     */
    private String preProcess(String input){
        DefaultReRule defaultRule = (DefaultReRule) rule;
        input = StringUtils.deleteWhitespace(input);
        // 在相邻两个子母间加上and符号，方便处理
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length() - 1; i++) {
            char currentChar = input.charAt(i);
            builder.append(currentChar);

            if ((defaultRule.isCharacter(String.valueOf(currentChar)) ||
                    defaultRule.isBracketEnd(String.valueOf(currentChar)))
                    && defaultRule.isNormalCharacter(String.valueOf(input.charAt(i+1)))){
                builder.append(LexConstants.Operator.AND.getValue());
            }
        }
        builder.append(input.charAt(input.length()-1));
        return builder.toString();
    }
}
