package ecnu.compiling.compilingmate.lex.entity.token;

public class LanguageDefinition {
    private String name;
    private String reExpression;
    private TokenKind tokenKind;

    public LanguageDefinition() {
    }

    public LanguageDefinition(String name, String reExpression, TokenKind tokenKind) {
        this.name = name;
        this.reExpression = reExpression;
        this.tokenKind = tokenKind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReExpression() {
        return reExpression;
    }

    public void setReExpression(String re) {
        this.reExpression = re;
    }

    public TokenKind getTokenKind() {
        return tokenKind;
    }

    public void setTokenKind(TokenKind tokenKind) {
        this.tokenKind = tokenKind;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LanguageDefinition){
            LanguageDefinition other = (LanguageDefinition) obj;
            return this.reExpression.equals(other.reExpression);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.reExpression.hashCode();
    }
}
