package ecnu.compiling.compilingmate.lex.policy.rule;

import ecnu.compiling.compilingmate.lex.constants.LexConstants;
import ecnu.compiling.compilingmate.lex.entity.Token;

import java.util.HashSet;
import java.util.Set;

public abstract class Rule {

    private Set<Token> operators;

    private Set<Token> normalCharacters;

    private Set<Token> specialCharacters;

    private Set<Token> breakers;

    private Token emptyInput;

    public Rule() {
        operators = new HashSet<>();
        normalCharacters = new HashSet<>();
        specialCharacters = new HashSet<>();
        breakers = new HashSet<>();
        emptyInput = LexConstants.EMPTY;
    }

    public Token getEmptyInput() {
        return emptyInput;
    }

    public void setEmptyInput(Token emptyInput) {
        this.emptyInput = emptyInput;
    }

    public void addOperator(Token token){
        operators.add(token);
    }

    public void addCharacter(Token token){
        normalCharacters.add(token);
    }

    public void addSpecialCharacter(Token token){
        specialCharacters.add(token);
    }

    public void addBreaker(Token token){
        breakers.add(token);
    }

    public Set<Token> getCharacters() {
        return normalCharacters;
    }

    public Set<Token> getOperators() {
        return operators;
    }

    public Set<Token> getSpecialCharacters() {
        return specialCharacters;
    }

    public Set<Token> getBreakers() {
        return breakers;
    }

    public boolean isNormalCharacter(String token){
        return this.normalCharacters.contains(new Token(token));
    }

    public boolean isSpecialCharacter(String token){
        return this.specialCharacters.contains(new Token(token));
    }

    public boolean isCharacter(String token){
        return this.isNormalCharacter(new Token(token)) || this.isSpecialCharacter(new Token(token));
    }

    public boolean isOperator(String token){
        return this.operators.contains(new Token(token));
    }

    public boolean isBreaker(String token){
        return this.breakers.contains(new Token(token));
    }

    public boolean isEmptyCharacter(Token token){
        return this.emptyInput.equals(token);
    }

    public boolean isNormalCharacter(Token token){
        return this.normalCharacters.contains(token);
    }

    public boolean isSpecialCharacter(Token token){
        return this.specialCharacters.contains(token);
    }

    public boolean isCharacter(Token token){
        return this.isNormalCharacter(token) || this.isSpecialCharacter(token);
    }

    public boolean isOperator(Token token){
        return this.operators.contains(token);
    }

    public boolean isBreaker(Token token){
        return this.breakers.contains(token);
    }

    public boolean isTokenLeagal(Token token){
        return this.isCharacter(token) || this.isOperator(token) || this.isBreaker(token);
    }
}
