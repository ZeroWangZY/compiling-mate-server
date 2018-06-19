package ecnu.compiling.compilingmate.service;

import ecnu.compiling.compilingmate.synEntity.ActionRequestDto;
import ecnu.compiling.compilingmate.synEntity.ProductionDto;
import ecnu.compiling.compilingmate.synEntity.RequestDto;
import ecnu.compiling.compilingmate.syntax.entity.Production;

import java.util.List;
import java.util.Map;

public interface SyntaxParsingService {
    Map<String,Object> getParsingOutput(RequestDto requestDto);
    List<Production> productionProcess(List<ProductionDto> productionDtos, String startSymbol);
    Map<String,Object> getActionOutput(ActionRequestDto actionRequestDto);
    Map<String,Object> getParsingLL1Output(String startSymbol, List<Map<String,String>> productions, String type);
}
