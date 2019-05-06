import Model.Rule;

import java.util.ArrayList;

public class Helper {

    // return false if terminal => null
    // used in step 5
    public static boolean checkIfTerminalNULL(char terminal, ArrayList<Rule> originalRules){
        for (int x = 0; x < originalRules.size(); x++) {
            if (terminal == originalRules.get(x).getLeftSide().charAt(0)){
                if (Character.isUpperCase(originalRules.get(x).getLeftSide().charAt(0))){
                    if (originalRules.get(x).getRightSide().toLowerCase().equals("null")){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static String getFirstUsingTerminal(char terminal, ArrayList<Rule> firsts){
        String first = "";
        for (int x = 0; x <firsts.size() ; x++) {
            if (firsts.get(x).getLeftSide().charAt(0)== terminal){
                first = firsts.get(x).getRightSide();
                break;
            }
        }

        return first;
    }

    // return true if null
    public static boolean checkIfLastCharNULL(char lastChar, ArrayList<Rule> originalRules){
        for (int x = 0; x < originalRules.size(); x++) {
            if (originalRules.get(x).getRightSide().equals("null")){
                if (String.valueOf(lastChar).equals(originalRules.get(x).getLeftSide())){
                    return true;
                }
            }
        }
        return false;
    }



}
