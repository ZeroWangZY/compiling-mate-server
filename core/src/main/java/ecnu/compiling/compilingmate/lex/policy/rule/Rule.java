package ecnu.compiling.compilingmate.lex.policy.rule;

import ecnu.compiling.compilingmate.lex.constants.LexConstants;
import ecnu.compiling.compilingmate.lex.entity.Token;
import ecnu.compiling.compilingmate.lex.entity.TokenKind;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Rule {

    protected Map<String, TokenKind> operators;

    protected Map<String, TokenKind> normalCharacters;

    protected Map<String, TokenKind> specialCharacters;

    protected Token phraseBreaker;

    protected Token emptyInput;

    public Rule() {
        operators = new HashMap<>();
        normalCharacters = new HashMap<>();
        specialCharacters = new HashMap<>();
        emptyInput = LexConstants.EMPTY;
    }

    public Token getEmptyInput() {
        return emptyInput;
    }

    public void setEmptyInput(Token emptyInput) {
        this.emptyInput = emptyInput;
    }

    public void addOperator(String token, TokenKind tokenKind){
        operators.put(token, tokenKind);
    }

    public void addCharacter(String token, TokenKind tokenKind){
        normalCharacters.put(token, tokenKind);
    }

    public void addSpecialCharacter(String token, TokenKind tokenKind){
        specialCharacters.put(token, tokenKind);
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
        return this.isCharacter(token) || this.isOperator(token) || this.isBreaker(token);
    }


    public TokenKind getTokenKind(String token){
        if (this.isBreaker(token))
            return TokenKind.PHRASE_BREAKER;

        for (Map.Entry<String, TokenKind> e : this.operators.entrySet()) {
            if (this.matchKey(e.getKey(), token)){
                return e.getValue();
            }
        }
        for (Map.Entry<String, TokenKind> e : this.specialCharacters.entrySet()) {
            if (this.matchKey(e.getKey(), token)){
                return e.getValue();
            }
        }
        for (Map.Entry<String, TokenKind> e : this.normalCharacters.entrySet()) {
            if (this.matchKey(e.getKey(), token)){
                return e.getValue();
            }
        }

        return TokenKind.UNKNOWN;
    }

    protected boolean matchKey(String key, String token) {
        return key.equals(token);
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
