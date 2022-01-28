package wniemiec.mobilang.asc.coder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wniemiec.mobilang.asc.framework.FrameworkCoderFactory;
import wniemiec.mobilang.asc.framework.FrameworkCoreCoder;
import wniemiec.mobilang.asc.framework.FrameworkScreensCoder;
import wniemiec.mobilang.asc.models.PersistenceData;
import wniemiec.mobilang.asc.models.ScreenData;

public class MobilangCoder {
    
    private FrameworkCoderFactory frameworkCoderFactory;
    private PersistenceData persistenceData;

    // key: filename; value: code
    private Map<String, List<String>> screensCode;
    private Map<String, List<String>> persistenceCode;
    private Map<String, List<String>> coreCode;
    private List<ScreenData> screensData;

    public MobilangCoder(
        PersistenceData persistenceData, 
        List<ScreenData> screensData,
        FrameworkCoderFactory frameworkCoderFactory
    ) {
        this.persistenceData = persistenceData;
        this.screensData = screensData;
        this.frameworkCoderFactory = frameworkCoderFactory;
        screensCode = new HashMap<>();
        persistenceCode = new HashMap<>();
        coreCode = new HashMap<>();
    }

    public void generateCode() {
        generateCodeForScreens();
        generateCodeForCore();
        generateCodeForPersistence();
    }

    private void generateCodeForPersistence() {
    }

    private void generateCodeForCore() {
        FrameworkCoreCoder frameworkCoreCoder = frameworkCoderFactory.getCoreCoder(screensCode.keySet());

        coreCode = frameworkCoreCoder.generateCode();        
    }

    private void generateCodeForScreens() {
        FrameworkScreensCoder frameworkScreensCoder = frameworkCoderFactory.getScreensCoder(screensData);

        screensCode = frameworkScreensCoder.generateCode();

        //for (ScreenData screenData : screensData) {
        //    generateCodeForScreen(screenData);
        //    break; // TMP
        //}
    }

    /*private void generateCodeForScreen(ScreenData screenData) {
        FrameworkScreenCoder frameworkScreenCoder = frameworkCoderFactory.getScreenCoder(screenData);

        List<String> code = frameworkScreenCoder.generateCode();
        screensCode.put("src/screens/" + screenData.getName(), code);

        /*System.out.println("\n\n----- CODE FOR SCREEN " + screenData.getName() + " ----");
        for (String line : code) {
            System.out.println(line);
        }
    }
    */

    public Map<String, List<String>> getScreensCode() {
        return screensCode;
    }

    public Map<String, List<String>> getPersistenceCode() {
        return persistenceCode;
    }

    public Map<String, List<String>> getCoreCode() {
        return coreCode;
    }
}
