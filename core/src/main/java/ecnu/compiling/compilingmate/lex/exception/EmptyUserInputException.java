package ecnu.compiling.compilingmate.lex.exception;

public class EmptyUserInputException extends BadUserInputException {

    private static final String errorCode = "1101";

    public EmptyUserInputException() {
        super("input string is blank!");
    }
}
