package result;

/**
 * Created by LQ on 2016/1/7.
 */
public class Login extends BaseResult {

    private boolean isOk=false;
    private Object data = null;
    private String info = null;

    protected Login(boolean isOk,Object data,String info) {
        this.isOk = isOk;
        this.data = data;
        this.info = info;
    }
    @Override
    protected boolean isOk() {
        return isOk;
    }

    @Override
    protected Object getData() {
        return data;
    }

    @Override
    protected String getInfo() {
        return info;
    }


    public static Result success() {
        return new Login(true, null, null);
    }

    public static Result failed() {
        return new Login(false, -1, null);
    }






}
