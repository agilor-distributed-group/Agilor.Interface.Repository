package agilor.distributed.relational.data.exceptions;

/**
 * Created by LQ on 2015/12/23.
 */
public class NullParameterException extends Exception {
    private String name;

    public NullParameterException(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    @Override
    public String getMessage() {
        return "the parameter of " + name + " is empty";
    }
}
