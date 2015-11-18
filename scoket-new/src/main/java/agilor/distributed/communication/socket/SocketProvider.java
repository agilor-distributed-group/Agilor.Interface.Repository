package agilor.distributed.communication.socket;

import java.net.Socket;

/**
 * Created by LQ on 2015/10/19.
 */
public interface SocketProvider {

    Socket getSocket(String ip,int port) throws Exception;
    void returnSocket(Socket socket) throws Exception;
}
