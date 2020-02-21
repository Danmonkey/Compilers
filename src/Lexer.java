import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.exit;

public class Lexer {

    public Lexer(){

    }

    public ArrayList<Token> lex(String inputString)    {
    Pattern keys = Pattern.compile("^(if|int|void|else|return|while)(?![a-zA-Z])");
    Pattern nums = Pattern.compile("^\\d+");
    Pattern ID = Pattern.compile("^[a-zA-Z]+");
    Pattern stripper = Pattern.compile("(^\\s+)");
    Pattern delim = Pattern.compile("^[;()\\[\\]{},]");
    Pattern mathOp = Pattern.compile("^[+\\-/*=]");
    Pattern relOp = Pattern.compile("^[<>=!]=|^[<>]");
    Pattern shortComment = Pattern.compile("^//");
    Pattern longComment = Pattern.compile("^/\\*");
    Pattern errors = Pattern.compile("^[^a-zA-Z0-9;()\\[\\]{}*/+\\-<>=, \\s]|!(?!=)");
    boolean commentflag = false;
    File pull = null;
    Scanner yeet = null;
    int countDiff = 0;
    ArrayList<Token> holdsIt = new ArrayList<>();
        try {
        pull = new File(inputString);
        yeet = new Scanner(pull);
    } catch (Exception e) {
        System.out.println("REJECT");
        exit(-1);
    }
        yeet.useDelimiter("\n");
        while (yeet.hasNext()) {
        String toLex = yeet.next();
        while (!toLex.isEmpty()) {
            Matcher lCommentBegin = longComment.matcher(toLex);
            if (lCommentBegin.find()) {
                commentflag = true;
                toLex = lCommentBegin.replaceFirst("");
            }
            if (!commentflag) {
                Matcher sCommentMatch = shortComment.matcher(toLex);
                if (sCommentMatch.find())
                    toLex = "";

                Matcher keyMatch = keys.matcher(toLex);
                if (keyMatch.find()) {
                    String s = keyMatch.group();
                    Token addMe = new Token(s, type.key);
                    holdsIt.add(addMe);
                    toLex = keyMatch.replaceFirst("");
                    countDiff++;
                    continue;
                }
                Matcher IDMatch = ID.matcher(toLex);
                if(IDMatch.find()) {
                    String s = IDMatch.group();
                    Token addMe = new Token(s, type.ID);
                    holdsIt.add(addMe);
                    toLex = IDMatch.replaceFirst("");
                    countDiff++;
                    continue;
                }
                Matcher delimMatch = delim.matcher(toLex);
                if (delimMatch.find()) {
                    String s = delimMatch.group();
                    Token addMe = new Token(s, type.delim);
                    holdsIt.add(addMe);
                    toLex = delimMatch.replaceFirst("");
                    countDiff++;
                    continue;
                }
                Matcher relMatch = relOp.matcher(toLex);
                if (relMatch.find()) {
                    String s = relMatch.group();
                    Token addMe = new Token(s, type.relop);
                    holdsIt.add(addMe);
                    toLex = relMatch.replaceFirst("");
                    countDiff++;
                    continue;
                }
                Matcher mathMatch = mathOp.matcher(toLex);
                if (mathMatch.find()) {
                    String s = mathMatch.group();
                    Token addMe = new Token(s, type.mathOp);
                    holdsIt.add(addMe);
                    toLex = mathMatch.replaceFirst("");
                    countDiff++;
                    continue;
                }

                Matcher numMatch = nums.matcher(toLex);
                if (numMatch.find()) {
                    String s = numMatch.group();
                    Token addMe = new Token(s, type.num);
                    holdsIt.add(addMe);
                    toLex = numMatch.replaceFirst("");
                    countDiff++;
                    continue;
                }
                Matcher errorMatch = errors.matcher(toLex);
                if (errorMatch.find()) {
                    String s = errorMatch.group();
                    Token addMe = new Token(s, type.errors);
                    holdsIt.add(addMe);
                    toLex = errorMatch.replaceFirst("");
                    countDiff++;
                    continue;
                }
                Matcher stripMatch = stripper.matcher(toLex);
                if (stripMatch.find()){
                    toLex = stripMatch.replaceFirst("");
                }
            } else {
                toLex = lexBigComment(toLex);
                if(toLex==null){
                    commentflag=false;
                    toLex = "";
                }
                else if (toLex.isEmpty()){
                    toLex=yeet.next();
                }
                else{
                    commentflag=false;
                }
            }
        }
    }
        return holdsIt;
}

    public static String lexBigComment(String lexMe){
        char[] chary = lexMe.toCharArray();
        int boundary = 0;
        for(int i =0;i<chary.length-1;i++){
            if(chary[i]=='*'){
                if(chary[i+1]=='/'){
                    boundary = i+2;
                    break;
                }
            }
        }
        if(boundary==0){
            return "";
        }
        else if(boundary>=chary.length){
            return null;
        }
        else{
            StringBuilder keep = new StringBuilder();
            for (;boundary<=chary.length-1;boundary++){
                keep.append(chary[boundary]);
            }
            return keep.toString();
        }
    }
}
