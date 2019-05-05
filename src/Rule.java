public class Rule {

    int index;
    String leftSide;
    String rightSide;

    public Rule(){}
    public Rule(int index,String leftSide, String rightSide) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLeftSide() {
        return leftSide;
    }

    public void setLeftSide(String leftSide) {
        this.leftSide = leftSide;
    }

    public String getRightSide() {
        return rightSide;
    }

    public void setRightSide(String rightSide) {
        this.rightSide = rightSide;
    }
}
