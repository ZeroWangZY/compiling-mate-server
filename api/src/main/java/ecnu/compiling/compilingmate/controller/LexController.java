package ecnu.compiling.compilingmate.controller;

import ecnu.compiling.compilingmate.lex.entity.token.LanguageDefinition;
import ecnu.compiling.compilingmate.entity.Result;
import ecnu.compiling.compilingmate.lex.policy.rule.Rule;
import ecnu.compiling.compilingmate.service.LexService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/lex")
public class LexController {

    @Resource
    LexService lexService;

    @RequestMapping(value = "/scan", method = RequestMethod.POST)
    @ResponseBody
    public Result scan(@RequestParam(value = "inputCode") String inputCode,
                       @RequestParam(value = "reDefs")Set<LanguageDefinition> reDefs){
        Result result = new Result();

        try {
            result.setSuccess(true);
            result.setData(lexService.scanCodeByRule(inputCode, reDefs));
        } catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/reProcessingOutput", method = RequestMethod.POST)
    @ResponseBody
    public Result reProcessingOutput(@RequestParam(value = "input") String input, @RequestParam(value = "ruleName", required = false, defaultValue = "") String ruleName){
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
