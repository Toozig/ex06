package oop.ex6;


import java.util.List;


public class Sjavac {


    private static final String IF_WHILE_BLOCK = "IfWhileBlock";
    private static final String METHOD_DECLARE = "MethodDeclare";
    private static final String SCOPE_CLOSING = "ScopeClosing";
    private static final String RETURN = "return";
    private static final String INVALID_IN_THE_OUTER_SCOPE = "Invalid in the outer scope";
    private static final String METHOD_CALL = "MethodCall";

    public static void main(String[] args) throws MyExceptions {

        ScopeC curScope = null;
        try {
            curScope = globalScopeCreator(args[0]);
        } catch (MyExceptions myExceptions) {
            System.err.println(myExceptions.getMessage() + " in Line " );
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

    // checks if the line being checked is valid for the global scope
    private static boolean isValidGlobalLine(String lineType) {
        return lineType.equals(IF_WHILE_BLOCK) || lineType.equals(RETURN) || lineType.equals(SCOPE_CLOSING)
                || lineType.equals(METHOD_CALL);
    }


    // create the global scope of of the java doc
    private static ScopeC globalScopeCreator(String filePath) throws MyExceptions {
        ScopeC curScope = new ScopeC(null);
        Parser parser = new Parser(filePath);
        List<String> javadoc = parser.getJavaDoc();
        int counter = 0;
        for (String commandLine : javadoc) {
            String lineType = parser.lineDefining(commandLine);
            Line line = new Line(commandLine, lineType);
            if (counter == 0) {
                if (isValidGlobalLine(lineType)) {
                    throw new MyExceptions(INVALID_IN_THE_OUTER_SCOPE + " in line " + commandLine);
                }
                ScopeC newScope = line.interperate(curScope);
                if (!newScope.equals(curScope)) {
                    curScope = newScope;
                    counter++;
                }
            } else if (lineType.equals(IF_WHILE_BLOCK) || lineType.equals(METHOD_DECLARE)) {
                counter++;
                curScope.addScopeLines(line);
            } else if (lineType.equals(SCOPE_CLOSING)) {
                counter--;
                curScope.addScopeLines(line);
                if (counter == 0) {
                    curScope = curScope.getFather();
                }
            } else {
                curScope.addScopeLines(line);
            }
        }
        return curScope;
    }

}




