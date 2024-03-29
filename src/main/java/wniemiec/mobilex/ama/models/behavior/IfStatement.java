package wniemiec.mobilex.ama.models.behavior;


/**
 * Responsible for representing an if statement from behavior code.
 */
public class IfStatement implements Instruction {

    //-------------------------------------------------------------------------
    //		Attributes
    //-------------------------------------------------------------------------
    private final Expression test;
    private final Instruction body;


    //-------------------------------------------------------------------------
    //		Constructor
    //-------------------------------------------------------------------------
    public IfStatement(Expression test, Instruction body) {
        this.test = test;
        this.body = body;
    }


    //-------------------------------------------------------------------------
    //		Methods
    //-------------------------------------------------------------------------
    @Override
    public String toCode() {
        StringBuilder code = new StringBuilder();

        code.append("if (");
        code.append(test.toCode());
        code.append(") ");

        if (body == null) {
            code.append("{ }");
        }
        else {
            code.append(body.toCode());
        }

        return code.toString();
    }
}
