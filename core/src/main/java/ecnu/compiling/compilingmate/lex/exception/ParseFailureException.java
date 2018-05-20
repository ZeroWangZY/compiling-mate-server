package ecnu.compiling.compilingmate.lex.exception;

import com.google.gson.Gson;

public class ParseFailureException extends RuntimeException {
    private static final String errorCode = "100";
    private Object[] input;

    public ParseFailureException(Object... input) {
        super(String.format("Parse failure at : %s", new Gson().toJson(input)));
        this.input = input;
    }
}
