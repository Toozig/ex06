package oop.ex6;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sjavac {


    public static final String IF_WHILE_BLOCK = "IfWhileBlock";
    public static final String METHOD_DECLARE = "MethodDeclare";
    public static final String SCOPE_CLOSING = "ScopeClosing";
    public static final String RETURN = "return";
    public static final String INVALID_IN_THE_OUTER_SCOPE = "Invalid in the outer scope";
    public static final String METHOD_CALL = "MethodCall";

    public static void main(String[] args) throws MyExceptions {
        Parser parser = new Parser("Files/Moodle Example/playg");
        List<String> javadoc = parser.getJavaDoc();

        ScopeC curScope = new ScopeC(null);
        int counter = 0;
        for (int i = 0; i < javadoc.size() ; i++) {
            String commandLine = javadoc.get(i);
            String lineType = parser.lineDefining(commandLine);
            Line line = new Line(commandLine, lineType);
            if (counter == 0) {
                if(isValidGlobalLine(lineType)){
                    System.err.println(INVALID_IN_THE_OUTER_SCOPE+ "in line " + commandLine);
                    System.out.println("1");
                    return;
                }
                try{
                    ScopeC newScope = line.interperate(curScope);
                    if(!newScope.equals(curScope)){
                        curScope = newScope;
                        counter++;
                    }
                } catch (MyExceptions e){
                    System.err.println(e.getMessage() + "in line " + commandLine);
                    System.out.println("1");
                    return;
                }
            }else if(lineType.equals(IF_WHILE_BLOCK) || lineType.equals(METHOD_DECLARE)){
                counter++;
                curScope.addScopeLines(line);
            }
            else if(lineType.equals(SCOPE_CLOSING)){
                counter--;
                curScope.addScopeLines(line);
                if(counter == 0){
                    curScope = curScope.getFather();
                }
            }
            else {
                curScope.addScopeLines(line);
            }
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
        return lineType.equals(IF_WHILE_BLOCK)||lineType.equals(RETURN)||lineType.equals(SCOPE_CLOSING)
                || lineType.equals(METHOD_CALL);
    }

}




