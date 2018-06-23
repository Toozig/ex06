package oop.ex6;

import java.util.ArrayList;

/**
 * A method object represents an method in the sjava file
 */
public class Method extends MScope {
    //constants
    private ArrayList<Variables> arguments;
    private String name;
    private boolean isCalled;
    private boolean gotReturn;

    /**
     * Constructs method object
     * @param father the father of this method
     * @param arguments the variable arguments of the method
     * @param name the name of the method
     */
    Method(MScope father, ArrayList<Variables> arguments, String name) {
        super(father);
        this.name = name;
        this.isCalled = false;
        this. arguments = addsVariablesOfMethod(arguments);
        this.gotReturn = false;
    }

    /**
     * Check if the method was called
     * @return true iff the method was called
     */

     boolean isCalled() {
        return isCalled;
    }

    /**
     * Check if the method got returned
     * @return true iff the method got returned
     */
     boolean GotReturn() {
        return gotReturn;
    }

    /**
     * Sets the got return
     * @param gotReturn boolean arg determines whether the method was called
     */
     void setGotReturn(boolean gotReturn) {
        this.gotReturn = gotReturn;
    }


    /**
     * Adds the variable arguments to the method varlist
     * @param varList the variables list
     */
    private ArrayList<Variables> addsVariablesOfMethod(ArrayList<Variables> varList) {
        for (Variables variable: varList) {
            this.addVariable(variable);
        }
        return varList;
    }

    /**
     * Get's the arguments of the method
     * @return the arguments
     */
     ArrayList<Variables> getArguments() {
        return arguments;
    }

    /**
     * Get's the name of the method
     * @return the name of the method
     */
    public String getName() {
        return name;
    }

    /**
     * Runs the method if it was called
     * @param MScope the scope the method is called
     * @throws ParsingException if the method was illegal
     */
     void runMethod(MScope MScope) throws ParsingException {
        MScope father = this.getFather();
        if(MScope == this) {
            return;
        }
        isCalled = true;
        for(Line line: this.getScopeLines()){
            line.interpret(this);
        }
        this.setFather(father);
        isCalled = false;
    }
}
