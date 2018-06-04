package ecnu.compiling.compilingmate.lex.entity;

import java.util.regex.Pattern;

public enum LanguageTokenType {

    BASIC_TYPE_CHAR("(char)", TokenKind.KEYWORD),

    BASIC_TYPE_INTEGER("(short)|(int)|(long)", TokenKind.KEYWORD),

    BASIC_TYPE_FLOATING("(float)|(double)", TokenKind.KEYWORD),

    BASIC_TYPE_BOOL("(boolean)", TokenKind.KEYWORD),

    REFERENCE("[a-zA-Z_]+[a-zA-Z0-9_]*", TokenKind.IDENTIFIER),

    OPERATOR_SELF("\\+\\+|\\-\\-|\\+=|\\-=|\\*=|\\/=|%=|\\&=|\\|=|\\^=|", TokenKind.OPERATOR),

    OPERATOR_EQUAL("=", TokenKind.OPERATOR),

    OPERATOR_LOGIC_TWO("\\&\\&|\\|\\|", TokenKind.OPERATOR),

    OPERATOR_LOGIC_ONE("!", TokenKind.OPERATOR),

    OPERATOR_MATH("[\\+\\-\\*/%]", TokenKind.OPERATOR),

    OPERATOR_RELATION("==|!=|>=|<=|>|<", TokenKind.OPERATOR),

    OPERATOR_BIT_MOVE("<<|>{2,3}|\\&|\\||\\^", TokenKind.OPERATOR),

    OPERATOR_BIT_CALCULATE("\\&|\\||\\^", TokenKind.OPERATOR),

    BRACKET_START("\\(", TokenKind.BRACKET),

    BRACKET_END("\\)", TokenKind.BRACKET),

    NUMBER("[0-9]+(\\.[0-9]*)?", TokenKind.VALUE),

    BASIC_VALUE_TRUE("(true)", TokenKind.KEYWORD),

    BASIC_VALUE_FALSE("(false)", TokenKind.KEYWORD),

    SEMICOLON(";", TokenKind.PHRASE_BREAKER);

    String regularExpression;

    TokenKind tokenKind;

    LanguageTokenType(String regularExpression, TokenKind tokenKind){
        this.regularExpression = regularExpression;
        this.tokenKind = tokenKind;
    }

    public boolean canMatch(String input){
        return Pattern.matches(this.regularExpression, input);
    }

    public String getRegularExpression() {
        return regularExpression;
    }

    public TokenKind getTokenKind() {
        return tokenKind;
    }

    public void setRegularExpression(String regularExpression) {
        this.regularExpression = regularExpression;
    }
}
