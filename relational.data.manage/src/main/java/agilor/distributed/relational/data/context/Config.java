package agilor.distributed.relational.data.context;

import agilor.distributed.relational.data.db.DB;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.Properties;


/**
 * Created by LQ on 2015/12/24.
 */
public class Config {


    private final static Logger logger = LoggerFactory.getLogger(Config.class);


    private static String zkAddress=null;
    private static int zkTimeout= 3000;

    private static String sessionPath=null;
    private static int sessionTimeout=0;
    private static String jdbcUrl=null;
    private static String jdbcUserName=null;
    private static String jdbcPassword=null;



    private static C3p0Plugin c3p0 =null;
    private static ActiveRecordPlugin arp = null;
    private static EhCachePlugin cache = null;


    private static boolean isInit=false;


    public static void init(String fileName) {
        if(!isInit) {
            Properties pps = new Properties();

            try {
                pps.load(Object.class.getResourceAsStream("/" + fileName));

                zkAddress=pps.getProperty("zkAddress");
                zkTimeout=Integer.parseInt(pps.getProperty("zkTimeout", "3000"));
                sessionPath = pps.getProperty("sessionPath", null);
                sessionTimeout = Integer.parseInt(pps.getProperty("sessionTimeout", "600000"));
                jdbcUrl = pps.getProperty("jdbcUrl", null);
                jdbcUserName = pps.getProperty("jdbcUserName", null);
                jdbcPassword = pps.getProperty("jdbcPassword", null);


                if(!StringUtils.isEmpty(sessionPath)&&sessionPath.endsWith("/")) {
                    sessionPath = sessionPath.substring(0, sessionPath.length() - 1);
                }


                c3p0 = new C3p0Plugin(jdbcUrl, jdbcUserName, jdbcPassword);
                arp = new ActiveRecordPlugin(c3p0);


                cache = new EhCachePlugin(Object.class.getResourceAsStream("/cache-context.xml"));

                arp.addMapping("users", DB.User.class);
                arp.addMapping("devices", DB.Device.class);
                arp.addMapping("deviceTypes", DB.DeviceType.class);
                arp.addMapping("sensorOfTypes", DB.SensorOfType.class);
                arp.addMapping("sensors", DB.Sensor.class);


                c3p0.start();
                arp.start();
                cache.start();




                isInit=true;

            } catch (IOException e) {
                logger.error("load config file {} error", fileName);
            }
        }
    }


    public static void dispose()
    {
        isRuning=false;
        isInit=false;
    }





    private static boolean isRuning=true;



    public static boolean isRuning() {
        return isRuning;
    }

    public static void setIsRuning(boolean isRuning) {
        Config.isRuning = isRuning;
    }


    public static String getSessionPath() {
        return sessionPath;
    }

    public static int getSessionTimeout() {
        return sessionTimeout;
    }

    public static String getJdbcUrl() {
        return jdbcUrl;
    }

    public static String getJdbcUserName() {
        return jdbcUserName;
    }

    public static String getJdbcPassword() {
        return jdbcPassword;
    }

    public static String getZkAddress() {
        return zkAddress;
    }

    public static int getZkTimeout() {
        return zkTimeout;
    }
}
