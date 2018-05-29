package ecnu.compiling.compilingmate.lex.constants;

import ecnu.compiling.compilingmate.lex.entity.Token;

public interface LexConstants {

    Token EMPTY = new Token(SpecialToken.EMPTY.getValue());

    enum Operator{
        OR("|"),
        AND("."),
        BRACKET_START("("),
        BRACKET_END(")");

        String value;

        private Operator(String value){
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

        private SpecialToken(String value){
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
