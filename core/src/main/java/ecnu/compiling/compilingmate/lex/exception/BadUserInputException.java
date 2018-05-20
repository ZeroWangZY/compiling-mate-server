package ecnu.compiling.compilingmate.lex.exception;

public class BadUserInputException extends RuntimeException {
    private static final String errorCode = "110";
    private String input;

    public BadUserInputException(String input) {
        super(String.format("bad user input : %s", input));
        this.input = input;
    }
}
