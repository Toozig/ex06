package oop.ex6;


import java.io.IOException;
import java.util.List;
public class Sjavac {

    public static final String DOESN_T_EXIST = "Doesn't exist";

    public static void main(String[] args) {
        ScopeC curScope ;
        try {
            curScope = Facade.globalScopeCreator(args[0]);
        }
        catch (IOException e){
            System.out.println("2");
            System.err.println(e.getMessage() + " " +DOESN_T_EXIST);
            return;
        }
        catch (MyExceptions myExceptions) {
            System.out.println("1");
            System.err.println(myExceptions.getMessage());
            return;
        }
        for (Method method : curScope.getMethodArr()) {
            curScope = method;
            for (Line line : method.getScopeLines()) {
                try {
                    curScope = line.interperate(curScope);
                } catch (MyExceptions myExceptions) {
                    System.out.println("1");
                    System.err.println(myExceptions.getMessage());
                    return;
                }
            }
        }
        System.out.println("0");
    }





}




