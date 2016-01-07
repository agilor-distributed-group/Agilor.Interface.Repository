package agilor.distributed.relational.data.context;



/**
 * Created by LQ on 2015/12/25.
 */
public interface IConnection {
    void addResponseData(String key,Object value);

    String getHost();
    int getPort();

    String attr(String key);

}
