package agilor.distributed.relational.data.exceptions;

/**
 * Created by LQ on 2015/12/23.
 */
public enum ExceptionTypes {

    SPECIALCHAR(-1, "has special chars"),
    STRTOOSHORT(-2,"the string is too short"),
    STRTOOLONG(-3,"the string is too long"),
    MODELRROR(-4," this is a error model");


    private int code = 0;
    private String description = null;

    ExceptionTypes(int code, String description) {
        this.code = code;
        this.description = description;
    }


    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
