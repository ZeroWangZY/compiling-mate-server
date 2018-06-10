package ecnu.compiling.compilingmate.controller;

import ecnu.compiling.compilingmate.service.SyntaxParsingService;
import ecnu.compiling.compilingmate.service.SyntaxParsingServiceImpl;
import ecnu.compiling.compilingmate.synEntity.RequestDto;
import ecnu.compiling.compilingmate.synEntity.Result;
import ecnu.compiling.compilingmate.syntax.entity.Production;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/syntax")
public class SyntaxController {
    @Resource
    SyntaxParsingService syntaxParsingService=new SyntaxParsingServiceImpl();

    @RequestMapping("/parsingProcessingOutput")
    @ResponseBody
    //            @RequestParam("production")Production[] productions,
    //            @RequestParam("startSymbol") String startSymbol,
    //            @RequestParam("type") int type // 0:SLR, 1:LR

    public Result parsingProcessingOutput(@RequestBody RequestDto requestDto){
        Result result=new Result();
        try {
            result.setData(syntaxParsingService.getParsingOutput(requestDto));
            result.setSuccess(true);
        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }


}
