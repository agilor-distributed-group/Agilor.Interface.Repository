package result;

/**
 * Created by LQ on 2016/1/6.
 */
public class ActionResult extends BaseResult {

    private boolean isOk = false;
    private String data = null;
    private String info = null;


    public ActionResult(boolean isOk)
    {

        this(isOk,null,null);
    }

    public ActionResult(boolean isOk,String data, String info)
    {
        this.isOk=isOk;
        this.data=data;
        this.info=info;
    }


    @Override
    public boolean isOk() {
        return false;
    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public String getInfo() {
        return null;
    }
}
