package ecnu.compiling.compilingmate.lex.policy.rule;

import ecnu.compiling.compilingmate.lex.constants.LexConstants;
import ecnu.compiling.compilingmate.lex.entity.token.LanguageDefinition;

import java.util.Set;

public class CustomizedRule extends Rule {

    public CustomizedRule(Set<LanguageDefinition> defSet) {
        super(defSet);
        if (this.phraseBreaker == null){
            this.phraseBreaker = LexConstants.DEFAULT_PHRASE_BREAKER;
        }
    }

}
