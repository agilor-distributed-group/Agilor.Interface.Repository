package agilor.distributed.storage.connection;

import agilor.distributed.storage.msg.LoginMsg;
import agilor.distributed.storage.msg.MsgTest;
import agilor.distributed.storage.msg.PingMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by LQ on 2015/7/30.
 */
public class Connection {
    private Bootstrap bootstrap=null;
    private ChannelFuture future=null;
    private EventLoopGroup group =null;
    private Channel channel = null;

    private String id=null;

    private final static Logger logger = LoggerFactory.getLogger(Connection.class);



    private static boolean END =false;
    private static List<Connection> pool = new ArrayList<>();


//    static {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (!END)
//                {
//                    Iterator<Connection> it = pool.iterator();
//                    while (it.hasNext()) {
//                        Connection c = it.next();
//                        if (c.isOpen()) {
//                            c.getChannel().writeAndFlush(new PingMsg());
//                        }
//                    }
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        }).start();
//    }



    public Connection() throws InterruptedException {
        init();
    }
    public void init() throws InterruptedException {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);

        bootstrap.option(ChannelOption.TCP_NODELAY, true);

        bootstrap.remoteAddress("127.0.0.1", 9982);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new IdleStateHandler(20, 10, 0));
                ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(MsgTest.class.getClassLoader())));
                ch.pipeline().addLast(new ObjectEncoder());

                ch.pipeline().addLast(new ClientHandler());

            }
        });

        future = bootstrap.connect().sync();
        logger.info("connect state is {}",future.isSuccess());



        channel = future.channel();
        //channel.writeAndFlush(new MsgTest());


        //future.channel().closeFuture()
        pool.add(this);
    }

    public void close() throws InterruptedException {
        if(group==null||future==null||bootstrap==null) return;

        if(future.channel().isOpen())
        {
            future.channel().closeFuture().sync();
            group.shutdownGracefully();

        }
    }

    public void open() throws InterruptedException {
        if (group == null || bootstrap == null) init();

        if (!future.channel().isOpen()) {
            future = bootstrap.connect("127.0.0.1", 9981).sync();


            if (future.isSuccess()) {

            }
        }

    }

    public boolean isOpen(){
        return (!(group==null||bootstrap==null||channel==null))||channel.isOpen();
    }

    public void register(ChannelHandler handler) {
        bootstrap.handler(handler);
    }

    public Channel getChannel() {
        return channel;
    }
}
