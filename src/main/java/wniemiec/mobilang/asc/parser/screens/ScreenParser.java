package wniemiec.mobilang.asc.parser.screens;

import java.util.List;
import java.util.SortedMap;

import wniemiec.mobilang.asc.framework.FrameworkParserFactory;
import wniemiec.mobilang.asc.framework.FrameworkScreenParser;
import wniemiec.mobilang.asc.models.Node;
import wniemiec.mobilang.asc.models.ScreenData;
import wniemiec.mobilang.asc.models.StyleSheet;
import wniemiec.mobilang.asc.models.Tag;
import wniemiec.mobilang.asc.parser.Parser;
import wniemiec.mobilang.asc.parser.exception.ParseException;
import wniemiec.mobilang.asc.parser.screens.behavior.Behavior;
import wniemiec.mobilang.asc.parser.screens.behavior.BehaviorParser;
import wniemiec.mobilang.asc.parser.screens.structure.StructureParser;
import wniemiec.mobilang.asc.parser.screens.style.StyleParser;

public class ScreenParser implements Parser {

    private SortedMap<String, List<Node>> tree;
    private String id;
    private Node structureNode;
    private Node styleNode;
    private Node behaviorNode;
    private StructureParser structureParser;
    private StyleParser styleParser;
    private BehaviorParser behaviorParser;
    private FrameworkScreenParser frameworkParser;
    private FrameworkParserFactory frameworkParserFactory;
    private ScreenData screenData;

    public ScreenParser(SortedMap<String, List<Node>> tree, Node screenNode, FrameworkParserFactory frameworkParserFactory) {
        this.tree = tree;
        this.frameworkParserFactory = frameworkParserFactory;
        id = screenNode.getAttribute("id");
        id = normalize(id) + "Screen";
        
        
        for (Node node : tree.get(screenNode.getId())) {
            if (node.getLabel().contains("structure")) {
                structureNode = node;
            }
            else if (node.getLabel().contains("style")) {
                styleNode = node;
            }
            else if (node.getLabel().contains("behavior")) {
                behaviorNode = node;
            }
        }
    }

    private String normalize(String str) {
        String normalizedString = capitalize(str);

        normalizedString = str.replaceAll("[\\-\\_]+", "");
        normalizedString = capitalize(normalizedString);

        return normalizedString;
    }

    private String capitalize(String str) {
        char firstChar = Character.toUpperCase(str.charAt(0));
        
        return firstChar + str.substring(1).toLowerCase();
    }

    @Override
    public void parse() throws ParseException {
        //System.out.println("-----< SCREEN PARSER >-----");
        
        //System.out.println("Screen id: " + id);
        
        structureParser = new StructureParser(tree, structureNode);
        styleParser = new StyleParser(tree, styleNode);
        behaviorParser = new BehaviorParser(tree, behaviorNode);

        Tag structure = structureParser.parse();
        StyleSheet style = styleParser.parse();
        Behavior behavior = behaviorParser.parse();

        
        frameworkParser = frameworkParserFactory.getScreenParser(
            id,
            structure,
            style,
            behavior
        );
        
        frameworkParser.parse();

        this.screenData = frameworkParser.getScreenData();
        
        //System.out.println("-------------------------------\n");
    }

   
    public ScreenData getScreenData() {
        return screenData;
    }

}
