package agilor.distributed.relational.data.entities;

import agilor.distributed.relational.data.db.DB;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * Created by LQ on 2015/12/23.
 */
public class Device {
    private  int id;
    private String name;
    private int typeId;
    private Date dateCreated;
    private int creatorId;


    private DeviceType type;
    private User creator;


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

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {

        if (this.typeId != typeId) {
            this.typeId = typeId;
            type = null;
        }
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
        if (this.creatorId != creatorId) {
            this.creatorId = creatorId;
            creator = null;
        }
    }


    public DeviceType getType() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (this.typeId <= 0)
            return null;
        if (type == null)
            type = DB.DeviceType.instance().findFirst("select * from deviceTypes where id=" + typeId).build(DeviceType.class);
        return type;
    }

    public User getCreator() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (this.creatorId <= 0)
            return null;
        if (creator == null)
            creator = DB.User.instance().findFirst("select * from users where id=" + creatorId).build(User.class);
        return creator;
    }
}
