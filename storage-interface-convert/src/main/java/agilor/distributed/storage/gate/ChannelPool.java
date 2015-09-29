package agilor.distributed.storage.gate;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



/**
 * Created by LQ on 2015/8/10.
 */
public class ChannelPool {
    private static Long index=0l;
    private static final Logger logger = LoggerFactory.getLogger(ChannelPool.class);


    private static Map<String,ChannelContext> map = new ConcurrentHashMap<>();



    public static ChannelContext getChanel(String id) {
        return map.get(id);
    }

    public synchronized static String insert(Channel channel) {
        String id = buildId();
        map.put(id, new ChannelContext(channel));

        logger.info("insert channel,channel id is {},the count equals {}",id,map.size());

        return id;
    }

    public synchronized static void remove(String id)
    {
        map.remove(id);
        logger.info("remove channel,channel id is {},the count equals {}", id, map.size());
    }

    public static boolean exist(String id) {
        return map.containsKey(id);
    }

    private synchronized static String buildId() {
        logger.info("build id,channel id is {},the count equals {}", index + 1, map.size());
        return (index++).toString();
    }










}
