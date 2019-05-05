import java.util.ArrayList;

public class Helper {

    // return false if terminal => null
    // used in step 5
    public boolean checkIfTerminalNULL(char terminal, ArrayList<Rule> originalRules){
        for (int x = 0; x < originalRules.size(); x++) {
            if (terminal == originalRules.get(x).getLeftSide().charAt(0)){
                if (originalRules.get(x).getRightSide().toLowerCase().equals("null")){
                    return false;
                }
            }
        }
        return true;
    }
}
