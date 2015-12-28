package agilor.distributed.relational.data.services;

import agilor.distributed.relational.data.context.RequestContext;
import agilor.distributed.relational.data.db.DB;
import agilor.distributed.relational.data.entities.Device;
import agilor.distributed.relational.data.entities.DeviceType;
import agilor.distributed.relational.data.exceptions.ExceptionTypes;
import agilor.distributed.relational.data.exceptions.ValidateParameterException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.ICallback;
import com.jfinal.plugin.activerecord.Model;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LQ on 2015/12/23.
 */
public class DeviceTypeService {




    public DeviceTypeService(RequestContext context)
    {

    }



    public List<DeviceType> all() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<DB.DeviceType> list = DB.DeviceType.instance().find("select * from deviceTypes");
        if (list.size() > 0) {
            List<DeviceType> result = new ArrayList<>(list.size());

            for (DB.DeviceType it : list) {
                result.add(it.build(DeviceType.class));
            }
            return result;
        }
        return null;
    }



    public List<DeviceType> all(String userName) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<DB.DeviceType> list = DB.DeviceType.instance().find("select t1.* from deviceTypes t1,users t2 where t1.creatorId=t2.id and t2.userName='" + userName + "'");
        if (list.size() > 0) {
            List<DeviceType> result = new ArrayList<>(list.size());

            for (DB.DeviceType it : list) {
                result.add(it.build(DeviceType.class));
            }
            return result;
        }
        return null;
    }


    public DeviceType insert(DeviceType data) throws ValidateParameterException {

        if (data.getSensor() <= 0)
            throw new ValidateParameterException("sensor", ExceptionTypes.MODELRROR);


        Model<DB.DeviceType> model = new DB.DeviceType();
        model.set("name", data.getName())
                .set("scope", data.getScope().value())
                .set("sensor", data.getSensor())
                .set("creatorId", data.getCreatorId())
                .save();

        data.setId(model.getInt("id"));
        return data;
    }

    public boolean delete(int id) {
        return DB.DeviceType.instance().deleteById(id);
    }

    public  boolean update(DeviceType data) {
        return new DB.DeviceType()
                .set("id", data.getId())
                .set("name", data.getName())
                .set("scope", data.getScope().value())
                .set("sensor", data.getSensor())
                .update();
    }

    public long getDeviceCount(int id)
    {
        return Db.queryLong("select * from devices where typeId="+id);
    }


}
