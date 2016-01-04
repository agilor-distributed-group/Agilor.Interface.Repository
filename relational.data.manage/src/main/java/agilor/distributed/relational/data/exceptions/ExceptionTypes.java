package agilor.distributed.relational.data.exceptions;

/**
 * Created by LQ on 2015/12/23.
 */
public enum ExceptionTypes {

    NULL(0,"no exception"),
    SPECIALCHAR(-1, "has special chars"),
    STRTOOSHORT(-2,"the string is too short"),
    STRTOOLONG(-3,"the string is too long"),
    MODELRROR(-4," this is a error model"),
    FILED_IS_NULL(-5,"the file can't is empty "),



    SQL_ONLY_UNIQUE(-1000,"sql only unique error"),
    SQL_ALREADY_EXIST(-1001,"data is exist"),
    SQL_NOT_EXIST(-1002,"data don't exist"),
    SQL_FOREIGN_KEY(-1003,"foreigen key error");


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
