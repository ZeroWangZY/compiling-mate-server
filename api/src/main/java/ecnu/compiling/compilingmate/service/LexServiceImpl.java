package ecnu.compiling.compilingmate.service;

import ecnu.compiling.compilingmate.entity.*;
import ecnu.compiling.compilingmate.lex.analyzer.dfa.SubsetConstructionAnalyzer;
import ecnu.compiling.compilingmate.lex.analyzer.nfa.TompsonAnalyzer;
import ecnu.compiling.compilingmate.lex.constants.LexConstants;
import ecnu.compiling.compilingmate.lex.dto.NfaToDfaDto;
import ecnu.compiling.compilingmate.lex.dto.ReToNfaDto;
import ecnu.compiling.compilingmate.lex.entity.graph.*;
import ecnu.compiling.compilingmate.lex.policy.rule.DefaultReRule;
import ecnu.compiling.compilingmate.lex.policy.rule.Rule;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("LexService")
public class LexServiceImpl implements LexService{

    //todo
    @Override
    public Rule getRuleByName(String ruleName) {
        return new DefaultReRule();
    }

    @Override
    public Map<String, Object> fullLexAnalyzeByTompsonAndSubsetConstruction(String input, Rule rule){
        Map<String, Object> result = new HashMap<>();

        ReToNfaDto dto = new TompsonAnalyzer().analyze(input, rule);
        result.put("tompsonData", this.toTompsonData(dto));

        StateGraph fullGraph = dto.getGraphs().stream()
                .filter(stateGraph -> stateGraph.getRefId().equals(dto.getRoot().getId()))
                .findFirst().get();

        result.put("nfaToDfaData", this.getNfaToDfaBySubSet(fullGraph));

        return result;
    }

    @Override
    public TompsonData getReToDfaByTompson(String input, Rule rule) {
        ReToNfaDto dto = new TompsonAnalyzer().analyze(input, rule);
        return this.toTompsonData(dto);
    }

    @Override
    public DfaData getNfaToDfaBySubSet(StateGraph stateGraph) {
        NfaToDfaDto dto = new SubsetConstructionAnalyzer().analyze(stateGraph, null);

        DfaData dfaData = new DfaData();

        dfaData.setDfa(toGraph(dto.getdStates(), dto.getEdges()));
        dfaData.setStates(flatDState(dto.getdStates()));

        return dfaData;
    }

    private TompsonData toTompsonData(ReToNfaDto dto){
        TompsonData tompsonData = new TompsonData();
        tompsonData.setReTree(dto.getRoot());

        Map<Integer, Graph> nfaMap = new HashMap<>();

        dto.getGraphs().forEach(stateGraph
                -> nfaMap.put(stateGraph.getRefId(), toGraph(stateGraph.getStates(), stateGraph.getEdges())));
        tompsonData.setNfaMap(nfaMap);

        return tompsonData;
    }

    private Map<Integer, List<Integer>> flatDState(Set<DfaState> dfaStates){
        Map<Integer, List<Integer>> result = new HashMap<>();

        dfaStates.forEach(ds ->
                result.put(ds.getId(), ds.getNfaStates().stream()
                        .mapToInt(State::getId).boxed().collect(Collectors.toList())));

        return result;
    }

    private Graph toGraph(Collection<? extends State> states, Collection<Edge> edges){
        Graph graph = new Graph();

        Set<GraphNode> nodeSet = new HashSet<>();
        Set<GraphLink> linkSet = new HashSet<>();

        states.forEach(dState -> nodeSet.add(toGraphNode(dState)));
        edges.forEach(edge -> linkSet.add(toGraphLink(edge)));

        graph.setNodes(nodeSet);
        graph.setLinks(linkSet);

        return graph;
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
