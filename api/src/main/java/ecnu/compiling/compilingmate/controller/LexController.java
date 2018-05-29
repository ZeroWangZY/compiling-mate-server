package ecnu.compiling.compilingmate.controller;

import ecnu.compiling.compilingmate.entity.DfaData;
import ecnu.compiling.compilingmate.entity.Result;
import ecnu.compiling.compilingmate.entity.TompsonData;
import ecnu.compiling.compilingmate.lex.dto.ReToNfaDto;
import ecnu.compiling.compilingmate.lex.policy.rule.Rule;
import ecnu.compiling.compilingmate.service.LexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
@RequestMapping("/lex")
public class LexController {

    @Resource
    LexService lexService;

    @RequestMapping("/reProcessingOutput")
    @ResponseBody
    public Result reProcessingOutput(@RequestParam("input") String input, @RequestParam("ruleName") String ruleName){
        Result result = new Result();

        try {
            Rule rule = lexService.getRuleByName(ruleName);
            result.setSuccess(true);
            result.setData(lexService.fullLexAnalyzeByTompsonAndSubsetConstruction(input, rule));
        } catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }

        return result;
    }

}
