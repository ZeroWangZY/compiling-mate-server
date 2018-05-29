package ecnu.compiling.compilingmate.lex.policy.scanner;

import ecnu.compiling.compilingmate.lex.entity.Token;
import ecnu.compiling.compilingmate.lex.exception.IllegalTokenException;
import ecnu.compiling.compilingmate.lex.policy.rule.Rule;

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
        List<String> tokens = new ArrayList<>();
        tokens.add(input);
        for (Token breaker : rule.getBreakers()) {
            List<String> newTokens = new ArrayList<>();
            for (String token : tokens){
                for (String afterBroken : token.split(breaker.getContent())) {
                    newTokens.add(afterBroken);
                }
            }

            tokens = newTokens;

        }

        return tokens;
    }

    protected Token convert(String str) throws IllegalTokenException{
        Token token = new Token(str);
        if (!rule.isTokenLeagal(token)){
            throw new IllegalTokenException(str);
        }
        return token;
    }
}
