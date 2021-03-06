package wniemiec.mobilex.ama.framework.ionic.app;

import java.nio.file.Path;
import wniemiec.io.java.Terminal;
import wniemiec.mobilex.ama.export.exception.AppGenerationException;
import wniemiec.mobilex.ama.util.io.FileManager;


/**
 * Responsible for generating mobile applications through Ionic framework.
 * 
 * Commands:
 *     npm install @capacitor/core
 *     ionic build
 *     ionic capacitor add android
 *     ionic capacitor build android
 */
public class IonicAppGenerator {

    //-------------------------------------------------------------------------
    //		Attributes
    //-------------------------------------------------------------------------
    private final Path sourceCode;
    private final Path mobileOutput;
    private Terminal terminal;
    private FileManager fileManager;


    //-------------------------------------------------------------------------
    //		Constructor
    //-------------------------------------------------------------------------
    public IonicAppGenerator(
        Path sourceCode, 
        Path output, 
        Terminal terminal, 
        FileManager fileManager
    ) {
        mobileOutput = output.resolve("mobile");
        this.sourceCode = sourceCode;
        this.terminal = terminal;
        this.fileManager = fileManager;
    }


    //-------------------------------------------------------------------------
    //		Methods
    //-------------------------------------------------------------------------
    public Path generateMobileApplicationFor(String platform) 
    throws AppGenerationException {
        String normalizedPlatform = platform.toLowerCase();

        if (normalizedPlatform.equals("android")) {
            generateAndroidApp();
        }
        else if (normalizedPlatform.contains("ios")) {
            generateIosApp();
        }
        else {
            throw new AppGenerationException("There is no compatibility with " +
                                             "this mobile platform: " + platform);
        }

        return mobileOutput;
    }

    private void generateAndroidApp() throws AppGenerationException {
        AndroidAppGenerator appGenerator = new AndroidAppGenerator(
            sourceCode, 
            mobileOutput,
            terminal,
            fileManager
        );

        appGenerator.generateApp();
    }

    private void generateIosApp() throws AppGenerationException {
        IosAppGenerator appGenerator = new IosAppGenerator(
            sourceCode, 
            mobileOutput,
            terminal,
            fileManager
        );
        appGenerator.generateApp();
    }


    //-------------------------------------------------------------------------
    //		Setters
    //-------------------------------------------------------------------------
    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }
}