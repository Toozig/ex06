import java.util.ArrayList;

public enum TypeFactory {
    Variable{
        protected ScopeC interpret() throws src.MyExceptions {
            parser.parseVar(command,scopeC);
            return scopeC;
        }
    },
    MethodDeclare{

        protected ScopeC interpret() throws src.MyExceptions {
            Method innerScope = parser.parseMethodDeceleration(command, scopeC);
            scopeC.addInnerScope(innerScope);
            return innerScope;
            }

    },
    VariableAssignment{
        protected ScopeC interpret() throws src.MyExceptions {
            parser.assignVar(command,scopeC);
            return scopeC;
        }

    },
    IfWhileBlock{

        protected ScopeC interpret() throws src.MyExceptions {
            ScopeC innerScope = parser.ParesIfWhile(command, scopeC);
            scopeC.addInnerScope(innerScope);
            return innerScope;

        }

    },
    MethodCall{
        protected ScopeC interpret() throws src.MyExceptions {
            return parser.parseMethodCall(scopeC, command);
        }

    },
    lineError{
        protected ScopeC interpret() throws src.MyExceptions {
            throw new src.MyExceptions();
        }


    },
    ScopeClosing{
        protected ScopeC interpret() throws src.MyExceptions {
            ScopeC father = scopeC.getFather();
            if(father == null){
                throw new src.MyExceptions(); // todo exception handling
            }
            return scopeC.getFather();
        }
    },
    Return {

        protected ScopeC interpret() throws src.MyExceptions {
            return scopeC;
        }
    },
    Note{

        protected ScopeC interpret() throws src.MyExceptions {
            return scopeC;
        }
    },
    Emptyline{
        protected ScopeC interpret() throws src.MyExceptions {

            return scopeC;
        }

    };
    abstract protected ScopeC interpret() throws src.MyExceptions;
    protected Parser parser = new Parser();
    protected String command;
    protected ScopeC  scopeC;

    protected void setLine(String line, ScopeC scopeC){
        command =line;
        this.scopeC = scopeC;
    }

}
