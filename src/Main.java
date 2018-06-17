import java.util.ArrayList;
import java.util.List;

public class Main {



    public static void main(String[] args) throws src.MyExceptions {

        Parser parser = new Parser("Files/Moodle Example/playg");
        List<String> javadoc = parser.getJavaDoc();
        Scope curScope = new Scope(null,new ArrayList<>());

        for (String commandLine : javadoc) {
            String lineType = parser.lineDefining(commandLine);
            TypeFactory line = TypeFactory.valueOf(lineType);
            curScope = line.interpret(commandLine, curScope);

        }

    }
}
