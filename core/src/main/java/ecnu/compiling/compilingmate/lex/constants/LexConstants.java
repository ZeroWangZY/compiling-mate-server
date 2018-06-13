package ecnu.compiling.compilingmate.lex.constants;

import ecnu.compiling.compilingmate.lex.entity.token.Token;
import ecnu.compiling.compilingmate.lex.entity.token.TokenKind;

public interface LexConstants {

    Token EMPTY = new Token("RE_EMPTY", SpecialToken.EMPTY.getValue(), TokenKind.SPECIAL_VALUE);

    Token DEFAULT_PHRASE_BREAKER = new Token("SEMICOLON",";", TokenKind.PHRASE_BREAKER);

    enum Operator{
        OR("|"),
        AND("."),
        BRACKET_START("("),
        BRACKET_END(")");

        String value;

        Operator(String value){
            this.value = value;
        }

        public String getValue(){
            return this.value;
        }

        public boolean equals(String value){
            return this.value.equals(value);
        }
    }

    enum SpecialToken{
        REPEAT_OR_NONE("*"),
        EMPTY("%EMPTY%");

        String value;

        SpecialToken(String value){
            this.value = value;
        }

        public String getValue(){
            return this.value;
        }

        public boolean equals(String value){
            return this.value == value;
        }
    }
}
