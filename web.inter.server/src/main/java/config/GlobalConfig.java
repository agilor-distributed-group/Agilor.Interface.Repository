package config;


import com.jfinal.config.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import controller.UserController;

/**
 * Created by LQ on 2015/12/24.
 */
public class GlobalConfig extends JFinalConfig   {

    @Override
    public void configConstant(Constants constants) {



    }

    @Override
    public void configRoute(Routes routes) {
        routes.add("/user", UserController.class);
    }

    @Override
    public void configPlugin(Plugins plugins) {
        loadPropertyFile("database.properties");
        C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbc"), getProperty("user"), getProperty("password"));
        plugins.add(c3p0Plugin);
        ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
        plugins.add(arp);


    }

    @Override
    public void configInterceptor(Interceptors interceptors) {


    }

    @Override
    public void configHandler(Handlers handlers) {

    }
}
