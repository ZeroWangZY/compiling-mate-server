package ecnu.compiling.compilingmate.lex.policy.scanner;

import ecnu.compiling.compilingmate.lex.entity.token.Token;

import java.util.List;

public interface LexScanner {
    List<Token> parse(String input);
}
