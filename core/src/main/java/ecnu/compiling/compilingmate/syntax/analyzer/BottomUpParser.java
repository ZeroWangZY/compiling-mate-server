package ecnu.compiling.compilingmate.syntax.analyzer;

import ecnu.compiling.compilingmate.syntax.entity.LR1Items;
import ecnu.compiling.compilingmate.syntax.entity.Production;

import java.util.List;

public interface BottomUpParser {
    List<LR1Items> constructItems(List<Production> productions,List<String> nt,List<String> t);
}
