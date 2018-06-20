package oop.ex6;

public class Line {
    protected TypeFactory typeFactory;
    protected String commandline;
    public Line(String commandline,String commandType){
        this.commandline = commandline;
        typeFactory = TypeFactory.valueOf(commandType);

    }
    protected ScopeC interperate (ScopeC scope) throws MyExceptions {
        return typeFactory.interpret(scope,commandline);
    }

}
