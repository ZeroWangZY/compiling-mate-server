package ecnu.compiling.compilingmate.lex.analyzer;

import ecnu.compiling.compilingmate.lex.exception.BadUserInputException;
import ecnu.compiling.compilingmate.lex.policy.rule.DefaultReRule;
import ecnu.compiling.compilingmate.lex.policy.rule.Rule;

public abstract class AbstractAnalyzer<From, To> implements LexAnalyzer<From, To> {

    private Rule rule;

    @Override
    public To analyze(From input, Rule rule) throws BadUserInputException {
        this.rule = rule == null ? new DefaultReRule() : rule;
        return this.process(input);
    }

    protected Rule getRule(){
        return this.rule;
    }

    protected abstract To process(From input);

}
