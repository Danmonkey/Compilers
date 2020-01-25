public class Token {
    private String contents = "";
    private type theType;
    Token(){

    }

    Token(String sCont, type sType){
        contents = sCont;
        theType = sType;
    }

    public String getContents() {
        return contents;
    }

    public void setType(type tType){
        theType = tType;
    }

    public String toString(){
        return contents.concat(String.valueOf(theType));
    }
}
