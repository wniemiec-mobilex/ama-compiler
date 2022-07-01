package wniemiec.mobilex.ama.framework.ionic.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import wniemiec.io.java.BabelTranspiler;
import wniemiec.mobilex.ama.coder.exception.CoderException;
import wniemiec.mobilex.ama.models.behavior.Behavior;


public class IonicBehaviorParser {

    //-------------------------------------------------------------------------
    //		Attributes
    //-------------------------------------------------------------------------
    private final BabelTranspiler babelTranspiler;
    private final IonicMobilangDirectiveParser directiveParser;
    private final FunctionParser functionConverter;
    private final StyleParser styleParser;
    private final EventBehaviorParser eventParser;
    private List<String> parsedCode;
    private List<String> babelErrorLog;


    //-------------------------------------------------------------------------
    //		Constructor
    //-------------------------------------------------------------------------
    public IonicBehaviorParser() {
        babelErrorLog = new ArrayList<>();
        parsedCode = new ArrayList<>();
        babelTranspiler = new BabelTranspiler(babelErrorLog::add);
        directiveParser = new IonicMobilangDirectiveParser();
        functionConverter = new FunctionParser();
        styleParser = new StyleParser();
        eventParser = new EventBehaviorParser();
    }


    //-------------------------------------------------------------------------
    //		Methods
    //-------------------------------------------------------------------------
    public void parse(Behavior behavior) throws CoderException {
        validateArgument(behavior);
        //runBabel(behavior);
        parsedCode = behavior.toCode();
        //runFunctionProcessor();
        runStyleParser();
        runEventParser();
        runDirectiveParser();
    }

    private void validateArgument(Behavior behavior) {
        if (behavior == null) {
            throw new IllegalArgumentException("Behavior cannot be null");
        }
    }

    private void runBabel(Behavior behavior) throws CoderException {
        parsedCode = runBabelTranspiler(behavior.toCode());

        babelErrorLog = babelErrorLog
            .stream()
            .filter(message -> !message.contains("npm notice"))
            .collect(Collectors.toList());

        if (!babelErrorLog.isEmpty()) {
            throw new CoderException(babelErrorLog);
        }
    }

    private List<String> runBabelTranspiler(List<String> code) 
    throws CoderException {
        try {
            return babelTranspiler.fromCode(code);
        } 
        catch (IOException e) {
            throw new CoderException(e.getMessage());
        }
    }
    
    private void runFunctionProcessor() {  
        functionConverter.parse(parsedCode);
        parsedCode = functionConverter.getParsedCode();
    }

    private void runStyleParser() {  
        styleParser.parse(parsedCode);
        parsedCode = styleParser.getParsedCode();
    }

    private void runEventParser() {  
        eventParser.parse(parsedCode);
        parsedCode = eventParser.getParsedCode();
    }

    private void runDirectiveParser() {
        directiveParser.parse(parsedCode);
        parsedCode = directiveParser.getParsedCode();
    }


    //-------------------------------------------------------------------------
    //		Getters
    //-------------------------------------------------------------------------
    public List<String> getParsedCode() {
        return parsedCode;
    }
}