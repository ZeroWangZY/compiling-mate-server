package ecnu.compiling.compilingmate.syntax.analyzer;

import ecnu.compiling.compilingmate.syntax.entity.Goto;
import ecnu.compiling.compilingmate.syntax.entity.LR1Items;
import ecnu.compiling.compilingmate.syntax.entity.ParsingTable;
import ecnu.compiling.compilingmate.syntax.entity.Production;

import java.util.List;
import java.util.Map;

public interface BottomUpParser<Items> {
    Map<String,Object> parse(List<Production> productions, List<String> nt, List<String> t, String startSymbol, List<Goto> gotoList);
    List<Items> constructItems(List<Production> productions,List<String> nt,List<String> t,String startSymbol,List<Goto> gotoList);
    ParsingTable constructParsingTable(List<String> t, List<String> nt, List<Items> itemsList, List<Goto> gotoList, List<Production> productions, String startSymbol);
}
