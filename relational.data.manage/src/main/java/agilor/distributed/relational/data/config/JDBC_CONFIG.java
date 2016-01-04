package agilor.distributed.relational.data.config;

import agilor.distributed.relational.data.db.DB;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;

import javax.security.auth.login.Configuration;
import java.util.Properties;

/**
 * Created by LQ on 2015/12/24.
 */
public class JDBC_CONFIG implements CONFIG {

    private static String url = null;
    private static String user = null;
    private static String password = null;
    private static String cacheFile = null;


    private static C3p0Plugin c3p0 =null;
    private static ActiveRecordPlugin arp = null;
    private static EhCachePlugin cache = null;






    @Override
    public void parse(Properties pps) {
        url = pps.getProperty("jdbc");
        user = pps.getProperty("user");
        password = pps.getProperty("password");
        cacheFile=pps.getProperty("cacheFile");




        c3p0 = new C3p0Plugin(url, user, password);
        arp = new ActiveRecordPlugin(c3p0);





        cache = new EhCachePlugin(Object.class.getResourceAsStream("/"+cacheFile) );

        arp.addMapping("users", DB.User.class);
        arp.addMapping("devices", DB.Device.class);
        arp.addMapping("deviceTypes", DB.DeviceType.class);
        arp.addMapping("sensorOfTypes", DB.SensorOfType.class);
        arp.addMapping("sensors", DB.Sensor.class);


        c3p0.start();
        arp.start();
        cache.start();
    }


}
