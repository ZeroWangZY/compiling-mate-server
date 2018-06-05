package ecnu.compiling.compilingmate.controller;

import ecnu.compiling.compilingmate.service.SyntaxParsingService;
import ecnu.compiling.compilingmate.service.SyntaxParsingServiceImpl;
import ecnu.compiling.compilingmate.synEntity.Result;
import ecnu.compiling.compilingmate.syntax.entity.Production;
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
    //            @RequestParam("production")Production[] productions,
    //            @RequestParam("startSymbol") String startSymbol,
    //            @RequestParam("type") int type // 0:SLR, 1:LR

    public Result parsingProcessingOutput(){
        Result result=new Result();
        result.setData(syntaxParsingService.getSlrParsingOutput(1));
        result.setSuccess(true);
        return result;
    }


    @RequestMapping("/lrParsingOutput")
    @ResponseBody
    public Result lrParsingOutput(){
        Result result=new Result();
        result.setData(syntaxParsingService.getSlrParsingOutput(1));
        result.setSuccess(true);
        return result;
    }
}
