package agilor.distributed.relational.data.entities;

import agilor.distributed.communication.client.Value;
import agilor.distributed.relational.data.db.DB;
import com.jfinal.plugin.activerecord.Model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Created by LQ on 2015/12/23.
 */
public class DeviceType {
    public static enum ScopeTypes  {

        PRIVATE((byte) 0),
        PUBLIC((byte) 1);

        private int flag;

        ScopeTypes(byte flag) {
            this.flag = flag;
        }

        public int value() {
            return flag;
        }

        public static ScopeTypes value(int flag) {



            if (flag == 0)
                return PRIVATE;
            if (flag == 1)
                return PUBLIC;
            return PRIVATE;
        }
    }

    private int id;
    private String name;
    private int creatorId;
    private ScopeTypes scope;

    private User creator;

    private List<SensorOfType>  sensors;


    public DeviceType() {
        scope = ScopeTypes.PRIVATE;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        if (creatorId != this.creatorId) {
            this.creatorId = creatorId;
            creator = null;
        }
    }

    public int getSensorCount() {

        if(getSensors()!=null)
            return getSensors().size();
        return 0;
    }

    public ScopeTypes getScope() {
        return scope;
    }

    public void setScope(ScopeTypes scope) {
        this.scope = scope;
    }

    public User getCreator() throws Exception {

        if (id <= 0)
            return null;
        if (creator == null)
            creator = DB.User.instance().findFirst("select * from users where id=?", creatorId).build(User.class);
        return creator;
    }


    public List<SensorOfType> getSensors()  {
        return sensors;
    }


    public void addSensor(SensorOfType sensor)
    {
        if(getSensors()==null)
            sensors = new ArrayList<>();
        sensors.add(sensor);
    }


    public Device build()
    {
        Device device = new Device();
        device.setTypeId(getId());
        device.setDateCreated(new Date());

        List<DB.SensorOfType> list = DB.SensorOfType.instance().find("SELECT * FROM sensorOfTypes where typeId=?", getId());
        if(list.size()>0)
        {
            for(DB.SensorOfType it:list) {
                Sensor s = new Sensor();
                s.setBaseName(UUID.randomUUID().toString());
                s.setDataType(Value.Types.value((it.getInt("type").byteValue())));
                s.setDateCreated(new Date());
                s.setDateLastWrite(null);
                s.setName(s.getBaseName());
                device.addSensor(s);

            }
        }

        return device;


    }



}
