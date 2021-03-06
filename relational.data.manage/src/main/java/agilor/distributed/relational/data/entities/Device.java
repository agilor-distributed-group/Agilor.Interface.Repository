package agilor.distributed.relational.data.entities;

import agilor.distributed.communication.client.Value;
import agilor.distributed.relational.data.db.DB;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LQ on 2015/12/23.
 */
public class Device implements Creator {
    private  int id;
    private String name;
    private Integer typeId;
    private Date dateCreated;
    private int creatorId;


    private List<Sensor> sensors=null;


public Device()
{
    dateCreated = new Date();
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

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId=creatorId;
    }


    public List<Sensor> getSensors()
    {
        return sensors;
    }

    public void addSensor(Sensor sensor)
    {
        if(getSensors()==null)
            sensors=new ArrayList<>();
        sensors.add(sensor);
    }


    public void addSensors(List<Sensor> sensors) {
        if (getSensors() == null)
            this.sensors = new ArrayList<>();
        this.sensors.addAll(sensors);
    }


}
