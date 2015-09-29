package agilor.distributed.storage.msg;

import java.io.Serializable;

/**
 * Created by LQ on 2015/8/11.
 */
public class MsgTest implements Serializable {

    private static final long serialVersionUID = -699813881551303785L;
    private String a="a";
    private String b ="b";

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }
}
