import java.lang.*;
import java.lang.reflect.Method;

public class Playground {

    public static void main(String[] args) throws ClassNotFoundException {
        String var = "int   a   =   4";
        String[] vara =var.split("\\s+");
        for (String item:vara){
            System.out.println(item);
        }
    }

}
