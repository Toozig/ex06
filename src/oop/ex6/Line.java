package oop.ex6;

/**
 * Represents a line in the sjava file
 */
public class Line {
    private LineTypeFactory lineTypeFactory;
    private String commandline;

    /**
     * Constructs a line
     * @param commandline the command of the line
     * @param commandType the type of the line
     */
    Line(String commandline,String commandType){
        this.commandline = commandline;
        lineTypeFactory = LineTypeFactory.valueOf(commandType);

    }

    /**
     * Interpret's the line
     * @param scope the scope the line is in
     * @return the scope that the lineTypeFactory returned with the updated scope
     * @throws ParsingException if the line was illegal
     */
    protected MScope interpret(MScope scope) throws ParsingException {
        return lineTypeFactory.interpret(scope,commandline);
    }

}
