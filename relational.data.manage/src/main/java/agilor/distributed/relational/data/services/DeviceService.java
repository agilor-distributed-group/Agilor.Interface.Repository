package agilor.distributed.relational.data.services;

import agilor.distributed.communication.client.Value;
import agilor.distributed.relational.data.db.DB;
import agilor.distributed.relational.data.entities.Device;
import agilor.distributed.relational.data.entities.Sensor;
import agilor.distributed.relational.data.exceptions.NullParameterException;
import agilor.distributed.relational.data.exceptions.SqlHandlerException;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Model;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by LQ on 2015/12/23.
 */
public class DeviceService {
    private final static Logger logger = LoggerFactory.getLogger(DeviceService.class);



    /**
     * 获取所有设备
     * @return
     */
    public List<Device> all() {
        List<DB.Device> list = DB.Device.instance().find("SELECT *　FROM DEVICES");
        if (list.size() > 0) {
            List<Device> result = new ArrayList<>(list.size());
            try {
                for (DB.Device it : list) {
                    result.add(it.build(Device.class));
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

    public List<Device> all(int userId) {
        List<DB.Device> list = DB.Device.instance().find("SELECT *　FROM DEVICES WHERE CreatorId=" + userId);
        if (list.size() > 0) {
            List<Device> result = new ArrayList<>(list.size());
            try {
                for (DB.Device it : list) {

                    result.add(it.build(Device.class));
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


    public List<Device> all(int userId, int typeId) throws InstantiationException, IllegalAccessException {
        List<DB.Device> list = DB.Device.instance().find("SELECT *　FROM DEVICES WHERE typeId=? and creatorId=?",typeId,userId);
        if (list.size() > 0) {
            List<Device> result = new ArrayList<>(list.size());
            for (DB.Device it : list) {
                result.add(it.build(Device.class));
            }

            return result;
        }
        return null;
    }


    public void insert(Device device) throws NullParameterException, SqlHandlerException {

        if (StringUtils.isEmpty(device.getName()))
            throw new NullParameterException("name");



        try {


            boolean db_result = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {


                    Model<DB.Device> model = new DB.Device();
                    model.set("name", device.getName())
                            .set("typeId", device.getTypeId())
                            .set("creatorId", device.getCreatorId())
                            .set("dateCreated", device.getDateCreated());


                    if (model.save()) {

                        int id = model.get("id");

                        List<Sensor> sensors = device.getSensors();

                        if (sensors != null && sensors.size() > 0) {
                            for (Sensor it : sensors) {

                                DB.Sensor sensor = new DB.Sensor();
                                sensor.set("deviceId", id)
                                        .set("baseName", it.getBaseName())
                                        .set("name", it.getName())
                                        .set("creatorId",it.getCreatorId())
                                        .set("type",it.getType().value())
                                        .set("dateCreated", it.getDateCreated());

                                if (sensor.save())
                                    it.setId(sensor.getInt("id"));


                                int re =0;
                                if ((re= Agilor.instance().createTagNode(it.getBaseName(), new Value(it.getType()))) != 0) {
                                    logger.info("write agilor target error !!!!!!!!!");
                                    return false;
                                }

                            }
                        }
                    }
                    return true;
                }
            });
        }catch (ActiveRecordException e)
        {
            throw new SqlHandlerException(e.getCause());
        }

    }




    public void update(int id,String name)
    {
        DB.Device.instance().findFirst("SELECT * FROM DEVICES WHERE id="+id).set("name",name).update();
    }


    public boolean delete(int id)
    {
        return Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                if (DB.Device.instance().findById(id).delete()) {

                    //未完成 没有接口
                }
                return false;
            }
        });
    }


    public Device getById(int id) throws InstantiationException, IllegalAccessException {
        DB.Device model = DB.Device.instance().findById(id);
        if (model != null)
            return model.build(Device.class);
        else
            return null;
    }


}
