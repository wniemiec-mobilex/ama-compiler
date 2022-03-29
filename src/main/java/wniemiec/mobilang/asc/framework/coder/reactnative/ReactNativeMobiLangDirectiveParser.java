package wniemiec.mobilang.asc.framework.coder.reactnative;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wniemiec.util.java.StringUtils;

/**
 * Responsible for parsing MobiLang directives in screen behavior.
 */
class ReactNativeMobiLangDirectiveParser {

    // -------------------------------------------------------------------------
    // Attributes
    // -------------------------------------------------------------------------
    private List<String> parsedLines;

    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------
    public List<String> parse(List<String> lines) {
        parsedLines = new ArrayList<>();

        for (String line : lines) {
            parseLine(line);
        }

        return parsedLines;
    }

    private void parseLine(String line) {
        String parsedLine = line;

        if (isMobiLangDirective(line)) {
            parsedLine = parseMobiLangDirective(line);
        }

        parsedLines.add(parsedLine);
    }

    private boolean isMobiLangDirective(String line) {
        return line.contains("mobilang:");
    }

    private String parseMobiLangDirective(String line) {
        String parsedLine = line;

        if (isScreenDirective(line)) {
            parsedLine = parseScreenDirective(line);
        }
        else if (isParamDirective(line)) {
            parsedLine = parseParamDirective(line);
        }

        return parsedLine;
    }

    private boolean isScreenDirective(String line) {
        return line.contains("mobilang:screen:");
    }

    private String parseScreenDirective(String line) {
        Pattern pattern = Pattern.compile(".+mobilang:screen:([A-z0-9-_]+).+");
        Matcher matcher = pattern.matcher(line);

        if (!matcher.matches()) {
            return line;
        }

        String screenName = matcher.group(1);

        return line.replace("mobilang:screen:" + screenName, screenName + ".html");
    }

    private boolean isParamDirective(String line) {
        return line.contains("mobilang:param:");
    }

    private String parseParamDirective(String line) {
        Pattern pattern = Pattern.compile(".+mobilang:param:([A-z0-9-_]+).+");
        Matcher matcher = pattern.matcher(line);

        if (!matcher.matches()) {
            return line;
        }

        String paramName = matcher.group(1);
        String paramQuery = "window.location.href.split('?')[1].split(\"" + paramName + "=\")[1].split(\"&\")[0]";

        return line.replace("\"mobilang:param:" + paramName + "\"", paramQuery);
    }
}