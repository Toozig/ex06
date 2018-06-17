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
    private static final String VARIABLE = "Variable";
    private static final String METHOD_DECLARE = "Method_Declare";
    private static final String VARIABLE_ASSIGNMENT = "Variable_Assignment";
    private static final String NOTE = "Note";
    private static final String COMMA = ",";
    private static final String IF_WHILE_BLOCK = "If_While_Block";
    private static final String SCOPE_CLOSING = "Scope_Closing";
    private static final String METHOD_CALL = "Method_Call";
    private static final String RETURN = "Return";
    private static final String LINE_ERROR = "line_Error";
    private static final String INDENTATION = "    ";
    private static final String NO_CHAR_BEFORE = "^";
    private static final int OUTER_SCOPE = 0;
    private static final int FIRST_LINE = 0;
    private static final int CATALAN = 1;
    private static final String EmptyLine = " *";
    private static final String EMPTY_LINE = "Empty line";
    private static final String END_STATEMENT = ";";
    private static final String SCOPE_OPENING = "{";
    private String VariableDecleration = "\\s*((final\\s+)?(int|boolean|double|String|char))";
    private String Names = "\\s*((([a-z]|[A-Z])+)\\w*)|(_+([a-z]|[A-Z]|\\d)+)";
    private List<String> javaDoc;
    final private String MethodDecleration =  "\\s*void\\s+(" + Names + ")\\s*\\(("+ VariableDecleration +
            "\\s+(((([a-z]|[A-Z])+)\\w*)|(_+([a-z]|[A-Z]|\\d)+\\s*)\\s*))?(\\s*\\)\\s*\\{)?\\s*";
    final private String MethodCall = "\\s*(((([a-z]|[A-Z])+)\\w*)|(_+([a-z]|[A-Z]|\\d)+))\\s*\\" +
            "(((\\s*((([a-z]|[A-Z])+)\\w*)\\s*|(_+([a-z]|[A-Z]|\\d)+))(\\)\\s*;)?|(\\s*\\)\\s*;))";
    final private String VariableAssignment = "\\s*(" + Names + ")\\s*=\\s*(((" + Names + "))" +
            "|(-?\\d+(.\\d+)?|(\\\"[\\w\\W]+\\\")||\\\'[\\w\\W]+\\\'))\\s*\\;?";
    final private String IfWhile = "\\s*(if|while)\\s*((\\(.+\\)\\s*\\{\\s*)|\\(\\s*\\s*" +
            "(((([a-z]|[A-Z])+)\\w*)|(_+([a-z]|[A-Z]|\\d)+))\\s*\\((-?\\d)+(\\.\\d+)?)";
    final private String returnVar = "\\s*return\\s*;\\s*";
    final private String ScopeClosing = "\\s*}\\s*";
    final private String Note = "^\\/\\/.*";
    final private String VaribelCreation = VariableDecleration +"\\s+(((([a-z]|[A-Z])+)\\w*)|(_+([a-z]|[A-Z]|\\d)+))" +
            "\\s*(=\\s*(-?\\d+.(\\d+)?|\\\"[\\w\\W]+\\\"|\\\'[\\w\\W]+\\\'|"+ Names +"))?\\s*;?";
    private Scope curScope;


    /**
     * constructor of the parser class
     * @param sJavaFilePath  simplified java document
     * @throws MyExceptions in case file is not legal txt document.
     */
    public Parser(String sJavaFilePath) throws MyExceptions {
        javaDoc = convertToStringArr(sJavaFilePath);
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

    /**
     *  Define the line of the java doc
     * @param fullLine string of the java doc line
     * @return definition word of the line
     * @throws MyExceptions in case the line is illegal
     */
    protected String lineDefining(String fullLine) throws MyExceptions {

        String lineDeceleration = fullLine.split(COMMA)[0];
        Pattern pattern = Pattern.compile(VaribelCreation);
        Matcher matcher = pattern.matcher(lineDeceleration);
        if (matcher.matches()) {
            return VARIABLE;
        }
        pattern = Pattern.compile(MethodDecleration);
        matcher = pattern.matcher(lineDeceleration);
        if (matcher.matches()) {
            lineEnd(fullLine,SCOPE_OPENING);
            return METHOD_DECLARE;
        }
        pattern = Pattern.compile(MethodCall);
        matcher = pattern.matcher(lineDeceleration);
        if (matcher.matches()) {
            lineEnd(fullLine,END_STATEMENT);
            return METHOD_CALL;
        }
        pattern = Pattern.compile(VariableAssignment);
        matcher = pattern.matcher(lineDeceleration);
        if (matcher.matches()) {
            lineEnd(fullLine,END_STATEMENT);
            return VARIABLE_ASSIGNMENT;
        }
        pattern = Pattern.compile(IfWhile);
        matcher = pattern.matcher(lineDeceleration);
        if (matcher.matches()) {
            lineEnd(fullLine,SCOPE_OPENING);
            return IF_WHILE_BLOCK;
        }
        pattern = Pattern.compile(returnVar);
        matcher = pattern.matcher(lineDeceleration);
        if (matcher.matches()) {
            lineEnd(fullLine,END_STATEMENT);
            return RETURN;
        }
        pattern = Pattern.compile(ScopeClosing);
        matcher = pattern.matcher(lineDeceleration);
        if (matcher.matches()) {
            return SCOPE_CLOSING;
        }
        pattern = Pattern.compile(Note);
        matcher = pattern.matcher(lineDeceleration);
        if (matcher.matches()) {
            return NOTE;
        }
        pattern = Pattern.compile(EmptyLine);
        matcher = pattern.matcher(lineDeceleration);
        if(matcher.matches()){
            return EMPTY_LINE;
        }
        else {
            return LINE_ERROR;
        }
    }

    protected List<String> getJavaDoc() {
        return javaDoc;
    }

    /**
     * Checks if the end of a line is legal
     * @param line the checked line
     * @param endChar the char supposed to be at the end
     * @throws MyExceptions if the line ending is ilegal
     */
    private void lineEnd(String line, String endChar) throws MyExceptions {
        line.replaceAll("\\s+","");
        if(!line.endsWith(endChar)){
            throw new MyExceptions();
        }
    }

    //    public List<String> checker() {
//        ArrayList<String> re = new ArrayList<>();
//        for (String line : javaDoc) {
//            String[] splitString = line.split(COMMA);
//            String lineDefiner = lineDefining(splitString[0]);
//            re.add(lineDefiner);
//        }
//             return re;
//    }
//    protected Scope globalScopeCreator() throws MyExceptions {
//        Scope globalScope = new Scope(null, javaDoc);
//        curScope = globalScope;
//        for (int i = 0; i < javaDoc.size(); i++) {
//            String line = javaDoc.get(i);
//            String[] splitString = line.split(COMMA);
//            String lineDefiner = lineDefining(splitString[0]);
//            switch (lineDefiner) {
//                case VARIABLE:
//                    parseVar(splitString);
//                    lineEnd(line, END_STATEMENT);
//                    break;
//                case METHOD_DECLARE:
//                    Scope innerScope = parseMethod(splitString, i);
//                    curScope.addInnerScope(innerScope);
//                    i = i + innerScope.getTextArray().size() - 1;
//                    lineEnd(line, SCOPE_OPENING);
//                    break;
//                case VARIABLE_ASSIGNMENT:
//                    assignVariable(splitString);
//                    lineEnd(line, END_STATEMENT);
//                    break;
//                case NOTE:
//                case EMPTY_LINE:
//                    break;
//                default:
//                    // todo exception handling
//            }
//
//        }
//        return globalScope;

//    }
    // todo assigning value to a value;
//    private void assignVariable(String[] assignLine) {
//

//    }
//    private Scope createInnerScope(List<String> scopeText, int index) {
//        int catalan = CATALAN;
//        ArrayList<String> innerScope = new ArrayList<>();
//        innerScope.add(scopeText.get(index));
//        while (catalan != OUTER_SCOPE) {
//            index++;
//            String[] line = scopeText.get(index).split(COMMA);
//            String lineDefiner = lineDefining(line[FIRST_LINE]);
//            if (lineDefiner.equals(METHOD_DECLARE) || lineDefiner.equals(IF_WHILE_BLOCK)) {
//                catalan++;
//            }
//            innerScope.add(scopeText.get(index));
//            if (lineDefiner.equals(SCOPE_CLOSING)) {
//                catalan--;
//            }
//        }
//        return new Scope(curScope, innerScope);

//    }

    // todo method that takes a line of if\while deceleration and checks it
//    private Scope parseCondition(String[] varLine, int index) {
//        // line phrasing
//        // should we add to a method "title" field?
//        Scope innerScope = createInnerScope(curScope.getTextArray(), index);
//        return innerScope;
//

//    }
    // todo method that takes a line of method deceleration and turns it into method
//    private Scope parseMethod(String[] varLine, int index) {
//
//        // line phrasing
//        // should we add to a method "title" field?
//        Scope innerScope = createInnerScope(curScope.getTextArray(), index);

//        return innerScope;
//    }
//
//    // todo method that takes a line of var deceleration and turns it into varibales
//    private void parseVar(String[] varLine) {

//    }



//    private String notePatteredCreator() {
//        int outerScope = OUTER_SCOPE;
//        Scope father = curScope.getFather();
//        while (father != null) {
//            outerScope++;
//            father = father.getFather();
//        }
//        String indentation = INDENTATION;
//        indentation = new String(new char[outerScope]).replace("\0", indentation);
//        return NO_CHAR_BEFORE + indentation + Note;
//    }
}

