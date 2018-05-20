package ecnu.compiling.compilingmate.lex.analyzer;


public interface LexAnalyzer<From, To> {
    To analyze(From input);
}
