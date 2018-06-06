package ecnu.compiling.compilingmate.service;

import ecnu.compiling.compilingmate.entity.DfaData;
import ecnu.compiling.compilingmate.entity.TompsonData;
import ecnu.compiling.compilingmate.lex.entity.graph.StateGraph;
import ecnu.compiling.compilingmate.lex.entity.token.LanguageDefinition;
import ecnu.compiling.compilingmate.lex.entity.token.TokenizedPhrase;
import ecnu.compiling.compilingmate.lex.policy.rule.Rule;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LexService {
    Rule getRuleByName(String ruleName);
    Map<String, Object> fullLexAnalyzeByTompsonAndSubsetConstruction(String input, Rule rule);
    TompsonData getReToDfaByTompson(String input, Rule rule);
    DfaData getNfaToDfaBySubSet(StateGraph stateGraph);

    List<TokenizedPhrase> scanCodeByRule(String code, Set<LanguageDefinition> definitions);
}
