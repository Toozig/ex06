package oop.ex6;

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

    final static private String INVALID_NAME = "Invalid name";
    final static private String FALSE = "false";
    final static private String TRUE = "true";
    final static private String GET_METHOD_NAME_REGEX = "\\s*(.*)\\s*\\(";
    final static private String VARIABLE = "Variable";
    final static private String METHOD_DECLARE = "MethodDeclare";
    final static private String VARIABLE_ASSIGNMENT = "VariableAssignment";
    final static private String NOTE = "Note";
    final static private String COMMA = ",";
    final static private String IF_WHILE_BLOCK = "IfWhileBlock";
    final static private String SCOPE_CLOSING = "ScopeClosing";
    final static private String METHOD_CALL = "MethodCall";
    final static private String RETURN = "Return";
    final static private String LINE_ERROR = "lineError";
    final static private String INDENTATION = "    ";
    final static private String NO_CHAR_BEFORE = "^";
    final static private int OUTER_SCOPE = 0;
    final static private int FIRST_LINE = 0;
    final static private int CATALAN = 1;
    final static private String WHITE_SPACE = "\\s+";
    final static private String FINAL = "final";
    final static private int FIRST_VAR_DECLARE = 0;
    final static private String INT = "int";
    final static private String DOUBLE = "double";
    final static private String BOOLEAN = "boolean";
    final static private String CHAR = "char";
    final static private String STRING = "String";
    final static private String INCOMPATIBLE_TYPE = "incompatibleType";
    final static private String EmptyLine = "\\s*";
    final static private String EMPTY_LINE = "Emptyline";
    final static private String END_STATEMENT = ";";
    final static private String SCOPE_OPENING = "{";
    final static private String INSIDE_PARRENTESS = "\\((.*?)\\)\\s*\\{" + "\\s*";
    final static private String GET_INSIDE_PERENTLESS_INFO = "\\((.*?)\\)\\s*(\\{|\\;)\\s*";
    final static private String METHOD_NAME = "\\s*void\\s*(.*?)\\s*\\(\\s*";
    final static private String LOGICAL_OPERATORS = "(\\|\\||&&)";
    final static private String CONDITION_PATTEREN = "(^\\s*" + LOGICAL_OPERATORS + ")|" +
            LOGICAL_OPERATORS + "\\s*" + LOGICAL_OPERATORS + "|" + LOGICAL_OPERATORS + "\\s*$";
    final static private String BOOLEAN_EXP = "((-?\\d)+(\\.\\d+)?)|(s*((true)|(false))\\s*)";
    final static private String Names = "\\s*((([a-z]|[A-Z])+)\\w*)|(_+([a-z]|[A-Z]|\\d)+)";
    final static private String MethodNames = "\\s*(([a-z]|[A-Z])+)(_|([a-z]|[A-Z]|\\d))*";
    final static private String EQUALS = "=";
    final static private String EMPTYSTRING = "";
    final static private String METHOD_VARS = "(\\s*((final\\s+)?(int|boolean|double|String|char))" + WHITE_SPACE +
            Names + ")\\s*";
    final static private String VariableDeceleration = "\\s*((final\\s+)?(int|boolean|double|String|char))";
    final static private String MethodDeceleration = "^\\s*void\\s+\\S+\\s*\\(.*\\)\\s*\\{\\s*";
    final static private String MethodCall = "^\\s*\\S*\\s*\\(.*\\)\\s*;\\s*";
    final static private String VariableAssignment = "^\\s*\\S+\\s*=\\s*.+\\s*.*;\\s*";
    final static private String IfWhile = "^\\s*(if|while)\\s*\\(.+\\)\\s*\\{\\s*";
    final static private String returnVar = "\\s*return\\s*;\\s*";
    final static private String ScopeClosing = "\\s*}\\s*";
    final static private String Note = "^\\/\\/.*";
    final static private String VariableCreation = "^\\s*(final)?\\s*(final\\s+)?(int|boolean|double|String|char)\\s+([^\\s+]+\\s*)+;\\s*";
    final static private String INVALID_BOOLEAN_ARGUMENT = "Invalid boolean argument";
    final static private String CONDITIONVALIDTYPES = BOOLEAN + "|" + INT + "|" + DOUBLE;
    final static private String INVALID_FILE = "Invalid sJava file";
    final static private String INVALID_LINE = "Invalid line format";
    final static private String EXIST_VAR = "There's another variable with the same name";
    final static private String Final_Var_No_INITIALIZION = "Final Var must be initialize";
    final static private String INCOMPATIBLE_VAR_DECELERATION = "Incompatible var deceleration";
    final static private String INCOMPATIBLE_METHOD_NAME = "Incompatible method name";
    final static private String INCOMPATIBLE_NUMBER_OF_ARGS_TO_THE_METHOD = "Incompatible number of args to the method";
    final static private String TYPEERROR = "Incompatible args type";
    final static private String ASSIGNING_WITH_NON_EXISTING_VARIABLE = "Assigning with non existing variable";
    final static private String TRYING_TO_ASSIGN_NON_EXISTING_VARIABLE = "Trying to assign non existing variable";
    final static private String NO_EXISTING_VAR_INCOMPATIBLE_TYPE = "No existing var,incompatible type";
    final static private String EXISTING_VAR = "Existing var with the same name";
    final static private String ERROR_UN_INITIALIZED_VARIABLE = "ERROR: unInitialized variable";
    final static private int ONE = 1;
    final static private int NAMEINDEX = 0;
    final static private int VALUEINDEX = 1;
    final static private int PROPPERLENGTH = 2;
    final static private int METHODVARINDEXNAME = 1;
    final static private int METHODVARINDEXTYPE = 0;
    final static private int ZERO = 0;
    final static private String DOUBLEQUATATION = "\'";
    final static private String SINGLEQUATATION = "\"";
    final static private int NOTCHAR = 3;
    final static private String MORE_THAN_ONE_VALUE = "More than one value";
    final static private String SINGLEWHITESPACE = " ";
    final static private  String RECOGNIZE_INT_REGEX = "\\s*\\-?\\d+\\s*";
    final static private  String RECOGNIZE_STRING_REGEX = "\\s*\\\".*\\\"\\s*";
    final static private  String CHAR_REGEX_RECOGNIZE = "\\s*\\'.\\'\\s*";
    final static private  String DOUBLE_REGEX_RECOGNIZER = "\\s*\\-?\\d+(\\.\\d+)?\\s*";
    private List<String> javaDoc;
    private static HashMap<String, String> pattenToDefDict;
    private static HashMap<String, String> varTypeDict;


    /**
     * constructor of the parser class
     *
     * @param sJavaFilePath simplified java document
     * @throws MyExceptions in case file is not legal txt document.
     */
    public Parser(String sJavaFilePath) throws MyExceptions {
        javaDoc = convertToStringArr(sJavaFilePath);
    }


    /**
     * constructor of the parser class
     *
     * @throws MyExceptions in case file is not legal txt document.
     */
    public Parser() {
        javaDoc = null;
    }

    //creates dictionary of pattens and their meaning
    static {
        HashMap<String, String> lineTypeDictionary = new HashMap<>();
        lineTypeDictionary.put(VariableCreation, VARIABLE);
        lineTypeDictionary.put(MethodDeceleration, METHOD_DECLARE);
        lineTypeDictionary.put(MethodCall, METHOD_CALL);
        lineTypeDictionary.put(VariableAssignment, VARIABLE_ASSIGNMENT);
        lineTypeDictionary.put(IfWhile, IF_WHILE_BLOCK);
        lineTypeDictionary.put(ScopeClosing, SCOPE_CLOSING);
        lineTypeDictionary.put(Note, NOTE);
        lineTypeDictionary.put(EmptyLine, EMPTY_LINE);
        lineTypeDictionary.put(returnVar, RETURN);
        pattenToDefDict = lineTypeDictionary;
        HashMap<String, String> varTypeDic = new HashMap<>();
        varTypeDic.put(INT, RECOGNIZE_INT_REGEX);
        varTypeDic.put(STRING, RECOGNIZE_STRING_REGEX);
        varTypeDic.put(CHAR, CHAR_REGEX_RECOGNIZE);
        varTypeDic.put(DOUBLE, DOUBLE_REGEX_RECOGNIZER);
        varTypeDic.put(BOOLEAN, ("(\\s*\\-?\\d+(\\.\\d+)?\\s*)|(\\s*((true)|(false))\\s*)"));
        varTypeDict = varTypeDic;
    }

    /**
     * Assigns var with a data
     * @param line the line of the assignment
     * @param scope the current scope
     * @throws MyExceptions thrown if assigning isn't legal
     */
    protected void assignVar(String line, ScopeC scope) throws MyExceptions {
        String[] varLine = line.split(COMMA);
        varLine[varLine.length - ONE] = varLine[varLine.length - ONE].replace(END_STATEMENT, EMPTYSTRING);
        for (String var : varLine) {
            String[] varAssign = trimStringLst(var.split(EQUALS));
            String varName = varAssign[NAMEINDEX];
            String varValue = varAssign[VALUEINDEX];
            Variables curVariable = scope.getVariable(varName);
            boolean isVarInTheGlobalScope = scope.isVarInTheGlobalScope(curVariable);
            if (curVariable != null && (!(curVariable.getIsFinal()))) {
                if (isVarInTheGlobalScope && scope.getFather() != null) {
                    Variables variableCopy = new Variables(curVariable.getName(), curVariable.getType(), true,
                            curVariable.getIsFinal());
                    scope.addVariable(variableCopy);
                }
                boolean isTypeMatched = isTypeMatch(varValue, curVariable.getType());
                if (!isTypeMatched) {
                    Variables existVar = getExistingVar(scope, varValue, curVariable.getType());
                    Method method = (Method) scope;
                    if (!existVar.isInitialized()) {
                        throw new MyExceptions(ERROR_UN_INITIALIZED_VARIABLE);
                    }

                }
                if (!isVarInTheGlobalScope || scope.getFather() == null) {
                    curVariable.setInitialized(true);
                }

            } else {
                throw new MyExceptions(TRYING_TO_ASSIGN_NON_EXISTING_VARIABLE);
            }

        }

    }

    /**
     * Parse variables from method deceleration
     * @param vars the string of the vars
     * @return list with the vars
     * @throws MyExceptions thrown if the vars doesn't match the method deceleration
     */
    protected ArrayList<Variables> parseVarsFromMethod(String vars) throws MyExceptions {
        ArrayList<Variables> finalVars = new ArrayList<>();
        String[] variables = vars.split(COMMA);
        Variables variable;
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(METHOD_VARS);
        for (String item : variables) {
            matcher = pattern.matcher(item);
            String[] var = item.trim().split(WHITE_SPACE);
            if (!(var.length == PROPPERLENGTH)) {
                throw new MyExceptions(INCOMPATIBLE_VAR_DECELERATION);
            }
            if (matcher.matches()) {
                for (Variables methodVar : finalVars) {
                    if (var[ONE].equals(methodVar.getName())) {
                        throw new MyExceptions(EXISTING_VAR);
                    }

                }
                if (!isNameValid(var[METHODVARINDEXNAME])) {
                    throw new MyExceptions(INVALID_NAME);
                }
                variable = new Variables(var[METHODVARINDEXNAME], var[METHODVARINDEXTYPE], true, false);
                finalVars.add(variable);
            } else {
                throw new MyExceptions(INCOMPATIBLE_VAR_DECELERATION);
            }


        }
        return finalVars;
    }

    /**
     * Gets an existing var with the matching name and var
     * @param scope the current scope
     * @param varValue the name of the var
     * @param type the type of the var
     * @return the exist var if exists
     * @throws MyExceptions throws an exception if doesn't exist
     */
    private Variables getExistingVar(ScopeC scope, String varValue, String type) throws MyExceptions {
        Variables existVar = scope.getVariable(varValue);
        if (existVar != null && existVar.getType().equals(type)) {
            return existVar;
        } else {
            throw new MyExceptions(NO_EXISTING_VAR_INCOMPATIBLE_TYPE);
        }
    }



    /**
     * Parses a var deceleration
     * @param line the variable line
     * @param scope the current scope
     * @return the vars list
     * @throws MyExceptions
     */
    protected ArrayList<Variables> parseVar(String line, ScopeC scope) throws MyExceptions {
        ArrayList<Variables> vars = new ArrayList<>();
        String[] varLine = line.split(COMMA);
        varLine[varLine.length - ONE] = varLine[varLine.length - ONE].replace(END_STATEMENT, EMPTYSTRING);
        boolean isFinal = isFinal(varLine[FIRST_VAR_DECLARE]);
        varLine[FIRST_VAR_DECLARE] = varLine[FIRST_VAR_DECLARE].replace(FINAL, EMPTYSTRING).trim();
        String type = extractType(varLine[FIRST_VAR_DECLARE]);
        varLine[FIRST_VAR_DECLARE] = varLine[FIRST_VAR_DECLARE].replace(type, EMPTYSTRING).trim();
        String[] variableString = varLine[FIRST_VAR_DECLARE].split(EQUALS);
        String[] finalLst = trimStringLst(variableString);
        boolean isInitialized = false;
        vars.add(createVar(finalLst, isInitialized, type, isFinal, scope, vars));
        for (int i = ONE; i < varLine.length; i++) {
            finalLst = trimStringLst(varLine[i].split(EQUALS));
            isInitialized = false;
            vars.add(createVar(finalLst, isInitialized, type, isFinal, scope, vars));


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
    protected Method parseMethodDeceleration(String line, ScopeC scope) throws MyExceptions {
        String methodVars = extractString(line, GET_INSIDE_PERENTLESS_INFO).trim();
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(METHOD_NAME);
        matcher = pattern.matcher(line);
        matcher.find();
        String methodName = matcher.group(ONE);
        if (!isMethodNameValid(methodName) || scope.getFather() != null) {
            throw new MyExceptions(INCOMPATIBLE_METHOD_NAME);
        }
        ArrayList<Variables> arguments = new ArrayList<>();
        if (!methodVars.equals(EMPTYSTRING)) {
            arguments = parseVarsFromMethod(methodVars);
        }
        return new Method(scope, arguments, methodName);
    }

    /**
     * Parse a method call
     * @param scope the current scope
     * @param line the line of the method call
     * @return the updated method
     * @throws MyExceptions if something is incompatible with the call and the original method
     */
    protected Method parseMethodCall(ScopeC scope, String line) throws MyExceptions {
        String methodName = extractString(line, GET_METHOD_NAME_REGEX);
        methodName = methodName.trim();
        Method method = scope.getMethod(methodName);
        String methodArgumentsString = extractString(line, GET_INSIDE_PERENTLESS_INFO);
        methodArgumentsString = methodArgumentsString.replace(SINGLEWHITESPACE, EMPTYSTRING);
        String[] methodArgArr = methodArgumentsString.split(COMMA);
        ArrayList<Variables> methodVar = method.getArguments();
        if (methodArgArr.length == ONE && methodArgArr[ZERO].equals(EMPTYSTRING)) {
            methodArgArr = new String[ZERO];
        }
        if (methodArgArr.length != methodVar.size()) {
            throw new MyExceptions(INCOMPATIBLE_NUMBER_OF_ARGS_TO_THE_METHOD);
        }
        for (int i = ZERO; i < methodArgArr.length; i++) {
            String input = methodArgArr[i];
            Variables varArg = methodVar.get(i);
            String argType = varArg.getType();
            boolean isTypeValid = isTypeMatch(input, argType);
            if (isTypeValid) {
                varArg.setInitialized(true);
            }
            else{
            Variables var = getExistingVar(scope, input, argType);
            if (!var.getType().equals(argType) || !var.isInitialized()) {
                throw new MyExceptions(TYPEERROR);
            }
        }
    }
        return method;
}

    /**
     * Get a substring of a string by using regex
     * @param line the line to check if matches
     * @param regex the regex
     * @return the matched string
     */
    private String extractString(String line, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        matcher.find();
        return matcher.group(ONE);
    }


    /**
     * Check if the variable name is valid
     * @param name the name to check
     * @return true iff the name is valid
     */
    private boolean isNameValid(String name) {
        name.replace(Parser.WHITE_SPACE, EMPTYSTRING);
        Pattern pattern = Pattern.compile(Names);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /**
     * Is the method name valid
     * @param name the name
     * @return true iff the methid name valid
     */
    private boolean isMethodNameValid(String name) {
        name.replace(Parser.WHITE_SPACE, EMPTYSTRING);
        Pattern pattern = Pattern.compile(MethodNames);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /**
     * Creates new variable
     * @param line the line to create the var from
     * @param isInitialized is the var initialized
     * @param type the var type
     * @param isFinal is the var final
     * @param scope the current scope
     * @param vars a list of the current variables
     * @return the updated variable list
     * @throws MyExceptions if the data or the name is wrong
     */

    private Variables createVar(String[] line, boolean isInitialized, String type, Boolean isFinal, ScopeC scope,
                                ArrayList<Variables> vars) throws MyExceptions {
        String name = line[ZERO];
        if (line.length > PROPPERLENGTH) {
            throw new MyExceptions(MORE_THAN_ONE_VALUE);
        }
        switch (line.length) {
            case PROPPERLENGTH:
                isInitialized = isTypeMatch(line[ONE], type);
                if (!isInitialized) {
                    isInitialized = CheckIfExistVar(line[ONE], type, scope);
                    if (!isInitialized) {
                        throw new MyExceptions(ASSIGNING_WITH_NON_EXISTING_VARIABLE);
                    }
                }
                break;
        }

        if (!isNameValid(name)) {
            throw new MyExceptions(INVALID_NAME);
        }
        if (isThereAnotherVarIdentical(name, scope, vars)) {
            throw new MyExceptions(EXIST_VAR);
        }

        if (isFinal && !isInitialized) {
            throw new MyExceptions(Final_Var_No_INITIALIZION);
        }
        Variables var = new Variables(name, type, isInitialized, isFinal);
        return var;
    }

    /**
     * Check if theres another var with the same name
     * @param name the name to verify
     * @param scope current scope
     * @param vars Variable arraylist of the method args, check also there
     * @return true iff another identical var exists
     */

    private boolean isThereAnotherVarIdentical(String name, ScopeC scope, ArrayList<Variables> vars) {
        ArrayList<Variables> allVars = new ArrayList<>();
        allVars.addAll(scope.getVarArray());
        allVars.addAll(vars);
        for (Variables var : allVars) {
            if (var.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Trims a string list
     * @param lst the list to trim
     * @return the trimmed string list
     */
    private String[] trimStringLst(String[] lst) {
        String[] finalLst = new String[lst.length];
        for (int i = ZERO; i < lst.length; i++) {
            finalLst[i] = lst[i].trim();
        }
        return finalLst;

    }


    /**
     * Check if the variable is final
     * @param line the line to parse
     * @return true iff theres a final command
     */
    private Boolean isFinal(String line) {
        String[] lineVar = line.split(WHITE_SPACE);
        return lineVar[ZERO].equals(FINAL);

    }

    /**
     * Extract the type from the arg line
     * @param line the line to parse
     * @return the type
     */
    private String extractType(String line) {
        String[] lineVar = line.split(WHITE_SPACE);
        return lineVar[ZERO];
    }


    /**
     * Check if variables's data is actually an existing other variable
     *
     * @param varData the var name (the original var data)
     * @param type    the type of the variable
     * @param scope   the current scope
     * @return true iff there's an existing var with that name and type
     */
    private boolean CheckIfExistVar(String varData, String type, ScopeC scope) {
        Variables existVar = scope.getVariable(varData);
        if (existVar != null) {
            if (TypeIsValidForBooleanArg(type, existVar)) {
                return existVar.isInitialized();
            }
        }
        return false;
    }

    /**
     * Check if the type of the data is valid for boolean arg
     *
     * @param type     the type of the data
     * @param existVar the exist var
     * @return true iff the type is valid boolean arg
     */
    private boolean TypeIsValidForBooleanArg(String type, Variables existVar) {
        String varType = existVar.getType();
        return varType.equals(type) || (type.equals(BOOLEAN) &&
                (varType.equals(INT) || varType.equals(DOUBLE))) || type.equals(DOUBLE) && varType.equals(INT);
    }

    /**
     * Define the line of the java doc
     *
     * @param fullLine string of the java doc line
     * @return definition word of the line
     * @throws MyExceptions in case the line is illegal
     */
    protected String lineDefining(String fullLine) throws MyExceptions {
        for (String linePattern : pattenToDefDict.keySet()) {
            Pattern pattern = Pattern.compile(linePattern);
            Matcher matcher = pattern.matcher(fullLine);
            if (matcher.matches()) {
                String lineDef = pattenToDefDict.get(linePattern);
                if (lineEnd(fullLine, lineDef)) {
                    return lineDef;
                } else {
                    throw new MyExceptions(INVALID_LINE); //todo exception handling
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
            throw new MyExceptions(INVALID_FILE);
        }
    }

    /**
     * Gets the javadoc
     *
     * @return javadoc
     */
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
        line = line.replaceAll(WHITE_SPACE, EMPTYSTRING);
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

    /**
     * Parses if\while
     *
     * @param line  the line of the if\while definition
     * @param scope the current scope
     * @return אthe if\while scope
     * @throws MyExceptions thrown if the condition isn't valid
     */
    protected ScopeC ParesIfWhile(String line, ScopeC scope) throws MyExceptions {
        String conditions = extractString(line, GET_INSIDE_PERENTLESS_INFO);
        Pattern pattern = Pattern.compile(CONDITION_PATTEREN);
        Matcher matcher = pattern.matcher(conditions);
        if (matcher.find()) {
            throw new MyExceptions(INVALID_BOOLEAN_ARGUMENT); // todo exceptions
        }
        String[] conditionsArr = conditions.split(LOGICAL_OPERATORS);
        for (String condition : conditionsArr) {
            condition = condition.trim();
            if (isConditionValid(scope, condition)) break;
        }
        return new ScopeC(scope);
    }


    /**
     * checks if a condition in if\while block is a valid condition.
     *
     * @param scopeC    the current scope
     * @param condition the condition to check
     * @return true iff the condition is valid
     * @throws MyExceptions thrown if the condition isn't valid
     */
    private boolean isConditionValid(ScopeC scopeC, String condition) throws MyExceptions {
        condition = condition.trim();
        Method method = scopeC.getScopesMethod();
        if (!(isConditionTextValid(condition, scopeC))) {
            Variables var = scopeC.getVariable(condition); // condition might be variable
            if (var == null) {
                throw new MyExceptions(INVALID_BOOLEAN_ARGUMENT);
            }

            if (isaBooleanArgValid(var, method.isCalled(), method)) {
                throw new MyExceptions(INVALID_BOOLEAN_ARGUMENT);
            }
            if (!method.isCalled() || isConditionTextValid(condition, scopeC)) {
                return true;
            }
            throw new MyExceptions(INVALID_BOOLEAN_ARGUMENT);
        }
        return false;
    }

    /**
     * checks if a given variable is a valid boolean argument
     *
     * @param var      the variable
     * @param isCalled is the method called
     * @param method   the method the condition is in
     * @return true iff the boolean argument is valid
     */
    private boolean isaBooleanArgValid(Variables var, boolean isCalled, Method method) {
        ArrayList<Variables> vars = method.getArguments();
        boolean type = var.getType().equals(BOOLEAN) ||
                var.getType().equals(DOUBLE) || var.getType().equals(INT);
        boolean isInitialized = var.isInitialized();
        boolean isVarArg = vars.contains(var);
        return (!(type && (isInitialized || (!isCalled) && isVarArg)));
    }


    /**
     * checks if a string of condition is a valid argument.
     *
     * @param condition the condition to check
     * @param scopeC    current scope
     * @return true iff condition is valid
     */
    private boolean isConditionTextValid(String condition, ScopeC scopeC) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(BOOLEAN_EXP);
        matcher = pattern.matcher(condition);
        if (matcher.matches()) {
            return true;
        }
        Variables var = scopeC.getVariable(condition);
        if (var == null) {
            return false;
        }
        String type = var.getType();
        pattern = Pattern.compile(CONDITIONVALIDTYPES);
        matcher = pattern.matcher(type);
        if (var.isInitialized() && (matcher.matches())) {
            return true;
        }


        return false;
    }

    /**
     * Check if the data matches the type
     *
     * @param chekedVar the data to check
     * @param type      the type to check
     * @return true iff matches
     */
    public boolean isTypeMatch(String chekedVar, String type) {
        Pattern pattern = Pattern.compile(varTypeDict.get(type));
        Matcher matcher = pattern.matcher(chekedVar);
        return matcher.matches();

    }
}
