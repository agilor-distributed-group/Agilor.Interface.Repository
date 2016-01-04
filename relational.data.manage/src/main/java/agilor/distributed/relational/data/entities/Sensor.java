package agilor.distributed.relational.data.entities;

import agilor.distributed.communication.client.Value;
import agilor.distributed.relational.data.services.Agilor;

import java.util.Date;

/**
 * Created by LQ on 2015/12/23.
 */
public class Sensor {

    private int id;
    private String name;
    private Date dateCreated;
    private int deviceId;
    private Date dateLastWrite;
    private String baseName;
    private Value.Types dataType;
    private int creatorId;


    public Sensor()
    {
        dateCreated=new Date();
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public Date getDateLastWrite() {
        return dateLastWrite;
    }

    public void setDateLastWrite(Date dateLastWrite) {
        this.dateLastWrite = dateLastWrite;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public Value.Types getDataType() {
        return dataType;
    }

    public void setDataType(Value.Types dataType) {
        this.dataType = dataType;
    }


    public void write(Value value) {
        Agilor.instance().write(this.getName(), value);
    }


    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }




    public void write(String tagName, String value)
    {
        Value val = new Value(dataType);
        switch (dataType)
        {
            case FLOAT:val.setFvalue(Float.parseFloat(value));break;
            case BOOL:val.setBvalue(Boolean.parseBoolean(value));break;
            case INT:val.setLvalue(Integer.parseInt(value));break;
            case STRING:val.setSvalue(value);break;
        }


        Agilor.instance().write(tagName,val);

    }
}
