package ecnu.compiling.compilingmate.entity;

import java.util.Set;

public class Graph {
    private Set<GraphNode> nodes;

    private Set<GraphLink> links;

    public Set<GraphNode> getNodes() {
        return nodes;
    }

    public void setNodes(Set<GraphNode> nodes) {
        this.nodes = nodes;
    }

    public Set<GraphLink> getLinks() {
        return links;
    }

    public void setLinks(Set<GraphLink> links) {
        this.links = links;
    }
}
