package ecnu.compiling.compilingmate.lex.exception;


public class ConstructStateFailureException extends RuntimeException {
    private static final String errorCode = "101";
    private String msg;

    public ConstructStateFailureException(String msg) {
        super(String.format("Parse failure : %s", msg));
        this.msg = msg;
    }
}
