package agilor.distributed.storage.gate.handler;

import agilor.distributed.storage.gate.ChannelContext;
import agilor.distributed.storage.gate.ChannelPool;
import agilor.distributed.storage.gate.msg.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by LQ on 2015/8/10.
 */
public class InServerHandler extends SimpleChannelInboundHandler<BaseMsg> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseMsg msg) throws Exception {
        switch (msg.getType()) {
            case LOGIN:
                LOGIN(channelHandlerContext, (LoginMsg) msg);
                break;
            case PING:
                PING((PingMsg) msg);
                break;
            case ASK:
                ASK(channelHandlerContext, (AskMsg) msg);
                break;
            default:
                break;
        }
    }


    private void LOGIN(ChannelHandlerContext context, LoginMsg msg) {
        System.out.println(8884);

        String id = msg.getClientId();
        if (!ChannelPool.exist(id)) {
            id = ChannelPool.insert(context.channel());
        }
        context.writeAndFlush(id);
    }

    private void PING(PingMsg msg) {
        String id = msg.getClientId();
        ChannelContext context = ChannelPool.getChanel(id);
        if (context != null) {
            context.ping();
        }
    }

    private void ASK(ChannelHandlerContext context,AskMsg msg)
    {
        String id = msg.getClientId();
        ChannelContext c = ChannelPool.getChanel(id);
        c.reset();

        //处理ASK

    }
}
