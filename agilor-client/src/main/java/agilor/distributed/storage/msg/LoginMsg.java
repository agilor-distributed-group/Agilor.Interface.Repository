package agilor.distributed.storage.msg;

import java.io.Serializable;

/**
 * Created by LQ on 2015/8/10.
 */
public class LoginMsg extends BaseMsg implements Serializable {

    private static final long serialVersionUID = -6345055893283175206L;
    private int a =1;


    public LoginMsg()
    {
        this.type=MsgType.LOGIN;
    }


    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }
}
