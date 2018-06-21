package oop.ex6;

/**
 * Represents a line in the sjava file
 */
public class Line {
    private TypeFactory typeFactory;
    private String commandline;

    /**
     * Constructs a line
     * @param commandline the command of the line
     * @param commandType the type of the line
     */
    Line(String commandline,String commandType){
        this.commandline = commandline;
        typeFactory = TypeFactory.valueOf(commandType);

    }

    /**
     * Interpret's the line
     * @param scope the scope the line is in
     * @return the scope that the typeFactory returned with the updated scope
     * @throws ParsingException if the line was illegal
     */
    protected ScopeC interpret(ScopeC scope) throws ParsingException {
        return typeFactory.interpret(scope,commandline);
    }

}
