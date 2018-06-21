package oop.ex6;

import java.util.ArrayList;

/**
 * Type factory object classifies the different interpretation according to the line type
 */
public enum TypeFactory {
    Variable{
        protected ScopeC interpret(ScopeC scopeC, String command) throws ParsingException {
            ArrayList<Variables> varArr = parser.parseVar(command,scopeC);
            for (Variables var: varArr) {
                scopeC.addVariable(var);
            }
            return scopeC;
        }
    },
    MethodDeclare{
        protected ScopeC interpret(ScopeC scopeC, String command) throws ParsingException {
            Method innerScope = parser.parseMethodDeceleration(command, scopeC);
            scopeC.addToMethodArr(innerScope);
            return innerScope;
            }

    },
    VariableAssignment{
        protected ScopeC interpret(ScopeC scopeC, String command) throws ParsingException {
            parser.assignVar(command,scopeC);
            return scopeC;
        }

    },
    IfWhileBlock{
        protected ScopeC interpret(ScopeC scopeC, String command) throws ParsingException {
            ScopeC innerScope = parser.ParseIfWhile(command, scopeC);
            return innerScope;

        }

    },
    MethodCall{
        protected ScopeC interpret(ScopeC scopeC, String command) throws ParsingException {

            Method calledMethod = parser.parseMethodCall(scopeC, command);
            calledMethod.runMethod(scopeC);
            return scopeC;
        }

    },
    lineError{
        protected ScopeC interpret(ScopeC scopeC, String command) throws ParsingException {
            throw new ParsingException(INVALID_LINE_FORMAT);
        }


    },
    ScopeClosing{
        protected ScopeC interpret(ScopeC scopeC, String command) throws ParsingException {
            ScopeC father = scopeC.getFather();
            if(father == null){
                throw new ParsingException(OUTER_SCOPE_CLOSE);
            }
            return scopeC.getFather();
        }
    },
    Return {
        protected ScopeC interpret(ScopeC scopeC, String command) throws ParsingException {
            return scopeC;
        }
    },
    Note{
        protected ScopeC interpret(ScopeC scopeC, String command) throws ParsingException {
            return scopeC;
        }
    },
    Emptyline{
        protected ScopeC interpret(ScopeC scopeC, String command) throws ParsingException {
            return scopeC;
        }

    };

    public static final String OUTER_SCOPE_CLOSE = "Outer scope shouldn't be close";
    public static final String INVALID_LINE_FORMAT = "Invalid line format";

    /**
     * Interprets a line according to it's type
     * @param scopeC the scope in which the line is
     * @param command the command
     * @return the updated scope after the interpretation
     * @throws ParsingException thrown if the line was illegal
     */
    abstract protected ScopeC interpret(ScopeC scopeC, String command) throws ParsingException;
    protected Parser parser = new Parser();
    
}
