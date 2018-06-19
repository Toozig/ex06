import java.io.*;
import java.lang.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Playground {


    public static void main(String[] args) throws ClassNotFoundException, src.MyExceptions, IOException {


        String lineDeceleration = "void foo(int a, boolean b) {";
            Pattern pattern = Pattern.compile("^\\s*void\\s+\\S+\\s*\\(.*\\)\\s*\\{\\s*");
            Matcher matcher = pattern.matcher(lineDeceleration);
        System.out.println(matcher.matches());
        Parser p = new Parser("Files/Moodle Example/playg");

        File pg = new File("Files/Moodle Example/playg");
        List<String> original = p.convertToStringArr("Files/Moodle Example/playg");
        BufferedWriter writer = new BufferedWriter(new FileWriter("Files/Moodle Example/playgRESULT"));

        for (String orLine : original) {
            String VerLine = p.lineDefining(orLine);
            writer.write(orLine + " ---> " + VerLine);
            writer.newLine();

        }
        writer.close();
    }



}
