package ecnu.compiling.compilingmate.service;

import ecnu.compiling.compilingmate.lox.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("CoService")
public class CoServiceImpl implements CoService {


    @Override
    public Map<String, Object> runCode(String code) {
        Map<String, Object> result = new HashMap<>();
        final Interpreter interpreter = new Interpreter();
        Scanner scanner = new Scanner(code);
        List<Token> tokens = scanner.scanTokens();
        result.put("tokens", tokens);
        // For now, just print the tokens.
//        for (Token token : tokens) {
////            System.out.println(token);
////        }

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();
//        result.put("statements", statements);

        interpreter.interpret(statements);
        result.put("output", interpreter.getOutput());

        return result;

    }
}
