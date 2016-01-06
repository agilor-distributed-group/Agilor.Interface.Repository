package agilor.distributed.relational.data.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LQ on 2016/1/5.
 */
public class TestConnection implements IConnection {

    private Map<String,Object> attrs = new HashMap<>();

    @Override
    public void addResponseData(String key, Object value) {
        attrs.put(key,value);
    }

    @Override
    public String getHost() {
        return "192.168.1.125";
    }

    @Override
    public int getPort() {
        return 300;
    }

    @Override
    public String attr(String key) {
//        if(key == "DISTRIBUTED_SESSION_FINAL")
//            return
        Object val = attrs.get(key);

        return val==null?null:val.toString();

    }
}
