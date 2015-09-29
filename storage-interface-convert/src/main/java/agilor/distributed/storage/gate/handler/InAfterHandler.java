package agilor.distributed.storage.gate.handler;

import agilor.distributed.storage.gate.msg.LoginMsg;
import agilor.distributed.storage.gate.msg.Msg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by LQ on 2015/8/10.
 */
public class InAfterHandler extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
//        int a =1;
//
//
//        ByteBuf buf = (ByteBuf)msg;
//        byte[] bytes = new byte[buf.readableBytes()];
//
//        buf.readBytes(bytes);
//
        System.out.println(1234);
//
//        ObjectMapper mapper = new ObjectMapper();
//        LoginMsg m = mapper.readValue(bytes, LoginMsg.class);
//        channelHandlerContext.writeAndFlush(102);
    }
}
