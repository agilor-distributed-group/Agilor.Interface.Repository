package agilor.distributed.web.inter.server.result;

/**
 * Created by LQ on 2016/1/6.
 */
public class Action extends BaseResult {

    private boolean isOk = false;
    private Object data = null;
    private String info = null;


    protected Action(boolean isOk,Object data,String info) {
        this.isOk = isOk;
        this.data = data;
        this.info = info;
    }

    public Action(boolean isOk, String data, String info)
    {
        this.isOk=isOk;
        this.data=data;
        this.info=info;
    }


    @Override
    public boolean isOk() {
        return isOk;
    }

    @Override
    public Object getData()
    {
        return data;
    }

    @Override
    public String getInfo() {
        return info;
    }


    public static Result success() {
        return new Action(true, null, null);
    }

    public static Result success(Object data) {return new Action(true,data,null);}



    public static Result failed() {
        return new Action(false, -1, null);
    }

    public static Result failed(Object data,String info){return new Action(false,data,info);}


    public static Result error(){
        return new Action(false,-2,null);
    }

    public static Result notFound() {
        return new Action(false, -3, null);
    }


    public static Result validate(String info) {
        return new Action(false, -4, info);
    }

    public static Result exist()
    {
        return new Action(false,-5,null);
    }

    public static Result disable(){
        return new Action(false,-1000,null);
    }






}
