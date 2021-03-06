package wniemiec.mobilex.ama.framework.reactnative;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import wniemiec.io.java.Consolex;
import wniemiec.io.java.StandardTerminalBuilder;
import wniemiec.io.java.Terminal;
import wniemiec.mobilex.ama.coder.exception.CoderException;
import wniemiec.mobilex.ama.export.exception.AppGenerationException;
import wniemiec.mobilex.ama.framework.Framework;
import wniemiec.mobilex.ama.framework.reactnative.app.ReactNativeAppGenerator;
import wniemiec.mobilex.ama.framework.reactnative.coder.ReactNativeCoreCoder;
import wniemiec.mobilex.ama.framework.reactnative.coder.ReactNativeScreensCoder;
import wniemiec.mobilex.ama.models.CodeFile;
import wniemiec.mobilex.ama.models.Project;
import wniemiec.mobilex.ama.models.Properties;
import wniemiec.mobilex.ama.models.Screen;
import wniemiec.mobilex.ama.util.data.Validator;
import wniemiec.mobilex.ama.util.io.FileManager;
import wniemiec.mobilex.ama.util.io.StandardFileManager;


/**
 * Responsible for managing React Native framework.
 * 
 * See: https://reactnative.dev
 */
public class ReactNativeFramework implements Framework {

    //-------------------------------------------------------------------------
    //		Attributes
    //-------------------------------------------------------------------------
    private final ReactNativeProjectManager projectManager;
    private final Terminal terminal;
    private final FileManager fileManager;


    //-------------------------------------------------------------------------
    //		Constructor
    //-------------------------------------------------------------------------
    public ReactNativeFramework() {
        terminal = buildStandardTerminal();
        fileManager = new StandardFileManager();
        projectManager = new ReactNativeProjectManager(terminal, fileManager);
    }

    public ReactNativeFramework(Terminal terminal, FileManager fileManager) {
        Validator.validateTerminal(terminal);
        Validator.validateFileManager(fileManager);

        projectManager = new ReactNativeProjectManager(terminal, fileManager);
        this.terminal = terminal;
        this.fileManager = fileManager;
    }


    //-------------------------------------------------------------------------
    //		Methods
    //-------------------------------------------------------------------------
    private Terminal buildStandardTerminal() {
        return StandardTerminalBuilder
            .getInstance()
            .outputHandler(Consolex::writeDebug)
            .outputErrorHandler(Consolex::writeDebug)
            .build();
    }
    
    @Override
    public void createProject(Properties properties, Path location) 
    throws IOException {
        Validator.validateProperties(properties);
        Validator.validateLocation(location);

        projectManager.createProject(properties, location);
    }

    @Override
    public void addProjectDependency(String dependency, Path location) 
    throws IOException {
        Validator.validateDependency(dependency);
        Validator.validateLocation(location);

        projectManager.addProjectDependency(dependency, location);
    }

    @Override
    public Project generateCode(List<Screen> screens) throws CoderException {
        Validator.validateScreens(screens);

        List<CodeFile> code = new ArrayList<>();
        Set<String> dependencies = new HashSet<>();
        
        generateScreensCode(code, screens);
        generateCoreCode(code, dependencies);

        return new Project(code, dependencies);
    }
    
    private void generateScreensCode(List<CodeFile> code, List<Screen> screens) 
    throws CoderException {
        ReactNativeScreensCoder screensCoder = new ReactNativeScreensCoder(screens);

        code.addAll(screensCoder.generateCode());
    }

    private void generateCoreCode(List<CodeFile> code, Set<String> dependencies) {
        ReactNativeCoreCoder coreCoder = new ReactNativeCoreCoder();

        code.addAll(coreCoder.generateCode());
        dependencies.addAll(coreCoder.getDependencies());
    }

    @Override
    public void generateMobileApplicationFor(String platform, Path source, Path output) 
    throws AppGenerationException {
        Validator.validatePlatform(platform);
        Validator.validateSource(source);
        Validator.validateOutput(output);

        ReactNativeAppGenerator appGenerator = new ReactNativeAppGenerator(
            source, 
            output, 
            terminal, 
            fileManager
        );

        appGenerator.generateMobileApplicationFor(platform);
    }
}
