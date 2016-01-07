package result;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * Created by LQ on 2016/1/7.
 */
public class Data extends BaseResult {

    private boolean isOk=false;
    private Object data = null;
    private String info = null;

    protected Data(boolean isOk,Object data,String info) {
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
        return new Data(true, null, null);
    }

    public static Result failed() {
        return new Data(false, -2, null);
    }


    public static Result notFound() {
        return new Data(false, -3, null);
    }


    public static Result validate(String info) {
        return new Data(false, -4, info);
    }


    public static Result error(){
        return new Data(false,-5,null);
    }
}
