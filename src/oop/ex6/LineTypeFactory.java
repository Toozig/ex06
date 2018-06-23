package oop.ex6;

import java.util.ArrayList;

/**
 * Type factory object classifies the different interpretation according to the line type
 */
public enum LineTypeFactory {
    Variable{
        protected MScope interpret(MScope MScope, String command) throws ParsingException {
            ArrayList<Variables> varArr = parser.parseVar(command, MScope);
            for (Variables var: varArr) {
                MScope.addVariable(var);
            }
            return MScope;
        }
    },
    MethodDeclare{
        protected MScope interpret(MScope MScope, String command) throws ParsingException {
            Method innerScope = parser.parseMethodDeceleration(command, MScope);
            MScope.addToMethodArr(innerScope);
            return innerScope;
            }

    },
    VariableAssignment{
        protected MScope interpret(MScope MScope, String command) throws ParsingException {
            parser.assignVar(command, MScope);
            return MScope;
        }

    },
    IfWhileBlock{
        protected MScope interpret(MScope MScope, String command) throws ParsingException {
            return parser.ParseIfWhile(command, MScope);

        }

    },
    MethodCall{
        protected MScope interpret(MScope MScope, String command) throws ParsingException {

            Method calledMethod = parser.parseMethodCall(MScope, command);
            calledMethod.runMethod(MScope);
            return MScope;
        }

    },
    lineError{
        protected MScope interpret(MScope MScope, String command) throws ParsingException {
            throw new ParsingException(INVALID_LINE_FORMAT);
        }


    },
    ScopeClosing{
        protected MScope interpret(MScope MScope, String command) throws ParsingException {
            MScope father = MScope.getFather();
            if(father == null){
                throw new ParsingException(OUTER_SCOPE_CLOSE);
            }
            return MScope.getFather();
        }
    },
    Return {
        protected MScope interpret(MScope MScope, String command) {
            return MScope;
        }
    },
    Note{
        protected MScope interpret(MScope MScope, String command) {
            return MScope;
        }
    },
    Emptyline{
        protected MScope interpret(MScope MScope, String command) {
            return MScope;
        }
    };

    public static final String OUTER_SCOPE_CLOSE = "Outer scope shouldn't be close";
    public static final String INVALID_LINE_FORMAT = "Invalid line format";

    /**
     * Interprets a line according to it's type
     * @param MScope the scope in which the line is
     * @param command the command
     * @return the updated scope after the interpretation
     * @throws ParsingException thrown if the line was illegal
     */
    abstract protected MScope interpret(MScope MScope, String command) throws ParsingException;
    protected Parser parser = Parser.getParser();
    
}
