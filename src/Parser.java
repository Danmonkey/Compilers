import java.util.ArrayList;

import static java.lang.System.exit;

public class Parser {
    private ArrayList<Token> theTokens;
    private int walker = 0;

    public Parser(ArrayList<Token> WALK){
        theTokens = WALK;
    }

    public void parse(){
            declist();
        System.out.println("ACCEPT");
    }

    public void declist(){
        dec();
        declistprime();
    }

    private void declistprime() {

    }

    public void dec(){
        typespec();
        if(theTokens.get(walker).getType()==type.ID){
            walker++;
        }
        else{
            System.out.println("REJECT");
            exit(-1);
        }
        decfactored();
    }

    private void decfactored() {

    }

    private void typespec() {
        if(theTokens.get(walker).getContents().contentEquals("int")||theTokens.get(walker).getContents().contentEquals("void"))
            walker++;
        else{
            System.out.println("REJECT");
            exit(-1);
        }
    }

    private boolean tryToAccept(){
        return true;
    }
}
