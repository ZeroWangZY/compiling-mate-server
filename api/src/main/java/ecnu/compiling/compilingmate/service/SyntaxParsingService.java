package ecnu.compiling.compilingmate.service;

import java.util.Map;

public interface SyntaxParsingService {
    Map<String,Object> getSlrParsingOutput(int type);
}
