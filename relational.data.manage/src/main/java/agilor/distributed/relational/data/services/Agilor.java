package agilor.distributed.relational.data.services;

import com.agilor.distribute.client.nameManage.AgilorDistributeClient;

/**
 * Created by LQ on 2015/12/28.
 */
public class Agilor {
    private static AgilorDistributeClient client = null;


    private Agilor(){}


    public static AgilorDistributeClient  instance()
    {
        if(client==null)
            client = new AgilorDistributeClient();
        return client;
    }


}