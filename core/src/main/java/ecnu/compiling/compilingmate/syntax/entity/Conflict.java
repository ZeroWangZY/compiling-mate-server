package ecnu.compiling.compilingmate.syntax.entity;

import java.util.ArrayList;
import java.util.List;

public class Conflict {
    private int row;
    private int col;
    List<String> content;

    public Conflict(int row, int col, String s) {
        this.row = row;
        this.col = col;
        this.content = new ArrayList<>();
        this.content.add(s);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public List<String> getContent() {
        return content;
    }

    public void addContent(String s){
        content.add(s);
    }
}
