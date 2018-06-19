package ecnu.compiling.compilingmate.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ecnu.compiling.compilingmate.lex.entity.token.LanguageDefinition;
import ecnu.compiling.compilingmate.entity.Result;
import ecnu.compiling.compilingmate.lex.policy.rule.Rule;
import ecnu.compiling.compilingmate.service.LexService;
import org.json.JSONObject;
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

    @RequestMapping(value = "/scan", method = RequestMethod.POST)
    @ResponseBody
    public Result scan(@RequestBody String json){
        Result result = new Result();
        Gson GSON = new Gson();

        JsonObject params = GSON.fromJson(json, JsonObject.class);
        String inputCode = params.get("inputCode").getAsString();
        Set<LanguageDefinition> reDefs = new HashSet<>();

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

    @RequestMapping(value = "/reProcessingOutput", method = RequestMethod.POST)
    @ResponseBody
    public Result reProcessingOutput(@RequestBody String json){
        JSONObject jsonObj = new JSONObject(json);
        Result result = new Result();

        if (jsonObj != null) {
            String input = jsonObj.getString("input");
            String ruleName = "";

            try {
                Rule rule = lexService.getRuleByName(ruleName);
                result.setSuccess(true);
                result.setData(lexService.fullLexAnalyzeByTompsonAndSubsetConstruction(input, rule));
            } catch (Exception e) {
                result.setSuccess(false);
                result.setMsg(e.getMessage());
            }
        } else {
            result.setSuccess(false);
            result.setMsg("Input invalid");
        }

        return result;
    }

}
