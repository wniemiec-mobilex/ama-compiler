package wniemiec.mobilex.ama.coder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wniemiec.mobilex.ama.coder.exception.CoderException;
import wniemiec.mobilex.ama.framework.Framework;
import wniemiec.mobilex.ama.framework.MockFramework;
import wniemiec.mobilex.ama.models.CodeFile;
import wniemiec.mobilex.ama.models.Screen;
import wniemiec.mobilex.ama.models.Style;
import wniemiec.mobilex.ama.models.StyleSheetRule;
import wniemiec.mobilex.ama.models.behavior.Behavior;
import wniemiec.mobilex.ama.models.behavior.Declaration;
import wniemiec.mobilex.ama.models.behavior.Declarator;
import wniemiec.mobilex.ama.models.behavior.Instruction;
import wniemiec.mobilex.ama.models.behavior.Literal;
import wniemiec.mobilex.ama.models.tag.Tag;


class MobilangCoderTest {
    
    //-------------------------------------------------------------------------
    //		Attributes
    //-------------------------------------------------------------------------
    private List<Screen> builtScreens;
    private Framework framework;
    private MobilangCoder coder;
    private List<String> dependencies;
    private String screenName;
    private Tag screenStructure;
    private Style screenStyle;
    private Behavior screenBehavior;
    private List<CodeFile> codeFiles;
    private List<Screen> screens;


    //-------------------------------------------------------------------------
    //		Test hooks
    //-------------------------------------------------------------------------
    @BeforeEach
    void setUp() {
        builtScreens = new ArrayList<>();
        screens = null;
        framework = null;
        dependencies = new ArrayList<>();
        screenName = null;
        screenStructure = null;
        screenStyle = null;
        screenBehavior = null;
        codeFiles = new ArrayList<>();
        coder = null;
    }

    //-------------------------------------------------------------------------
    //		Tests
    //-------------------------------------------------------------------------
    @Test
    void testGenerateCodeWithDependencies() throws CoderException, IOException {
        withFramework(new MockFramework());
        withDependency("foo/bar");
        withScreenName("SomeFunnyScreenName");
        withScreenStructure(buildPInsideADivWithValue("some text"));
        withScreenStyle(buildPStyleUsingBlueAndWhite());
        withScreenBehavior(buildDeclarationWithIdAndAssignment("pi", "3.1416"));
        buildScreen();
        withScreens(builtScreens);
        doCodeGeneration();
        assertDependenciesAreCorrect();
    }

    @Test
    void testGenerateCodeWithoutDependencies() throws CoderException {
        withFramework(new MockFramework());
        withScreenName("SomeFunnyScreenName");
        withScreenStructure(buildPInsideADivWithValue("some text"));
        withScreenStyle(buildPStyleUsingBlueAndWhite());
        withScreenBehavior(buildDeclarationWithIdAndAssignment("pi", "3.1416"));
        buildScreen();
        withScreens(builtScreens);
        doCodeGeneration();
        assertHasCorrectCodeFiles();
    }

    @Test
    void testGenerateCodeWithoutFramework() throws CoderException {
        withFramework(null);
        withScreenName("SomeFunnyScreenName");
        withScreenStructure(buildPInsideADivWithValue("some text"));
        withScreenStyle(buildPStyleUsingBlueAndWhite());
        withScreenBehavior(buildDeclarationWithIdAndAssignment("pi", "3.1416"));
        buildScreen();
        withScreens(builtScreens);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            doCodeGeneration();
        });
    }

    @Test
    void testGenerateCodeWithoutScreens() throws CoderException {
        withFramework(new MockFramework());
        withScreens(null);
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            doCodeGeneration();
        });
    }

    @Test
    void testGenerateCodeWithEmptyScreens() throws CoderException {
        withFramework(new MockFramework());
        withScreens(new ArrayList<>());
        doCodeGeneration();
        assertHasEmptyCodeFiles();
    }


    //-------------------------------------------------------------------------
    //		Methods
    //-------------------------------------------------------------------------
    private void withFramework(Framework framework) {
        this.framework = framework;
    }
    
    private void withDependency(String dependency) throws IOException {
        dependencies.add(dependency);
        framework.addProjectDependency(dependency, null);
    }

    private void withScreenName(String name) {
        this.screenName = name;
    }

    private Tag buildPInsideADivWithValue(String value) {
        Tag divTag = Tag.getNormalInstance("div");
        Tag pTag = Tag.getNormalInstance("p");

        divTag.addChild(pTag);
        pTag.setValue(value);

        return divTag;
    }

    private void withScreenStructure(Tag structure) {
        screenStructure = structure;
    }

    private Style buildPStyleUsingBlueAndWhite() {
        Style style = new Style();
        StyleSheetRule rule = new StyleSheetRule();

        rule.addSelector("p");
        rule.addDeclaration("background-color", "blue");
        rule.addDeclaration("color", "white");
        style.addRule(rule);

        return style;
    }

    private void withScreenStyle(Style style) {
        screenStyle = style;
    }

    private Behavior buildDeclarationWithIdAndAssignment(String id, String assignment) {
        Declaration declaration = buildLiteralDeclaration(id, assignment);

        return buildBehaviorWith(declaration);
    }

    private Declaration buildLiteralDeclaration(String id, String assignment) {
        Declarator declarator = new Declarator(
            "string", 
            "let", 
            id, 
            Literal.ofString(assignment)
        );
        
        return new Declaration("let", declarator);
    }

    private Behavior buildBehaviorWith(Instruction... declarations) {
        return new Behavior(Arrays.asList(declarations));
    }

    private void withScreenBehavior(Behavior behavior) {
        screenBehavior = behavior;
    }

    private void buildScreen() {
        Screen screen = new Screen.Builder()
            .name(screenName)
            .structure(screenStructure)
            .style(screenStyle)
            .behavior(screenBehavior)
            .build();
        
        builtScreens.add(screen);
        codeFiles.add(new CodeFile(screenName, generateScreenCode()));
    }

    private List<String> generateScreenCode() {
        List<String> code = new ArrayList<>();

        code.addAll(screenStructure.toCode());
        code.addAll(screenStyle.toCode());
        code.addAll(screenBehavior.toCode());

        return code;
    }

    private void withScreens(List<Screen> screens) {
        this.screens = screens;
    }

    private void doCodeGeneration() throws CoderException {
        coder = new MobilangCoder(screens, framework);

        coder.generateCode();
    }

    private void assertHasCorrectCodeFiles() {
        Assertions.assertEquals(codeFiles, coder.getCodeFiles());
    }

    private void assertDependenciesAreCorrect() {
        Assertions.assertTrue(coder.getDependencies().containsAll(dependencies));
    }

    private void assertHasEmptyCodeFiles() {
        Assertions.assertTrue(coder.getCodeFiles().isEmpty());
    }
}
