package ecnu.compiling.compilingmate.lex.policy;

import ecnu.compiling.compilingmate.lex.policy.rule.DefaultReRule;
import ecnu.compiling.compilingmate.lex.policy.rule.Rule;
import ecnu.compiling.compilingmate.lex.policy.scanner.AbstractScanner;
import ecnu.compiling.compilingmate.lex.policy.scanner.DefaultReScanner;
import ecnu.compiling.compilingmate.lex.policy.scanner.LexScanner;

public final class ScannerFactory {

    public static DefaultReScanner getLexScanner() {
        return new DefaultReScanner();
    }

    public static LexScanner getLexScanner(Rule rule){
        if (rule == null){
            return ScannerFactory.getLexScanner();
        }
        if (rule instanceof DefaultReRule){
            return getLexScanner();
        }
        return new AbstractScanner(rule) {};
    }
}
