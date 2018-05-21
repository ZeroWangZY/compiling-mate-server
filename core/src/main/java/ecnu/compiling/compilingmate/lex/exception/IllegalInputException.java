package ecnu.compiling.compilingmate.lex.exception;

import com.google.gson.Gson;

public class IllegalInputException extends RuntimeException {
    private static final String errorCode = "101";
    private Object[] input;

    public IllegalInputException(Object... input) {
        super(String.format("illegal input: %s", new Gson().toJson(input)));
        this.input = input;
    }
}
