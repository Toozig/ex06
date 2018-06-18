import java.util.ArrayList;

public enum TypeFactory {
    Variable{
        protected ScopeC interpret(String line, ScopeC scopeC){
             ArrayList<Variables> varArr = parser.parseVar(line, scopeC);
            for (Variables var :  varArr) {
                scopeC.addVariable(var);
            }
            return scopeC;
        }

    },
    MethodDeclare{
        protected ScopeC interpret(String line, ScopeC scopeC) throws src.MyExceptions {
            Method innerScope = parser.parseMethodDeceleration(line, scopeC);
            scopeC.addInnerScope(innerScope);
            scopeC.addToMethodArr(innerScope);
            return scopeC;
            }

    },
    VariableAssignment{
        protected ScopeC interpret(String line, ScopeC scopeC){
            parser.assignVar(line, scopeC);
            return scopeC;
        }

    },
    IfWhileBlock{
        protected ScopeC interpret(String line, ScopeC scopeC) throws src.MyExceptions {
            ScopeC innerScopeC = parser.ParesIfWhile(line, scopeC);
            scopeC.addInnerScope(innerScopeC);
            return innerScopeC;

        }

    },
    Method_all{
        protected ScopeC interpret(String line, ScopeC scopeC){
            return scopeC.getFather();
        }

    },
    line_Error{
        protected ScopeC interpret(String line, ScopeC scopeC) throws src.MyExceptions {
            throw new src.MyExceptions();
        }


    },
    ScopeClosing{
        protected ScopeC interpret(String line, ScopeC scopeC) throws src.MyExceptions {
            ScopeC father = scopeC.getFather();
            if(father == null){
                throw new src.MyExceptions(); // todo exception handling
            }
            return scopeC.getFather();
        }
    },

    Return{
        @Override
        protected ScopeC interpret(String line, ScopeC scopeC) {
            return scopeC;
        }
    },
    Note{
        protected ScopeC interpret(String line, ScopeC scopeC) {
            return scopeC;
        }
    },
    Emptyline{
        protected ScopeC interpret(String line, ScopeC scopeC) {
            return scopeC;
        }
    };
    abstract protected ScopeC interpret(String line, ScopeC scopeC) throws src.MyExceptions;
    Parser parser = new Parser();

}
