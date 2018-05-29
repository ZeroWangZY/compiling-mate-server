package ecnu.compiling.compilingmate.lex.analyzer;


import ecnu.compiling.compilingmate.lex.policy.rule.Rule;

public interface LexAnalyzer<From, To> {
    To analyze(From input, Rule rule);
}
