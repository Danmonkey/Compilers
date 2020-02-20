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
        if(theTokens.get(walker).getContents().contentEquals("int")||theTokens.get(walker).getContents().contentEquals("void")){
            tryToWalk();
        }
        else if(theTokens.get(walker).getType()!=type.terminal){
            REJECT();
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
            fundec();
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
            tryToWalk();
            localDec();
            statementList();
            if(theTokens.get(walker).getContents().contentEquals("}")){
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
            tryToWalk();
            expressionFactored();
        }
        else if(theTokens.get(walker).getType()==type.num||theTokens.get(walker).getContents().contentEquals("(")){
            simpleExpression();
            if(theTokens.get(walker).getContents().contentEquals(";")){
                tryToWalk();
            }
            else{
                REJECT();
            }
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
            else{
                REJECT();
            }
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
            additiveExpressionPrimeFactored();
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
        if(theTokens.get(walker).getContents().contentEquals("int")||theTokens.get(walker).getContents().contentEquals("void")){
            tryToWalk();
            paramFactored();
        }
        else{
            REJECT();
        }
    }

    private void paramFactored() {
        if(theTokens.get(walker).getType()==type.ID){
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
}
