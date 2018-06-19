import src.MyExceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {



    public static void main(String[] args) throws src.MyExceptions {
        Parser parser = new Parser("Files/Moodle Example/playg");
        List<String> javadoc = parser.getJavaDoc();
        TypeFactory last;
        ScopeC curScope = new ScopeC(null);
        int counter = 0;
        for (int i = 0; i < javadoc.size() ; i++) {
            String commandLine = javadoc.get(i);
            String lineType = parser.lineDefining(commandLine);
            TypeFactory line = TypeFactory.valueOf(lineType);
            line.setLine(commandLine, curScope);
             last  = line;
            if (counter == 0) {
                try{
                    ScopeC newScope = line.interpret();
                    if(!newScope.equals(curScope)){
                        curScope = newScope;
                        counter++;
                    }
                } catch (src.MyExceptions e){
                    System.err.println(e.getMessage() + "in line " + i);
                    System.out.println("1");
                    return;
                }
            }else if(lineType.equals("IfWhileBlock") || lineType.equals("MethodDeclare")){
                counter++;
                curScope.addScopeLines(line);
            }
            else if(lineType.equals("ScopeClosing")){
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
            for (TypeFactory line :
                    method.getScopeLines()) {
                try {
                    String liineddas = line.getCommand();
                    line.interpret();
                } catch (MyExceptions myExceptions) {
                    System.err.println(myExceptions.getMessage() + " in Line " + line.getCommand());
//                    return;
                }
            }
        }
        System.out.println("0");
    }

}




