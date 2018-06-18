import jdk.nashorn.internal.runtime.Scope;

import java.util.ArrayList;

public class Method extends ScopeC {

    private ArrayList<Variables> arguments;
    private String name;

    public Method(ScopeC father, ArrayList<Variables> arguments, String name) throws src.MyExceptions {
        super(father);
        this.name = name;
        this. arguments = isLegalVar(arguments);
    }

    // checks if the method variable ar valid
    private ArrayList<Variables> isLegalVar(ArrayList<Variables> varList) throws src.MyExceptions {
        for (Variables variable: varList) {
            if(variable.getData() != null){
                throw new src.MyExceptions(); // todo exceptions , initialized variable
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
}
