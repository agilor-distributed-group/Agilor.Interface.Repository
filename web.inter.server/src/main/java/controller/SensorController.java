package controller;

import agilor.distributed.communication.client.Value;
import agilor.distributed.relational.data.db.DB;
import agilor.distributed.relational.data.entities.Sensor;
import agilor.distributed.relational.data.services.SensorService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by LQ on 2015/12/30.
 */
public class SensorController extends DistrController {

    private SensorService service = null;


    public SensorController() throws Exception {
        service = new SensorService(getContext());
    }


    public void insert() {
        int deviceId = getParaToInt("di");
    }

    public void insertOfType()
    {
        int typeId = getParaToInt("ti");
        ObjectMapper mapper = new ObjectMapper();

    }


    public void write() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        int type = getParaToInt("t");
        String v = getPara("v");
        Value val = new Value(Value.Types.value((byte) type));
        switch (val.getValueType()) {
            case BOOL:
                val.setBvalue(Boolean.valueOf(v));
                break;
            case FLOAT:
                val.setFvalue(Float.valueOf(v));
                break;
            case INT:
                val.setLvalue(Integer.valueOf(v));
                break;
            case STRING:
                val.setSvalue(v);
                break;
        }


        int id = getParaToInt("i");

        Sensor sensor = service.findById(id);
        sensor.write(val);
    }
}
