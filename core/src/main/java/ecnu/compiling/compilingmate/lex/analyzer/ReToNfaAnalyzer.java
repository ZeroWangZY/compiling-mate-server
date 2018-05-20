package ecnu.compiling.compilingmate.lex.analyzer;

import ecnu.compiling.compilingmate.lex.constants.LexConstants;
import ecnu.compiling.compilingmate.lex.utils.LexUtils;
import ecnu.compiling.compilingmate.lex.entity.StateUnit;
import org.apache.commons.lang3.StringUtils;

public abstract class ReToNfaAnalyzer implements LexAnalyzer<String, StateUnit> {

    /**
     * 主流程（对外接口）
     *
     * @param input
     * @return
     */
    @Override
    public StateUnit analyze(String input){
        String formattedInput = this.preProcess(input);
        return this.process(formattedInput);
    }


    /**
     * 1、删去所有空格
     * 2、检查括号是否合法
     * 3、补充省略的and符号
     *
     * @param input
     * @return
     */
    protected String preProcess(String input){
        // 去空格
        input = StringUtils.deleteWhitespace(input);

        if (!LexUtils.isBracketLegal(input)){
            return "";
        }

        // 在相邻两个子母间加上and符号，方便处理
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length() - 1; i++) {
            Character currentChar = input.charAt(i);
            builder.append(currentChar);

            if ((LexUtils.isCharacter(currentChar) || LexUtils.isBracketEnd(currentChar))
                    && LexUtils.isNormalCharacter(input.charAt(i+1))){
                builder.append(LexConstants.Operator.AND.getValue());
            }
        }
        builder.append(input.charAt(input.length()-1));
        return builder.toString();
    }

    protected abstract StateUnit process(String input);


}
