public class Variable {
    private boolean isFunc = false;
    private int val;
    private boolean isInt = true;
    private boolean needsParams = false;
    private boolean isAry = false;


    public Variable(int sVal, boolean inty){
        val = sVal;
        isInt = inty;
    }
    public Variable(boolean inty, boolean needsParams){
        isInt = inty;
        isFunc = true;
        this.needsParams = needsParams;
    }
    public Variable(boolean isAry, int size){
        val = size;
        this.isAry = isAry;
    }

    public boolean Func(){
        return isFunc;
    }

    public boolean Int(){
        return isInt;
    }

    public int Val(){
        return val;
    }

    public void setVal(int nVal){
        val = nVal;
    }

    public boolean getParam(){
        return needsParams;
    }
}
