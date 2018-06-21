package oop.ex6;

import java.io.*;
import java.lang.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Playground {


    private static String[] singleVarArrCreator(String expression) throws ParsingException {
        expression = expression.trim();
        String ptrn = "([A-Za-z0-9_]*)\\s*(=\\s*(([A-Za-z0-9_]+)|\\\".*\\\"))?\\s*";
        Pattern pattern = Pattern.compile(ptrn);
        Matcher matcher = pattern.matcher(expression);
        if(!matcher.matches()) {
            throw  new ParsingException("Invalid line of var creation");
        }
        return  new String[] {matcher.group(1), matcher.group(3)};
    }


    public static void main(String[] args) throws ClassNotFoundException, ParsingException, IOException {
        String line = "eger";
        String[] dafdas = singleVarArrCreator(line);
    }
}
//        System.out.println(matcher.group(1));


//
//        String a = "\'c\'";
//        int x  = 3;
//        Parser p = new Parser("Files/Moodle Example/playg");
//
//        boolean adsad = p.isTypeMatch(a,"char");
//
//        File pg = new File("Files/Moodle Example/playg");
//        List<String> original = p.convertToStringArr("Files/Moodle Example/playg");
//        BufferedWriter writer = new BufferedWriter(new FileWriter("Files/Moodle Example/playgRESULT"));
//
//        for (String orLine : original) {
//            String VerLine = p.lineDefining(orLine);
//            writer.write(orLine + " ---> " + VerLine);
//            writer.newLine();
//
//        }
//        writer.close();
//    }
//
//
//
//}
