package ecnu.compiling.compilingmate.lex.policy.scanner;

import com.google.common.collect.Lists;
import ecnu.compiling.compilingmate.lex.entity.token.TokenKind;
import ecnu.compiling.compilingmate.lex.entity.token.TokenizedPhrase;
import ecnu.compiling.compilingmate.lex.exception.IllegalTokenException;
import ecnu.compiling.compilingmate.lex.policy.rule.CustomizedRule;
import ecnu.compiling.compilingmate.lex.policy.rule.SimpleLanguageRule;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LanguageScanner extends AbstractScanner {
    public LanguageScanner(CustomizedRule rule) {
        super(rule);
    }

    public List<TokenizedPhrase> parseAsPhrases(String input) {
        List<TokenizedPhrase> tokenizedPhrases = Lists.newArrayList();

        List<String> phrases = this.breakPhrases(input);
        for (int i = 0; i < phrases.size(); i++) {
            tokenizedPhrases.add(new TokenizedPhrase(i+1, this.parse(phrases.get(i))));
        }

        Collections.sort(tokenizedPhrases);
        return tokenizedPhrases;
    }

    @Override
    protected List<String> breakInput(String input) {
        String[] preProcessed = input.split(" ");

        List<String> results = Lists.newArrayList();

        for (String preProcessedStr : preProcessed) {
            if (StringUtils.isNotEmpty(preProcessedStr)){
                results.addAll(doSpit(preProcessedStr));
            }
        }

        return results;
    }

    private List<String> breakPhrases(String input){
        if (StringUtils.isNotEmpty(input) && rule.getPhraseBreaker() != null) {
            String[] afterSplit = input.split(rule.getPhraseBreaker().getContent());
            List<String> result = Lists.newArrayList();
            Arrays.stream(afterSplit).forEach(str -> {
                str = str.trim();
                str += rule.getPhraseBreaker().getContent();
                result.add(str);
            });
            return result;
        }

        return Lists.newArrayList();
    }

    private List<String> doSpit(String smallInput){
        smallInput = StringUtils.deleteWhitespace(smallInput);
        List<String> results = Lists.newArrayList();

        int currentStartIndex = 0;
        while (currentStartIndex < smallInput.length()) {
            int currentEndIndex = smallInput.length();

            while (currentEndIndex > currentStartIndex) {
                String subStr = smallInput.substring(currentStartIndex, currentEndIndex);
                TokenKind kind = rule.getTokenKind(subStr);
                if (kind != TokenKind.UNKNOWN) {
                    results.add(subStr);
                    break;
                } else {
                    currentEndIndex--;
                }
            }

            if (currentEndIndex == currentStartIndex){
                throw new IllegalTokenException(String.valueOf(smallInput.charAt(currentEndIndex)));
            }

            currentStartIndex = currentEndIndex;
        }

        return results;
    }
}
