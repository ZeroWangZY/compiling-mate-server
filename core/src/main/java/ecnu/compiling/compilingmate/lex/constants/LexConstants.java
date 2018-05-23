package ecnu.compiling.compilingmate.lex.constants;

public interface LexConstants {

    Character EMPTY_CHAR = 'e';

    enum Operator{
        OR('|'),
        AND('.'),
        BRACKET_START('('),
        BRACKET_END(')');

        Character value;

        private Operator(char value){
            this.value = value;
        }

        public char getValue(){
            return this.value;
        }

        public boolean equals(Character value){
            return this.value == value;
        }
    }

    enum SpecialCharacter{
        REPEAT_OR_NONE('*'),
        EMPTY('e');

        Character value;

        private SpecialCharacter(char value){
            this.value = value;
        }

        public char getValue(){
            return this.value;
        }

        public boolean equals(Character value){
            return this.value == value;
        }
    }
}
