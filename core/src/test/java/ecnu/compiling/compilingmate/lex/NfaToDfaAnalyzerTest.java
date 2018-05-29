package ecnu.compiling.compilingmate.lex;

import com.google.gson.Gson;
import ecnu.compiling.compilingmate.lex.analyzer.AbstractAnalyzer;
import ecnu.compiling.compilingmate.lex.analyzer.dfa.SubsetConstructionAnalyzer;
import ecnu.compiling.compilingmate.lex.analyzer.nfa.TompsonAnalyzer;
import ecnu.compiling.compilingmate.lex.dto.NfaToDfaDto;
import ecnu.compiling.compilingmate.lex.dto.ReToNfaDto;
import ecnu.compiling.compilingmate.lex.entity.Token;
import ecnu.compiling.compilingmate.lex.entity.graph.DfaState;
import ecnu.compiling.compilingmate.lex.entity.graph.NfaState;
import ecnu.compiling.compilingmate.lex.entity.graph.StateGraph;
import ecnu.compiling.compilingmate.lex.policy.rule.DefaultReRule;
import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class NfaToDfaAnalyzerTest extends TestCase{

    String case1 = "d|(ab)*c";
    ReToNfaDto dto = new TompsonAnalyzer().analyze(case1,null);
    SubsetConstructionAnalyzer analyzer = new SubsetConstructionAnalyzer();

    public void testAnalyze(){
        StateGraph stateGraph = dto.getGraphs().get(dto.getGraphs().size()-1);
        NfaToDfaDto result = analyzer.analyze(stateGraph,null);
        assertEquals(5,result.getdStates().size());
        assertEquals(6, result.getEdges().size());
    }

    public void testCalculateTokens(){
        StateGraph stateGraph = dto.getGraphs().get(dto.getGraphs().size()-1);

        try {
            Method format = (SubsetConstructionAnalyzer.class).getDeclaredMethod("calculateTokens", StateGraph.class);
            format.setAccessible(true);//设为可见

            Field field = (AbstractAnalyzer.class).getDeclaredField("rule");
            field.setAccessible(true);
            field.set(analyzer, new DefaultReRule());

            Set<Token> result = (Set<Token>) format.invoke(analyzer, stateGraph);

            System.out.println(new Gson().toJson(result));

            assertEquals(4, result.size());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testCalculateMove(){
        StateGraph stateGraph = dto.getGraphs().get(dto.getGraphs().size()-1);

        try {
            Method format = (SubsetConstructionAnalyzer.class).getDeclaredMethod("calculateMove", Set.class, Token.class);
            format.setAccessible(true);//设为可见

            Field field = (AbstractAnalyzer.class).getDeclaredField("rule");
            field.setAccessible(true);
            field.set(analyzer, new DefaultReRule());

            Set<NfaState> result = (Set<NfaState>) format.invoke(analyzer, stateGraph.getStates(), new Token("a"));


            int expectSize = (int) stateGraph.getEdges().stream().filter(edge -> edge.getTag().equals(new Token("a"))).count();

            assertEquals(expectSize, result.size());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testCalculateClosure(){
        StateGraph stateGraph = dto.getGraphs().get(dto.getGraphs().size()-1);

        try {
            Method format = (SubsetConstructionAnalyzer.class).getDeclaredMethod("calculateClosure", Set.class);
            format.setAccessible(true);//设为可见

            Field field = (AbstractAnalyzer.class).getDeclaredField("rule");
            field.setAccessible(true);
            field.set(analyzer, new DefaultReRule());

            Set<NfaState> start = new HashSet<>();
            start.add(stateGraph.getStartState());

            DfaState result = (DfaState) format.invoke(analyzer, start);

            System.out.println(result.getNfaStates().stream().map(NfaState::getId).collect(Collectors.toList()));
            assertEquals(5, result.getNfaStates().size());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
