import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import agilor.distributed.relational.data.entities.User;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Created by LQ on 2015/12/21.
 */



public class Program {
    public static void main(String[] args) throws IOException, SQLException {


        int size = 10000;
//
//        Reader config = Resources.getResourceAsReader("ibatis.xml");
//        SqlSessionFactory builder = new SqlSessionFactoryBuilder().build(config);
//        SqlSession session = builder.openSession();


        long start = System.currentTimeMillis();

        //插入
//        for(int i=0;i<size;i++) {
//
//
//            relational.data.manage.entities.User data = new relational.data.manage.entities.User();
//            data.setUserName(String.valueOf(i)+"-ibatis");
//            data.setPassword(String.valueOf(i));
//            session.insert("relational.data.manage.entities.User.add", data);
//            session.commit();
//        }
        //查询

//        for(int i=5;i<size+5;i++) {
//            relational.data.manage.entities.User u = session.selectOne("relational.data.manage.entities.User.select-sigle", i);
//        }
        //System.out.println(u.getUserName());


        //System.out.println("ibatis:" + (System.currentTimeMillis()-start));


        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/agilor.distributed", "root", "1234");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Statement stat = conn.createStatement();

        start = System.currentTimeMillis();
//        for(int i=0;i<size;i++) {
//
//
//            relational.data.manage.entities.User data = new relational.data.manage.entities.User();
//            data.setUserName(String.valueOf(i)+"-jdbc");
//            data.setPassword(String.valueOf(i));
//
//            stat.execute("INSERT INTO users (username,password,dateCreated) VALUES " +
//                    "('" + data.getUserName() + "','" + data.getPassword() + "','" + format.format(data.getDateCreated()) + "')");
//        }

        //for (int i = 5; i < size + 5; i++) {
            ResultSet set = stat.executeQuery("SELECT * FROM USERS WHERE id=100");
            //ResultSet set = stat.executeQuery("SELECT * FROM USERS WHERE id=" + i);
            if (set.next()) {
                User u = new User();
                u.setId(set.getInt("id"));
                u.setUserName(set.getString("userName"));
                u.setPassword(set.getString("password"));
                u.setDateCreated(set.getDate("dateCreated"));
            }
        //}


        System.out.println("jdbc:" + (System.currentTimeMillis() - start));


        stat.close();
        conn.close();


        C3p0Plugin cp = new C3p0Plugin("jdbc:mysql://localhost:3306/agilor.distributed", "root", "1234");

        ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);

        //arp.addMapping("users", User.class);

        cp.start();
        arp.start();

        start = System.currentTimeMillis();
        //for (int i = 5; i < size + 5; i++) {


            //Model<User> model = User.dao.findById(100);

//            User data = new User();
//            data.setPassword(model.getStr("password"));
//            data.setId(model.getInt("id"));
//            data.setUserName(model.getStr("userName"));
//            data.setDateCreated(model.getDate("dateCreated"));
        //}





        //System.out.println(data.getUserName());
        System.out.println("jFinal:" + (System.currentTimeMillis() - start));

        cp.stop();
        arp.stop();






    }
}
