package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.Paths.get;

public class Parser {
    private String VariableDecleration = "\\s*((final\\s+)?(int|boolean|double|String|char))";
    private String Names = "\\s*((([a-z]|[A-Z])+)\\w*)|(_+([a-z]|[A-Z]|\\d)+)";
    private List<String> javaDoc;
    private String MethodDecleration = "\\s*void\\s+(" + Names + ")\\s*\\(((" +
            VariableDecleration + "\\s+" + Names + ")(\\)\\s*\\{)?|(\\s*\\)\\s*\\{))";
    private String MethodCall = "\\s*(" + Names + ")\\s*\\(((" + Names + ")(\\)\\s*;)?|(\\s*\\)\\s*;))";
    private String VariableAssignment = "\\s*(" + Names + ")\\s*=\\s*(((" + Names + "))|(\\d+(.\\d+)?|(\\\"[\\w\\W]+\\\")))";
    private String IfWhile = "\\s*(if|while)\\s*\\(.+\\)\\s*\\{";
    private String returnVar = "\\s*return\\s*";
    private String ScopeClosing = "\\s*}\\s*";
    private String Note = "\\\\\\.*";
    private Scope curScope;


    public Parser(String sJavaFilePath) throws MyExceptions {
        javaDoc = convertToStringArr(sJavaFilePath);
    }

    protected Scope globalScopeCreator() {
        Scope globalScope = new Scope(null, javaDoc);
        curScope = globalScope;
        for (int i = 0; i <= javaDoc.size(); i++) {
            String line = javaDoc.get(i);
            String[] splitString = line.split(",");
            String lineDefiner = lineDefining(splitString[0]);
            switch (lineDefiner) {
                case "Variable":
                    parseVar(splitString);
                    break;
                case "MethodDeclare":
                    Scope innerScope = parseMethod(splitString, i);
                    break;
                case "IfWhileBlock":
                    parseCondition(splitString, i);
                    break;
                case "VariableAssignment":
                    assignVariable(splitString);
                case "Note":
                    break;
                default:
                    // todo exception handling
            }

        }
        return globalScope;
    }

    // todo assigning value to a value;
    private void assignVariable(String[] assignLine) {

    }

    private Scope createInnerScope(List<String> scopeText, int index) {
        int catalan = 1;
        ArrayList<String> innerScope = new ArrayList<>();
        innerScope.add(scopeText.get(index));
        while (catalan != 0) {
            index++;
            String[] line = scopeText.get(index).split(",");
            String lineDefiner = lineDefining(line[0]);
            if (lineDefiner.equals("MethodDeclare") || lineDefiner.equals("IfWhileBlock")) {
                catalan++;
            }
            innerScope.add(scopeText.get(index));
            if (lineDefiner.equals("ScopeClosing")) {
                catalan--;
            }
        }
        return new Scope(curScope, innerScope);
    }


    // todo method that takes a line of if\while deceleration and checks it
    private Scope parseCondition(String[] varLine, int index) {
        // line phrasing
        // should we add to a method "title" field?
        Scope innerScope = createInnerScope(curScope.getTextArray(), index);
        curScope.addInnerScope(innerScope);
        return innerScope;

    }

    // todo method that takes a line of method deceleration and turns it into method
    private Scope parseMethod(String[] varLine, int index) {

        // line phrasing
        // should we add to a method "title" field?
        Scope innerScope = createInnerScope(curScope.getTextArray(), index);
        curScope.addInnerScope(innerScope);
        return innerScope;

    }

    // todo method that takes a line of var deceleration and turns it into varibales
    private void parseVar(String[] varLine) {

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

    private String lineDefining(String line) {
        Pattern pattern = Pattern.compile(VariableDecleration);
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            return "Variable";
        }
        pattern = Pattern.compile(MethodDecleration);
        if (matcher.matches()) {
            return "MethodDeclare";
        }
        pattern = Pattern.compile(MethodCall);
        if (matcher.matches()) {
            return "MethodCall";
        }
        pattern = Pattern.compile(VariableAssignment);
        if (matcher.matches()) {
            return "VariableAssignment";
        }
        pattern = Pattern.compile(IfWhile);
        if (matcher.matches()) {
            return "IfWhileBlock";
        }
        pattern = Pattern.compile(returnVar);
        if (matcher.matches()) {
            return "Return";
        }
        pattern = Pattern.compile(ScopeClosing);
        if (matcher.matches()) {
            return "ScopeClosing";
        }
        String notePattered; //in case the note is in innerScope
        notePattered = notePatteredCreator();
        pattern = Pattern.compile(notePattered);
        if (matcher.matches()) {
            return "Note";
        } else {
            return "lineError";
        }
    }

    private String notePatteredCreator() {
        int outerScope = 0;
        Scope father = curScope.getFather();
        while (father != null) {
            outerScope++;
            father = father.getFather();
        }
        String indentation = "    ";
        indentation = new String(new char[outerScope]).replace("\0", indentation);
        return "^" + indentation + Note;
    }
}

