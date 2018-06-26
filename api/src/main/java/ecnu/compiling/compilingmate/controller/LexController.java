package ecnu.compiling.compilingmate.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ecnu.compiling.compilingmate.lex.entity.token.LanguageDefinition;
import ecnu.compiling.compilingmate.entity.Result;
import ecnu.compiling.compilingmate.entity.TompsonData;
import ecnu.compiling.compilingmate.lex.directlymethod.REtoDFA;
import ecnu.compiling.compilingmate.lex.dto.ReToNfaDto;
import ecnu.compiling.compilingmate.lex.policy.rule.Rule;
import ecnu.compiling.compilingmate.service.LexService;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/lex")
public class LexController {

    @Resource
    LexService lexService;

//    @RequestMapping(value = "/scan", method = RequestMethod.POST)
//    @ResponseBody
//    public Result scan(@RequestParam(value = "inputCode") String inputCode,
//                       @RequestParam(value = "reDefs")Set<LanguageDefinition> reDefs){
//        Result result = new Result();
//
//        try {
//            result.setSuccess(true);
//            result.setData(lexService.scanCodeByRule(inputCode, reDefs));
//        } catch (Exception e){
//            result.setSuccess(false);
//            result.setMsg(e.getMessage());
//        }
//
//        return result;
//    }

    @RequestMapping(value = "/reProcessingOutput", method = RequestMethod.POST)
    @ResponseBody
    public Result reProcessingOutput(@RequestBody String json){
        JSONObject jsonObj = new JSONObject(json);

        Result result = new Result();
        Gson GSON = new Gson();
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

    @RequestMapping(value = "/REtoDFAOutput", method = RequestMethod.POST)
    @ResponseBody
    public Result retoDFAOutput(@RequestBody String json) throws JSONException{
    	JSONObject jsonObj = new JSONObject(json);
        String regularExpression = jsonObj.getString("input");
        Result result = new Result();

        try {
        	REtoDFA rEtoDFA = REtoDFA.getREtoDFA(regularExpression);
        	if (rEtoDFA.toDFA() == "success"){
            	result.setSuccess(true);
            	result.setData(rEtoDFA.getResult());
        	}
        	else 
        		result.setSuccess(false);
        } catch (Exception e){
            result.setSuccess(false);
            result.setMsg("Input invalid");
        }

        return result;
    }
}
