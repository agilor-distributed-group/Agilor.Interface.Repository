package controller;

import agilor.distributed.relational.data.db.DB;
import agilor.distributed.relational.data.entities.Sensor;
import agilor.distributed.relational.data.exceptions.DataNotExistException;
import agilor.distributed.relational.data.services.DeviceService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jfinal.kit.JsonKit;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by LQ on 2015/12/28.
 */
public class DeviceController extends DistrController {

    private DeviceService service = null;

    public DeviceController() throws Exception {
        service = new DeviceService(getContext());
    }

    public void insert() throws NoSuchMethodException, InstantiationException, IllegalAccessException, DataNotExistException, InvocationTargetException, IOException {
        boolean t = getParaToBoolean("t",false);

        String n = getPara("n");


        if(t) {
            int id = getParaToInt("i");
            service.insert(id, n);
        }
        else {

            String sensor_str = getPara("s");
            ObjectMapper mapper = new ObjectMapper();

            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, Sensor.class);
            List<Sensor> o = (List<Sensor>) mapper.readValue(sensor_str, javaType);
        }
    }

    public void delete()
    {
        int id = getParaToInt("i",0);

        service.delete(id);
    }

    public void update() {
        String name = getPara("n");
        int id = getParaToInt("i", 0);
        service.update(id, name);
    }
}
