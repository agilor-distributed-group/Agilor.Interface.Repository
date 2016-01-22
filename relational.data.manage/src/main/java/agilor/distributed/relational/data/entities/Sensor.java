package agilor.distributed.relational.data.entities;

import agilor.distributed.communication.client.Value;
import agilor.distributed.relational.data.services.Agilor;

import java.util.Date;
import java.util.UUID;

/**
 * Created by LQ on 2015/12/23.
 */
public class Sensor implements Creator {

    private int id;
    private String name;
    private Date dateCreated;
    private int deviceId;
    private Date dateFinalWrite;
    private String baseName;
    private Value.Types type;
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

    public Date getDateFinalWrite() {
        return dateFinalWrite;
    }

    public void setDateFinalWrite(Date dateFinalWrite) {
        this.dateFinalWrite = dateFinalWrite;
    }

    public String getBaseName() {
        if(baseName==null)
            baseName = UUID.randomUUID().toString();
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public Value.Types getType() {
        return type;
    }

    public void setType(Value.Types type) {
        this.type = type;
    }


    public void write(Value value) {
        Agilor.instance().write(this.getBaseName(), value);
    }


    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }




    public void write(String tagName, String value)
    {
        Value val = new Value(type);
        switch (type)
        {
            case FLOAT:val.setFvalue(Float.parseFloat(value));break;
            case BOOL:val.setBvalue(Boolean.parseBoolean(value));break;
            case INT:val.setLvalue(Integer.parseInt(value));break;
            case STRING:val.setSvalue(value);break;
        }
        Agilor.instance().write(tagName,val);
    }
}
