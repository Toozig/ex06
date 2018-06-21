package oop.ex6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.Paths.get;

/**
 * Integrates all of the components of the extracting, parsing and interpreting the sjava file
 */
public class Facade {
    //constants
    private static final int SCOPE_OPEN_PARENTTESSES = 1;
    private static final String METHOD_INSIDE_A_METHOD = "Method inside a method";
    private static final String IF_WHILE_BLOCK = "IfWhileBlock";
    private static final String METHOD_DECLARE = "MethodDeclare";
    private static final String SCOPE_CLOSING = "ScopeClosing";
    private static final String RETURN = "Return";
    private static final String INVALID_IN_THE_OUTER_SCOPE = "Invalid in the outer scope";
    private static final String METHOD_CALL = "MethodCall";
    private static final String NO_RETURN_IN_METHOD_ERROR = "No return in method";
    private static final String NOT_ENOUGH_CLOSING_BRACKETS = "Not enough closing brackets";
    private static final String NOTE = "Note";
    private static final int INDEXSTART = 1;
    public static final int METHODCLOSED = 0;
    public static final int ZERO = 0;

    /**
     * Creates a method object according to given lines
     * @param javadoc the javadoc - all of the lines of the file
     * @param index the index in which the method starts in the javadoc
     * @param line the lie the method starts in
     * @param globalScope the global scope
     * @return the method object
     * @throws ParsingException thrown if something in the method lines were illegal
     */
    static Method createMethod(List<String> javadoc, int index, Line line, ScopeC globalScope) throws ParsingException {
        Method method = (Method) line.interpret(globalScope);
        int scopeOpen = SCOPE_OPEN_PARENTTESSES;
        Parser parser = new Parser();

        for (int i = index + INDEXSTART; i < javadoc.size(); i++) {
            String commandLine = javadoc.get(i);
            String lineType = parser.lineDefining(commandLine);
            Line methodLine = new Line(commandLine, lineType);
            if (MethodNotFinished(method, lineType)) {
                method.setGotReturn(false);
            }
            switch (lineType) {
                case RETURN:
                    if (scopeOpen == SCOPE_OPEN_PARENTTESSES) {
                        method.setGotReturn(true);
                    }
                    break;
                case IF_WHILE_BLOCK:
                    scopeOpen++;
                    break;
                case SCOPE_CLOSING:
                    scopeOpen--;
                    break;
                case METHOD_DECLARE:
                    throw new ParsingException(METHOD_INSIDE_A_METHOD);
            }

            method.addScopeLines(methodLine);
            if (scopeOpen == METHODCLOSED) {
                if (!method.GotReturn()) {
                    throw new ParsingException(NO_RETURN_IN_METHOD_ERROR);
                }
                return method;
            }
        }
        throw new ParsingException(NOT_ENOUGH_CLOSING_BRACKETS);
    }

    /**
     * Checks if the method wasn't finished
     * @param method the method to check
     * @param lineType the type of the line
     * @return true iff the method wasn't finished
     */
    static boolean MethodNotFinished(Method method, String lineType) {
        return method.GotReturn() && (!lineType.equals(RETURN) && !lineType.equals(NOTE) && !lineType.equals
                (SCOPE_CLOSING));
    }




    /**
     * Checks if the line being checked is not valid for the global scope
     * @param lineType the type of the line
     * @return true iff this is not a permitted line in the global scope
     */
    static boolean isNotValidGlobalLine(String lineType) {
        return lineType.equals(IF_WHILE_BLOCK) || lineType.equals(RETURN) || lineType.equals(SCOPE_CLOSING)
                || lineType.equals(METHOD_CALL);
    }


    // create the global scope of of the java doc

    /**
     * Creates the global scope of the file
     * @param filePath the path of the file
     * @return the global scope
     * @throws ParsingException if one of the lines was illegal
     * @throws IOException if no such file exists
     */
    static ScopeC globalScopeCreator(String filePath) throws ParsingException, IOException {
        ScopeC curScope = new ScopeC(null);
        Parser parser = new Parser(filePath);
        List<String> javadoc = parser.getJavaDoc();
        for (int i = ZERO; i < javadoc.size(); i++) {
            String commandLine = javadoc.get(i);
            String lineType = parser.lineDefining(commandLine);
            Line line = new Line(commandLine, lineType);
            if (isNotValidGlobalLine(lineType)) {
                throw new ParsingException(INVALID_IN_THE_OUTER_SCOPE);
            }
            if (lineType.equals(METHOD_DECLARE)) {
                Method method = createMethod(javadoc, i, line, curScope);
                i = i + method.getScopeLines().size();
            }
            line.interpret(curScope);
        }
        return curScope;
    }


    /**
     * This method takes a text file and turns it into an array of String, each index contains line from the txt
     * @param path the path of the file
     * @return Array of Strings
     * @throws IOException thrown if the file doesn't exist
     */
    protected static List<String> convertToStringArr(String path) throws IOException {
        Path filePath = get(path);
        return Files.readAllLines(filePath);
    }

}
