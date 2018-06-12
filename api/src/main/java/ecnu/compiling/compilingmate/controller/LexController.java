package ecnu.compiling.compilingmate.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ecnu.compiling.compilingmate.lex.entity.token.LanguageDefinition;
import ecnu.compiling.compilingmate.entity.Result;
import ecnu.compiling.compilingmate.lex.policy.rule.Rule;
import ecnu.compiling.compilingmate.service.LexService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/lex")
public class LexController {

    @Resource
    LexService lexService;

    @RequestMapping(value = "/scan", method = RequestMethod.GET)
    @ResponseBody
    public Result scan(@RequestBody JsonObject params){
        Result result = new Result();

        String inputCode = params.get("inputCode").getAsString();
        Set<LanguageDefinition> reDefs = new HashSet<>();
        Gson GSON = new Gson();
        params.get("reDefs").getAsJsonArray().forEach(def -> reDefs.add(GSON.fromJson(def, LanguageDefinition.class)));


        try {
            result.setSuccess(true);
            result.setData(lexService.scanCodeByRule(inputCode, reDefs));
        } catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/reProcessingOutput", method = RequestMethod.GET)
    @ResponseBody
    public Result reProcessingOutput(@RequestBody JsonObject params){
        Result result = new Result();
        String input = params.get("input").getAsString();
        try {
            Rule rule = lexService.getRuleByName("");
            result.setSuccess(true);
            result.setData(lexService.fullLexAnalyzeByTompsonAndSubsetConstruction(input, rule));
        } catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }

        return result;
    }

}
