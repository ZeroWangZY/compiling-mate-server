package ecnu.compiling.compilingmate.syntax;

import ecnu.compiling.compilingmate.syntax.analyzer.SLRParser;
import ecnu.compiling.compilingmate.syntax.entity.LR0Item;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SLRParserTest {
    SLRParser slrParser=new SLRParser();
    @Test
    public void hello(){
        String[] input = {"id","*","id","+","id"};
        java.util.List<LR0Item> productions = new ArrayList<>();
        java.util.List<String> t=Arrays.asList("id","+","*","(",")","$");
        List<String> ntList=Arrays.asList("E","T","F");
        //初始化productions,添加E'->E
        productions.add(new LR0Item("E'", new String[]{"E"},0));
        productions.add(new LR0Item("E", new String[]{"E", "+", "T"},0));
        productions.add(new LR0Item("E", new String[]{"T"},0));
        productions.add(new LR0Item("T", new String[]{"T", "*", "F"},0));
        productions.add(new LR0Item("T", new String[]{"F"},0));
        productions.add(new LR0Item("F", new String[]{"(", "E", ")"},0));
        productions.add(new LR0Item("F", new String[]{"id"},0));
        slrParser.parsing(input,productions,t,ntList);
    }
}
