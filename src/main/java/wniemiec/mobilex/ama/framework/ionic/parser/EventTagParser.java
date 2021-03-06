package wniemiec.mobilex.ama.framework.ionic.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wniemiec.data.java.Encryptor;
import wniemiec.data.java.Encryptors;
import wniemiec.mobilex.ama.models.EventTag;
import wniemiec.mobilex.ama.models.tag.Tag;


class EventTagParser {

    //-------------------------------------------------------------------------
    //		Attributes
    //-------------------------------------------------------------------------
    private static final String ATTRIBUTE_ONCLICK;
    private final List<EventTag> events;
    private final IonicMobilangDirectiveParser directiveParser;
    private Tag parsedTag;
    private Encryptor encryptor;


    //-------------------------------------------------------------------------
    //		Initialization block
    //-------------------------------------------------------------------------
    static {
        ATTRIBUTE_ONCLICK = "onclick";
    }


    //-------------------------------------------------------------------------
    //		Constructor
    //-------------------------------------------------------------------------
    public EventTagParser() {
        directiveParser = new IonicMobilangDirectiveParser();
        events = new ArrayList<>();
        parsedTag = Tag.getEmptyInstance();
        encryptor = Encryptors.md5();

    }
    

    //-------------------------------------------------------------------------
    //		Methods
    //-------------------------------------------------------------------------
    public void parse(Tag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag cannot be null");
        }

        parsedTag = tag.clone();
        
        if (tag.hasAttribute(ATTRIBUTE_ONCLICK)) {
            parseTagWithOnClick(parsedTag);
        }
    }

    private void parseTagWithOnClick(Tag tag) {
        String id;
        
        if (tag.hasAttribute("id")) {
            id = tag.getAttribute("id");
        }
        else {
            id = generateUniqueIdentifier();
            tag.addAttribute("id", id);
        }
        
        String parsedValue = directiveParser.parse(tag.getAttribute(ATTRIBUTE_ONCLICK));
        EventTag event = new EventTag(id, ATTRIBUTE_ONCLICK, parsedValue);
        events.add(event);
        //events.put(id, "onclick = () => " + tag.getAttribute(ATTRIBUTE_ONCLICK));

        tag.removeAttribute(ATTRIBUTE_ONCLICK);
    }

    private String generateUniqueIdentifier() {
        return ("_" + encryptor.encrypt(String.valueOf(generateRandomNumber())));
    }
	
	private double generateRandomNumber() {
		return (new Date().getTime() + (Math.random() * 9999 + 1));
	}


    //-------------------------------------------------------------------------
    //		Getters
    //-------------------------------------------------------------------------
    public List<EventTag> getEvents() {
        return events;
    }

    public Tag getParsedTag() {
        return parsedTag;
    }
}
