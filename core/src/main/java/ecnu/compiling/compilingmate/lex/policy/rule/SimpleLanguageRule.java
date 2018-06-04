package ecnu.compiling.compilingmate.lex.policy.rule;

import ecnu.compiling.compilingmate.lex.entity.LanguageTokenType;
import ecnu.compiling.compilingmate.lex.entity.Token;
import ecnu.compiling.compilingmate.lex.entity.TokenKind;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class SimpleLanguageRule extends Rule {

    public SimpleLanguageRule(){
        super();
        for (LanguageTokenType tokenType : LanguageTokenType.values()){
            switch (tokenType.getTokenKind()){
                case KEYWORD:
                    this.addSpecialCharacter(tokenType.getRegularExpression(), tokenType.getTokenKind());
                    break;
                case OPERATOR:
                case BRACKET:
                    this.addOperator(tokenType.getRegularExpression(), tokenType.getTokenKind());
                    break;
                case IDENTIFIER:
                case SPECIAL_VALUE:
                case VALUE:
                    this.addCharacter(tokenType.getRegularExpression(), tokenType.getTokenKind());
                    break;
                case PHRASE_BREAKER:
                    this.setPhraseBreaker(new Token(tokenType.getRegularExpression(), tokenType.getTokenKind()));
                    break;
            }
        }
    }

    @Override
    protected boolean matchKey(String key, String token){
        return Pattern.matches(key, token);
    }

}
