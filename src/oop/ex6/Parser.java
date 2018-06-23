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


/**
 * Parses the lines in the file and checks it's validity
 */
public class Parser {

    private static Parser parser = null;
    //Constants
    final static private String INVALID_NAME = "Invalid name";
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
    final static private String WHITE_SPACE = "\\s+";
    final static private String FINAL = "final";
    final static private int FIRST_VAR_DECLARE = 0;
    final static private String INT = "int";
    final static private String DOUBLE = "double";
    final static private String BOOLEAN = "boolean";
    final static private String CHAR = "char";
    final static private String STRING = "String";
    final static private String EmptyLine = "\\s*";
    final static private String EMPTY_LINE = "Emptyline";
    final static private String END_STATEMENT = ";";
    final static private String SCOPE_OPENING = "{";
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
    final static private String MethodDeceleration = "^\\s*void\\s+\\S+\\s*\\(.*\\)\\s*\\{\\s*";
    final static private String MethodCall = "^\\s*\\S*\\s*\\(.*\\)\\s*;\\s*";
    final static private String VariableAssignment = "^\\s*\\S+\\s*=\\s*.+\\s*.*;\\s*";
    final static private String IfWhile = "^\\s*(if|while)\\s*\\(.+\\)\\s*\\{\\s*";
    final static private String returnVar = "\\s*return\\s*;\\s*";
    final static private String ScopeClosing = "\\s*}\\s*";
    final static private String Note = "^\\/\\/.*";
    final static private String VariableCreation = "^\\s*(final)?\\s*(final\\s+)?(int|boolean|double|String|char)\\s+(" +
            "[^\\s+]+\\s*)+;\\s*";
    final static private String INVALID_BOOLEAN_ARGUMENT = "Invalid boolean argument";
    final static private String CONDITIONVALIDTYPES = BOOLEAN + "|" + INT + "|" + DOUBLE;
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
    final static private int ZERO = 0;
    final static private int ONE = 1;
    final static private int NAMEINDEX = 0;
    final static private int VALUEINDEX = 1;
    final static private int PROPPERLENGTH = 2;
    final static private int METHODVARINDEXNAME = 1;
    final static private int METHODVARINDEXTYPE = 0;
    final static private String MORE_THAN_ONE_VALUE = "More than one value";
    final static private String SINGLEWHITESPACE = " ";
    final static private String RECOGNIZE_INT_REGEX = "\\s*\\-?\\d+\\s*";
    final static private String RECOGNIZE_STRING_REGEX = "\\s*\\\".*\\\"\\s*";
    final static private String CHAR_REGEX_RECOGNIZE = "\\s*\\'.\\'\\s*";
    final static private String DOUBLE_REGEX_RECOGNIZER = "\\s*\\-?\\d+(\\.\\d+)?\\s*";
    final static private String INVALID_LINE_OF_VAR_CREATION = "Invalid line of var creation";
    final static private int VALUE = 3;
    final static private int ARGUMENT = 1;
    final static private String ASSIGNINGREGEX = "([A-Za-z0-9_]*)\\s*(=\\s*(([A-Za-z0-9_.-]+)|(\".*\")|('.')))?\\s*";
    private static final String VALID_BOOLEAN_REGEX_EXP = "(\\s*\\-?\\d+(\\.\\d+)?\\s*)|(\\s*((true)|(false))\\s*)";
    final static private int DATAINDEX = 1;
    final static private int NUMOFARGSIFFINAL = 3;
    final static private int FINALINDEX = 0;
    final static private int TYPEINDEXIFFINAL = 1;
    final static private int NAMEINDEXIFFINAL = 2;
    final static private int MINIMALNUMOFARGS = 2;
    //Attributes
    private static HashMap<String, String> pattenToDefDict;
    private static HashMap<String, String> varTypeDict;



    /**
     * constructor of the parser class
     */
    private Parser() {
    }

    static Parser getParser(){
        if(parser == null){
            parser = new Parser();
        }
        return parser;
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
        varTypeDic.put(BOOLEAN, VALID_BOOLEAN_REGEX_EXP);
        varTypeDict = varTypeDic;
    }

    /**
     * Parses single var argument - assignment or creation
     * @param expression the expression to parse
     * @return String list in which the first index is the name of the var and the second index there's the value
     * @throws ParsingException thrown if the line is invalid
     */
    private String[] singleVarArrCreator(String expression) throws ParsingException {
        expression = expression.trim();
        Pattern pattern = Pattern.compile(ASSIGNINGREGEX);
        Matcher matcher = pattern.matcher(expression);
        if (!matcher.matches()) {
            throw new ParsingException(INVALID_LINE_OF_VAR_CREATION);
        }
        return new String[]{matcher.group(ARGUMENT), matcher.group(VALUE)};
    }

    /**
     * Assigns var with a data
     *
     * @param line  the line of the assignment
     * @param scope the current scope
     * @throws ParsingException thrown if assigning isn't legal
     */
     void assignVar(String line, MScope scope) throws ParsingException {
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
                    Variables variableCopy = new Variables(curVariable.getName(), curVariable.getType(),
                            true,
                            curVariable.getIsFinal());
                    scope.addVariable(variableCopy);
                }
                boolean isTypeMatched = isTypeMatch(varValue, curVariable.getType());
                if (!isTypeMatched) {
                    Variables existVar = getExistingVar(scope, varValue, curVariable.getType());
                    if (!existVar.isInitialized()) {
                        throw new ParsingException(ERROR_UN_INITIALIZED_VARIABLE);
                    }

                }
                if (!isVarInTheGlobalScope || scope.getFather() == null) {
                    curVariable.setInitialized(true);
                }

            } else {
                throw new ParsingException(TRYING_TO_ASSIGN_NON_EXISTING_VARIABLE);
            }

        }

    }

    /**
     * Parse variables from method deceleration
     *
     * @param vars the string of the vars
     * @return list with the vars
     * @throws ParsingException thrown if the vars doesn't match the method deceleration
     */
    private ArrayList<Variables> parseVarsFromMethod(String vars) throws ParsingException {
        ArrayList<Variables> finalVars = new ArrayList<>();
        String[] variables = vars.split(COMMA);
        Variables variable;
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(METHOD_VARS);
        for (String item : variables) {
            boolean isFinal = false;
            matcher = pattern.matcher(item);
            String[] var = item.trim().split(WHITE_SPACE);
            if (var.length >NUMOFARGSIFFINAL||var.length< MINIMALNUMOFARGS) {
                throw new ParsingException(INCOMPATIBLE_VAR_DECELERATION);
            }
            String type = var[METHODVARINDEXTYPE];
            String name = var[METHODVARINDEXNAME];
            if (matcher.matches()) {
                for (Variables methodVar : finalVars) {
                    if (var[ONE].equals(methodVar.getName())) {
                        throw new ParsingException(EXISTING_VAR);
                    }
                }
                if(var.length== NUMOFARGSIFFINAL){
                    isFinal = var[FINALINDEX].equals(FINAL);
                    type = var[TYPEINDEXIFFINAL];
                    name = var[NAMEINDEXIFFINAL];
                }
                if (isNameNotValid(var[METHODVARINDEXNAME])) {
                    throw new ParsingException(INVALID_NAME);
                }
                variable = new Variables(name, type, true, isFinal);
                finalVars.add(variable);
            } else {
                throw new ParsingException(INCOMPATIBLE_VAR_DECELERATION);
            }
        }
        return finalVars;
    }

    /**
     * Gets an existing var with the matching name and var
     *
     * @param scope    the current scope
     * @param varValue the name of the var
     * @param type     the type of the var
     * @return the exist var if exists
     * @throws ParsingException throws an exception if doesn't exist
     */
    private Variables getExistingVar(MScope scope, String varValue, String type) throws ParsingException {
        Variables existVar = scope.getVariable(varValue);
        if (existVar != null && existVar.getType().equals(type)) {
            return existVar;
        } else {
            throw new ParsingException(NO_EXISTING_VAR_INCOMPATIBLE_TYPE);
        }
    }


    /**
     * Parses a var deceleration
     *
     * @param line  the variable line
     * @param scope the current scope
     * @return the vars list
     * @throws ParsingException thrown if something in the var deceleration or assignment was wrong
     */
    ArrayList<Variables> parseVar(String line, MScope scope) throws ParsingException {
        ArrayList<Variables> vars = new ArrayList<>();
        String[] varLine = line.split(COMMA);
        boolean isInitialized = false;
        varLine[varLine.length - ONE] = varLine[varLine.length - ONE].replace(END_STATEMENT, EMPTYSTRING);
        boolean isFinal = isFinal(varLine[FIRST_VAR_DECLARE]);
        varLine[FIRST_VAR_DECLARE] = varLine[FIRST_VAR_DECLARE].replace(FINAL, EMPTYSTRING).trim();
        String type = extractType(varLine[FIRST_VAR_DECLARE]);
        varLine[FIRST_VAR_DECLARE] = varLine[FIRST_VAR_DECLARE].replace(type, EMPTYSTRING).trim();
        String[] variableString = singleVarArrCreator(varLine[FIRST_VAR_DECLARE]);
        if (variableString[DATAINDEX] != null) {
            isInitialized = true;

        }
        String[] finalLst = trimStringLst(variableString);
        vars.add(createVar(finalLst, isInitialized, type, isFinal, scope, vars));
        for (int i = ONE; i < varLine.length; i++) {
            finalLst = singleVarArrCreator(varLine[i]);
            vars.add(createVar(finalLst, false, type, isFinal, scope, vars));


        }
        return vars;
    }


    /**
     * This method  turns a method deceleration into a scope repressing the method
     * @param line the line in the java file which declare the method
     * @param scope Scope of the current scope
     * @return return Scope of the created method
     * @throws ParsingException if something in the method deceleration was illegal
     */
    Method parseMethodDeceleration(String line, MScope scope) throws ParsingException {
        String methodVars = extractString(line, GET_INSIDE_PERENTLESS_INFO).trim();
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(METHOD_NAME);
        matcher = pattern.matcher(line);
        matcher.find();
        String methodName = matcher.group(ONE);
        if (!isMethodNameValid(methodName) || scope.getFather() != null) {
            throw new ParsingException(INCOMPATIBLE_METHOD_NAME);
        }
        ArrayList<Variables> arguments = new ArrayList<>();
        if (!methodVars.equals(EMPTYSTRING)) {
            arguments = parseVarsFromMethod(methodVars);
        }
        return new Method(scope, arguments, methodName);
    }

    /**
     * Parse a method call
     *
     * @param scope the current scope
     * @param line  the line of the method call
     * @return the updated method
     * @throws ParsingException if something is incompatible with the call and the original method
     */
    Method parseMethodCall(MScope scope, String line) throws ParsingException {
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
            throw new ParsingException(INCOMPATIBLE_NUMBER_OF_ARGS_TO_THE_METHOD);
        }
        for (int i = ZERO; i < methodArgArr.length; i++) {
            String input = methodArgArr[i];
            Variables varArg = methodVar.get(i);
            String argType = varArg.getType();
            boolean isTypeValid = isTypeMatch(input, argType);
            if (isTypeValid) {
                varArg.setInitialized(true);
            } else {
                Variables var = getExistingVar(scope, input, argType);
                if (!var.getType().equals(argType) || !var.isInitialized()) {
                    throw new ParsingException(TYPEERROR);
                }
            }
        }
        return method;
    }

    /**
     * Get a substring of a string by using regex
     *
     * @param line  the line to check if matches
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
     *
     * @param name the name to check
     * @return true iff the name is valid
     */
    private boolean isNameNotValid(String name) {
        name.replace(Parser.WHITE_SPACE, EMPTYSTRING);
        Pattern pattern = Pattern.compile(Names);
        Matcher matcher = pattern.matcher(name);
        return !matcher.matches();
    }

    /**
     * Is the method name valid
     *
     * @param name the name
     * @return true iff the methid name valid
     */
    private boolean isMethodNameValid(String name) {
        name = name.replace(Parser.WHITE_SPACE, EMPTYSTRING);
        Pattern pattern = Pattern.compile(MethodNames);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /**
     * Creates new variable
     *
     * @param line          the line to create the var from
     * @param isInitialized is the var initialized
     * @param type          the var type
     * @param isFinal       is the var final
     * @param scope         the current scope
     * @param vars          a list of the current variables
     * @return the updated variable list
     * @throws ParsingException if the data or the name is wrong
     */

    private Variables createVar(String[] line, boolean isInitialized, String type, Boolean isFinal, MScope scope,
                                ArrayList<Variables> vars) throws ParsingException {
        String name = line[ZERO];
        if (line.length > PROPPERLENGTH) {
            throw new ParsingException(MORE_THAN_ONE_VALUE);
        }
        String data = line[ONE];
        if (data != null) {
            isInitialized = isTypeMatch(line[ONE], type);
            if (!isInitialized) {
                isInitialized = CheckIfExistVar(line[ONE], type, scope);
                if (!isInitialized) {
                    throw new ParsingException(ASSIGNING_WITH_NON_EXISTING_VARIABLE);
                }
            }

        }
        if (isNameNotValid(name)){
            throw new ParsingException(INVALID_NAME);
        }
        if (isThereAnotherVarIdentical(name, scope, vars)){
            throw new ParsingException(EXIST_VAR);
        }
        if (isFinal && !isInitialized){
            throw new ParsingException(Final_Var_No_INITIALIZION);
        }
        return new Variables(name, type, isInitialized, isFinal);
    }

    /**
     * Check if theres another var with the same name
     *
     * @param name  the name to verify
     * @param scope current scope
     * @param vars  Variable arraylist of the method args, check also there
     * @return true iff another identical var exists
     */

    private boolean isThereAnotherVarIdentical(String name, MScope scope, ArrayList<Variables> vars) {
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
     *
     * @param lst the list to trim
     * @return the trimmed string list
     */
    private String[] trimStringLst(String[] lst) {
        String[] finalLst = new String[lst.length];
        for (int i = ZERO; i < lst.length; i++) {
            if (lst[i] != null) {
                finalLst[i] = lst[i].trim();
            }
        }
        return finalLst;

    }


    /**
     * Check if the variable is final
     *
     * @param line the line to parse
     * @return true iff theres a final command
     */
    private Boolean isFinal(String line) {
        String[] lineVar = line.split(WHITE_SPACE);
        return lineVar[ZERO].equals(FINAL);

    }

    /**
     * Extract the type from the arg line
     *
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
    private boolean CheckIfExistVar(String varData, String type, MScope scope) {
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
     * @throws ParsingException in case the line is illegal
     */
    String lineDefining(String fullLine) throws ParsingException {
        for (String linePattern : pattenToDefDict.keySet()) {
            Pattern pattern = Pattern.compile(linePattern);
            Matcher matcher = pattern.matcher(fullLine);
            if (matcher.matches()) {
                String lineDef = pattenToDefDict.get(linePattern);
                if (lineEnd(fullLine, lineDef)) {
                    return lineDef;
                } else {
                    throw new ParsingException(INVALID_LINE);
                }
            }
        }
        return LINE_ERROR;
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
     * @return the if while scope
     * @throws ParsingException thrown if the condition isn't valid
     */
    MScope ParseIfWhile(String line, MScope scope) throws ParsingException {
        String conditions = extractString(line, GET_INSIDE_PERENTLESS_INFO);
        Pattern pattern = Pattern.compile(CONDITION_PATTEREN);
        Matcher matcher = pattern.matcher(conditions);
        if (matcher.find()) {
            throw new ParsingException(INVALID_BOOLEAN_ARGUMENT);
        }
        String[] conditionsArr = conditions.split(LOGICAL_OPERATORS);
        for (String condition : conditionsArr) {
            condition = condition.trim();
            if (isConditionValid(scope, condition)) break;
        }
        return new MScope(scope);
    }


    /**
     * checks if a condition in if\while block is a valid condition.
     *
     * @param MScope    the current scope
     * @param condition the condition to check
     * @return true iff the condition is valid
     * @throws ParsingException thrown if the condition isn't valid
     */
    private boolean isConditionValid(MScope MScope, String condition) throws ParsingException {
        condition = condition.trim();
        Method method = MScope.getScopesMethod();
        if (!(isConditionTextValid(condition, MScope))) {
            Variables var = MScope.getVariable(condition); // condition might be variable
            if (var == null) {
                throw new ParsingException(INVALID_BOOLEAN_ARGUMENT);
            }

            if (isaBooleanArgValid(var, method.isCalled(), method)) {
                throw new ParsingException(INVALID_BOOLEAN_ARGUMENT);
            }
            if (!method.isCalled() || isConditionTextValid(condition, MScope)) {
                return true;
            }
            throw new ParsingException(INVALID_BOOLEAN_ARGUMENT);
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
     * @param MScope    current scope
     * @return true iff condition is valid
     */
    private boolean isConditionTextValid(String condition, MScope MScope) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(BOOLEAN_EXP);
        matcher = pattern.matcher(condition);
        if (matcher.matches()) {
            return true;
        }
        Variables var = MScope.getVariable(condition);
        if (var == null) {
            return false;
        }
        String type = var.getType();
        pattern = Pattern.compile(CONDITIONVALIDTYPES);
        matcher = pattern.matcher(type);
        return var.isInitialized() && (matcher.matches());


    }

    /**
     * Check if the data matches the type
     *
     * @param chekedVar the data to check
     * @param type      the type to check
     * @return true iff matches
     */
    private boolean isTypeMatch(String chekedVar, String type) {
        Pattern pattern = Pattern.compile(varTypeDict.get(type));
        Matcher matcher = pattern.matcher(chekedVar);
        return matcher.matches();

    }

    /**
     * This method takes a text file and turns it into an array of String, each index contains line from the txt
     * @param path the path of the file
     * @return Array of Strings
     * @throws IOException thrown if the file doesn't exist
     */
     List<String> convertToStringArr(String path) throws IOException {
        Path filePath = get(path);
        return Files.readAllLines(filePath);
    }
}
