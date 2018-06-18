import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {



    public static void main(String[] args) throws src.MyExceptions {
        String mydata = "2131231 && dsadasdc || ewws";
        Pattern pattern = Pattern.compile("(^\\s*(\\|\\||&&))|(\\|\\||&&)\\s*(\\|\\||&&)|(\\|\\||&&)\\s*$");
        Matcher matcher = pattern.matcher(mydata);


        Parser parser = new Parser("Files/Moodle Example/playg");
        List<String> javadoc = parser.getJavaDoc();
        ScopeC curScope = new ScopeC(null);
        int counter = 0;
        for (String commandLine : javadoc) {
            String lineType = parser.lineDefining(commandLine);
            TypeFactory line = TypeFactory.valueOf(lineType);
            line.setLine(commandLine, curScope);
            if (counter == 0) {
                try{
                    ScopeC newScope = line.interpret();
                    if(!newScope.equals(curScope)){
                        curScope = newScope;
                        counter++;
                    }
                } catch (Exception e){
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
            for (TypeFactory line :
                    method.getScopeLines()) {
                line.interpret();
            }
        }
        System.out.println("0");
    }

}




