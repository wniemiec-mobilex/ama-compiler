package wniemiec.mobilex.ama.coder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import wniemiec.mobilex.ama.coder.exception.CoderException;
import wniemiec.mobilex.ama.export.exception.AppGenerationException;
import wniemiec.mobilex.ama.framework.Framework;
import wniemiec.mobilex.ama.models.CodeFile;
import wniemiec.mobilex.ama.models.Project;
import wniemiec.mobilex.ama.models.Properties;
import wniemiec.mobilex.ama.models.Screen;


class MockFramework implements Framework {

    //-------------------------------------------------------------------------
    //		Attributes
    //-------------------------------------------------------------------------
    private boolean created;
    private Set<String> dependencies;
    private String generatedMobileApplication;


    //-------------------------------------------------------------------------
    //		Constructor
    //-------------------------------------------------------------------------
    MockFramework() {
        created = false;
        dependencies = new HashSet<>();
        generatedMobileApplication = null;
    }


    //-------------------------------------------------------------------------
    //		Methods
    //-------------------------------------------------------------------------
    @Override
    public void createProject(Properties properties, Path location) 
    throws IOException {
        created = true;
    }

    @Override
    public void addProjectDependency(String dependency, Path location) 
    throws IOException {
        dependencies.add(dependency);
    }

    @Override
    public Project generateCode(List<Screen> screens) throws CoderException {
        List<CodeFile> codeFiles = generateCodeFiles(screens);
        
        return new Project(codeFiles, dependencies);
    }

    private List<CodeFile> generateCodeFiles(List<Screen> screens) {
        return screens
            .stream()
            .map(screen -> new CodeFile(screen.getName(), generateScreenCode(screen)))
            .collect(Collectors.toList());
    }

    private List<String> generateScreenCode(Screen screen) {
        List<String> code = new ArrayList<>();

        code.addAll(screen.getStructure().toCode());
        code.addAll(screen.getStyle().toCode());
        code.addAll(screen.getBehavior().toCode());

        return code;
    }

    @Override
    public void generateMobileApplicationFor(String platform, Path source, Path output) 
    throws AppGenerationException {
        generatedMobileApplication = platform;
    }


    //-------------------------------------------------------------------------
    //		Getters
    //-------------------------------------------------------------------------
    public boolean isCreated() {
        return created;
    }

    public Set<String> getDependencies() {
        return dependencies;
    }

    public String getGeneratedMobileApplication() {
        return generatedMobileApplication;
    }
}
