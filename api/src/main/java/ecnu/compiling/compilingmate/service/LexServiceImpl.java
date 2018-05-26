package ecnu.compiling.compilingmate.service;

import ecnu.compiling.compilingmate.entity.Graph;
import ecnu.compiling.compilingmate.entity.GraphLink;
import ecnu.compiling.compilingmate.entity.GraphNode;
import ecnu.compiling.compilingmate.entity.TompsonData;
import ecnu.compiling.compilingmate.lex.analyzer.TompsonAnalyzer;
import ecnu.compiling.compilingmate.lex.constants.LexConstants;
import ecnu.compiling.compilingmate.lex.dto.ReToNfaDto;
import ecnu.compiling.compilingmate.lex.entity.graph.Edge;
import ecnu.compiling.compilingmate.lex.entity.graph.State;
import ecnu.compiling.compilingmate.lex.entity.graph.StateGraph;
import ecnu.compiling.compilingmate.lex.policy.rule.DefaultReRule;
import ecnu.compiling.compilingmate.lex.policy.rule.Rule;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service("LexService")
public class LexServiceImpl implements LexService{

    //todo
    @Override
    public Rule getRuleByName(String ruleName) {
        return new DefaultReRule();
    }

    @Override
    public TompsonData getReToDfaByTompson(String input, Rule rule) {
        ReToNfaDto dto = new TompsonAnalyzer().analyze(input, rule);

        TompsonData tompsonData = new TompsonData();
        tompsonData.setReTree(dto.getRoot());

        Map<Integer, Graph> nfaMap = new HashMap<>();

        for (StateGraph stateGraph : dto.getGraphs()) {
            Graph graph = new Graph();
            Set<GraphNode> nodeSet = new HashSet<>();
            Set<GraphLink> linkSet = new HashSet<>();

            stateGraph.getStates().forEach(state -> nodeSet.add(toGraphNode(state)));
            stateGraph.getEdges().forEach(edge -> linkSet.add(toGraphLink(edge)));
            graph.setNodes(nodeSet);
            graph.setLinks(linkSet);

            nfaMap.put(stateGraph.getRefId(), graph);
        }

        tompsonData.setNfaMap(nfaMap);

        return tompsonData;
    }

    private GraphNode toGraphNode(State state){
        GraphNode node = new GraphNode();
        node.setId(state.getId());
        node.setStart(state.isStart());
        node.setEnd(state.isEnd());
        return node;
    }

    private GraphLink toGraphLink(Edge edge){
        GraphLink link = new GraphLink();
        link.setFrom(edge.getFrom());
        link.setTo(edge.getTo());

        if (!LexConstants.SpecialToken.EMPTY.getValue().equals(edge.getTag().getContent())){
            link.setTag(edge.getTag().getContent());
        }

        return link;
    }
}
