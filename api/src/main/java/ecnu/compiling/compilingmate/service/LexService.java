package ecnu.compiling.compilingmate.service;

import ecnu.compiling.compilingmate.entity.TompsonData;
import ecnu.compiling.compilingmate.lex.policy.rule.Rule;

public interface LexService {
    Rule getRuleByName(String ruleName);
    TompsonData getReToDfaByTompson(String input, Rule rule);
}
