import agilor.distributed.communication.client.Target;
import agilor.distributed.communication.client.TargetCollection;
import agilor.distributed.communication.client.Value;
import agilor.distributed.communication.client.ValueCollection;
import agilor.distributed.communication.protocol.*;
import agilor.distributed.communication.socket.Connection;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by LQ on 2015/10/19.
 */


public class Client {


    @Test
    public void ping() throws Exception {
        Connection connection = new Connection("127.0.0.1",3200,5000,new SimpleProtocol());
        agilor.distributed.communication.client.Client client = new agilor.distributed.communication.client.Client(connection);





//        for(int i =0;i<10;i++) {
//
//            client.open();
//            client.ping();
//            client.close();
//        }


        int size =10;
        Random random = new Random();
        Thread[] threads = new Thread[size];

        //int borrow


        int index =0;
        for( int i=0;i<size;i++)
        {
            threads[i] = new Thread(new Runnable() {
                private agilor.distributed.communication.client.Client client = null;

                @Override
                public void run() {

                    Connection connection = new Connection("127.0.0.1", 3200, 5000,new SimpleProtocol());
                    client = new agilor.distributed.communication.client.Client(connection);
                    int max = random.nextInt(10);

                    while (max-- > 0) {
                        if(!client.isOpen())
                            try {
                                client.open();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        client.ping();

                        if(random.nextBoolean())
                            try {
                                client.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
            threads[i].start();

        }


        for(int i=0;i<size;i++)
        {
            threads[i].join();
        }





//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();




    }


    @Test
    public void simple() throws IOException {
        Socket socket = new Socket("127.0.0.1",3200);
        DataOutputStream os = new DataOutputStream( socket.getOutputStream());
        DataInputStream in = new DataInputStream( socket.getInputStream());

        os.write('a');
        os.flush();

        os.close();
        in.close();
        socket.close();


    }



    @Test
    public void addTarget() throws Exception {
        Connection connection = new Connection("10.0.0.148",10001,5000,SimpleProtocol.getInstance());

        //Connection connection = new Connection("127.0.0.1",3200,5000,SimpleProtocol.getInstance());


        agilor.distributed.communication.client.Client client = new agilor.distributed.communication.client.Client(connection);

        client.open();
        Value val = new Value(Value.Types.FLOAT);
        val.setFvalue(10);
        client.addTarget("test", "test_device", val);
        client.close();
    }


    @Test
    public void addValue() throws Exception {
        Connection connection = new Connection("10.0.0.148", 10001, 5000, SimpleProtocol.getInstance());
        agilor.distributed.communication.client.Client client = new agilor.distributed.communication.client.Client(connection);
        client.open();
        Value val = new Value(Value.Types.FLOAT);
        val.setFvalue(102.5f);
        client.addValue("Simu11_2", "Simu11", val);

        client.close();
    }

    @Test
    public void addValues() throws Exception {
        float[] values = new float[]{10f,20.5f,0.1f,55.5f,180.5f};

        Connection connection = new Connection("10.0.0.148", 10001, 5000, SimpleProtocol.getInstance());
        agilor.distributed.communication.client.Client client = new agilor.distributed.communication.client.Client(connection);
        client.open();

        Calendar time = Calendar.getInstance();

        time.set(Calendar.YEAR,2015);
        time.set(Calendar.MONTH,9);
        time.set(Calendar.DAY_OF_MONTH, 14);
        time.set(Calendar.HOUR,10);
        time.set(Calendar.MINUTE, 10);
        time.set(Calendar.SECOND, 9);
        time.set(Calendar.MILLISECOND, 10);




        for(int i=0;i<values.length;i++) {
            Value val = new Value(Value.Types.FLOAT);
            val.setFvalue(values[i]);
            time.add(Calendar.SECOND, 1);
            val.setTime(time);
            Thread.sleep(1000);
            client.addValue("Simu11_1", "Simu11", val);
        }

        client.close();


    }


    @Test
    public void getValue() throws Exception {

        Connection connection = new Connection("10.0.0.148", 10001, 5000, SimpleProtocol.getInstance());
        agilor.distributed.communication.client.Client client = new agilor.distributed.communication.client.Client(connection);
        client.open();


        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        start.set(Calendar.YEAR,2015);
        start.set(Calendar.MONTH,10);
        start.set(Calendar.DAY_OF_MONTH,18);
        start.set(Calendar.HOUR_OF_DAY,15);
        start.set(Calendar.MINUTE, 38);
        start.set(Calendar.SECOND,35);


        end.set(Calendar.YEAR, 2015);
        end.set(Calendar.MONTH,10);
        end.set(Calendar.DAY_OF_MONTH, 18);
        end.set(Calendar.HOUR_OF_DAY,15);
        end.set(Calendar.MINUTE, 38);
        end.set(Calendar.SECOND, 39);


//        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println( myFormatter.format(start.getTime()));
//
//        System.out.println(start.getTimeInMillis()/1000);
//        System.out.println(end.getTimeInMillis()/1000);
//
//
//
//        Calendar start_2 = Calendar.getInstance();
//        Calendar end_2 =Calendar.getInstance();
//
//
//        start_2.set(2015, 10, 18, 15, 38, 35);
//        end_2.set(2015, 10, 18, 15, 38,39);
//
//
//        System.out.println(myFormatter.format(start_2.getTime()));
//        System.out.println(start_2.getTimeInMillis()/1000);
//        System.out.println(end_2.getTimeInMillis()/1000);
//
//
//        if(true) return;

        //System.out.println();







        ValueCollection collection = client.getValue(start, end, "Simu11_1");

        if(collection!=null) {
            for (Value it : collection) {
                System.out.println(it.getFvalue());
            }
        }


        client.close();



//        ValueCollection list = new ValueCollection();
//        for(int i=0;i<2;i++) {
//            Value it = new Value(Value.Types.FLOAT);
//            it.setFvalue(20.5f);
//            list.add(it);
//        }
//
//
//
//        Protocol protocol = SimpleProtocol.getInstance();
//        byte[] data = protocol.resolve(list);
//
//
//
//
//        Assemble assemble = protocol.assemble(data, 0, data.length);
//        Token token = assemble.next();
//
//
//
//
//
//        ValueCollection collection = token.toClass(ValueCollection.class);
//
//
//
//        for (Value i:collection) {
//
//            System.out.println(i.getFvalue());
//        }
    }


    public void set_value(int count,agilor.distributed.communication.client.Client client) throws Exception {
        long start = System.currentTimeMillis();
        Value value = new Value(Value.Types.FLOAT);
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            value.setFvalue(random.nextFloat());
            client.addValue("Simu11_1", "Simu11", value);
        }

        long s = (System.currentTimeMillis() - start);
        System.out.println("write value  " + count + " times ,the time is " + s+" ,average "+(s*1.0/count));
    }

    /**
     * 写值 压力测试
     * @throws Exception
     */
    @Test
    public void set_value_pressure_test() throws Exception {
        Connection connection = new Connection("10.0.0.148", 10001, 5000, SimpleProtocol.getInstance());
        agilor.distributed.communication.client.Client client = new agilor.distributed.communication.client.Client(connection);
        client.open();
        set_value(1, client);

        set_value(1,client);
        set_value(1,client);
        set_value(1,client);

        set_value(100,client);
        set_value(1000,client);


        //set_value(100000,client);

        client.close();
    }



    @Test
    public void set_value_pressure_test2() throws Exception {
        Connection connection = new Connection("11.0.0.16", 3200, 5000, SimpleProtocol.getInstance());
        agilor.distributed.communication.client.Client client = new agilor.distributed.communication.client.Client(connection);
        client.open();
        Value val = new Value(Value.Types.FLOAT);
        val.setFvalue(102.5f);
        long st=System.currentTimeMillis();
        for(int j=1;j<=1000;j++) {
            for (int i = 0; i < 10; i++) {
                val.setFvalue(102.5f + i);
                client.addValue("T" + String.valueOf(j), "Simu1", val);
            }
        }

        System.out.println(connection.timm_all);
        System.out.println(System.currentTimeMillis() - st);

        client.close();;
    }


    @Test
    public  void  test_socket() throws IOException {
        Socket socket = new Socket("11.0.0.16", 3200);
        socket.setSoTimeout(2000);


        byte[] data = new byte[35];
        for (int i = 0; i < data.length; i++) {
            data[i] = 5;
        }

        DataInputStream in = new DataInputStream(socket.getInputStream());

        DataOutputStream os = new DataOutputStream(socket.getOutputStream());


        long st = System.currentTimeMillis();

        for (int j = 1; j <= 1000; j++) {
            for (int i = 0; i < 10; i++) {
                os.write(data);
                os.flush();

                 in.readInt();

                 in.readByte();
            }
        }

        System.out.println(System.currentTimeMillis() - st);
    }

    @Test
    public void get_all_target() throws Exception {
        Connection connection = new Connection("10.0.0.148", 10001, 1000*60, SimpleProtocol.getInstance());
        agilor.distributed.communication.client.Client client = new agilor.distributed.communication.client.Client(connection);
        client.open();


        TargetCollection collection = client.getAllTag();
        if(collection!=null)
        {
            System.out.println(collection.size());
            for (Target it:collection) {
                System.out.println("name:" + it.getName() + "       type:" + it.getType().toString());
            }
        }
        else
            System.out.print("null");
        client.close();
    }







}
