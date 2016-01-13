package agilor.distributed.relational.data.services;

import agilor.distributed.relational.data.context.RequestContext;
import agilor.distributed.relational.data.db.DB;
import agilor.distributed.relational.data.entities.Device;
import agilor.distributed.relational.data.entities.DeviceType;
import agilor.distributed.relational.data.entities.SensorOfType;
import agilor.distributed.relational.data.exceptions.ExceptionTypes;
import agilor.distributed.relational.data.exceptions.SqlHandlerException;
import agilor.distributed.relational.data.exceptions.ValidateParameterException;
import com.jfinal.plugin.activerecord.*;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.PUBLIC_MEMBER;

import javax.xml.bind.ValidationException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LQ on 2015/12/23.
 */
public class DeviceTypeService {








    public List<DeviceType> all()  {
        List<DB.DeviceType> list = DB.DeviceType.instance().find("select * from deviceTypes");
        if (list.size() > 0) {
            List<DeviceType> result = new ArrayList<>(list.size());
            try {
                for (DB.DeviceType it : list) {
                    result.add(it.build(DeviceType.class));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            return result;
        }
        return null;
    }



    public List<DeviceType> all(String userName) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<DB.DeviceType> list = DB.DeviceType.instance().find("select t1.* from deviceTypes t1,users t2 where t1.creatorId=t2.id and t2.userName=?", userName);
        if (list.size() > 0) {
            List<DeviceType> result = new ArrayList<>(list.size());

            for (DB.DeviceType it : list) {
                result.add(it.build(DeviceType.class));
            }
            return result;
        }
        return null;
    }


    public List<DeviceType> all(int userId)
    {
        List<DB.DeviceType> list = DB.DeviceType.instance().find("SELECT * FROM deviceTypes where creatorId=?",userId);
        if(list.size()>0)
        {
            List<DeviceType> result = new ArrayList<>(list.size());
            try {
                for(DB.DeviceType it:list)
                {
                    result.add(it.build(DeviceType.class));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            return result;
        }
        return null;
    }


    /**
     * 插入新数据，测试通过
     * @param data
     * @return
     * @throws ValidateParameterException
     * @throws SqlHandlerException
     */
    public DeviceType insert(DeviceType data) throws ValidateParameterException {

        if (data.getSensorCount() <= 0)
            throw new ValidateParameterException("sensor", ExceptionTypes.MODELRROR);

        if (StringUtils.isEmpty(data.getName()))
            throw new ValidateParameterException("name", ExceptionTypes.FILED_IS_NULL);

        if (data.getCreatorId() <= 0)
            throw new ValidateParameterException("creatorId", ExceptionTypes.FILED_IS_NULL);


        Model<DB.DeviceType> model = new DB.DeviceType();
        //try {
        boolean tx = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                model.set("name", data.getName())
                        .set("scope", data.getScope().value())
                        .set("creatorId", data.getCreatorId())
                        .save();

                int id = model.getInt("id");
                for (SensorOfType it : data.getSensors()) {
                    if (it.getId() == 0) {
                        Model<DB.SensorOfType> s = (new DB.SensorOfType()
                                .set("type", it.getType().value())
                                .set("typeId", id));

                        if (s.save())
                            it.setId(s.getInt("id"));
                    }
                }

                return true;
            }
        });
//        }catch (ActiveRecordException e)
//        {
//            throw new SqlHandlerException(e.getCause());
//        }


        data.setId(model.getInt("id"));
        return data;
    }

    public boolean delete(int id) {
        return DB.DeviceType.instance().deleteById(id);
    }

    public  boolean update(DeviceType data) throws ValidateParameterException {

        if (data.getId() <= 0)
            throw new ValidateParameterException("id", ExceptionTypes.FILED_IS_NULL);

        Model<DB.DeviceType> model = DB.DeviceType.instance().findById(data.getId());
        if (model != null) {
            return model.set("name", data.getName())
                    .set("scope", data.getScope())
                    .update();
        }
        return false;
    }

    public long getDeviceCount(int id)
    {
        return Db.queryLong("select * from devices where typeId="+id);
    }


    public DeviceType getById(int id) throws InstantiationException, IllegalAccessException {
        DB.DeviceType model = DB.DeviceType.instance().findById(id);
        if (model != null)
            return model.build(DeviceType.class);
        return null;
    }





}
