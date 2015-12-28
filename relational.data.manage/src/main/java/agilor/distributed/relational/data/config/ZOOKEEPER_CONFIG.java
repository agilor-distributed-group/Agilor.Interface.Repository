package agilor.distributed.relational.data.config;

import java.util.Properties;

/**
 * Created by LQ on 2015/12/24.
 */
public class ZOOKEEPER_CONFIG implements CONFIG {
    private static String address;

    private static int timeout;


    private static String dataPath;



    @Override
    public void parse(Properties pps) {

    }

    public static String getAddress() {
        return address;
    }


    public  int getTimeout() {
        return timeout;
    }

    public  String getDataPath() {
        return dataPath;
    }
}
