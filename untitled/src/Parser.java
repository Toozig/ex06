package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.Paths.get;

public class Parser {
    private String VariableDecleration = "\\s*((final\\s+)?(int|boolean|double|String|char))";
    private String Names = "\\s*((([a-z]|[A-Z])+)\\w*)|(_+([a-z]|[A-Z]|\\d)+)"
    private List<String> javaDoc;
    private String MethodDecleration = "\\s*void\\s+("+Names+")\\s*\\((("+
            VariableDecleration +"\\s+"+Names+")(\\)\\s*\\{)?|(\\s*\\)\\s*\\{))";
    private String MethodCall = "\\s*("+Names+")\\s*\\(((" + Names + ")(\\)\\s*;)?|(\\s*\\)\\s*;))";
    private String VariableAssignment = "\\s*("+Names+")\\s*=\\s*((("+Names+"))|(\\d+(.\\d+)?|(\\\"[\\w\\W]+\\\")))";
    private String IfWhile = "\\s*(if|while)\\s*\\(.+\\)\\s*{";
    private String returnVar = "\\s*return\\s*";
    private String ScopeClosing = "\\s*}\\s*";
    private String Note = "^\\\\\\.*";
    private Scope globalScope;
    private Scope curScope;


    public Parser(String sJavaFilePath) throws MyExceptions {
        javaDoc =  convertToStringArr(sJavaFilePath);
        globalScope = new Scope(null);
        curScope = globalScope;

    }

    protected Scope fileValidator(){
        for (String line: javaDoc) {
            String[] splitString = line.split(",");
            lineDefining(splitString[0]);


        }
    }

    /**
     * This method takes a text file and turns it into an array of String, each index contains line from the txt
     *
     * @return Array of Strings.
     */
    private List<String> convertToStringArr(String path) throws MyExceptions {

        try {
            Path filePath = get(path);
            return Files.readAllLines(filePath);
        }
        // TODO see HTF we handle the exception.
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

        private String lineDefining(String line){
         Pattern pattern = Pattern.compile(VariableDecleration);
         Matcher matcher = pattern.matcher(line);
         if (matcher.matches()){
             return "Variable";
         }
         pattern = Pattern.compile(MethodDecleration);
         if(matcher.matches()){
             return "MethodDeclare";
         }
         pattern = Pattern.compile(MethodCall);
         if(matcher.matches()){
             return "MethodDeclare";
         }
        pattern = Pattern.compile(VariableAssignment);
        if(matcher.matches()){
            return "VariableAssignment";
        }
        pattern = Pattern.compile(IfWhile);
        if(matcher.matches()){
            return "IfWhileBlock";
        }
        pattern = Pattern.compile(returnVar);
        if(matcher.matches()){
            return "Return";
        }
        pattern = Pattern.compile(ScopeClosing);
        if(matcher.matches()){
            return "ScopeClosing";
        }
        pattern = Pattern.compile(Note);
        if(matcher.matches()){
            return "Note";
        }
        else{
            return "lineError";
        }
        }
}
