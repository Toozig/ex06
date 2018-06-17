import src.MyExceptions;

public enum TypeFactory {
    Variable{
        protected Scope interpret(String line, Scope scope){
            return scope.getFather();
        }

    },
    Method_Declare{
        protected Scope interpret(String line, Scope scope){
            return scope.getFather();
        }

    },
    Variable_Assignment{
        protected Scope interpret(String line, Scope scope){
            return scope.getFather();
        }

    },
    If_While_Block{
        protected Scope interpret(String line, Scope scope){
            return scope.getFather();
        }

    },
    Method_Call{
        protected Scope interpret(String line, Scope scope){
            return scope.getFather();
        }

    },
    line_Error{
        protected Scope interpret(String line, Scope scope) throws src.MyExceptions {
            throw new src.MyExceptions();
        }


    },
    Scope_Closing{
        protected Scope interpret(String line, Scope scope){
            return scope.getFather();
        }
    },

    Return{
        @Override
        protected Scope interpret(String line, Scope scope) {
            return scope;
        }
    },
    Note{
        protected Scope interpret(String line, Scope scope) {
            return scope;
        }
    };
    abstract protected Scope interpret(String line, Scope scope) throws src.MyExceptions;
}
