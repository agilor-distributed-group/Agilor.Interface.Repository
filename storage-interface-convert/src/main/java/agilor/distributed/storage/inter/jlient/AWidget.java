package agilor.distributed.storage.inter.jlient;

/**
 * Created by LQ on 2015/8/31.
 */
public abstract class AWidget implements AWidgetImpl {

    protected Agilor agilor=null;

    @Override
    public  void attached() { }

    protected Agilor getAgilor() {
        return agilor;
    }

    protected void setAgilor(Agilor agilor) {
        this.agilor = agilor;
    }
}
