package agilor.distributed.relational.data.entities;

import agilor.distributed.communication.client.Value;
import agilor.distributed.relational.data.db.DB;
import com.jfinal.plugin.activerecord.Model;

/**
 * Created by LQ on 2015/12/30.
 */
public class SensorOfType implements Creator {

    private int id;
    private Value.Types type = null;
    private int typeId=0;


    public SensorOfType() {
        type = Value.Types.FLOAT;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Value.Types getType() {
        return type;
    }

    public void setType(Value.Types valueType) {
        this.type = valueType;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Override
    public int getCreatorId() {
        Model<DB.DeviceType> model = DB.DeviceType.instance().findById(typeId);
        if (model == null)
            return 0;
        else return model.getInt("creatorId");
    }
}
