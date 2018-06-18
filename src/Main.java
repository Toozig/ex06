import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {



    public static void main(String[] args) throws src.MyExceptions {
        String mydata = "2131231 && dsadasdc || ewws";
        Pattern pattern = Pattern.compile("(^\\s*(\\|\\||&&))|(\\|\\||&&)\\s*(\\|\\||&&)|(\\|\\||&&)\\s*$");
        Matcher matcher = pattern.matcher(mydata);

        System.out.println(matcher.find());

        Parser parser = new Parser("Files/Moodle Example/playg");
        List<String> javadoc = parser.getJavaDoc();
        Scope curScope = new Scope(null, new ArrayList<>(), "Global");

        for (String commandLine : javadoc) {
            String lineType = parser.lineDefining(commandLine);
            TypeFactory line = TypeFactory.valueOf(lineType);
            if (curScope.getFather() == null) {
                try{
                    curScope = line.interpret(commandLine, curScope);
                }
            }
        } catch (Exception e){
            System.out.println("1");
            return;

        }
        System.out.println("0");
    }

}




