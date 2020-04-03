import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.System.exit;

public class Parser {
    private ArrayList<Token> theTokens;
    private int walker = 0;
    private int currentScope = 0;
    private boolean mSeen = false;
    private ArrayList<HashMap<String, Variable>> scopifier = new ArrayList<HashMap<String, Variable>>();

    public Parser(ArrayList<Token> WALK){
        theTokens = WALK;
    }

    public void parse(){
        scopifier.add(new HashMap<String, Variable>(theTokens.size())); //global scope
        declist();
        System.out.println("ACCEPT");
    }

    public void declist(){
        dec();
        declistprime();
    }

    private void declistprime() {
        if(theTokens.get(walker).getContents().contentEquals("int")||theTokens.get(walker).getContents().contentEquals("void")){
            declist();
        }
    }

    public void dec(){
        typespec();
        if(theTokens.get(walker).getType()==type.ID){
            tryToWalk();
        }
        else{
            REJECT();
        }
        decfactored();
    }

    private void decfactored() {
        if(theTokens.get(walker).getContents().contentEquals("[")){
            tryToWalk();
            vardec();
            if(theTokens.get(walker).getContents().contentEquals(";")){
                tryToWalk();
            }
            else{
                REJECT();
            }
        }
        else if(theTokens.get(walker).getContents().contentEquals("(")){
            tryToWalk();
            if(mSeen){
                REJECT();
            }
            else if(theTokens.get(walker-2).getContents().contentEquals("main")){
                mSeen=true;
            }
            fundec();
        }
        else{
            if(theTokens.get(walker).getContents().contentEquals(";")){
                tryToWalk();
            }
            else{
                REJECT();
            }
            scopifier.get(currentScope).put(theTokens.get(walker-2).getContents(), new Variable(0, true));
        }
    }

    private void fundec() {
        paramlist();
        if(theTokens.get(walker).getContents().contentEquals(")")){
            tryToWalk();
        }
        else{
            REJECT();
        }
        compoundStatement();
    }

    private void compoundStatement() {
        if(theTokens.get(walker).getContents().contentEquals("{")){
            tryToWalk();
            localDec();
            statementList();
            if(theTokens.get(walker).getContents().contentEquals("}")){
                scopifier.remove(currentScope--);
                tryToWalk();
            }
            else{
                REJECT();
            }
        }
        else{
            REJECT();
        }
    }

    private void statementList() {
        String yeet = theTokens.get(walker).getContents();
        Token checkMe = theTokens.get(walker);
        if(yeet.contentEquals("if")||yeet.contentEquals("return")||yeet.contentEquals("while")||yeet.contentEquals(";")||yeet.contentEquals("(")||yeet.contentEquals("{")||checkMe.getType()==type.ID||checkMe.getType()==type.num){
            statement();
            statementList();
        }
    }

    private void statement() {
        Token yeet = theTokens.get(walker);
        if(yeet.getContents().contentEquals("(")||yeet.getContents().contentEquals(";")||yeet.getType()==type.ID||yeet.getType()==type.num){
            expressionStmt();
        }
        else if(yeet.getContents().contentEquals("{")){
            scopifier.add(++currentScope, new HashMap<String, Variable>(theTokens.size()));
            tryToWalk();
            localDec();
            statementList();
            if(theTokens.get(walker).getContents().contentEquals("}")){
                scopifier.remove(currentScope--);
                tryToWalk();
            }
            else{
                REJECT();
            }
        }
        else if(yeet.getContents().contentEquals("if")){
            selectionStatement();
        }
        else if(yeet.getContents().contentEquals("while")){
            iterationStatement();
        }
        else if(yeet.getContents().contentEquals("return")){
            returnStatement();
        }
        else{
            REJECT();
        }
    }

    private void returnStatement() {
        tryToWalk();
        expressionStmt();
    }

    private void iterationStatement() {
        tryToWalk();
        if(theTokens.get(walker).getContents().contentEquals("(")){
            tryToWalk();
            expression();
            if(theTokens.get(walker).getContents().contentEquals(")")){
                tryToWalk();
                statement();
            }
            else{
                REJECT();
            }
        }
        else{
            REJECT();
        }
    }

    private void expression() {
        if(theTokens.get(walker).getType()==type.ID){
            scopeBounce(currentScope, theTokens.get(walker).getContents());
            tryToWalk();
            expressionFactored();
        }
        else if(theTokens.get(walker).getType()==type.num||theTokens.get(walker).getContents().contentEquals("(")){
            simpleExpression();
        }
    }

    private void simpleExpression() {
        additiveExpression();
        simpleExpressionFactored();
    }

    private void simpleExpressionFactored() {
        if(theTokens.get(walker).getType()==type.relop){
            tryToWalk();
            additiveExpression();
        }
    }

    private void additiveExpression() {
        term();
        additiveExpressionPrime();
    }

    private void additiveExpressionPrime() {
        if(theTokens.get(walker).getContents().contentEquals("+")||theTokens.get(walker).getContents().contentEquals("-")){
            tryToWalk();
            term();
            additiveExpressionPrime();
        }
    }

    private void term() {
        factor();
        termPrime();
    }

    private void termPrime() {
        if(theTokens.get(walker).getContents().contentEquals("*")||theTokens.get(walker).getContents().contentEquals("/")){
            tryToWalk();
            factorPrime();
            termPrime();
        }
    }

    private void factorPrime() {
        if(theTokens.get(walker).getContents().contentEquals("(")){
            tryToWalk();
            expression();
            if(theTokens.get(walker).getContents().contentEquals(")")){
                tryToWalk();
            }
            else{
                REJECT();
            }
        }
        else if(theTokens.get(walker).getType()==type.num){
            tryToWalk();
        }
        else if(theTokens.get(walker).getType()==type.ID){
            tryToWalk();
            factorMod();
        }
    }

    private void factorMod() {
        if(theTokens.get(walker).getContents().contentEquals("(")){
            callMod();
        }
        else if(theTokens.get(walker).getContents().contentEquals("[")){
            tryToWalk();
            expression();
            if(theTokens.get(walker).getContents().contentEquals("]")){
                tryToWalk();
            }
            else{
                REJECT();
            }
        }
    }

    private void callMod() {
        if(theTokens.get(walker).getContents().contentEquals("(")){
            tryToWalk();
            argsList();
            if(theTokens.get(walker).getContents().contentEquals(")")){
                tryToWalk();
            }
            else{
                REJECT();
            }
        }
        else{
            REJECT();
        }
    }

    private void argsList() {
        if(theTokens.get(walker).getType()==type.ID){
            expressionFactored();
            argsListPrime();
        }
        else if(theTokens.get(walker).getContents().contentEquals("(")){
            tryToWalk();
            expression();
            if(theTokens.get(walker).getContents().contentEquals(")")){
                tryToWalk();
            }
            else{
                REJECT();
            }
            termPrime();
            additiveExpressionPrime();
            simpleExpressionFactored();
            if(theTokens.get(walker).getContents().contentEquals(";")){
                tryToWalk();
                argsListPrime();
            }
            else{
                REJECT();
            }
        }
        else if(theTokens.get(walker).getType()==type.num){
            tryToWalk();
            termPrime();
            additiveExpressionPrime();
            simpleExpressionFactored();
            if(theTokens.get(walker).getContents().contentEquals(";")){
                tryToWalk();
                argsListPrime();
            }
            else{
                REJECT();
            }
        }
    }

    private void argsListPrime() {
        if(theTokens.get(walker).getContents().contentEquals(",")){
            tryToWalk();
            argsListPrime();
        }
    }

    private void factor() {
        if(theTokens.get(walker).getContents().contentEquals("(")){
            tryToWalk();
            if(scopifier.get(0).get(theTokens.get(walker-2).getContents()).getParam()){
                int holdval = walker;
                expression();
                if(holdval==walker){
                    REJECT();
                }
            }
            if(theTokens.get(walker).getContents().contentEquals(")")){
                tryToWalk();
            }
            else{
                REJECT();
            }
        }
        else if(theTokens.get(walker).getType()==type.num){
            tryToWalk();
        }
        else{
            REJECT();
        }
    }

    private void expressionFactored() {
        if(theTokens.get(walker).getContents().contentEquals("[")){
            tryToWalk();
            expression();
            if(theTokens.get(walker).getContents().contentEquals("]")){
                tryToWalk();
            }
            else{
                REJECT();
            }
            if(theTokens.get(walker).getContents().contentEquals("=")){
                tryToWalk();
                expression();
            }
            else if(theTokens.get(walker).getContents().contentEquals(";")){
                tryToWalk();
            }
            else{
                REJECT();
            }
        }
        else if(theTokens.get(walker).getContents().contentEquals("=")){
            tryToWalk();
            expression();
        }
        else{
            simpleExpressionPrime();
        }
    }

    private void simpleExpressionPrime() {
        termFactored();
        simpleExpressionFactoredPrime();
    }

    private void termFactored() {
        if(theTokens.get(walker).getContents().contentEquals("(")){
            callMod();
        }
        else{
            varFactored();
        }
        termPrime();
    }

    private void varFactored() {
        if(theTokens.get(walker).getContents().contentEquals("[")){
            tryToWalk();
            expression();
            if(theTokens.get(walker).getContents().contentEquals("]")){
                tryToWalk();
            }
            else{
                REJECT();
            }
        }
    }

    private void simpleExpressionFactoredPrime() {
        if(theTokens.get(walker).getType()==type.relop){
            tryToWalk();
            term();
        }
    }

    private void additiveExpressionPrimeFactored() {
        if(theTokens.get(walker).getContents().contentEquals("+")||theTokens.get(walker).getContents().contentEquals("-")){
            tryToWalk();
            term();
            additiveExpressionPrime();
        }
    }

    private void selectionStatement() {
        tryToWalk();
        if(theTokens.get(walker).getContents().contentEquals("(")){
            tryToWalk();
            expression();
            if(theTokens.get(walker).getContents().contentEquals(")")){
                tryToWalk();
                statement();
                selectionStatementFactored();
            }
            else{
                REJECT();
            }
        }
        else{
            REJECT();
        }
    }

    private void selectionStatementFactored() {
        if(theTokens.get(walker).getContents().contentEquals("else")){
            tryToWalk();
            if(theTokens.get(walker).getContents().contentEquals("else")){
                REJECT();
            }
            statement();
        }

    }

    private void expressionStmt() {
        if(theTokens.get(walker).getContents().contentEquals(";")){
            tryToWalk();
        }
        else{
            expression();
            if(theTokens.get(walker).getContents().contentEquals(";")){
                tryToWalk();
            }
            else{
                REJECT();
            }
        }
    }

    private void localDec() {
        if(theTokens.get(walker).getContents().contentEquals("int")||theTokens.get(walker).getContents().contentEquals("void")){
            tryToWalk();
            if(theTokens.get(walker).getType()==type.ID){
                scopifier.get(currentScope).put(theTokens.get(walker).getContents(), new Variable(0, true));
                tryToWalk();
                vardec();
                if(theTokens.get(walker).getContents().contentEquals(";")){
                    tryToWalk();
                    localDec();
                }
                else{
                    REJECT();
                }
            }
            else{
                REJECT();
            }
        }
    }

    private void paramlist() {
        param();
        paramListPrime();
    }

    private void paramListPrime() {
        if(theTokens.get(walker).getContents().contentEquals(",")){
            tryToWalk();
            param();
            paramListPrime();
        }
    }

    private void param() {
        scopifier.add(++currentScope, new HashMap<String, Variable>(theTokens.size()));
        if(theTokens.get(walker).getContents().contentEquals("int")){
            scopifier.get(currentScope-1).put(theTokens.get(walker-2).getContents(), new Variable(theTokens.get(walker-3).getContents().contentEquals("int"), true));
            tryToWalk();
            paramFactored();
        }
        else if (theTokens.get(walker).getContents().contentEquals("void")){
            scopifier.get(currentScope-1).put(theTokens.get(walker-2).getContents(), new Variable(theTokens.get(walker-3).getContents().contentEquals("int"), false));
            tryToWalk();
            paramFactored();
        }
        else{
            REJECT();
        }
    }

    private void paramFactored() {
        if(theTokens.get(walker).getType()==type.ID){
            if(scopifier.get(currentScope).containsKey(theTokens.get(walker).getContents())||theTokens.get(walker-1).getContents().contentEquals("void")){
                REJECT();
            }
            else{
                scopifier.get(currentScope).put(theTokens.get(walker).getContents(), new Variable(0, true));
            }
            tryToWalk();
            if(theTokens.get(walker).getContents().contentEquals("[")){
                tryToWalk();
                if(theTokens.get(walker).getContents().contentEquals("]")){
                    tryToWalk();
                }
                else{
                    REJECT();
                }
            }
        }
    }

    private void vardec() {
        if (theTokens.get(walker).getType()==type.num){
            scopifier.get(currentScope).put(theTokens.get(walker-2).getContents(), new Variable(true, Integer.parseInt(theTokens.get(walker).getContents())));
            tryToWalk();
            if(theTokens.get(walker).getContents().contentEquals("]")){
                tryToWalk();
            }
            else{
                REJECT();
            }
        }
    }

    private void typespec() {
        if(theTokens.get(walker).getContents().contentEquals("int")||theTokens.get(walker).getContents().contentEquals("void"))
            tryToWalk();
        else{
            REJECT();
        }
    }
    private void tryToWalk(){
        if (walker<theTokens.size()){
            walker++;
        }
        else{
            REJECT();
        }
    }

    private void REJECT(){
        System.out.println("REJECT");
        exit(-1);
    }

    private void scopeBounce(int checker, String fval){
        if(checker<0){
            REJECT();
        }
        if(scopifier.get(checker).containsKey(fval)){

        }
        else{
            scopeBounce(checker -1, fval);
        }
    }
}
