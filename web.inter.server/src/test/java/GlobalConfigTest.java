import agilor.distributed.web.inter.server.config.GlobalConfig;
import org.junit.Test;


/**
 * Created by LQ on 2016/1/11.
 */
public class GlobalConfigTest {


    @Test
    public void initTest()
    {
        GlobalConfig globalConfig = new GlobalConfig();

    }


    @Test
    public void abstractTest()
    {
        ExtendAt a = new ExtendAt();
        a.init();
    }


}
