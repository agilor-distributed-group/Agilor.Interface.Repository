package agilor.distributed.web.inter.server.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by LQ on 2016/1/22.
 */
public class Global {

    private static boolean isDebug=false;



    public static void init() throws IOException {

        Properties pps = new Properties();

        InputStream in = Global.class.getClassLoader().getResourceAsStream("web.properties");
        pps.load(in);
        isDebug = Boolean.parseBoolean(pps.getProperty("isDebug", "false"));
    }

    public static boolean isDebug() {
        return isDebug;
    }
}
