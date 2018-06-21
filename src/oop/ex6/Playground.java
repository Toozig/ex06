package oop.ex6;

import java.io.*;
import java.lang.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Playground {


    public static void main(String[] args) throws ClassNotFoundException, MyExceptions, IOException {
        String line = " a = \"hey,Ijust met you\", b = \"and this is crazy\"";
        String ptrn = "(\\S*(\\s*=\\s*(\\\".*\\\")|\\S*)?\\s*)\\,";
        Pattern pattern = Pattern.compile(ptrn);
        Matcher matcher = pattern.matcher(line);
        matcher.find();
        System.out.println(matcher.group(1));














        String a = "\'c\'";
        int x  = 3;
        Parser p = new Parser("Files/Moodle Example/playg");

        boolean adsad = p.isTypeMatch(a,"char");

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
