package ecnu.compiling.compilingmate.syntax;

import ecnu.compiling.compilingmate.syntax.analyzer.LRParser;
import ecnu.compiling.compilingmate.syntax.entity.Goto;
import ecnu.compiling.compilingmate.syntax.entity.Production;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LRParserTest{

    LRParser lrParser=new LRParser();

    @Test
    public void constructItems(){
        List<Production> productions=new ArrayList<>();
//        productions.add(new Production("S'",new String[]{"S"}));
//        productions.add(new Production("S",new String[]{"a","A","d"}));
//        productions.add(new Production("S",new String[]{"b","A","c"}));
//        productions.add(new Production("S",new String[]{"a","e","c"}));
//        productions.add(new Production("S",new String[]{"b","e","d"}));
//        productions.add(new Production("A",new String[]{"e"}));
//
//        String[] nt=new String[]{"S","A"};
//        String[] t=new String[]{"a","d","b","c","e","$"};

        productions.add(new Production("S'",new String[]{"S"}));
        productions.add(new Production("S",new String[]{"L","=","R"}));
        productions.add(new Production("S",new String[]{"R"}));
        productions.add(new Production("L",new String[]{"*","R"}));
        productions.add(new Production("L",new String[]{"id"}));
        productions.add(new Production("R",new String[]{"L"}));

        String[] nt=new String[]{"S","L","R"};
        String[] t=new String[]{"=","*","id","$"};

        List<Goto> gotoList=new ArrayList<>();

        lrParser.constructItems(productions, Arrays.asList(nt),Arrays.asList(t),"S",gotoList);
    }

}
