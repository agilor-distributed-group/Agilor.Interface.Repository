package agilor.distributed.relational.data.config;



import com.jfinal.plugin.c3p0.C3p0Plugin;

import java.util.Properties;

/**
 * Created by LQ on 2015/12/25.
 */
public class GLOBAL_CONFIG implements CONFIG {

    private static long sessionTimeOut=1000*60*10;

    private static C3p0Plugin cp = null;


    @Override
    public void parse(Properties pps) {

    }


    public  long getSessionTimeOut() {
        return sessionTimeOut;
    }



    public static void INIT()
    {
        Object.class.getResourceAsStream("jdbc.properties");

    }
}
