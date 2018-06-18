public enum TypeFactory {
    Variable{
        protected Scope interpret(String line, Scope scope){
            parser.parseVar(line,scope);
            return scope;
        }

    },
    MethodDeclare{
        protected Scope interpret(String line, Scope scope) throws src.MyExceptions {
//            Method innerScope = parser.parseMethodDeceleration(line, scope);
//            scope.addInnerScope(innerScope);
            return scope;
            }

    },
    Variable_Assignment{
        protected Scope interpret(String line, Scope scope){
            parser.assignVar(line,scope);
            return scope;
        }

    },
    IfWhileBlock{
        protected Scope interpret(String line, Scope scope) throws src.MyExceptions {
            Scope innerScope = parser.ParesIfWhile(line, scope);
            scope.addInnerScope(innerScope);
            return innerScope;

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
        protected Scope interpret(String line, Scope scope) throws src.MyExceptions {
            Scope father = scope.getFather();
            if(father == null){
                throw new src.MyExceptions(); // todo exception handling
            }
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
    },
    Empty_line{
        protected Scope interpret(String line, Scope scope) {
            return scope;
        }
    };
    abstract protected Scope interpret(String line, Scope scope) throws src.MyExceptions;
    Parser parser = new Parser();

}
