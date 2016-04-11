package agilor.distributed.communication.socket;

import java.net.Socket;

/**
 * Created by LQ on 2015/10/19.
 */
public interface SocketProvider {

    BaseSocket getSocket(String ip,int port) throws Exception;
    void returnSocket(BaseSocket socket) throws Exception;
    void destorySocket(BaseSocket socket) throws Exception;
}
