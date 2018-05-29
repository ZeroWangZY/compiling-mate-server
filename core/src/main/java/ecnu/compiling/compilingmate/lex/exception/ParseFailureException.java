package ecnu.compiling.compilingmate.lex.exception;

public class ParseFailureException extends RuntimeException {
    private static final String errorCode = "100";
    private String msg;

    public ParseFailureException(String msg) {
        super(String.format("Parse failure : %s", msg));
        this.msg = msg;
    }
}
