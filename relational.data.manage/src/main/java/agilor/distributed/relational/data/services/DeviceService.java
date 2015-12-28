package agilor.distributed.relational.data.services;

import agilor.distributed.relational.data.context.RequestContext;
import agilor.distributed.relational.data.db.DB;
import agilor.distributed.relational.data.entities.Device;
import agilor.distributed.relational.data.entities.DeviceType;
import agilor.distributed.relational.data.entities.Sensor;
import agilor.distributed.relational.data.exceptions.DataNotExistException;
import com.agilor.distribute.client.nameManage.AgilorDistributeClient;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Model;
import jdk.nashorn.internal.ir.RuntimeNode;
import jdk.nashorn.internal.objects.NativeUint16Array;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LQ on 2015/12/23.
 */
public class DeviceService {

    private RequestContext context =null;

    public DeviceService(RequestContext context) {
        this.context = context;
    }


    public List<Device> all() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<DB.Device> list = DB.Device.instance().find("SELECT *　FROM DEVICES");
        if(list.size()>0) {
            List<Device> result = new ArrayList<>(list.size());
            for (DB.Device it : list) {
                result.add(it.build(Device.class));
            }

            return result;
        }
        return null;
    }

    public List<Device> all(int userId) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<DB.Device> list = DB.Device.instance().find("SELECT *　FROM DEVICES WHERE CreatorId="+userId);
        if(list.size()>0) {
            List<Device> result = new ArrayList<>(list.size());
            for (DB.Device it : list) {
                result.add(it.build(Device.class));
            }

            return result;
        }
        return null;
    }



    public List<Device> allByTypeId(int typeId) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<DB.Device> list = DB.Device.instance().find("SELECT *　FROM DEVICES WHERE typeId="+typeId);
        if(list.size()>0) {
            List<Device> result = new ArrayList<>(list.size());
            for (DB.Device it : list) {
                result.add(it.build(Device.class));
            }

            return result;
        }
        return null;
    }


    public List<Sensor> insertByType(int typeId, String name) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, DataNotExistException {
        DeviceType type = DB.DeviceType.instance().findFirst("SELECT * FROM DeviceTypes where id=" + typeId).build(DeviceType.class);
        if(type==null)
            throw new DataNotExistException("deviceTypes",typeId);

        List<Sensor> result = new ArrayList<Sensor>();

        Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {



                Model<DB.Device> model = new DB.Device();
                model.set("name",name);
                if(model.save()) {

                    int id = model.get("id");
                    for (int i = 0; i < type.getSensor(); i++) {
                        String base_name = context.user().getId() + "#" + name + "#" + System.currentTimeMillis();
                        DB.Sensor sensor = new DB.Sensor();
                        sensor.set("deviceId", id).set("baseName", base_name).set("name", name).save();

                        try {
                            result.add(sensor.build(Sensor.class));
                        } catch (Exception e) {
                            return false;
                        }
                        //写入agilor
                    }
                }
                return true;
            }
        });
        return result;
    }

    //public List<Sensor> insert(String name,)
}
