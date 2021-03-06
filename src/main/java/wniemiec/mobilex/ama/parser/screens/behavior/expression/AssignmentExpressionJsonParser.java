package wniemiec.mobilex.ama.parser.screens.behavior.expression;

import org.json.JSONException;
import org.json.JSONObject;
import wniemiec.mobilex.ama.models.behavior.AssignmentExpression;
import wniemiec.mobilex.ama.models.behavior.Expression;
import wniemiec.mobilex.ama.parser.exception.ParseException;


/**
 * Responsible for parsing assignment expressions from behavior node from 
 * MobiLang AST.
 */
class AssignmentExpressionJsonParser implements ExpressionJsonParser {

    //-------------------------------------------------------------------------
    //		Attributes
    //-------------------------------------------------------------------------
    private static AssignmentExpressionJsonParser instance;
    private final ExpressionParser expressionParser;


    //-------------------------------------------------------------------------
    //		Constructor
    //-------------------------------------------------------------------------
    private AssignmentExpressionJsonParser() {
        expressionParser = ExpressionParser.getInstance();
    }


    //-------------------------------------------------------------------------
    //		Factory
    //-------------------------------------------------------------------------
    public static AssignmentExpressionJsonParser getInstance() {
        if (instance == null) {
            instance = new AssignmentExpressionJsonParser();
        }

        return instance;
    }
    

    //-------------------------------------------------------------------------
    //		Methods
    //-------------------------------------------------------------------------
    @Override
    public Expression parse(JSONObject jsonObject) throws JSONException, ParseException {
        return new AssignmentExpression(
            jsonObject.getString("operator"),
            expressionParser.parse(jsonObject.getJSONObject("left")),
            expressionParser.parse(jsonObject.getJSONObject("right"))
        );
    }
}
