import Model.Rule;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static int numOfRules;
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Rule> originalRules = new ArrayList<>();
    static ArrayList<Rule> nullableRules = new ArrayList<>();
    static ArrayList<Rule> BDWRules = new ArrayList<>();
    static ArrayList<Rule> BWRules = new ArrayList<>();
    static ArrayList uniqueIdentifiers = new ArrayList();
    static ArrayList<Rule> FirstOFRules = new ArrayList<>();
    static ArrayList<Rule> FirstRightSide = new ArrayList<>();
    static ArrayList<Rule> FDB = new ArrayList<>();
    static ArrayList<Rule> DEO = new ArrayList<>();
    static ArrayList<Rule> EO = new ArrayList<>();
    static ArrayList<Rule> followedBY = new ArrayList<>();
    static ArrayList<Rule> extendedFollowedBY = new ArrayList<>();
    static ArrayList<Rule> followSet = new ArrayList<>();
    static ArrayList<Rule> selctionSet = new ArrayList<>();

    public static void main(String[] args) {

        System.out.println("Enter Number of Rules");
        numOfRules = scanner.nextInt();

        originalRules = getRules();

        //Display Nullable rules
        nullableRules = getNullableRules(originalRules);

        // apply step 2 BDW
        BDWRules = BDW(originalRules);

        //apply step 3 BW
        uniqueIdentifiers = getAllTerminalsAndNonTerminals(BDWRules);
        BWRules = getBeginWith(BDWRules, uniqueIdentifiers, getTransitive(BDWRules));

        // apply step 4 FirstOF()
        FirstOFRules = getFirstOfNonTerminals(BWRules, uniqueIdentifiers);

        // apply Step 5 First Right side
        FirstRightSide = computeFirstRightSide(originalRules, FirstOFRules);

        //apply step 6 FDB
        FDB = followedDirectlyBy(originalRules);

        // apply step 7 DEO
        DEO = DEO(originalRules);

        // apply step 8 EO
        EO = EndOF(DEO,uniqueIdentifiers, originalRules );

        // apply step 9
        followedBY = followedBY(EO,FDB,BWRules);

        // apply step 10
        extendedFollowedBY =extendedFB(EO);

        // apply step 11
        followSet = FollowSET(originalRules,followedBY);

        // apply step 12
        selctionSet = getSelctionSet(originalRules, followSet, FirstRightSide);
    }

    public static ArrayList<Rule> getRules(){
        ArrayList<Rule> rules = new ArrayList<>();
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
        return rules;
    }
    // step 1
    public static ArrayList<Rule> getNullableRules(ArrayList<Rule> mRules){
        ArrayList<Rule> nullableRules = new ArrayList<>();
        for (int x = 0; x <numOfRules ; x++) {
            if (mRules.get(x).getRightSide().equals( "null")){
                nullableRules.add(mRules.get(x));
            }
        }

        System.err.println("Apply Step 1");
        for (int i = 0; i <nullableRules.size() ; i++) {
            System.out.println("Nullable Model Rule " + nullableRules.get(i).getIndex());
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

        System.err.println("step 2 Display BDW LIST");
        for (int x = 0; x < bdwRules.size(); x++) {
            System.out.println(bdwRules.get(x).getLeftSide()+ " BDW " + bdwRules.get(x).getRightSide());
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

        System.err.print("step 3 Display Begin with \n");


        for (int x = 0; x <beginWithRules.size() ; x++) {
            System.out.println(beginWithRules.get(x).getLeftSide() + "  BW  " + beginWithRules.get(x).getRightSide());
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


    // step 4 get Model.First(X)
    public static ArrayList<Rule> getFirstOfNonTerminals(ArrayList<Rule> bwRules, ArrayList unique){
        ArrayList<Rule> firstList = new ArrayList<>();
        String first = "";

        for (int x = 0; x <unique.size() ; x++) {
            for (int y = 0; y < bwRules.size(); y++) {
                if (unique.get(x).equals(bwRules.get(y).getLeftSide())){
                    if (Character.isLowerCase(bwRules.get(y).getRightSide().charAt(0))){
                        first+=bwRules.get(y).getRightSide() + "  ";
                    }
                }
            }
            firstList.add(new Rule(x, unique.get(x).toString(),first));
            first  = "";
        }

        System.err.print("step 4 Display First OF *** \n");
        for (int x = 0; x < firstList.size(); x++) {
            String list ="";

            System.out.println("First( "+firstList.get(x).getLeftSide() + " ) = { " +firstList.get(x).getRightSide() + "}");
            list = "";
        }
        return firstList;
    }

    //step 5
    public static ArrayList<Rule> computeFirstRightSide(ArrayList<Rule> originalRules, ArrayList<Rule> firstOF){
        ArrayList<Rule> firstRightSide = new ArrayList();
        Rule currentRule = new Rule();
        boolean flagNull = false;
        String rightSide = "";

        for (int x = 0; x <originalRules.size() ; x++) {
            // getFirstRight
                rightSide = "";
            for (int z = 0; z <originalRules.get(x).getRightSide().length() ; z++) {
                rightSide+= Helper.getFirstUsingTerminal(originalRules.get(x).getRightSide().charAt(z),firstOF) + " ";
                flagNull = Helper.checkIfTerminalNULL(originalRules.get(x).getRightSide().charAt(z), originalRules);
                if (flagNull) {
                    break;
                }
            }
            firstRightSide.add(new Rule(x,originalRules.get(x).getRightSide(), rightSide));
        }

        System.err.print("step 5 DISPLAY FIRST RIGHT SIDE \n");
        for (int x = 0; x <firstRightSide.size() ; x++) {
            System.out.println("First(" + firstRightSide.get(x).getLeftSide() + ") = { " + firstRightSide.get(x).getRightSide()+ " }");
        }
        return firstRightSide;
    }

    // step 6

    public static ArrayList<Rule> followedDirectlyBy(ArrayList<Rule> originalRules){
        ArrayList<Rule> fdbList = new ArrayList<>();
        String rightSide = "";
        int index = 0;
        for (int i = 0; i < originalRules.size(); i++) {
         rightSide = originalRules.get(i).getRightSide();
       for (int x = 0; x < rightSide.length(); x++) {

                if (Character.isUpperCase(rightSide.charAt(x)) && Helper.checkIfTerminalNULL(rightSide.charAt(x),originalRules)){
                    if (rightSide.length()>index){
                        fdbList.add(new Rule(index, String.valueOf(rightSide.charAt(x)), String.valueOf(rightSide.charAt(x+1))));
                        index++;
                    }
//                    if (rightSide.length()>index) {
//                        fdbList.add(new Rule(index, String.valueOf(rightSide.charAt(x)), String.valueOf(rightSide.charAt(x + 2))));
//                        index++;
//                    }
               } else if (Character.isUpperCase(rightSide.charAt(x))){
                    if (rightSide.length()>index) {
                        fdbList.add(new Rule(index, String.valueOf(rightSide.charAt(x)), String.valueOf(rightSide.charAt(x + 1))));
                        index++;
                    }
                }
            }
        }
        System.err.print("step 6 FDB SET LIST "+"\n" );
        for (int x = 0; x <fdbList.size() ; x++) {
            System.out.println(fdbList.get(x).getLeftSide() + " FDB " + fdbList.get(x).getRightSide() + " ");
        }
        return fdbList;
    }

    // step 7
    public static ArrayList<Rule> DEO(ArrayList<Rule> originalRules){
        ArrayList<Rule> DEOList = new ArrayList<>();
        int index =0 ;
        String rightSide = "";
        char lastChar;
        for (int x = 0; x <originalRules.size() ; x++) {
            rightSide=originalRules.get(x).getRightSide();
            lastChar = rightSide.charAt(rightSide.length()-1);

            if (originalRules.get(x).getRightSide().equals("null")){
                continue;
            }

            if (Helper.checkIfLastCharNULL(lastChar, originalRules)){
                // if null
                DEOList.add(new Rule(index, String.valueOf(lastChar),originalRules.get(x).getLeftSide()));
                index++;
                lastChar = rightSide.charAt(rightSide.length()-2);
                DEOList.add(new Rule(index, String.valueOf(lastChar),originalRules.get(x).getLeftSide()));
                index++;
            }else {
                DEOList.add(new Rule(index, String.valueOf(lastChar),originalRules.get(x).getLeftSide()));
                index++;
            }
        }


        System.err.print("step 7 DISPLAY DEO \n");
        for (int x = 0; x <DEOList.size() ; x++) {
            System.out.println(DEOList.get(x).getLeftSide() + " DEO " + DEOList.get(x).getRightSide());
        }

        return DEOList;
    }

    // step 8
    public static ArrayList<Rule> EndOF(ArrayList<Rule> DEO, ArrayList unique, ArrayList<Rule> originalRules){
        ArrayList<Rule> EOList = new ArrayList<>();

        // first add deo to list

        for (int x = 0; x < DEO.size(); x++) {
            EOList.add(DEO.get(x));
        }


        // add transitive
        ArrayList<Rule> transitiveList = getTransitive(DEO);
        for (int x = 0; x <transitiveList.size() ; x++) {
            EOList.add(transitiveList.get(x));
        }

        // add reflexive
        for (int x = 0; x <unique.size() ; x++) {
            if (Helper.checkIfTerminalNULL(unique.get(x).toString().charAt(0), originalRules)){
                EOList.add(new Rule(0, unique.get(x).toString(), unique.get(x).toString()));
            }
        }

        System.err.print("step 8 DISPLAY EO LIST \n");
        for (int x = 0; x < EOList.size(); x++) {
            System.out.println(EOList.get(x).getLeftSide() +" EO " +EOList.get(x).getRightSide());
        }
        return EOList;
    }

    // step 9
    public static ArrayList<Rule> followedBY(ArrayList<Rule> EO, ArrayList<Rule> FDB, ArrayList<Rule> BW){
        ArrayList<Rule> FB = new ArrayList<>();
        int index = 0;
        for (int x = 0; x < EO.size(); x++) {
            for (int y = 0; y <FDB.size() ; y++) {
                if (EO.get(x).getRightSide().equals(FDB.get(y).getLeftSide())){
                    for (int z = 0; z <BW.size() ; z++) {
                        if (FDB.get(y).getRightSide().equals(BW.get(z).getLeftSide())){
                            FB.add(new Rule(index, EO.get(x).getLeftSide(),BW.get(z).getRightSide()));
                            index++;
                        }
                    }
                }
            }
        }
        System.err.print("step 9 DISPLAY FB LIST \n");
        for (int x = 0; x <FB.size() ; x++) {
            System.out.println(FB.get(x).getLeftSide() +" FB " +FB.get(x).getRightSide());
        }
        return FB;
    }

    // step 10
    public static ArrayList<Rule> extendedFB(ArrayList<Rule> EOList){
        ArrayList<Rule> extendedList = new ArrayList<>();
        for (int x = 0; x <EOList.size() ; x++) {
            if (EOList.get(x).getRightSide().equals("S")){
                if (Character.isUpperCase(EOList.get(x).getLeftSide().charAt(0))){
                    extendedList.add(EOList.get(x));
                }
            }
        }

        System.err.print("step 10 EXTENDED "+ extendedList.size()+"\n" );
        for (int x = 0; x <extendedList.size() ; x++) {
            System.out.println(extendedList.get(x).getLeftSide() + " EXTEND " + extendedList.get(x).getRightSide());
        }
        return extendedList;
    }

    // step 11
    public static ArrayList<Rule> FollowSET(ArrayList<Rule> originalRules, ArrayList<Rule> followedBY){
        // from step 9 select non-terminal for nullable rules
        ArrayList<Rule> followSETList = new ArrayList<>();
        char terminal;
        for (int x = 0; x < originalRules.size(); x++) {
            terminal = originalRules.get(x).getLeftSide().charAt(0);
            if (!Helper.checkIfTerminalNULL(terminal, originalRules)){
                for (int y = 0; y <followedBY.size() ; y++) {
                    if (String.valueOf(terminal).equals(followedBY.get(y).getLeftSide())){
                        if (Character.isLowerCase(followedBY.get(y).getRightSide().charAt(0))){
                            followSETList.add(followedBY.get(y));
                        }
                    }
                }
            }
        }
        System.err.print("step 11 FOLLOWED SET LIST \n" );
        for (int x = 0; x <followSETList.size() ; x++) {
            System.out.println(followSETList.get(x).getLeftSide() + " FB " + followSETList.get(x).getRightSide() + " ");
        }
        return followSETList;
    }

    // step 12
    public static ArrayList<Rule> getSelctionSet(ArrayList<Rule> originalRules, ArrayList<Rule> followSet,ArrayList<Rule> FirstList){
        ArrayList<Rule> selctionArrayList = new ArrayList<>();
        char terminal ;
        int flagNull = 0;
        for (int x = 0; x <originalRules.size() ; x++) {
            for (int i = 0; i <nullableRules.size() ; i++) {
                if (originalRules.get(x).getIndex()==nullableRules.get(i).getIndex()){
                  flagNull = 1;
                }
            }


            if (flagNull ==0){
                // not null
                for (int y = 0; y <FirstList.size() ; y++) {
                    if (FirstList.get(y).getLeftSide().equals(originalRules.get(x).getRightSide())){
                        selctionArrayList.add(FirstList.get(y));
                        break;
                    }
                }
            }else {
                // rule => null
                for (int y = 0; y <followSet.size() ; y++) {
                    if (followSet.get(y).getLeftSide().equals(originalRules.get(x).getLeftSide())){
                        selctionArrayList.add(followSet.get(y));
                        flagNull = 0;
                    }
                }
            }

        }
        System.err.print("step 12 SELECTION SET LIST \n" );

        for (int x = 0; x <selctionArrayList.size() ; x++) {
            System.out.println(selctionArrayList.get(x).getLeftSide() + "  =>  " + selctionArrayList.get(x).getRightSide() + " ");
        }
        return selctionArrayList;
    }
}
