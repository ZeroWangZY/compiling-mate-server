package ecnu.compiling.compilingmate.lex.policy.rule;

import ecnu.compiling.compilingmate.lex.entity.token.LanguageTokenType;

import java.util.regex.Pattern;

public class SimpleLanguageRule extends CustomizedRule {

    public SimpleLanguageRule(){
        super(LanguageTokenType.getLangDefs());
    }

}
