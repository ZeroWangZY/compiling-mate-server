package ecnu.compiling.compilingmate.controller;

import ecnu.compiling.compilingmate.service.SyntaxParsingService;
import ecnu.compiling.compilingmate.service.SyntaxParsingServiceImpl;
import ecnu.compiling.compilingmate.synEntity.RequestDto;
import ecnu.compiling.compilingmate.synEntity.Result;
import ecnu.compiling.compilingmate.syntax.entity.Production;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @RequestMapping(value="/parsingLL1Output",method= RequestMethod.POST)
    @ResponseBody
    public Result parsingLL1Output(@RequestParam(value = "startSymbol") String startSymbol, @RequestParam(value = "productions")String productions, @RequestParam(value = "type") String type){
        Result result=new Result();
        JSONArray array=JSONArray.fromObject(productions);
        List<Map<String,String>> productionList=new ArrayList<>();
        for(Object object:array){
            Map<String,String> map1=new HashMap<>();
            map1.put("left",((JSONObject)object).getString("left"));
            map1.put("right",((JSONObject)object).getString("right"));
            productionList.add(map1);
        }
        result.setData(syntaxParsingService.getParsingLL1Output(startSymbol,productionList,type));
        result.setSuccess(true);
        return result;
    }

}
