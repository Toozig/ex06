import java.util.ArrayList;

public enum TypeFactory {

    Variable {
        protected Scope interpret(String line, Scope scope) throws src.MyExceptions {
            ArrayList<Variables> varArr = parser.parseVar(line, scope);
            for (Variables var : varArr) {
                scope.addVariable(var);
            }
            return scope;
        }


    },
    MethodDeclare {
        protected Scope interpret(String line, Scope scope) throws src.MyExceptions {
//            Method innerScope = parser.parseMethodDeceleration(line, scope);
//            scope.addInnerScope(innerScope);
            return scope;
        }

    },
    VariableAssignment {
        protected Scope interpret(String line, Scope scope) {
            parser.assignVar(line, scope);
            return scope;
        }

    },
    IfWhileBlock {
        protected Scope interpret(String line, Scope scope) throws src.MyExceptions {
            Scope innerScope = parser.ParesIfWhile(line, scope);
            scope.addInnerScope(innerScope);
            return innerScope;

        }

    },
    MethodCall {
        protected Scope interpret(String line, Scope scope) {
            return scope.getFather();
        }

    },
    lineError {
        protected Scope interpret(String line, Scope scope) throws src.MyExceptions {
            throw new src.MyExceptions();
        }


    },
    ScopeClosing {
        protected Scope interpret(String line, Scope scope) throws src.MyExceptions {
            Scope father = scope.getFather();
            if (father == null) {
                throw new src.MyExceptions(); // todo exception handling
            }
            return scope.getFather();
        }
    },

    Return {
        @Override
        protected Scope interpret(String line, Scope scope) {
            return scope;
        }
    },
    Note {
        protected Scope interpret(String line, Scope scope) {
            return scope;
        }
    },
    Empty_line {
        protected Scope interpret(String line, Scope scope) {
            return scope;
        }
    };

    abstract protected Scope interpret(String line, Scope scope) throws src.MyExceptions;

    Parser parser = new Parser();

}
