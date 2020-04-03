import java.util.ArrayList;


public class Compiler {

    public static void main(String[] args) {
        Lexer lexMe = new Lexer();
        ArrayList<Token> lexemes = lexMe.lex(args[0]);
        Token terminal = new Token("$", type.terminal);
        lexemes.add(terminal);
        Parser parseMe = new Parser(lexemes);
        parseMe.parse();
    }
}
