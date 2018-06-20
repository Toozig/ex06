package oop.ex6;


import java.util.List;


public class Sjavac {


    public static final String METHOD_INSIDE_A_METHOD = "Method inside a method";
    private static final String IF_WHILE_BLOCK = "IfWhileBlock";
    private static final String METHOD_DECLARE = "MethodDeclare";
    private static final String SCOPE_CLOSING = "ScopeClosing";
    private static final String RETURN = "Return";
    private static final String INVALID_IN_THE_OUTER_SCOPE = "Invalid in the outer scope";
    private static final String METHOD_CALL = "MethodCall";
    public static final String NOT_ENOUGH_SCOPE_CLOSING = "Not enough scope closing";
    public static final String NO_RETURN_IN_METHOD_ERROR = "No return in method";
    public static final String NOT_ENOUGH_CLOSING_BRACKETS = "Not enough closing brackets";

    public static void main(String[] args) throws MyExceptions {

        ScopeC curScope = null;
        try {
            curScope = globalScopeCreator(args[0]);
        } catch (MyExceptions myExceptions) {
            System.err.println(myExceptions.getMessage() + " in Line ");
            System.out.println("1");
            return;
        }
        for (Method method : curScope.getMethodArr()) {
            curScope = method;
            for (Line line : method.getScopeLines()) {
                try {
                    curScope = line.interperate(curScope);
                } catch (MyExceptions myExceptions) {
                    System.err.println(myExceptions.getMessage() + " in Line " + line.commandline);
                    System.out.println("1");
                    return;
                }
            }
        }
        System.out.println("0");
    }

    // checks if the line being checked is not valid for the global scope
    private static boolean isNotValidGlobalLine(String lineType) {
        return lineType.equals(IF_WHILE_BLOCK) || lineType.equals(RETURN) || lineType.equals(SCOPE_CLOSING)
                || lineType.equals(METHOD_CALL);
    }


    // create the global scope of of the java doc
    private static ScopeC globalScopeCreator(String filePath) throws MyExceptions {
        ScopeC curScope = new ScopeC(null);
        Parser parser = new Parser(filePath);
        List<String> javadoc = parser.getJavaDoc();
        int counter = 0;
        for (int i = 0; i < javadoc.size(); i++) {
            String commandLine = javadoc.get(i);
            String lineType = parser.lineDefining(commandLine);
            Line line = new Line(commandLine, lineType);
            if (isNotValidGlobalLine(lineType)) {
                throw new MyExceptions(INVALID_IN_THE_OUTER_SCOPE + " in line " + commandLine);
            }
            if (lineType.equals(METHOD_DECLARE)) {
                Method method = createMethod(javadoc, i, line, curScope);
                i = i + method.getScopeLines().size();
            }
            line.interperate(curScope);

        }
//            if (counter == 0) {
//                if (!newScope.equals(curScope)) {
//                    curScope = newScope;
//                    counter++;
//                }
//            } else if (lineType.equals(IF_WHILE_BLOCK) || lineType.equals(METHOD_DECLARE)) {
//                counter++;
//                curScope.addScopeLines(line);
//            } else if (lineType.equals(SCOPE_CLOSING)) {
//                counter--;
//                curScope.addScopeLines(line);
//                if (counter == 0) {
//                    curScope = curScope.getFather();
//                }
//            } else {
//                curScope.addScopeLines(line);
//            }
//
//        }
//        if (counter != 0) {
//            throw new MyExceptions(NOT_ENOUGH_SCOPE_CLOSING);
//        }
//        if(!curScope.GotReturn()){
//            throw new MyExceptions("No return statement");
//        }
        return curScope;
    }


    private static Method createMethod(List<String> javadoc, int index, Line line, ScopeC globalScope) throws MyExceptions {
        Method method = (Method) line.interperate(globalScope);
        int scopeOpen = 1;
        Parser parser = new Parser();
        for (int i = index + 1; i < javadoc.size(); i++) {
            String commandLine = javadoc.get(i);
            String lineType = parser.lineDefining(commandLine);
            Line methodLine = new Line(commandLine, lineType);
            switch (lineType) {
                case RETURN:
                    method.setGotReturn(true);
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

}




