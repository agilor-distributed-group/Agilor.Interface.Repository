import agilor.distributed.relational.data.context.Config;
import agilor.distributed.relational.data.entities.Sensor;
import agilor.distributed.relational.data.services.SensorService;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * Created by LQ on 2016/1/4.
 */
public class CacheTest {

    @Test
    public void cache_test() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Config config = new Config();


        SensorService service = new SensorService();


        Random random = new Random();


        long start = System.currentTimeMillis();
        for(int i=0;i<100000;i++) {
            Sensor sensor = service.findById(random.nextInt(1000));
        }

        System.out.println(System.currentTimeMillis()-start);
    }



    @Test
    public void use_cache_2() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Config config = new Config();


        SensorService service = new SensorService();

        service.findById(32);
        service.findById(32);
    }






}
