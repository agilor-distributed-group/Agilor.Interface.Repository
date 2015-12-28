package agilor.distributed.relational.data.entities;

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
}
