package ecnu.compiling.compilingmate.lex.analyzer;

import ecnu.compiling.compilingmate.lex.constants.LexConstants;
import ecnu.compiling.compilingmate.lex.dto.ReToNfaDto;
import ecnu.compiling.compilingmate.lex.exception.BadUserInputException;
import ecnu.compiling.compilingmate.lex.utils.LexUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class ReToNfaAnalyzer implements LexAnalyzer<String, ReToNfaDto> {

    /**
     * 主流程（对外接口）
     *
     * @param input
     * @return
     */
    @Override
    public ReToNfaDto analyze(String input) throws BadUserInputException{
        if (!this.isInputLegal(input)){
            throw new BadUserInputException(input);
        }

        String formattedInput = this.preProcess(input);
        return this.process(formattedInput);
    }

    private boolean isInputLegal(String input){
        // 去空格
        input = StringUtils.deleteWhitespace(input);

        // 字符串不为空
        if (StringUtils.isEmpty(input)){
            return false;
        }

        // 左右括号可以匹配；首字母不为*或操作符
        return LexUtils.isBracketLegal(input) &&
                !LexUtils.isOperator(input.charAt(0)) &&
                !LexUtils.isRepeatOrNone(input.charAt(0));
    }


    /**
     * 1、删去所有空格
     * 2、补充省略的and符号
     *
     * @param input
     * @return
     */
    private String preProcess(String input){
        // 去空格
        input = StringUtils.deleteWhitespace(input);


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

    protected abstract ReToNfaDto process(String input);


}
