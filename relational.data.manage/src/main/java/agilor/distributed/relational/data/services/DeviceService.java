package agilor.distributed.relational.data.services;

import agilor.distributed.relational.data.db.DB;
import agilor.distributed.relational.data.entities.Device;
import agilor.distributed.relational.data.entities.DeviceType;
import agilor.distributed.relational.data.exceptions.DataNotExistException;
import com.agilor.distribute.client.nameManage.AgilorDistributeClient;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LQ on 2015/12/23.
 */
public class DeviceService {

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


    public void insertByType(int typeId,String name) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, DataNotExistException {
        DeviceType type = DB.DeviceType.instance().findFirst("SELECT * FROM DeviceTypes where id="+typeId).build(DeviceType.class);
        if(type==null)
            throw new DataNotExistException("deviceTypes",typeId);

        Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {

                Device a;

                new DB.Device().set("name",name).save();
                for(int i=0;i< type.getSensor();i++)
                {
                    //Agilor.instance().write();
                }

                //Agilor.instance().write();

                return false;
            }
        });


    }






}
