package agilor.distributed.storage;

import agilor.distributed.storage.gate.Server;

/**
 * Created by LQ on 2015/7/30.
 */
public class Program {



    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.run();


        //Agilor.Client
        //IClient client = new IClient("127.0.0.1",9090,2000);
        //client.open();
        //client.ping();
        //client.close();
    }
}
