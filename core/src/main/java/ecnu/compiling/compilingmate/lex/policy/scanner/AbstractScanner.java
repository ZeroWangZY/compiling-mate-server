package ecnu.compiling.compilingmate.lex.policy.scanner;

import ecnu.compiling.compilingmate.lex.entity.token.Token;
import ecnu.compiling.compilingmate.lex.exception.IllegalTokenException;
import ecnu.compiling.compilingmate.lex.policy.rule.Rule;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractScanner implements LexScanner {

    protected Rule rule;

    public AbstractScanner(Rule rule){
        this.rule = rule;
    }

    @Override
    public List<Token> parse(String input) throws IllegalTokenException{
        List<Token> tokenList = new ArrayList<>();
        this.breakInput(input)
                .forEach(tokenStr -> tokenList.add(this.convert(tokenStr)));

        return tokenList;
    }

    protected List<String> breakInput(String input){
        String[] preProcessed = input.split(" ");

        List<String> tokens = new ArrayList<>();

        for (String str : preProcessed) {
            for (String afterBroken : str.split(this.rule.getPhraseBreaker().getContent())) {
                if (StringUtils.isNotEmpty(afterBroken)){
                    tokens.add(afterBroken);
                }
            }
        }


        return tokens;
    }

    protected Token convert(String str) throws IllegalTokenException{
        Token token = new Token(rule.getDefName(str), str, rule.getTokenKind(str));
        if (!rule.isTokenLeagal(token)){
            throw new IllegalTokenException(str);
        }
        return token;
    }
}
