package agilor.distributed.relational.data.entities;

import agilor.distributed.relational.data.db.DB;


/**
 * Created by LQ on 2015/12/23.
 */
public class DeviceType {
    public static enum ScopeTypes {

        PRIVATE((byte) 0),
        PUBLIC((byte) 1);

        private byte flag;

        ScopeTypes(byte flag) {
            this.flag = flag;
        }

        public byte value() {
            return flag;
        }

        public static ScopeTypes value(byte f) {
            if (f == 0)
                return PRIVATE;
            if (f == 1)
                return PUBLIC;
            return PRIVATE;
        }
    }

    private int id;
    private String name;
    private int creatorId;
    private int sensor;
    private ScopeTypes scope;

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

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        if (creatorId != this.creatorId) {
            this.creatorId = creatorId;
            creator = null;
        }
    }

    public int getSensor() {
        return sensor;
    }

    public void setSensor(int sensor) {
        this.sensor = sensor;
    }

    public ScopeTypes getScope() {
        return scope;
    }

    public void setScope(ScopeTypes scope) {
        this.scope = scope;
    }

    public User getCreator() throws Exception {
        if(id<=0)
            return null;
        if(creator==null)
            creator = DB.User.instance().findFirst("select * from users where id="+creatorId).build(User.class);
        return creator;
    }
}
