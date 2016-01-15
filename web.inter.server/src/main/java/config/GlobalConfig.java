package config;


import agilor.distributed.relational.data.context.Config;
import com.jfinal.config.*;
import controller.DeviceController;
import controller.DeviceTypeController;
import controller.SensorController;
import controller.UserController;

/**
 * Created by LQ on 2015/12/24.
 */
public class GlobalConfig extends JFinalConfig   {


    public GlobalConfig()
    {
        Config.init("config.properties");


//        Properties pps = new Properties();
//        try {
//
//            InputStream stream = GlobalConfig.class.getClassLoader().getResourceAsStream("config.properties");
//
//            pps.load(Object.class.getResourceAsStream("/conwwwfig.properties"));
//        } catch (IOException e) {
//            System.out.println("32222222222222");
//            e.printStackTrace();
//        }

    }



    @Override
    public void configConstant(Constants constants) {



    }

    @Override
    public void configRoute(Routes routes) {

        routes.add("/user", UserController.class);
        routes.add("/device", DeviceController.class);
        routes.add("/devicetype", DeviceTypeController.class);
        routes.add("/sensor", SensorController.class);
    }

    @Override
    public void configPlugin(Plugins plugins) {




//        loadPropertyFile("config.properties");
//        C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("jdbcUserName"), getProperty("jdbcPassword"));
//        plugins.add(c3p0Plugin);
//        ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
//        plugins.add(arp);


    }

    @Override
    public void configInterceptor(Interceptors interceptors) {


    }

    @Override
    public void configHandler(Handlers handlers) {

    }
}
