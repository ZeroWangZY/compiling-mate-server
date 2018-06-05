package ecnu.compiling.compilingmate.lex.policy.rule;

import ecnu.compiling.compilingmate.lex.constants.LexConstants;
import ecnu.compiling.compilingmate.lex.entity.token.LanguageDefinition;
import ecnu.compiling.compilingmate.lex.entity.token.Token;
import ecnu.compiling.compilingmate.lex.entity.token.TokenKind;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class Rule {

    protected Map<String, LanguageDefinition> operators;

    protected Map<String, LanguageDefinition> normalCharacters;

    protected Map<String, LanguageDefinition> specialCharacters;

    protected Token phraseBreaker;

    protected Token emptyInput;

    public Rule(Set<LanguageDefinition> defSet) {
        operators = new HashMap<>();
        normalCharacters = new HashMap<>();
        specialCharacters = new HashMap<>();
        emptyInput = LexConstants.EMPTY;
        for (LanguageDefinition tokenType : defSet){
            switch (tokenType.getTokenKind()){
                case KEYWORD:
                    this.addSpecialCharacter(tokenType.getReExpression(), tokenType);
                    break;
                case OPERATOR:
                case BRACKET:
                    this.addOperator(tokenType.getReExpression(), tokenType);
                    break;
                case SPECIAL_VALUE:
                case IDENTIFIER:
                case VALUE:
                    this.addCharacter(tokenType.getReExpression(), tokenType);
                    break;
                case PHRASE_BREAKER:
                    this.setPhraseBreaker(new Token(tokenType.getName(), tokenType.getReExpression(), tokenType.getTokenKind()));
                    break;
            }
        }
    }

    public Token getEmptyInput() {
        return emptyInput;
    }

    public void setEmptyInput(Token emptyInput) {
        this.emptyInput = emptyInput;
    }

    public void addOperator(String token, LanguageDefinition def){
        operators.put(token, def);
    }

    public void addCharacter(String token, LanguageDefinition def){
        normalCharacters.put(token, def);
    }

    public void addSpecialCharacter(String token, LanguageDefinition def){
        specialCharacters.put(token, def);
    }

    public Token getPhraseBreaker() {
        return phraseBreaker;
    }

    public void setPhraseBreaker(Token phraseBreaker) {
        this.phraseBreaker = phraseBreaker;
    }

    public Set<String> getCharacters() {
        return normalCharacters.keySet();
    }

    public Set<String> getOperators() {
        return operators.keySet();
    }

    public Set<String> getSpecialCharacters() {
        return specialCharacters.keySet();
    }

    public boolean isNormalCharacter(String token){
        return this.existsInSet(this.normalCharacters.keySet(), token);
    }

    public boolean isSpecialCharacter(String token){
        return this.existsInSet(this.specialCharacters.keySet(), token);
    }

    public boolean isCharacter(String token){
        return this.isNormalCharacter(token) || this.isSpecialCharacter(token);
    }

    public boolean isOperator(String token){
        return this.existsInSet(this.operators.keySet(), token);
    }

    public boolean isBreaker(String token){
        return this.phraseBreaker != null && phraseBreaker.getContent().equals(token);
    }

    public boolean isEmptyCharacter(Token token){
        return this.emptyInput.equals(token);
    }

    public boolean isNormalCharacter(Token token){
        return this.isNormalCharacter(token.getContent());
    }

    public boolean isSpecialCharacter(Token token){
        return this.isSpecialCharacter(token.getContent());
    }

    public boolean isCharacter(Token token){
        return this.isNormalCharacter(token) || this.isSpecialCharacter(token);
    }

    public boolean isOperator(Token token){
        return this.isOperator(token.getContent());
    }

    public boolean isBreaker(Token token){
        return this.isBreaker(token.getContent());
    }

    public boolean isTokenLeagal(Token token){
        if (token == null || token.getTokenKind() == null || token.getTokenKind().equals(TokenKind.UNKNOWN)){
            return false;
        }
        return this.isCharacter(token) || this.isOperator(token) || this.isBreaker(token);
    }

    public String getDefName(String token){
        return this.getDefine(token).getName();
    }

    public TokenKind getTokenKind(String token){
        return this.getDefine(token).getTokenKind();
    }

    public LanguageDefinition getDefine(String token){
        if (this.isBreaker(token))
            return new LanguageDefinition(phraseBreaker.getName(), phraseBreaker.getContent(), TokenKind.PHRASE_BREAKER);

        if (this.emptyInput.getContent().equals(token))
            return new LanguageDefinition(emptyInput.getName(), emptyInput.getContent(), TokenKind.SPECIAL_VALUE);

        for (Map.Entry<String, LanguageDefinition> e : this.operators.entrySet()) {
            if (this.matchKey(e.getKey(), token)){
                return e.getValue();
            }
        }
        for (Map.Entry<String, LanguageDefinition> e : this.specialCharacters.entrySet()) {
            if (this.matchKey(e.getKey(), token)){
                return e.getValue();
            }
        }
        for (Map.Entry<String, LanguageDefinition> e : this.normalCharacters.entrySet()) {
            if (this.matchKey(e.getKey(), token)){
                return e.getValue();
            }
        }

        return new LanguageDefinition("UNDEFINED", token, TokenKind.UNKNOWN);
    }

    protected boolean matchKey(String key, String token){
        return Pattern.matches(key, token);
    }

    private boolean existsInSet(Set<String> keys, String target){
        for (String key : keys) {
            if (this.matchKey(key, target)){
                return true;
            }
        }
        return false;
    }
}
