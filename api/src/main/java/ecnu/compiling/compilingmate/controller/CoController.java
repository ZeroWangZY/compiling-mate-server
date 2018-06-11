package ecnu.compiling.compilingmate.controller;


import ecnu.compiling.compilingmate.entity.Result;
import ecnu.compiling.compilingmate.lex.policy.rule.Rule;
import ecnu.compiling.compilingmate.service.CoService;
import ecnu.compiling.compilingmate.service.CoServiceImpl;
import ecnu.compiling.compilingmate.service.LexService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ecnu.compiling.compilingmate.lox.Lox;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;

@Controller
@RequestMapping("/co")
public class CoController {
    @Resource
    CoService coService;

    @RequestMapping(value = "/compile", method = RequestMethod.POST)
    @ResponseBody
    public Result compile(@RequestBody String json){
        JSONObject jsonObj = new JSONObject(json);
        String code = jsonObj.getString("code");
        Lox.run(code);

        Result result = new Result();
        try {
            result.setSuccess(true);
            result.setData(coService.runCode(code));
        } catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
