package agilor.distributed.relational.data.config;

import java.util.Properties;

/**
 * Created by LQ on 2015/12/25.
 */
public class GLOBAL_CONFIG implements CONFIG {

    private static long sessionTimeOut=1000*60*10;


    @Override
    public void parse(Properties pps) {

    }


    public  long getSessionTimeOut() {
        return sessionTimeOut;
    }
}
