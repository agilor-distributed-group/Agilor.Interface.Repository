package agilor.distributed.relational.data.config;

import org.apache.commons.lang3.StringUtils;

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

        address = pps.getProperty("address");
        timeout = Integer.parseInt(pps.getProperty("timeout"));
        dataPath=pps.getProperty("dataPath");

        if(!StringUtils.isEmpty(dataPath)&&dataPath.endsWith("/")) {
            dataPath = dataPath.substring(0, dataPath.length() - 1);
        }

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
