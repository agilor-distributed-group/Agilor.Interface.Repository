package agilor.distributed.relational.data.entities;

import agilor.distributed.communication.client.Value;

/**
 * Created by LQ on 2015/12/30.
 */
public class SensorOfType {

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

}
