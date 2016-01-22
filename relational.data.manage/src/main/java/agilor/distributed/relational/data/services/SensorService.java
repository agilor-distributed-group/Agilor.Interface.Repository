package agilor.distributed.relational.data.services;

import agilor.distributed.communication.client.Value;
import agilor.distributed.relational.data.context.RequestContext;
import agilor.distributed.relational.data.db.DB;
import agilor.distributed.relational.data.entities.Sensor;
import agilor.distributed.relational.data.exceptions.ExceptionTypes;
import agilor.distributed.relational.data.exceptions.NullParameterException;
import agilor.distributed.relational.data.exceptions.ValidateParameterException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LQ on 2015/12/25.
 */
public class SensorService {

    private final static Logger logger = LoggerFactory.getLogger(SensorService.class);



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


    public boolean insert(Sensor sensor) throws NullParameterException, ValidateParameterException {

        if (StringUtils.isEmpty(sensor.getName()))
            throw new NullParameterException("name");

        if (sensor.getDeviceId() <= 0)
            throw new ValidateParameterException("deiveId", ExceptionTypes.FILED_IS_NULL);

        if(sensor.getCreatorId()<=0)
            throw new ValidateParameterException("creatorId",ExceptionTypes.FILED_IS_NULL);

        DB.Sensor model = new DB.Sensor();
        model.set("name", sensor.getName())
                .set("deviceId", sensor.getDeviceId())
                .set("baseName",sensor.getBaseName())
                .set("dateCreated", sensor.getDateCreated())
                .set("dateFinalWrite", sensor.getDateFinalWrite())
                .set("type", sensor.getType().value())
                .set("creatorId", sensor.getCreatorId());
        if (model.save()) {
            sensor.setId(model.getInt("id"));
            int re =0;
            if ((re= Agilor.instance().createTagNode(sensor.getBaseName(), new Value(sensor.getType()))) != 0) {
                logger.info("write agilor target error !!!!!!!!!");
                return false;
            }
            else logger.info("write agilor target success:{}",re);
        }
        else
            return false;
        return true;
    }



    public boolean update(Sensor sensor)
    {
        DB.Sensor model = new DB.Sensor();
        return model.set("name", sensor.getName())
                .set("id",sensor.getId())
//                .set("deviceId", sensor.getDeviceId())
//                .set("dateCreated", sensor.getDateCreated())
                .update();
    }



    public boolean delete(int id) {
        return DB.Sensor.instance().deleteById(id);
    }


    public Sensor findById(int id) throws InstantiationException, IllegalAccessException {
        DB.Sensor model = DB.Sensor.instance().findById(id);
        return model != null? model.build(Sensor.class):null;

    }


    public Sensor get(int id) throws InstantiationException, IllegalAccessException {
        return DB.Sensor.instance().findById(id).build(Sensor.class);
    }


}
