package ecnu.compiling.compilingmate.lex;

import com.google.gson.Gson;
import ecnu.compiling.compilingmate.lex.analyzer.nfa.TompsonAnalyzer;
import ecnu.compiling.compilingmate.lex.dto.ReToNfaDto;
import ecnu.compiling.compilingmate.lex.entity.token.Token;
import ecnu.compiling.compilingmate.lex.entity.graph.NfaState;
import ecnu.compiling.compilingmate.lex.entity.graph.StateGraph;
import ecnu.compiling.compilingmate.lex.policy.ScannerFactory;
import ecnu.compiling.compilingmate.lex.policy.rule.DefaultReRule;
import ecnu.compiling.compilingmate.lex.policy.scanner.DefaultReScanner;
import junit.framework.TestCase;
import org.junit.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class ThompsonReToNfaAnalyzerTest extends TestCase{

    TompsonAnalyzer lexAnalyzer = new TompsonAnalyzer();

    public void testAnalyze(){
        String case1 = "d|(ab)*c";
        ReToNfaDto dto = lexAnalyzer.analyze(case1,null);
    }

    public void testScan(){
        DefaultReScanner scanner = ScannerFactory.getLexScanner();
        String case1 = "d|(ab)*c";
        List<Token> tokenList1 = scanner.parse(case1);
        assertEquals(10, tokenList1.size());
        System.out.println(new Gson().toJson(tokenList1));

        String case2 = "d|(  ab ) * c";
        List<Token> tokenList2 = scanner.parse(case2);
        assertEquals(10, tokenList2.size());
        System.out.println(new Gson().toJson(tokenList2));

        assertEquals(tokenList1, tokenList2);
    }

    public void testSuffixExpression(){

        String case1 = "d|(ab)*c";
        String result1 = "dab.*c.|";

        String case2 = "n.a*(b|c(x*.y).z).m.p*";
        String result2 = "na*bcx*y.z.|mp*...";

        String case3 = "(a.b*.c)|(a.(b|c*))";

        try {
            Method format = (TompsonAnalyzer.class).getDeclaredMethod("toSuffixExpression", List.class);
            format.setAccessible(true);//设为可见

            Field field = (TompsonAnalyzer.class).getDeclaredField("defaultReRule");
            field.setAccessible(true);

            field.set(lexAnalyzer, new DefaultReRule());

            List<Token> result = (List<Token>) format.invoke(lexAnalyzer,ScannerFactory.getLexScanner().parse(case1));
            StringBuilder sb = new StringBuilder();
            result.forEach(token -> sb.append(token.getContent()));
            Assert.assertEquals(result1, sb.toString());

            result = (List<Token>) format.invoke(lexAnalyzer,ScannerFactory.getLexScanner().parse(case2));
            StringBuilder sb2 = new StringBuilder();
            result.forEach(token -> sb2.append(token.getContent()));
            Assert.assertEquals(result2, sb2.toString());


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testAnd(){

        String case1 = "a.b";

        try {
            Method format = (TompsonAnalyzer.class).getDeclaredMethod("and", StateGraph.class, StateGraph.class);
            Method generateSingle = (TompsonAnalyzer.class).getDeclaredMethod("generateSingle", Token.class);
            format.setAccessible(true);//设为可见
            generateSingle.setAccessible(true);

            Field field = (TompsonAnalyzer.class).getDeclaredField("defaultReRule");
            field.setAccessible(true);

            field.set(lexAnalyzer, new DefaultReRule());

            StateGraph a = (StateGraph) generateSingle.invoke(lexAnalyzer, new Token("a"));
            StateGraph b = (StateGraph) generateSingle.invoke(lexAnalyzer, new Token("b"));

            StateGraph result = (StateGraph) format.invoke(lexAnalyzer,a,b);

            NfaState resultStart = result.getStartState();
            NfaState resultEnd = result.getFinalState();

            Assert.assertEquals(resultEnd, resultStart.getNextState().getNextState());

            assertEquals(2, result.getEdges().size());
            assertEquals(3, result.getStates().size());

            System.out.println(new Gson().toJson(result));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testOr(){

        String case1 = "a|b";

        try {
            Method format = (TompsonAnalyzer.class).getDeclaredMethod("or", StateGraph.class, StateGraph.class);
            Method generateSingle = (TompsonAnalyzer.class).getDeclaredMethod("generateSingle", Token.class);
            format.setAccessible(true);//设为可见
            generateSingle.setAccessible(true);

            Field field = (TompsonAnalyzer.class).getDeclaredField("defaultReRule");
            field.setAccessible(true);

            field.set(lexAnalyzer, new DefaultReRule());

            StateGraph a = (StateGraph) generateSingle.invoke(lexAnalyzer, new Token("a"));
            StateGraph b = (StateGraph) generateSingle.invoke(lexAnalyzer, new Token("b"));

            StateGraph result = (StateGraph) format.invoke(lexAnalyzer,a,b);

            NfaState resultStart = result.getStartState();
            NfaState resultEnd = result.getFinalState();

            Assert.assertEquals(2, resultStart.getNextStatesWhenEmptyInput().size());
            Assert.assertEquals(true, resultEnd.isEnd());

            Assert.assertEquals(a.getStartState(),resultStart.getNextStatesWhenEmptyInput().get(0));
            Assert.assertEquals(b.getStartState(),resultStart.getNextStatesWhenEmptyInput().get(1));

            assertEquals(6, result.getStates().size());
            assertEquals(6, result.getEdges().size());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testRepeat(){

        String case1 = "a*";

        try {
            Method format = (TompsonAnalyzer.class).getDeclaredMethod("repeatOrNone", StateGraph.class);
            Method generateSingle = (TompsonAnalyzer.class).getDeclaredMethod("generateSingle", Token.class);
            format.setAccessible(true);//设为可见
            generateSingle.setAccessible(true);

            Field field = (TompsonAnalyzer.class).getDeclaredField("defaultReRule");
            field.setAccessible(true);

            field.set(lexAnalyzer, new DefaultReRule());

            StateGraph a = (StateGraph) generateSingle.invoke(lexAnalyzer, new Token("a"));

            StateGraph result = (StateGraph) format.invoke(lexAnalyzer,a);

            NfaState resultStart = result.getStartState();
            NfaState resultEnd = result.getFinalState();

            assertEquals(5, result.getEdges().size());
            assertEquals(4, result.getStates().size());
            assertTrue(resultStart.isStart());
            assertTrue(resultEnd.isEnd());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}