package ecnu.compiling.compilingmate.controller;

import ecnu.compiling.compilingmate.service.SyntaxParsingService;
import ecnu.compiling.compilingmate.service.SyntaxParsingServiceImpl;
import ecnu.compiling.compilingmate.synEntity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller("/syntax")
public class SyntaxController {
    @Resource
    SyntaxParsingService syntaxParsingService=new SyntaxParsingServiceImpl();

    @RequestMapping("/parsingProcessingOutput")
    @ResponseBody
    public Result parsingProcessingOutput(){
        Result result=new Result();
        result.setData(syntaxParsingService.getSlrParsingOutput());
        result.setSuccess(true);
        return result;
    }


    @RequestMapping("/lrParsingOutput")
    @ResponseBody
    public Result lrParsingOutput(){
        Result result=new Result();
        result.setData(syntaxParsingService.getLrParsingOutput());
        result.setSuccess(true);
        return result;
    }
}
