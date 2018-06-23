package oop.ex6;

import java.util.ArrayList;

/**
 * Represents a scope in the sjava file
 */
public class MScope {
    //Attributes
    private static final String NOSUCHMETHODEXIST = "No such method exist";
    private MScope father;
    private ArrayList<Variables> varArray;
    private ArrayList<MScope> innerScopeArr;
    private ArrayList<Method> methodArr;
    private ArrayList<Line> scopeLines;

    /**
     * Add method to the current scopes' method array
     * @param method the method's array
     */
    public void addToMethodArr(Method method) {
        this.methodArr.add(method);
    }

    /**
     * Constructs a scope object
     * @param father the father of this scope (the scope "above" it)
     */
    MScope(MScope father) {
        varArray = new ArrayList<>();
        this.father = father;
        this.innerScopeArr = new ArrayList<>();
        this.methodArr = new ArrayList<>();
        scopeLines = new ArrayList<>();

    }

    /**
     * Add a line to the scope's line
     * @param line the line to add
     */
    public void addScopeLines(Line line) {
        this.scopeLines.add(line);
    }

    /**
     * Get's the father of the scope
     * @return the father of the scope
     */
    public MScope getFather() {
        return father;
    }

    /**
     * Get's the scope's var array
     * @return the scope's var array
     */
    public ArrayList<Variables> getVarArray() {

        return varArray;
    }

    /**
     * Get's the method's var array
     * @return the method's var array
     */
    public ArrayList<Method> getMethodArr() {
        return methodArr;
    }

    /**
     * Get's the scope's lines
     * @return the scope's lines
     */
    public ArrayList<Line> getScopeLines() {
        return scopeLines;
    }

    /**
     * Get a variable from all of the scope above you including you
     * @param variable the var name to look for
     * @return the variable
     */
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

    /**
     * Check if a given var is in the global scope
     * @param var the var to check
     * @return true iff the var is in the global scope
     */
    protected boolean isVarInTheGlobalScope(Variables var) {
        MScope curScope = this;
        while (curScope.father != null) {
            curScope = curScope.father;
        }
        if (curScope.getVarArray().contains(var)) {
            return true;
        }
        return false;
    }

    /**
     * Get's a method from the global scope
     * @param methodName the name of the method to get to get
     * @return the method
     * @throws ParsingException thrown if there's no such method
     */
    protected Method getMethod(String methodName) throws ParsingException {
        MScope globalScope = this;
        while (globalScope.getFather() != null) {
            globalScope = globalScope.getFather();
        }
        for (Method method :
                globalScope.getMethodArr()) {
            if (method.getName().equals(methodName)) {
                return method;
            }

        }
        throw new ParsingException(NOSUCHMETHODEXIST);
    }

    /**
     * Set's the scope's father
     * @param father the father to set
     */
    public void setFather(MScope father) {
        this.father = father;
    }

    /**
     * Add variable to the scope's variable array
     * @param var the var to add
     */
    protected void addVariable(Variables var) {
        varArray.add(var);
    }




    /**
     * Get's the method in which a inner scope is inside
     * @return the method in which a inner scope is inside
     */
    Method getScopesMethod() {
        if (father.getFather() == null) {
            return (Method) this;
        }
        return father.getScopesMethod();
    }


}
