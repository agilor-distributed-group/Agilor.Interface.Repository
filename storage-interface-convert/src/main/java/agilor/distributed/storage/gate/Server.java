package agilor.distributed.storage.gate;

import agilor.distributed.storage.gate.handler.InAfterHandler;
import agilor.distributed.storage.gate.handler.InServerHandler;
import agilor.distributed.storage.gate.msg.MsgTest;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by LQ on 2015/7/30.
 */
public class Server implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(Server.class);

    @Override
    public void run() {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker);
        bootstrap.channel(NioServerSocketChannel.class);

        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_REUSEADDR, true);

        SocketChannel  cc = null;
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                //pipeline.addLast(new InServerHandler());
                pipeline.addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(MsgTest.class.getClassLoader())));
                pipeline.addLast(new ObjectEncoder());
                pipeline.addLast(new InAfterHandler());
                //cc=socketChannel;
            }
        });

        try {

            logger.info("the server ready to run,port is {}", 9982);
            ChannelFuture future = bootstrap.bind(9982).sync();
            logger.info("the server is runing...", 9982);
        } catch (InterruptedException e) {

            logger.info("the server catched exception info:\"{}\"", e.getMessage());
            e.printStackTrace();
        }
    }
}
