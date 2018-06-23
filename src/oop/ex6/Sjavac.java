package oop.ex6;


import java.io.IOException;
import java.util.List;

/**
 * Runs the compiler
 */
public class Sjavac {
    //Constants
    private static final String DOESN_T_EXIST = "Doesn't exist";
    private static final String IOEXCEPTION = "2";
    private static final String WHITESPACE = " ";
    private static final String PARSINGEXCEPTION = "1";
    private static final String ALLGOOD = "0";

    /**
     * Runs the compiler
     * By giving a file, the program will print "0" if the document is valid,
     *                                          "1" if the sjava file is not valid,
     *                                          "2" if the file is not valid.
     * @param args the args for the program. index 0 is the file path.
     */
    public static void main(String[] args) {
        List<String> fileToStringArr;
        Parser parser = Parser.getParser();
        try {
            fileToStringArr = parser.convertToStringArr(args[0]);
        }catch (IOException e){
            System.out.println(IOEXCEPTION);
            System.err.println(e.getMessage() + WHITESPACE + DOESN_T_EXIST);
            return;
        }

        try {
            assert fileToStringArr != null;
            if(Facade.isSjavaFileValid(fileToStringArr)){
                System.out.println(ALLGOOD);
            }
        }
        catch (ParsingException parsingException) {
            System.out.println(PARSINGEXCEPTION);
            System.err.println(parsingException.getMessage());
        }
            }
        }











