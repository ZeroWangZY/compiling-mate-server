package ecnu.compiling.compilingmate.lex.exception;

public class IllegalTokenException extends BadUserInputException {

    private static final String errorCode = "1102";

    public IllegalTokenException(String input) {
        super(String.format("Illegal token is scanned : [%s]",input));
    }
}
