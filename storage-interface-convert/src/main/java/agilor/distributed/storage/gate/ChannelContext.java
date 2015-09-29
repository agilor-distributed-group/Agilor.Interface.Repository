package agilor.distributed.storage.gate;

import com.sun.org.apache.bcel.internal.generic.RET;
import io.netty.channel.Channel;

/**
 * Created by LQ on 2015/8/10.
 */
public class ChannelContext {
    private Channel channel = null;
    private int p=0;
    private long timestamps=-1;

    /**
     *配置
     */

    private static int maxPing=10;
    private static long timeOut=10000;


    public ChannelContext(Channel channel) {
        this.channel = channel;
    }

    public void ping() {
        p++;
        if ((timeOut != 0 && (System.currentTimeMillis() - timestamps > timeOut)) || p > maxPing) {
            this.close();
            return;
        }
        timestamps = System.currentTimeMillis();
    }

    public void close() {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
    }

    public void reset() {
        p = 0;
        timeOut = System.currentTimeMillis();
    }

    public boolean isOpen() {
        return channel != null && channel.isOpen();
    }

    public Channel getChannel() {
        return channel;
    }




}
