import java.lang.*;
import java.lang.reflect.Method;

public class Playground {

    public static void main(String[] args) throws ClassNotFoundException {
        int x  = 3;
        Class c =  Class.forName("Files/google.java");
        Method[] jaja = c.getMethods();
    }

}
