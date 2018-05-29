package ecnu.compiling.compilingmate.entity;

public class GraphNode {
    private Integer id;
    private Boolean isStart;
    private Boolean isEnd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getStart() {
        return isStart;
    }

    public void setStart(Boolean start) {
        isStart = start;
    }

    public Boolean getEnd() {
        return isEnd;
    }

    public void setEnd(Boolean end) {
        isEnd = end;
    }
}
