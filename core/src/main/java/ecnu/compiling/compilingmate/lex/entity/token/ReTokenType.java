package ecnu.compiling.compilingmate.lex.entity.token;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum  ReTokenType {
    OR("\\|", TokenKind.OPERATOR),
    AND("\\.", TokenKind.OPERATOR),
    BRACKET_START("\\(", TokenKind.OPERATOR),
    BRACKET_END("\\)", TokenKind.OPERATOR),
    REPEAT_OR_NONE("\\*", TokenKind.SPECIAL_VALUE),
    EMPTY("%EMPTY%", TokenKind.SPECIAL_VALUE),
    VALUE("[a-zA-Z0-9]+", TokenKind.VALUE);

    String value;

    TokenKind tokenKind;

    ReTokenType(String value, TokenKind tokenKind){
        this.value = value;
        this.tokenKind = tokenKind;
    }

    public String getValue(){
        return this.value;
    }

    public TokenKind getTokenKind() {
        return tokenKind;
    }

    public boolean equals(String value){
        return this.value == value;
    }

    public static Set<LanguageDefinition> getLangDefs(){
        return Arrays.stream(ReTokenType.values())
                .map(value -> new LanguageDefinition(
                        value.name(), value.getValue(), value.getTokenKind())
                ).collect(Collectors.toSet());
    }
}
