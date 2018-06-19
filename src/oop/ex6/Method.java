package oop.ex6;

import jdk.nashorn.internal.runtime.Scope;

import java.util.ArrayList;

public class Method extends ScopeC {

    public static final String UNINITIALIZED_VARIABLE_ERROR = "Uninitialized variable call";
    private ArrayList<Variables> arguments;
    private String name;
    private boolean isCalled;

    public Method(ScopeC father, ArrayList<Variables> arguments, String name) throws MyExceptions {
        super(father);
        this.name = name;
        this.isCalled = false;
        this. arguments = isLegalVar(arguments);
    }

    public boolean isCalled() {
        return isCalled;
    }

    public void setCalled(boolean called) {
        isCalled = called;
    }

    // checks if the method variable ar valid
    private ArrayList<Variables> isLegalVar(ArrayList<Variables> varList) throws MyExceptions {
        for (Variables variable: varList) {
            if(variable.getData() != null){
                throw new MyExceptions(UNINITIALIZED_VARIABLE_ERROR); // todo exceptions , initialized variable
            }
        }
        return varList;
    }

    public void setArguments(ArrayList<Variables> arguments) {
        this.arguments = arguments;
    }

    public ArrayList<Variables> getArguments() {
        return arguments;
    }

    public String getName() {
        return name;
    }

    protected void runMethod(ScopeC scopeC) throws MyExceptions {
        ScopeC father = this.getFather();
        if(scopeC != this) {
            this.setFather(scopeC);
        }
        isCalled = true;
        for(Line line: this.getScopeLines()){
            line.interperate(this);
        }
        this.setFather(father);
        isCalled = false;

    }
}
