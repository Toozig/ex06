import src.Variables;

import java.util.ArrayList;
import java.util.List;

public class  Scope {

    private List<String> textArray;
    private Scope father;
    private ArrayList<src.Variables> varArray;
    private ArrayList<Scope> innerScopeArr;

    public Scope(Scope father, List<String> text){
        this.textArray = text;
        varArray = new ArrayList<>();
        this.father = father;
        innerScopeArr = new ArrayList<>();
        textArray = text;
    }

    public List<String> getTextArray() {
        return textArray;
    }

    public Scope getFather() {
        return father;
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



    protected void addVariable(String[] varLine){
    }

    protected void addInnerScope(Scope innerScope){
        innerScopeArr.add(innerScope);
    }


}
