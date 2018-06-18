import java.util.ArrayList;
import java.util.List;

public class ScopeC {

    private ScopeC father;
    private ArrayList<Variables> varArray;
    private ArrayList<ScopeC> innerScopeArr;
    private ArrayList<Method> methodArr;

    public void addToMethodArr(Method method) {
        this.methodArr.add(method);
    }

    public ScopeC(ScopeC father){
        varArray = new ArrayList<>();
        this.father = father;
        this.innerScopeArr = new ArrayList<>();
        this.methodArr = new ArrayList<>();
    }


    public ScopeC getFather() {
        return father;
    }
    public ArrayList<Variables> getVarArray(){

        return varArray;
    }


    public Variables getVariable(String variable){
        for (Variables var: varArray) {
            if(var.getName().equals(variable)){
                return var;
            }
        }
        if(father == null){
            return null;
            }
        else {
            return father.getVariable(variable);
        }
    }
    // todo
    protected boolean isArgValid(){
        return false;
    }

    protected Method getMethod(String methodName) throws src.MyExceptions {
        for (Method method :
                methodArr) {
            if(method.getName().equals(methodName)){
                return method;
            }

        }
        throw new src.MyExceptions();
    }



    protected void addVariable(Variables var){
        varArray.add(var);
    }

    protected void addInnerScope(ScopeC innerScopeC){

        innerScopeArr.add(innerScopeC);
    }


}
