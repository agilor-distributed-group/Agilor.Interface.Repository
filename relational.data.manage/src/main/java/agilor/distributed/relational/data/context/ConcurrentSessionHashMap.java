package agilor.distributed.relational.data.context;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by LQ on 2016/1/5.
 * 此map是为了以后去掉zookeeper依赖，服务层单独做分布式做准备，此时暂无用处
 */
public class ConcurrentSessionHashMap extends ConcurrentHashMap<String,String> {

    private String address=null;
    private int port = 0;


    public ConcurrentSessionHashMap(String address,int port)
    {
        this.address = address;
        this.port=port;
    }


    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
