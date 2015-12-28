package agilor.distributed.relational.data.exceptions;




/**
 * Created by LQ on 2015/12/28.
 */
public class DataNotExistException extends Exception  {

    private String name = null;
    private int id=0;



    public DataNotExistException(String name,int id)
    {
        this.name = name;
        this.id=id;
    }


    @Override
    public String getMessage() {
        return name + "(" + id + ") is not exist";
    }
}
