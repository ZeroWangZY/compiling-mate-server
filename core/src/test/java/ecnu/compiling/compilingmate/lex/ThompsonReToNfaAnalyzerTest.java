package ecnu.compiling.compilingmate.lex;

import ecnu.compiling.compilingmate.lex.analyzer.ReToNfaAnalyzer;
import ecnu.compiling.compilingmate.lex.analyzer.TompsonAnalyzer;
import ecnu.compiling.compilingmate.lex.entity.State;
import ecnu.compiling.compilingmate.lex.entity.StateGraph;
import junit.framework.TestCase;
import org.junit.Assert;

import java.lang.reflect.Method;

public class ThompsonReToNfaAnalyzerTest extends TestCase{

    TompsonAnalyzer lexAnalyzer = new TompsonAnalyzer();

    public void testAnalyze(){
        String case1 = "d|(a|b)*.c";

        lexAnalyzer.analyze(case1);
    }

    public void testPreProcess(){

        String case1 = "d | ( a | b ) * c";
        String result1 = "d|(a|b)*.c";

        String case2 = "n a * (b | c (x*y) z) m p*";
        String result2 = "n.a*(b|c(x*.y).z).m.p*";


        try {
            Method format = (ReToNfaAnalyzer.class).getDeclaredMethod("preProcess", String.class);
            format.setAccessible(true);//设为可见

            String result = (String) format.invoke(lexAnalyzer,case1);
            Assert.assertEquals(result1, result);

            result = (String) format.invoke(lexAnalyzer,case2);
            Assert.assertEquals(result2, result);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testSuffixExpression(){

        String case1 = "d|(a|b)*.c";
        String result1 = "dab|*c.|";

        String case2 = "n.a*(b|c(x*.y).z).m.p*";
        String result2 = "na*bcx*y.z.|mp*...";

        String case3 = "(a.b*.c)|(a.(b|c*))";

        try {
            Method format = (TompsonAnalyzer.class).getDeclaredMethod("toSuffixExpression", String.class);
            format.setAccessible(true);//设为可见

            String result = (String) format.invoke(lexAnalyzer,case1);
            Assert.assertEquals(result1, result);

            result = (String) format.invoke(lexAnalyzer,case2);
            Assert.assertEquals(result2, result);

            System.out.println((String) format.invoke(lexAnalyzer,case3));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testAnd(){

        String case1 = "a.b";

        try {
            Method format = (TompsonAnalyzer.class).getDeclaredMethod("and", StateGraph.class, StateGraph.class);
            Method generateSingle = (TompsonAnalyzer.class).getDeclaredMethod("generateSingle", Character.class);
            format.setAccessible(true);//设为可见
            generateSingle.setAccessible(true);

            StateGraph a = (StateGraph) generateSingle.invoke(lexAnalyzer, 'a');
            StateGraph b = (StateGraph) generateSingle.invoke(lexAnalyzer, 'b');

            StateGraph result = (StateGraph) format.invoke(lexAnalyzer,a,b);

            State resultStart = result.getStartState();
            State resultEnd = result.getFinalState();

            Assert.assertEquals(resultEnd, resultStart.getNextState().getNextState());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testOr(){

        String case1 = "a|b";

        try {
            Method format = (TompsonAnalyzer.class).getDeclaredMethod("or", StateGraph.class, StateGraph.class);
            Method generateSingle = (TompsonAnalyzer.class).getDeclaredMethod("generateSingle", Character.class);
            format.setAccessible(true);//设为可见
            generateSingle.setAccessible(true);

            StateGraph a = (StateGraph) generateSingle.invoke(lexAnalyzer, 'a');
            StateGraph b = (StateGraph) generateSingle.invoke(lexAnalyzer, 'b');

            StateGraph result = (StateGraph) format.invoke(lexAnalyzer,a,b);

            State resultStart = result.getStartState();
            State resultEnd = result.getFinalState();

            Assert.assertEquals(2, resultStart.getNextStatesWhenEmptyInput().size());
            Assert.assertEquals(true, resultEnd.isFinal());

            Assert.assertEquals(a.getStartState(),resultStart.getNextStatesWhenEmptyInput().get(0));
            Assert.assertEquals(b.getStartState(),resultStart.getNextStatesWhenEmptyInput().get(1));

            Assert.assertEquals(resultEnd,a.getFinalState().getNextState());
            Assert.assertEquals(resultEnd,b.getFinalState().getNextState());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testRepeat(){

        String case1 = "a*";

        try {
            Method format = (TompsonAnalyzer.class).getDeclaredMethod("repeatOrNone", StateGraph.class);
            Method generateSingle = (TompsonAnalyzer.class).getDeclaredMethod("generateSingle", Character.class);
            format.setAccessible(true);//设为可见
            generateSingle.setAccessible(true);

            StateGraph a = (StateGraph) generateSingle.invoke(lexAnalyzer, 'a');

            StateGraph result = (StateGraph) format.invoke(lexAnalyzer,a);

            State resultStart = result.getStartState();
            State resultEnd = result.getFinalState();

            Assert.assertEquals(a.getStartState(),resultStart.getNextStatesWhenEmptyInput().get(0));
            Assert.assertEquals(resultEnd,resultStart.getNextStatesWhenEmptyInput().get(1));

            Assert.assertEquals(resultEnd, a.getFinalState().getNextStatesWhenEmptyInput().get(0));
            Assert.assertEquals(a.getStartState(), a.getFinalState().getNextStatesWhenEmptyInput().get(1));

            Assert.assertEquals(a.getFinalState(),a.getStartState().getNextState());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}