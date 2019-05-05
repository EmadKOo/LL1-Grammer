import java.util.ArrayList;

public class First {
    int index ;
    String left;
    ArrayList first;

    public First(int index, String left, ArrayList first) {
        this.index = index;
        this.left = left;
        this.first = first;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public ArrayList getFirst() {
        return first;
    }

    public void setFirst(ArrayList first) {
        this.first = first;
    }
}
