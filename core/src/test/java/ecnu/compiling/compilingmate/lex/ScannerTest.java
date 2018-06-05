package ecnu.compiling.compilingmate.lex;

import ecnu.compiling.compilingmate.lex.entity.tree.TokenizedPhrase;
import ecnu.compiling.compilingmate.lex.policy.rule.SimpleLanguageRule;
import ecnu.compiling.compilingmate.lex.policy.scanner.SimpleLanguageScanner;
import junit.framework.TestCase;

import java.util.List;

public class ScannerTest extends TestCase {
    public void test(){
        SimpleLanguageScanner scanner = new SimpleLanguageScanner();

        String case1 = "int i = 0;\n i++; i=7;";
        List<TokenizedPhrase> tokenizedPhrases = scanner.parseAsPhrases(case1);

        for (TokenizedPhrase phrase : tokenizedPhrases) {
            System.out.println("Phrase" + phrase.getSeqNo() + ":");
            phrase.getTokens().forEach(token -> System.out.println(token));
            System.out.println();
        }
    }

    public void testRule(){
        SimpleLanguageRule rule = new SimpleLanguageRule();

        assertTrue(rule.isNormalCharacter("a"));
        assertTrue(rule.isNormalCharacter("abs"));
        assertTrue(rule.isNormalCharacter("ab12"));
        assertTrue(rule.isNormalCharacter("_a"));
        assertFalse(rule.isNormalCharacter("a=1"));

        assertTrue(rule.isOperator("+"));
        assertTrue(rule.isOperator("*"));
        assertTrue(rule.isOperator("*="));
        assertTrue(rule.isOperator("("));
    }
}
