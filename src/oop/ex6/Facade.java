package oop.ex6;

import java.io.IOException;
import java.util.List;


public class Facade {
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

    static Method createMethod(List<String> javadoc, int index, Line line, ScopeC globalScope) throws MyExceptions {
        Method method = (Method) line.interperate(globalScope);
        int scopeOpen = 1;
        Parser parser = new Parser();
        for (int i = index + 1; i < javadoc.size(); i++) {
            String commandLine = javadoc.get(i);
            String lineType = parser.lineDefining(commandLine);
            Line methodLine = new Line(commandLine, lineType);
            if (MethodNotFinished(method, lineType)) {
                method.setGotReturn(false);
            }
            switch (lineType) {
                case RETURN:
                    if (scopeOpen == 1) {
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
                    throw new MyExceptions(METHOD_INSIDE_A_METHOD);
            }

            method.addScopeLines(methodLine);
            if (scopeOpen == 0) {
                if (!method.GotReturn()) {
                    throw new MyExceptions(NO_RETURN_IN_METHOD_ERROR);
                }
                return method;
            }
        }
        throw new MyExceptions(NOT_ENOUGH_CLOSING_BRACKETS);
    }


    static boolean MethodNotFinished(Method method, String lineType) {
        return method.GotReturn() && (!lineType.equals(RETURN) && !lineType.equals(NOTE) && !lineType.equals(SCOPE_CLOSING));
    }


    // checks if the line being checked is not valid for the global scope
    static boolean isNotValidGlobalLine(String lineType) {
        return lineType.equals(IF_WHILE_BLOCK) || lineType.equals(RETURN) || lineType.equals(SCOPE_CLOSING)
                || lineType.equals(METHOD_CALL);
    }


    // create the global scope of of the java doc
    static ScopeC globalScopeCreator(String filePath) throws MyExceptions, IOException {
        ScopeC curScope = new ScopeC(null);
        Parser parser = new Parser(filePath);
        List<String> javadoc = parser.getJavaDoc();
        int counter = 0;
        for (int i = 0; i < javadoc.size(); i++) {
            String commandLine = javadoc.get(i);
            String lineType = parser.lineDefining(commandLine);
            Line line = new Line(commandLine, lineType);
            if (isNotValidGlobalLine(lineType)) {
                throw new MyExceptions(INVALID_IN_THE_OUTER_SCOPE);
            }
            if (lineType.equals(METHOD_DECLARE)) {
                Method method = createMethod(javadoc, i, line, curScope);
                i = i + method.getScopeLines().size();
            }
            line.interperate(curScope);
        }
        return curScope;
    }

}
