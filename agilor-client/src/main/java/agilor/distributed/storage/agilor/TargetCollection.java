package agilor.distributed.storage.agilor;

import java.util.ArrayList;

/**
 * Created by LQ on 2015/7/30.
 * 点集合,继承自list
 */
public class TargetCollection extends ArrayList<Target> {

    /**
     * 求和
     * @return
     */
    public Val sum(){
        System.out.println("sum...");
        return new Val();
    }

    /**
     * 最小值
     * @return
     */
    public Val min(){
        System.out.println("min...");
        return new Val();
    }


    /**
     * 最大值
     * @return
     */
    public Val max()
    {
        System.out.println("max...");
        return new Val();
    }

    public Val average()
    {
        System.out.println("average...");
        return new Val();
    }



}
