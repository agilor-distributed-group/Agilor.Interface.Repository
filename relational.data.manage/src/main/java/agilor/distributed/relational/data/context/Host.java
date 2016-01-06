package agilor.distributed.relational.data.context;

/**
 * Created by LQ on 2016/1/6.
 */
public class Host {

    private String address;
    private int port = 0;


    public Host(String data) {
        String[] s = data.split(":");

        address = s[0];
        port = Integer.parseInt(s[1]);
    }


    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
