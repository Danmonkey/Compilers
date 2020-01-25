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
        Pattern symbols = Pattern.compile("^([+\\-;/.()\\[\\]{}]|[<>=!]|\\*/|\\*)=");
        Pattern shortComment = Pattern.compile("^//");
        Pattern longComment = Pattern.compile("^\\*/");
	    File pull = null;
	    Scanner yeet = null;
	    int count = 0;
	    boolean commentFlag = false;
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
            Matcher keyMatch = keys.matcher(toLex);
            Matcher numMatch = nums.matcher(toLex);
            Matcher IDMatch = ID.matcher(toLex);
            Matcher stripMatch = stripper.matcher(toLex);
            Matcher symbolMatch = symbols.matcher(toLex);
            Matcher lCommentMatch = longComment.matcher(toLex);
            Matcher sCommentMatch = shortComment.matcher(toLex);
            stripMatch.replaceAll("");
            System.out.println("Read: " + toLex);
            int countDiff = 0;
            while (!toLex.isEmpty()) {

            }
        }
    }
}
