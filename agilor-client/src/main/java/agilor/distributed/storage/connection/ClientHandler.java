package agilor.distributed.storage.connection;

import agilor.distributed.storage.msg.BaseMsg;
import agilor.distributed.storage.msg.LoginMsg;
import agilor.distributed.storage.msg.MsgTest;
import agilor.distributed.storage.msg.PingMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

/**
 * Created by LQ on 2015/8/10.
 */
public class ClientHandler extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object baseMsg) throws Exception {
        ByteBuf buf = (ByteBuf) baseMsg;
        byte[] b = new byte[buf.readableBytes()];

        System.out.println(new String(b));

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                    PingMsg msg = new PingMsg();
                    ctx.writeAndFlush(msg);
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("active...");
        ctx.write(new MsgTest());
        ctx.flush();
        //ctx.write(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
        //ctx.write("abcd");
        //ctx.flush();
        //ctx.channel().write("abcd");

        //ctx.channel().flush();
    }


}
