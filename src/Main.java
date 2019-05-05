import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static int numOfRules;
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Rule> rules = new ArrayList<>();

    public static void main(String[] args) {

        System.out.println("Enter Number of Rules");
        numOfRules = scanner.nextInt();

        getRules();
//        getNullableRules(rules);
//        getAllTerminalsAndNonTerminals(BDW(rules));
//      getBeginWith(BDW(rules), getAllTerminalsAndNonTerminals(BDW(rules)), getTransitive(BDW(rules)));
      ArrayList<Rule> bwRules = getBeginWith(BDW(rules), getAllTerminalsAndNonTerminals(BDW(rules)), getTransitive(BDW(rules)));

        getFirstOfNonTerminals(bwRules,getAllTerminalsAndNonTerminals(BDW(rules)));
    }

    public static void getRules(){
        String leftSide, rightSide;
        Rule rule;
        for (int x = 0; x <numOfRules ; x++) {
            rule = new Rule(0,"","");
            System.out.println("Enter The left side of rule " + (x+1) );
            leftSide = scanner.next();
            rule.setLeftSide(leftSide);

            System.out.println("Enter The Right side of rule " + (x+1) );
            rightSide = scanner.next();
            rule.setRightSide(rightSide);

            rule.setIndex(x);

            rules.add(rule);
        }

        System.err.println("Display all Rules");
        for (int x = 0; x <rules.size() ; x++) {
            System.out.println(rules.get(x).getLeftSide() + " => "+ rules.get(x).getRightSide());
        }
    }
    // step 1
    public static ArrayList<Rule> getNullableRules(ArrayList<Rule> mRules){
        ArrayList<Rule> nullableRules = new ArrayList<>();
        for (int x = 0; x <numOfRules ; x++) {
            if (mRules.get(x).getRightSide().equals( "null")){
                nullableRules.add(mRules.get(x));
            }
        }

        System.err.println();
        for (int i = 0; i <nullableRules.size() ; i++) {
            System.out.println("Nullable Rule " + nullableRules.get(i).getIndex());
            System.out.println("Nullable Non Terminals " + nullableRules.get(i).getLeftSide());
            System.out.println("** ** ** ");
        }
        return nullableRules;
    }

    // step 2
    public static ArrayList<Rule> BDW(ArrayList<Rule> originalRules){
         // if rule indecates null escape it 
        ArrayList<Rule> bdwRules = new ArrayList<>();
        Rule bdw = new Rule(-1, "", "");
        for (int i = 0; i < originalRules.size(); i++) {
            if (!originalRules.get(i).getRightSide().equals("null")){
                bdw = new Rule(bdw.getIndex() + 1, originalRules.get(i).getLeftSide(),String.valueOf(originalRules.get(i).getRightSide().charAt(0)));
                bdwRules.add(bdw);

                //check if the first letter is nonTerminal also
                for (int x = 0; x < originalRules.size(); x++) {
                    if (bdw.getRightSide().equals(originalRules.get(x).getLeftSide()) && originalRules.get(x).getRightSide().equals("null")){
                        bdw = new Rule(bdw.getIndex() + 1 , originalRules.get(i).getLeftSide(),String.valueOf(originalRules.get(i).getRightSide().charAt(1)));
                        bdwRules.add(bdw);
                    }
                }

            }
        }

        System.err.println("Display BDW LIST");
        for (int x = 0; x < bdwRules.size(); x++) {
            System.out.println(bdwRules.get(x).getIndex() + " Left " + bdwRules.get(x).getLeftSide()+ " Right " + bdwRules.get(x).getRightSide());
        }
        System.out.println("Step 2 Finished **** ");
        return bdwRules;
    }

    // step 3
    public static ArrayList<Rule> getBeginWith(ArrayList<Rule> bdwRules, ArrayList uniqueTerminalAndNon, ArrayList<Rule> transitiveRules){
        ArrayList<Rule> beginWithRules = new ArrayList<>();
        int lastIndex = bdwRules.size();
        //add bdw to bw
        for (int x = 0; x <bdwRules.size() ; x++) {
            beginWithRules.add(bdwRules.get(x));
        }

        // apply Transitive
        for (int x = 0; x < transitiveRules.size(); x++) {
            beginWithRules.add(new Rule(lastIndex+x ,transitiveRules.get(x).getLeftSide(), transitiveRules.get(x).getRightSide()));
        }

        lastIndex = beginWithRules.size();
        // apply reflexive
        for (int x = 0; x < uniqueTerminalAndNon.size(); x++) {
            beginWithRules.add(new Rule(x+lastIndex , uniqueTerminalAndNon.get(x).toString(), uniqueTerminalAndNon.get(x).toString()));
        }

        System.err.print("Display Begin with \n");


        for (int x = 0; x <beginWithRules.size() ; x++) {
            System.out.println("Rule # " +(beginWithRules.get(x).getIndex()+1)+ "  " + beginWithRules.get(x).getLeftSide() + "  bw  " + beginWithRules.get(x).getRightSide());
        }
        return beginWithRules;
    }

    // to getReflexive
    public static ArrayList getAllTerminalsAndNonTerminals(ArrayList<Rule> bdwRules){
        ArrayList uniqueList = new ArrayList();
        Rule currentRule = null;
        int flagFound = 0;
        for (int x = 0; x <bdwRules.size() ; x++) {
            currentRule = bdwRules.get(x);

            //check left
            for (int i = 0; i <uniqueList.size() ; i++) {
                if (currentRule.getLeftSide().equals(uniqueList.get(i))){
                    flagFound = 1;
                }
            }
            if (flagFound == 0)
                uniqueList.add(currentRule.getLeftSide());

            flagFound =0;


            //check Right
            for (int i = 0; i <uniqueList.size() ; i++) {
                if (currentRule.getRightSide().equals(uniqueList.get(i))){
                    flagFound = 1;
                }
            }
            if (flagFound == 0)
                uniqueList.add(currentRule.getRightSide());
            flagFound =0;

        }

        for (int x = 0; x < uniqueList.size(); x++) {
            System.out.println("unique " + uniqueList.get(x));
        }
        return uniqueList;
    }

    // to get Transitive
    public static ArrayList<Rule> getTransitive(ArrayList<Rule> originalRules){
        ArrayList<Rule> transitiveRules = new ArrayList<>();
        for (int x = 0; x < originalRules.size(); x++) {
            for (int y = x+1; y < originalRules.size(); y++) {
                if (originalRules.get(x).getRightSide().equals(originalRules.get(y).getLeftSide())){
                    transitiveRules.add(new Rule(0,originalRules.get(x).getLeftSide() ,originalRules.get(y).getRightSide()));
                }
            }            
        }

        return transitiveRules;
    }


    // step 4 get First(X)
    public static ArrayList<First> getFirstOfNonTerminals(ArrayList<Rule> bwRules, ArrayList unique){
        ArrayList<First> firstList = new ArrayList<>();
        ArrayList first = new ArrayList();

        for (int x = 0; x <unique.size() ; x++) {
            for (int y = 0; y < bwRules.size(); y++) {
                if (unique.get(x).equals(bwRules.get(y).getLeftSide())){
                    if (Character.isLowerCase(bwRules.get(y).getRightSide().charAt(0))){
                        first.add(bwRules.get(y).getRightSide());
                    }
                }
            }
            firstList.add(new First(x, unique.get(x).toString(),first));
            first  = new ArrayList();
        }

        System.out.println("Step 4 Finished *** \n");
        for (int x = 0; x < firstList.size(); x++) {
            String list ="";
            for (int y = 0; y <firstList.get(x).getFirst().size() ; y++) {
                list+=firstList.get(x).getFirst()+"  ";
            }
            System.out.println(firstList.get(x).getIndex() + "  " + firstList.get(x).getLeft() + "  " +list);
            list = "";
        }
        return firstList;
    }


    // used in step 5 to get all first of terminal or non-terminal
    public String getFirstOfInOneLine(ArrayList<First> firstList, char terminal){
        String result = "";
        for (int x = 0; x < firstList.size(); x++) {
            if (terminal == firstList.get(x).getLeft().charAt(0)){
                for (int y = 0; y <firstList.get(x).getFirst().size() ; y++) {
                    result+=firstList.get(x).getFirst().get(y);
                }
            }
        }
        return result;
    }


    //step 5
    public static ArrayList<Rule> computeFirstRightSide(ArrayList<Rule> originalRules, ArrayList<First> firstOF){
        ArrayList<Rule> firstRightSide = new ArrayList();
        Rule currentRule = new Rule();
        for (int x = 0; x <originalRules.size() ; x++) {
            String leftSide = originalRules.get(x).getRightSide();


            String rightSide ="";
            for (int y = 0; y <leftSide.length() ; y++) {
                if (Character.isUpperCase(leftSide.charAt(y))){
                }
            }
        }
        return firstRightSide;
    }
}
