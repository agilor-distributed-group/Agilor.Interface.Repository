package agilor.distributed.relational.data.services;

import agilor.distributed.relational.data.db.DB;
import agilor.distributed.relational.data.entities.SensorOfType;
import agilor.distributed.relational.data.exceptions.NullParameterException;
import agilor.distributed.relational.data.exceptions.SqlHandlerException;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LQ on 2016/1/2.
 */
public class SensorOfTypeService {


    public List<SensorOfType> all(int typeId) {
        List<DB.SensorOfType> list = DB.SensorOfType.instance().find("SELECT * FROM sensorOfTypes where typeId=?", typeId);

        List<SensorOfType> result = null;
        if (list.size() > 0) {

            result = new ArrayList<>(list.size());
            try {
                for (DB.SensorOfType it : list) {
                    result.add(it.build(SensorOfType.class));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public long sensorCount(int typeId)
    {
        return Db.queryLong("select count(*) from sensorOfTypes where typeId=?",typeId);
    }


    public void insert(SensorOfType sensor) throws NullParameterException {
        if (sensor.getTypeId() == 0)
            throw new NullParameterException("typeId");


        DB.SensorOfType data = new DB.SensorOfType();
        //try {
            data.set("typeId", sensor.getTypeId())
                    .set("type", sensor.getType().value())
                    .save();

            sensor.setId(data.getInt("id"));
//        } catch (ActiveRecordException e) {
//            throw new SqlHandlerException(e.getCause());
//        }
    }



    public boolean delete(int id)
    {
        return DB.SensorOfType.instance().deleteById(id);
    }

    public void update(SensorOfType sensor) {
        DB.SensorOfType data = new DB.SensorOfType();
       // try {
            data.set("id",sensor.getId())
                    .set("typeId", sensor.getTypeId())
                    .set("type", sensor.getType().value())
                    .update();

            sensor.setId(data.getInt("id"));
//        } catch (ActiveRecordException e) {
//            throw new SqlHandlerException(e.getCause());
//        }
    }
}
