package agilor.distributed.relational.data.exceptions;

/**
 * Created by LQ on 2015/12/23.
 */
public class ValidateParameterException extends Exception {
    private String name;
    private ExceptionTypes error;


    public ValidateParameterException(String name,ExceptionTypes error) {
        this.name = name;
        this.error = error;
    }


    public String getName() {
        return name;
    }

    public ExceptionTypes getError() {
        return error;
    }

    @Override
    public String getMessage() {
        return "the parameter of " + name + " validate failed";
    }
}
