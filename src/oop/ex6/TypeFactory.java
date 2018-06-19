package oop.ex6;

import java.util.ArrayList;
import java.util.LinkedList;

public enum TypeFactory {
    Variable{
        protected ScopeC interpret(ScopeC scopeC, String command) throws MyExceptions {
            ArrayList<Variables<Object>> varArr = parser.parseVar(command,scopeC);
            for (Variables var: varArr) {
                scopeC.addVariable(var);
            }
            return scopeC;
        }
    },
    MethodDeclare{
        protected ScopeC interpret(ScopeC scopeC, String command) throws MyExceptions {
            Method innerScope = parser.parseMethodDeceleration(command, scopeC);
            scopeC.addToMethodArr(innerScope);
            return innerScope;
            }

    },
    VariableAssignment{
        protected ScopeC interpret(ScopeC scopeC, String command) throws MyExceptions {
            parser.assignVar(command,scopeC);
            return scopeC;
        }

    },
    IfWhileBlock{
        protected ScopeC interpret(ScopeC scopeC, String command) throws MyExceptions {
            ScopeC innerScope = parser.ParesIfWhile(command, scopeC);
            return innerScope;

        }

    },
    MethodCall{
        protected ScopeC interpret(ScopeC scopeC, String command) throws MyExceptions {

            Method calledMethod = parser.parseMethodCall(scopeC, command);
            calledMethod.runMethod(scopeC);
            return scopeC;
        }

    },
    lineError{
        protected ScopeC interpret(ScopeC scopeC, String command) throws MyExceptions {
            throw new MyExceptions(INVALID_LINE_FORMAT);
        }


    },
    ScopeClosing{
        protected ScopeC interpret(ScopeC scopeC, String command) throws MyExceptions {
            ScopeC father = scopeC.getFather();
            if(father == null){
                throw new MyExceptions(OUTER_SCOPE_CLOSE); // todo exception handling
            }
            return scopeC.getFather();
        }
    },
    Return {
        protected ScopeC interpret(ScopeC scopeC, String command) throws MyExceptions {
            return scopeC;
        }
    },
    Note{
        protected ScopeC interpret(ScopeC scopeC, String command) throws MyExceptions {
            return scopeC;
        }
    },
    Emptyline{
        protected ScopeC interpret(ScopeC scopeC, String command) throws MyExceptions {
            return scopeC;
        }

    };

    public static final String OUTER_SCOPE_CLOSE = "Outer scope shouldn't be close";
    public static final String INVALID_LINE_FORMAT = "Invalid line format";

    abstract protected ScopeC interpret(ScopeC scopeC,String command) throws MyExceptions;
    protected Parser parser = new Parser();
    
}
