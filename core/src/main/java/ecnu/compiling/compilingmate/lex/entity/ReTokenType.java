package ecnu.compiling.compilingmate.lex.entity;

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
}
