package ecnu.compiling.compilingmate.controller;

import ecnu.compiling.compilingmate.service.SyntaxParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller("/syntax")
public class SyntaxController {
    @Autowired
    SyntaxParsingService syntaxParsingService;

}
