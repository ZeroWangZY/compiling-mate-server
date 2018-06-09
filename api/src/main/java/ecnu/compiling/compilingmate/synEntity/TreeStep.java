package ecnu.compiling.compilingmate.synEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.context.annotation.Bean;

import java.io.Serializable;
import java.util.List;


public class TreeStep implements Serializable{
    private String type;
    private Node node;

    public TreeStep(String type, Node node) {
        this.type = type;
        this.node = node;
    }

    public String getType() {
        return type;
    }

    public Node getNode() {
        return node;
    }
}
