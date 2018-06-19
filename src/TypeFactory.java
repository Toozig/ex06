import java.util.ArrayList;
import java.util.LinkedList;

public enum TypeFactory {
    Variable{
        protected ScopeC interpret() throws src.MyExceptions {

            ArrayList<Variables> varArr = parser.parseVar(command,scopeC);
            for (Variables var: varArr) {
                scopeC.addVariable(var);
            }
            return scopeC;
        }
    },
    MethodDeclare{

        protected ScopeC interpret() throws src.MyExceptions {
            Method innerScope = parser.parseMethodDeceleration(command, scopeC);
            scopeC.addToMethodArr(innerScope);
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
            throw new src.MyExceptions(INVALID_LINE_FORMAT);
        }


    },
    ScopeClosing{
        protected ScopeC interpret() throws src.MyExceptions {
            ScopeC father = scopeC.getFather();
            if(father == null){
                throw new src.MyExceptions(OUTER_SCOPE_CLOSE); // todo exception handling
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

    public static final String OUTER_SCOPE_CLOSE = "Outer scope shouldn't be close";
    public static final String INVALID_LINE_FORMAT = "Invalid line format";

    abstract protected ScopeC interpret() throws src.MyExceptions;
    protected Parser parser = new Parser();
    protected String command;
    protected ScopeC scopeC;

    protected void setLine(String line, ScopeC scopeC){
        this.command =line;
        this.scopeC = scopeC;
    }

    public String getCommand() {
        return command;
    }
}
