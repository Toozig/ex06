package src;

import java.util.ArrayList;
import java.util.List;

public class  Scope {

    protected List<String> textArray;
    private Scope father;
    private ArrayList<Variables> varArray;

    public Scope(Scope father){
        this.textArray = new ArrayList<>();
        varArray = new ArrayList<>();
        this.father = father;
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

    public void addVariabel(Variables var){
        varArray.add(var);
    }


}
