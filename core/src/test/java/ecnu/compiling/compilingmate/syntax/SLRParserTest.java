package ecnu.compiling.compilingmate.syntax;

import ecnu.compiling.compilingmate.syntax.analyzer.SLRParser;
import ecnu.compiling.compilingmate.syntax.entity.LR0Item;
import ecnu.compiling.compilingmate.syntax.entity.Production;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SLRParserTest {
    SLRParser slrParser=new SLRParser();
    @Test
    public void hello(){
        String[] input = {"id","*","id","+","id"};
        java.util.List<Production> productions = new ArrayList<>();
        java.util.List<String> t=Arrays.asList("id","+","*","(",")","$");
        List<String> ntList=Arrays.asList("E","T","F");
        //初始化productions,添加E'->E
        productions.add(new Production("E'", new String[]{"E"}));
        productions.add(new Production("E", new String[]{"E", "+", "T"}));
        productions.add(new Production("E", new String[]{"T"}));
        productions.add(new Production("T", new String[]{"T", "*", "F"}));
        productions.add(new Production("T", new String[]{"F"}));
        productions.add(new Production("F", new String[]{"(", "E", ")"}));
        productions.add(new Production("F", new String[]{"id"}));
        slrParser.parsing(input,productions,t,ntList);
    }
}
