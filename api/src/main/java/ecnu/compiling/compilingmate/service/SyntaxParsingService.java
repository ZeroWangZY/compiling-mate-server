package ecnu.compiling.compilingmate.service;

import ecnu.compiling.compilingmate.synEntity.ProductionDto;
import ecnu.compiling.compilingmate.synEntity.RequestDto;
import ecnu.compiling.compilingmate.syntax.entity.Production;

import java.util.List;
import java.util.Map;

public interface SyntaxParsingService {
    Map<String,Object> getParsingOutput(RequestDto requestDto);
    List<Production> productionProcess(List<ProductionDto> productionDtos, String startSymbol);
}
