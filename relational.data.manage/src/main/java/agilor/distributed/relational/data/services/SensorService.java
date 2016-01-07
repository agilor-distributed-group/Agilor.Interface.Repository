package agilor.distributed.relational.data.services;

import agilor.distributed.relational.data.context.RequestContext;
import agilor.distributed.relational.data.db.DB;
import agilor.distributed.relational.data.entities.Sensor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LQ on 2015/12/25.
 */
public class SensorService {





    public List<Sensor> all(int deviceId) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<DB.Sensor> list = DB.Sensor.instance().find("SELECT * FROM Sensors WHERE deviceId="+deviceId);
        if(list.size()>0)
        {
            List<Sensor> result = new ArrayList<>(list.size());
            for(DB.Sensor it :list) {
                result.add(it.build(Sensor.class));
            }
            return result;
        }
        return null;
    }


    public Sensor insert(Sensor sensor) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DB.Sensor model = new DB.Sensor();
        model.set("name", sensor.getName())
                .set("deviceId", sensor.getDeviceId())
                .set("dateCreated", sensor.getDateCreated())
                .set("dateLastWrite", sensor.getDateLastWrite())
                .set("dataType", sensor.getDataType().value())
                .set("creatorId",sensor)
                .save();
        return model.build(Sensor.class);
    }



    public boolean update(Sensor sensor)
    {
        DB.Sensor model = new DB.Sensor();
        return model.set("name", sensor.getName())
                .set("id",sensor.getId())
                .set("deviceId", sensor.getDeviceId())
                .set("dateCreated", sensor.getDateCreated())
                .update();
    }



    public boolean delete(int id) {
        return DB.Sensor.instance().deleteById(id);
    }


    public Sensor findById(int id) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DB.Sensor model = DB.Sensor.instance().findById(id);
        return model != null? model.build(Sensor.class):null;

    }


    public Sensor get(int id) throws InstantiationException, IllegalAccessException {
        return DB.Sensor.instance().findById(id).build(Sensor.class);
    }


}
