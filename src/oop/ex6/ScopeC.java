package oop.ex6;

import java.util.ArrayList;

public class ScopeC {

    public static final String INVALID_LINE = "Invalid line";
    private ScopeC father;
    private ArrayList<Variables> varArray;
    private ArrayList<ScopeC> innerScopeArr;
    private ArrayList<Method> methodArr;
    private ArrayList<Line> scopeLines;

    public void addToMethodArr(Method method) {
        this.methodArr.add(method);
    }

    public ScopeC(ScopeC father) {
        varArray = new ArrayList<>();
        this.father = father;
        this.innerScopeArr = new ArrayList<>();
        this.methodArr = new ArrayList<>();
        scopeLines = new ArrayList<>();

    }

    public void addScopeLines(Line line) {
        this.scopeLines.add(line);
    }

    public ScopeC getFather() {
        return father;
    }

    public ArrayList<Variables> getVarArray() {

        return varArray;
    }

    public ArrayList<Method> getMethodArr() {
        return methodArr;
    }

    public ArrayList<Line> getScopeLines() {
        return scopeLines;
    }

    public void setScopeLines(ArrayList<Line> scopeLines) {
        this.scopeLines = scopeLines;
    }

    public Variables getVariable(String variable) {
        for (Variables var : varArray) {
            if (var.getName().equals(variable)) {
                return var;
            }
        }
        if (father == null) {
            return null;
        } else {
            return father.getVariable(variable);
        }
    }

    // todo
    protected boolean isVarInTheGlobalScope(Variables var) {
        ScopeC curScope = this;
        while (curScope.father != null) {
            curScope = curScope.father;
        }
        if (curScope.getVarArray().contains(var)) {
            return true;
        }
        return false;
    }

    protected Method getMethod(String methodName) throws MyExceptions {
        ScopeC globalScope = this;
        while (globalScope.getFather() != null) {
            globalScope = globalScope.getFather();
        }
        for (Method method :
                globalScope.getMethodArr()) {
            if (method.getName().equals(methodName)) {
                return method;
            }

        }
        throw new MyExceptions(INVALID_LINE);
    }

    public void setFather(ScopeC father) {
        this.father = father;
    }

    protected void addVariable(Variables var) {
        varArray.add(var);
    }

    protected void addInnerScope(ScopeC innerScopeC) {

        innerScopeArr.add(innerScopeC);
    }

    // Gets the method in which a inner scope is indise s
    Method getScopesMethod() {
        if (father.getFather() == null) {
            return (Method) this;
        }
        return father.getScopesMethod();
    }


}
