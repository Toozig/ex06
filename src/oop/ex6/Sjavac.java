package oop.ex6;


import java.io.IOException;

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
     * @param args the args for the program
     */
    public static void main(String[] args) {
        ScopeC curScope ;
        try {
            curScope = Facade.globalScopeCreator(args[0]);
        }
        catch (IOException e){
            System.out.println(IOEXCEPTION);
            System.err.println(e.getMessage() + WHITESPACE + DOESN_T_EXIST);
            return;
        }
        catch (ParsingException parsingException) {
            System.out.println(PARSINGEXCEPTION);
            System.err.println(parsingException.getMessage());
            return;
        }
        for (Method method : curScope.getMethodArr()) {
            curScope = method;
            for (Line line : method.getScopeLines()) {
                try {
                    curScope = line.interpret(curScope);
                } catch (ParsingException parsingException) {
                    System.out.println(PARSINGEXCEPTION);
                    System.err.println(parsingException.getMessage());
                    return;
                }
            }
        }
        System.out.println(ALLGOOD);
    }





}




