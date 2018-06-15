import src.MyExceptions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.Paths.get;

public class Parser {
    public static final String VARIABLE = "Variable";
    public static final String METHOD_DECLARE = "MethodDeclare";
    public static final String VARIABLE_ASSIGNMENT = "VariableAssignment";
    public static final String NOTE = "Note";
    public static final String COMMA = ",";
    public static final String IF_WHILE_BLOCK = "IfWhileBlock";
    public static final String SCOPE_CLOSING = "ScopeClosing";
    public static final String METHOD_CALL = "MethodCall";
    public static final String RETURN = "Return";
    public static final String LINE_ERROR = "lineError";
    public static final String INDENTATION = "    ";
    public static final String NO_CHAR_BEFORE = "^";
    public static final int OUTER_SCOPE = 0;
    public static final int FIRST_LINE = 0;
    public static final int CATALAN = 1;
    public static final String WHITE_SPACE = "\\s+";
    public static final String FINAL = "final";
    public static final int FIRST_VAR_DECLARE = 0;
    public static final String INT = "int";
    public static final String DOUBLE = "double";
    public static final String BOOLEAN = "boolean";
    public static final String CHAR = "char";
    public static final String STRING = "String";
    public static final String INCOMPATIBLE_TYPE = "incompatibleType";
    public static final String EmptyLine = " *";
    public static final String EMPTY_LINE = "Empty line";
    private String VariableDecleration = "\\s*((final\\s+)?(int|boolean|double|String|char))";
    private String Names = "\\s*((([a-z]|[A-Z])+)\\w*)|(_+([a-z]|[A-Z]|\\d)+)";
    private List<String> javaDoc;
    private String MethodDecleration =  "\\s*void\\s+(" + Names + ")\\s*\\(("+ VariableDecleration +
            "\\s+(((([a-z]|[A-Z])+)\\w*)|(_+([a-z]|[A-Z]|\\d)+\\s*)\\s*))?(\\s*\\)\\s*\\{)?\\s*";
    private String MethodCall = "\\s*(((([a-z]|[A-Z])+)\\w*)|(_+([a-z]|[A-Z]|\\d)+))\\s*\\" +
            "(((\\s*((([a-z]|[A-Z])+)\\w*)\\s*|(_+([a-z]|[A-Z]|\\d)+))(\\)\\s*;)?|(\\s*\\)\\s*;))";
    private String VariableAssignment = "\\s*(" + Names + ")\\s*=\\s*(((" + Names + "))" +
            "|(-?\\d+(.\\d+)?|(\\\"[\\w\\W]+\\\")||\\\'[\\w\\W]+\\\'))\\s*\\;?";
    private String IfWhile = "\\s*(if|while)\\s*((\\(.+\\)\\s*\\{\\s*)|\\(\\s*\\s*" +
            "(((([a-z]|[A-Z])+)\\w*)|(_+([a-z]|[A-Z]|\\d)+))\\s*\\((-?\\d)+(\\.\\d+)?)";
    private String returnVar = "\\s*return\\s*;\\s*";
    private String ScopeClosing = "\\s*}\\s*";
    private String Note = "^\\/\\/.*";
    private String VaribelCreation = VariableDecleration +"\\s+(((([a-z]|[A-Z])+)\\w*)|(_+([a-z]|[A-Z]|\\d)+))" +
            "\\s*(=\\s*(-?\\d+.(\\d+)?|\\\"[\\w\\W]+\\\"|\\\'[\\w\\W]+\\\'|"+ Names +"))?\\s*;?";
    private Scope curScope;


    public Parser(String sJavaFilePath) throws src.MyExceptions {
        javaDoc = convertToStringArr(sJavaFilePath);
    }

    protected Scope globalScopeCreator() {
        Scope globalScope = new Scope(null, javaDoc);
        curScope = globalScope;
        for (int i = 0; i <= javaDoc.size(); i++) {
            String line = javaDoc.get(i);
            String[] splitString = line.split(COMMA);
            String lineDefiner = lineDefining(splitString[0]);
            switch (lineDefiner) {
                case VARIABLE:
                    parseVar(splitString);
                    break;
                case METHOD_DECLARE:
                    Scope innerScope = parseMethod(splitString, i);
                    break;
                case VARIABLE_ASSIGNMENT:
                    assignVariable(splitString);
                    break;
                case NOTE:
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
        int catalan = CATALAN;
        ArrayList<String> innerScope = new ArrayList<>();
        innerScope.add(scopeText.get(index));
        while (catalan != OUTER_SCOPE) {
            index++;
            String[] line = scopeText.get(index).split(COMMA);
            String lineDefiner = lineDefining(line[FIRST_LINE]);
            if (lineDefiner.equals(METHOD_DECLARE) || lineDefiner.equals(IF_WHILE_BLOCK)) {
                catalan++;
            }
            innerScope.add(scopeText.get(index));
            if (lineDefiner.equals(SCOPE_CLOSING)) {
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

    private void assignVar(String[] varLine) {
        for (String var : varLine) {
            String[] varAssign = var.split(WHITE_SPACE);
            String varName = varAssign[0];
            String varValue = varAssign[2];
            Variables variable = curScope.getVariable(varName);
            if (variable != null && (!(variable.getisFinal()))) {
                try {
                    Object obj = dataAccordingToType(varValue, variable.getType());
                    variable.setData(obj);
                } catch (NumberFormatException e) {
                    Variables existVar = curScope.getVariable(varValue);
                    if(existVar!= null &&existVar.getType().equals(variable.getType())){
                        variable.setData(existVar.getData());
                    }
                    else{
                        throw new NumberFormatException();
                    }

                }

            }
        }
    }


    // todo method that takes a line of var deceleration and turns it into varibales
    private void parseVar(String[] varLine) {
        String[] variableString = varLine[FIRST_VAR_DECLARE].split(WHITE_SPACE);
        Variables var;
        Object data = null;
        String name = "";
        String type = "";
        boolean isFinal = false;
        switch (variableString.length) {
            case 2:
                type = variableString[0];
                name = variableString[1];
                break;

//                case 3:
//                    type = variableString[1];
//                    name = variableString[2];
//                    isFinal = variableString[0].equals(FINAL);
//                    break;
            case 4:
                type = variableString[0];
                name = variableString[1];
                try {
                    data = dataAccordingToType(variableString[3], type);
                } catch (NumberFormatException e) {
                    data = CheckIfExistVar(variableString[3], type);
                }
                break;
            case 5:
                isFinal = variableString[0].equals(FINAL);
                type = variableString[1];
                name = variableString[2];
                try {
                    data = dataAccordingToType(variableString[4], type);
                } catch (NumberFormatException e) {
                    data = CheckIfExistVar(variableString[4], type);
                }
                break;
        }
        var = new Variables(name, type, data, isFinal);
        curScope.addVariable(var);
        for (int i = 1; i < varLine.length; i++) {
            variableString = varLine[i].split(WHITE_SPACE);
            data = null;
            name = variableString[0];
            switch (variableString.length) {
                case 3:
                    try {
                        data = dataAccordingToType(variableString[2], type);
                    } catch (NumberFormatException e) {
                        data = CheckIfExistVar(variableString[2], type);
                    }
            }
            var = new Variables(name, type, data, isFinal);
            curScope.addVariable(var);


        }

    }

    private Object dataAccordingToType(String data, String type) {
        switch (type) {
            case INT:
                return Integer.parseInt(data);
            case DOUBLE:
                return Double.parseDouble(data);
            case BOOLEAN:
                return Boolean.parseBoolean(data);
            case CHAR:
            case STRING:
                if (data.startsWith("\"") && data.endsWith("\"")) {
                    return data.replace("\"", "");
                } else {
                    throw new NumberFormatException();
                }
        }
        throw new NumberFormatException();
    }

    private String CheckIfExistVar(String varData, String type) {
        Variables existVar = curScope.getVariable(varData);
        if (existVar != null) {
            if (existVar.getType().equals(type)) {
                return String.valueOf(existVar.getData());
            }
        }
        return null;
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
        Pattern pattern = Pattern.compile(VaribelCreation);
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            return VARIABLE;
        }
        pattern = Pattern.compile(MethodDecleration);
        if (matcher.matches()) {
            return METHOD_DECLARE;
        }
        pattern = Pattern.compile(MethodCall);
        if (matcher.matches()) {
            return METHOD_CALL;
        }
        pattern = Pattern.compile(VariableAssignment);
        if (matcher.matches()) {
            return VARIABLE_ASSIGNMENT;
        }
        pattern = Pattern.compile(IfWhile);
        if (matcher.matches()) {
            return IF_WHILE_BLOCK;
        }
        pattern = Pattern.compile(returnVar);
        if (matcher.matches()) {
            return RETURN;
        }
        pattern = Pattern.compile(ScopeClosing);
        if (matcher.matches()) {
            return SCOPE_CLOSING;
        }
//        String notePattered; //in case the note is in innerScope
//        notePattered = notePatteredCreator();
        pattern = Pattern.compile(Note);
        matcher = pattern.matcher(line);
        if (matcher.matches()) {
            return NOTE;
        }
        pattern = Pattern.compile(EmptyLine);
        matcher = pattern.matcher(line);
        if(matcher.matches()){
            return EMPTY_LINE;
        }
        else {
            return LINE_ERROR;
        }
    }

    private String notePatteredCreator() {
        int outerScope = OUTER_SCOPE;
        Scope father = curScope.getFather();
        while (father != null) {
            outerScope++;
            father = father.getFather();
        }
        String indentation = INDENTATION;
        indentation = new String(new char[outerScope]).replace("\0", indentation);
        return NO_CHAR_BEFORE + indentation + Note;
    }
}

