import src.MyExceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {



    public static void main(String[] args) throws src.MyExceptions {
        Parser parser = new Parser("Files/Moodle Example/playg");
        List<String> javadoc = parser.getJavaDoc();
//        String line = "int            a        , double           b         , char c";
//        ArrayList<Variables> vars = parser.parseVarsFromMethod(line);
//        for(Variables item:vars){
//            System.out.println(item.getName()+" "+item.getType()+" "+item.getData()+" "+item.getIsFinal());
//        }
        ScopeC curScope = new ScopeC(null);
        int counter = 0;
        for (int i = 0; i < javadoc.size() ; i++) {
            String commandLine = javadoc.get(i);
            String lineType = parser.lineDefining(commandLine);
            Line line = new Line(commandLine, lineType);
            if (counter == 0) {
                System.out.println(commandLine);
                try{
                    ScopeC newScope = line.interperate(curScope);
                    if(!newScope.equals(curScope)){
                        curScope = newScope;
                        counter++;
                    }
                } catch (src.MyExceptions e){
                    System.err.println(e.getMessage() + "in line " + commandLine);
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

}




