package ecnu.compiling.compilingmate.syntax.analyzer;

import ecnu.compiling.compilingmate.syntax.entity.Goto;
import ecnu.compiling.compilingmate.syntax.entity.LR1Items;
import ecnu.compiling.compilingmate.syntax.entity.ParsingTable;
import ecnu.compiling.compilingmate.syntax.entity.Production;

import java.util.List;

public interface BottomUpParser<Items> {
    List<Items> constructItems(List<Production> productions,List<String> nt,List<String> t,String startSymbol,List<Goto> gotoList);
    ParsingTable constructParsingTable(List<String> t, List<String> nt, List<Items> itemsList, List<Goto> gotoList, List<Production> productions, String startSymbol);
}
