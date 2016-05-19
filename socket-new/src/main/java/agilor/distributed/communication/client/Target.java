package agilor.distributed.communication.client;

import agilor.distributed.communication.protocol.ProtocolObject;

/**
 * Created by LQ on 2015/12/15.
 */
public class Target  {
    public Target(String name,Value.Types type)
    {
        this.name=name;
        this.type=type;
    }


    private String name;
    private Value.Types type;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Value.Types getType() {
        return type;
    }
}
