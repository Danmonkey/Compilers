import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import static java.lang.System.exit;
import java.util.regex.*;


public class Main {

    public static void main(String[] args) {
        Pattern keys = Pattern.compile("^(if|int|void|else|return|while)(?![a-zA-Z])");
        Pattern nums = Pattern.compile("^\\d+");
        Pattern ID = Pattern.compile("^[a-zA-Z]+");
        Pattern stripper = Pattern.compile("\\s");
        Pattern delim = Pattern.compile("^([;()\\[\\]{}]|\\*/)");
        Pattern mathOp = Pattern.compile("^[+\\-/*]|[<>=](?!=)");
        Pattern relOp = Pattern.compile("^[<>=!]=");
        Pattern shortComment = Pattern.compile("^//");
        Pattern longComment = Pattern.compile("^\\*/");
        Pattern lCommentEnd = Pattern.compile("^/\\*");
        Pattern withinComment = Pattern.compile("^[^*/]");
	    File pull = null;
	    Scanner yeet = null;
	    int count = 0;
	    ArrayList<Token> holdsIt = new ArrayList<>();
	    try{
	        pull = new File(args[0]);
            yeet = new Scanner(pull);
        } catch (Exception e) {
	        System.out.println("File name was invalid.");
            e.printStackTrace();
            exit(-1);
        }
	    yeet.useDelimiter("\n");
        while(yeet.hasNext()) {
            String toLex = yeet.next();
            Matcher stripMatch = stripper.matcher(toLex);
            System.out.println("Read: " + toLex);
            toLex = stripMatch.replaceAll("");
            int countDiff = 0;
            while (!toLex.isEmpty()) {
                Matcher keyMatch = keys.matcher(toLex);
                while(keyMatch.find()){
                    String s = keyMatch.group();
                    Token addMe = new Token(s, type.key);
                    holdsIt.add(addMe);
                    toLex = keyMatch.replaceFirst("");
                    countDiff++;
                }
                Matcher IDMatch = ID.matcher(toLex);
                while(IDMatch.find()){
                    String s = IDMatch.group();
                    Token addMe = new Token(s, type.ID);
                    holdsIt.add(addMe);
                    toLex = IDMatch.replaceFirst("");
                    countDiff++;
                }
                Matcher delimMatch = delim.matcher(toLex);
                while(delimMatch.find()){
                    String s = delimMatch.group();
                    Token addMe = new Token(s, type.delim);
                    holdsIt.add(addMe);
                    toLex = delimMatch.replaceFirst("");
                    countDiff++;
                }
                Matcher mathMatch = mathOp.matcher(toLex);
                while(mathMatch.find()){
                    String s = mathMatch.group();
                    Token addMe = new Token(s, type.mathOp);
                    holdsIt.add(addMe);
                    toLex = mathMatch.replaceFirst("");
                    countDiff++;
                }
                Matcher relMatch = relOp.matcher(toLex);
                while(relMatch.find()){
                    String s = relMatch.group();
                    Token addMe = new Token(s, type.relop);
                    holdsIt.add(addMe);
                    toLex = relMatch.replaceFirst("");
                    countDiff++;
                }
                Matcher numMatch = nums.matcher(toLex);
                while(numMatch.find()){
                    String s = numMatch.group();
                    Token addMe = new Token(s, type.num);
                    holdsIt.add(addMe);
                    toLex = numMatch.replaceFirst("");
                    countDiff++;
                }
                Matcher sCommentMatch = shortComment.matcher(toLex);
                if(sCommentMatch.find())
                    toLex = "";
                Matcher lCommentBegin = longComment.matcher(toLex);
                if(lCommentBegin.find()){
                    Matcher wCommentMatch = withinComment.matcher(toLex);
                    while(wCommentMatch.find()){
                        toLex = wCommentMatch.replaceFirst("");
                        if(toLex.isEmpty()) {
                            toLex = yeet.next();
                            wCommentMatch = withinComment.matcher(toLex);
                        }
                    }
                    Matcher eCommment = lCommentEnd.matcher(toLex);
                    if(eCommment.find()) {
                        toLex = eCommment.replaceFirst("");
                    } else System.out.println("fuck");
                }
                for(int i = count+countDiff;count<i;count++){
                    System.out.println(holdsIt.get(count).toString());
                }
            }
        }
    }
}
