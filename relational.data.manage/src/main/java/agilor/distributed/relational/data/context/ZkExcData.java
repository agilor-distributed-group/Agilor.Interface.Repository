package agilor.distributed.relational.data.context;

/**
 * Created by LQ on 2015/12/25.
 */
public class ZkExcData {

    public final static int UPDATE = 0;
    public final static int DELETE=1;


    private int action=-1;

    private String path = null;
    private SessionMetaData data=null;



    public ZkExcData(int action,String path, SessionMetaData data)
    {
        this.action=action;
        this.path=path;
        this.data=data;
    }


    public int getAction() {
        return action;
    }

    public String getPath() {
        return path;
    }

    public SessionMetaData getData() {
        return data;
    }
}
