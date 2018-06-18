import src.MyExceptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.Paths.get;

public class Parser {

    public static final String FALSE = "false";
    public static final String TRUE = "true";
    private static HashMap<String, String> pattenToDefDict;
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
    public static final String EMPTY_LINE = "Empty_line";
    private static final String END_STATEMENT = ";";
    private static final String SCOPE_OPENING = "{";
    public static final String INSIDE_PARRENTESS = "\\((.*?)\\)\\s*\\{" + "\\s*";
    public static final String GET_INSIDE_PERENTLESS_INFO = "\\((.*?)\\)\\s*\\{\\s*";
    public static final String METHOD_NAME = "\\s*void\\s*(.*?)\\s*\\(\\s*";
    public static final String LOGICAL_OPERATORS = "(\\|\\||&&)";
    public static final String CONDITION_PATTEREN = "(^\\s*" + LOGICAL_OPERATORS + ")|" +
            LOGICAL_OPERATORS + "\\s*" + LOGICAL_OPERATORS + "|" + LOGICAL_OPERATORS + "\\s*$";
    public static final String INT_OR_DOUBLE_REGEX = "(-?\\d)+(\\.\\d+)?";
    private String VariableDeceleration = "\\s*((final\\s+)?(int|boolean|double|String|char))";
    private String Names = "\\s*((([a-z]|[A-Z])+)\\w*)|(_+([a-z]|[A-Z]|\\d)+)";
    private List<String> javaDoc;
    //Magic numbers
    final private static String EQUALS = "=";
    final private static String EMPTYSTRING = "";
    final private String VariableDecleration = "\\s*((final\\s+)?(int|boolean|double|String|char))";
    final private String MethodDeceleration = "\\s*void\\s+(" + Names + ")\\s*\\((" + VariableDecleration +
            "\\s+(((([a-z]|[A-Z])+)\\w*)|(_+([a-z]|[A-Z]|\\d)+\\s*)\\s*))?(\\s*\\)\\s*\\{)?\\s*";
    final private String MethodCall = "\\s*(((([a-z]|[A-Z])+)\\w*)|(_+([a-z]|[A-Z]|\\d)+))\\s*\\" +
            "(((\\s*((([a-z]|[A-Z])+)\\w*)\\s*|(_+([a-z]|[A-Z]|\\d)+))(\\)\\s*;)?|(\\s*\\)\\s*;))";
    final private String VariableAssignment = "\\s*(" + Names + ")\\s*=\\s*(((" + Names + "))" +
            "|(-?\\d+(.\\d+)?|(\\\"[\\w\\W]+\\\")||\\\'[\\w\\W]+\\\'))\\s*\\;?";
    final private String IfWhile = "\\s*(if|while)\\s*((\\(.+\\)\\s*\\{\\s*)|\\(\\s*\\s*" +
            "(((([a-z]|[A-Z])+)\\w*)|(_+([a-z]|[A-Z]|\\d)+))\\s*\\(" + INT_OR_DOUBLE_REGEX + ")";
    final private String returnVar = "\\s*return\\s*;\\s*";
    final private String ScopeClosing = "\\s*}\\s*";
    final private String Note = "^\\/\\/.*";
    final private String VariableCreation = VariableDeceleration + "\\s+(((([a-z]|[A-Z])+)\\w*)|(_+([a-z]|[A-Z]|\\d)+))" +
            "\\s*(=\\s*(" + INT_OR_DOUBLE_REGEX + "|\\\"[\\w\\W]+\\\"|\\\'[\\w\\W]+\\\'|" + Names + "))?\\s*;?";


    /**
     * constructor of the parser class
     *
     * @param sJavaFilePath simplified java document
     * @throws MyExceptions in case file is not legal txt document.
     */
    public Parser(String sJavaFilePath) throws MyExceptions {
        javaDoc = convertToStringArr(sJavaFilePath);
        pattenToDefDict = dictCreator();
    }

    public Parser() {
        javaDoc = null;
        dictCreator();
    }


    //creates dictionary of pattens and their meaning
    private HashMap<String, String> dictCreator() {
        HashMap<String, String> dictionary = new HashMap<>();
        dictionary.put(VariableCreation, VARIABLE);
        dictionary.put(MethodDeceleration, METHOD_DECLARE);
        dictionary.put(MethodCall, METHOD_CALL);
        dictionary.put(VariableAssignment, VARIABLE_ASSIGNMENT);
        dictionary.put(IfWhile, IF_WHILE_BLOCK);
        dictionary.put(ScopeClosing, SCOPE_CLOSING);
        dictionary.put(Note, NOTE);
        dictionary.put(EmptyLine, EMPTY_LINE);
        dictionary.put(returnVar, RETURN);
        return dictionary;
    }



    protected void assignVar(String line, Scope scope) {
        String[] varLine = line.split(COMMA);
        varLine[varLine.length - 1] = varLine[varLine.length - 1].replace(END_STATEMENT, EMPTYSTRING);
        for (String var : varLine) {
            String[] varAssign = trimStringLst(var.split(EQUALS));
            String varName = varAssign[0];
            String varValue = varAssign[1];
            Variables variable = scope.getVariable(varName);
            if (variable != null && (!(variable.getIsFinal()))) {
                try {
                    Object obj = dataAccordingToType(varValue, variable.getType());
                    variable.setData(obj);
                } catch (NumberFormatException e) {
                    getExistingVar(scope, varValue, variable);

                }
            }
            throw new NumberFormatException();

        }
    }

    private void getExistingVar(Scope scope, String varValue, Variables variable) {
        Variables existVar = scope.getVariable(varValue);
        if (existVar != null && existVar.getType().equals(variable.getType())) {
            variable.setData(existVar.getData());
        } else {
            throw new NumberFormatException();
        }
    }


    // todo method that takes a line of var deceleration and turns it into varibales
    protected ArrayList<Variables> parseVar(String line, Scope scope) {
        ArrayList<Variables> vars = new ArrayList<>();
        String[] varLine = line.split(COMMA);
        varLine[varLine.length - 1] = varLine[varLine.length - 1].replace(END_STATEMENT, EMPTYSTRING);
        boolean isFinal = isFinal(varLine[FIRST_VAR_DECLARE]);
        varLine[FIRST_VAR_DECLARE] = varLine[FIRST_VAR_DECLARE].replace(FINAL, EMPTYSTRING);
        String type = extractType(varLine[FIRST_VAR_DECLARE]);
        varLine[FIRST_VAR_DECLARE] = varLine[FIRST_VAR_DECLARE].replace(type, EMPTYSTRING);
        String[] variableString = varLine[FIRST_VAR_DECLARE].split(EQUALS);
        String[] finalLst = trimStringLst(variableString);
        Variables var;
        ArrayList<Variables> variables;
        Object data = null;
        createVar(finalLst, data, type, isFinal, scope);
        for (int i = 1; i < varLine.length; i++) {
            finalLst = trimStringLst(varLine[i].split(EQUALS));
            data = null;
            vars.add(createVar(finalLst, data, type, isFinal, scope));


        }
        return vars;
    }


    /**
     * -     * This method  turns a method deceleration into a scope repressing the method
     * -     * @param line the line in the java file which declare the method
     * -     * @param scope Scope of the current scope
     * -     * @return Scope of the created method
     * -
     */
    protected Scope parseMethodDeceleration(String line, Scope scope) throws MyExceptions {
        String methodVars = extractString(line, GET_INSIDE_PERENTLESS_INFO);
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(METHOD_NAME);
        matcher = pattern.matcher(line);
        matcher.find();
        String methodName = matcher.group(1);
        if (!isNameValid(methodName)) {
            throw new MyExceptions(); //todo exceptions
        }
        Method methodScope = new Method(scope,methodName ,new ArrayList<Variables>)
        ArrayList<Variables> arguments = parseVar(methodVars, scope);
        methodScope.setArguments(arguments);
        return methodScope;
    }

    // Get a substring of a string by using regex
    private String extractString(String line, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        matcher.find();
        return matcher.group(1);
    }

    // checks if name of method/ variable is valid
    private boolean isNameValid(String name) {
        name.replace(Parser.WHITE_SPACE, "");
        Pattern pattern = Pattern.compile(Names);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }


    private Variables createVar(String[] line, Object data, String type, Boolean isFinal, Scope scope) throws NumberFormatException {
        String name = line[0];
        switch (line.length) {
            case 2:
                try {
                    data = dataAccordingToType(line[1], type);
                } catch (NumberFormatException e) {
                    data = CheckIfExistVar(line[1], type, scope);
                    if (data == null) {
                        throw new NumberFormatException();
                    }
                }
                break;
        }

        if(!isNameValid(name)){
            throw new MyExceptions();
        }
        if (isThereAnotherVarIdentical(name,scope)){
            throw new MyExceptions();
        }
        if(isFinal&&data==null){
            throw new MyExceptions();
        }
        Variables var = new Variables(name, type, data, isFinal);
        return var;
    }

    private String[] trimStringLst(String[] lst) {
        String[] finalLst = new String[lst.length];
        for (int i = 0; i < lst.length; i++) {
            finalLst[i] = lst[i].trim();
        }
        return finalLst;

    }


    private Boolean isFinal(String line) {
        String[] lineVar = line.split(WHITE_SPACE);
        return lineVar[0].equals(FINAL);

    }

    private String extractType(String line) {
        String[] lineVar = line.split(WHITE_SPACE);
        return lineVar[0];
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
                    if(type.equals(CHAR)&&data.length()>3){
                        throw new NumberFormatException();
                    }
                    return data.replace("\"", EMPTYSTRING);
                } else {
                    throw new NumberFormatException();

                }
        }
        throw new NumberFormatException();
    }

    private String CheckIfExistVar(String varData, String type, Scope scope) {
        Variables existVar = scope.getVariable(varData);
        if (existVar != null) {
            if (existVar.getType().equals(type)) {
                return String.valueOf(existVar.getData());
            }
        }
        return null;
    }

    /**
     * Define the line of the java doc
     *
     * @param fullLine string of the java doc line
     * @return definition word of the line
     * @throws MyExceptions in case the line is illegal
     */
    protected String lineDefining(String fullLine) throws MyExceptions {
        String lineDeceleration = fullLine.split(COMMA)[0];
        for (String linePattern : pattenToDefDict.keySet()) {
            Pattern pattern = Pattern.compile(linePattern);
            Matcher matcher = pattern.matcher(lineDeceleration);
            if (matcher.matches()) {
                String lineDef = pattenToDefDict.get(linePattern);
                if (lineEnd(fullLine, lineDef)) {
                    return lineDef;
                } else {
                    throw new MyExceptions(); //todo exception handling
                }
            }
        }
        return LINE_ERROR;
    }

    /**
     * This method takes a text file and turns it into an array of String, each index contains line from the txt
     *
     * @return Array of Strings.
     */
    protected List<String> convertToStringArr(String path) throws MyExceptions {

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


    protected List<String> getJavaDoc() {
        return javaDoc;
    }

    /**
     * Checks if the end of a line is legal
     *
     * @param line           the checked line
     * @param lineDefinition the defneition of the line
     * @return True if the line have legal ending, false otherwise.
     */
    private boolean lineEnd(String line, String lineDefinition) {
        line = line.replaceAll(WHITE_SPACE, "");
        switch (lineDefinition) {
            case METHOD_DECLARE:
            case IF_WHILE_BLOCK:
                return line.endsWith(SCOPE_OPENING);
            case VARIABLE_ASSIGNMENT:
            case VARIABLE:
            case METHOD_CALL:
            case RETURN:
                return line.endsWith(END_STATEMENT);
            default:
                return true;
        }
    }

    protected Scope ParesIfWhile(String line, Scope scope) throws MyExceptions {
        String conditions = extractString(line, GET_INSIDE_PERENTLESS_INFO);
        Pattern pattern = Pattern.compile(CONDITION_PATTEREN);
        Matcher matcher = pattern.matcher(conditions);
        if (matcher.find()) {
            throw new MyExceptions(); // todo expectations
        }
        String[] conditionsArr = conditions.split(LOGICAL_OPERATORS);
        for (String condition : conditionsArr) {
            if (isConditionValid(scope, condition)) break;
        }
        return new Scope(scope, null, IF_WHILE_BLOCK);
    }

    // checks if a condition in if\while block is a valid condition.
    private boolean isConditionValid(Scope scope, String condition) throws MyExceptions {
        condition = condition.trim();
        if (!(isConditionTextValid(condition))) {
            Variables var = scope.getVariable(condition); // condition might be variable
            if (var == null) {
                throw new MyExceptions(); //todo exception no such variable
            }
            if (var.getData()==null||!(var.getType().equals(DOUBLE)||var.getType().equals(INT))) {
                throw new MyExceptions();
            }
            if (isConditionTextValid(var.getData().toString())) {
                return true;
            }
            throw new MyExceptions(); //todo variable is not a double/int/ initialized
        }
        return false;
    }

    // checks if a string of condition is a valid argument.
    private boolean isConditionTextValid(String string) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(INT_OR_DOUBLE_REGEX);
        matcher = pattern.matcher(string);
        return matcher.matches() || string.equals(FALSE) || string.equals(TRUE);

    }
}
