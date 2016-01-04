package agilor.distributed.relational.data.services;

import agilor.distributed.relational.data.context.RequestContext;
import agilor.distributed.relational.data.db.DB;
import agilor.distributed.relational.data.entities.User;
import agilor.distributed.relational.data.exceptions.ExceptionTypes;
import agilor.distributed.relational.data.exceptions.NullParameterException;
import agilor.distributed.relational.data.exceptions.SqlHandlerException;
import agilor.distributed.relational.data.exceptions.ValidateParameterException;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Model;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by LQ on 2015/12/23.
 */
public class UserService {

    /**
     * 注册（通过）
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     * @throws NullParameterException     空字符串异常
     * @throws ValidateParameterException 字段验证不同过异常
     */
    public User register(String userName, String password) throws NullParameterException, ValidateParameterException, SqlHandlerException {

        if (StringUtils.isEmpty(userName))
            throw new NullParameterException("userName");

        //缺少验证是否含有特殊字符

        if (userName.length() < 5)
            throw new ValidateParameterException("userName", ExceptionTypes.STRTOOSHORT);

        if (userName.length() > 45)
            throw new ValidateParameterException("userName", ExceptionTypes.STRTOOLONG);


        if (StringUtils.isEmpty(password))
            throw new NullParameterException("password");
        if (password.length() < 6)
            throw new ValidateParameterException("password", ExceptionTypes.STRTOOSHORT);
        if (password.length() > 45)
            throw new ValidateParameterException("password", ExceptionTypes.STRTOOLONG);


        try {
            Model<DB.User> model = new DB.User();

            model.set("userName", userName)
                    .set("password", DigestUtils.md5Hex(password))
                    .set("dateCreated", new Date())
                    .save();
            User data = new User();
            data.setId(model.getInt("id"));
            data.setUserName(userName);
            data.setPassword(password);
            data.setDateCreated(model.getDate("dateCreated"));
            return data;



        }
        catch (ActiveRecordException e) {
            throw new SqlHandlerException(e.getCause());
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证用户是否存在(通过)
     * @param userName
     * @param password
     * @return
     * @throws NullParameterException
     * @throws ValidateParameterException
     */
    public User check(String userName, String password) throws NullParameterException, ValidateParameterException {
        if (StringUtils.isEmpty(userName))
            throw new NullParameterException("userName");

        //缺少验证是否含有特殊字符

        if (userName.length() < 5)
            throw new ValidateParameterException("userName", ExceptionTypes.STRTOOSHORT);

        if (userName.length() > 45)
            throw new ValidateParameterException("userName", ExceptionTypes.STRTOOLONG);


        if (StringUtils.isEmpty(password))
            throw new NullParameterException("password");
        if (password.length() < 6)
            throw new ValidateParameterException("password", ExceptionTypes.STRTOOSHORT);
        if (password.length() > 45)
            throw new ValidateParameterException("password", ExceptionTypes.STRTOOLONG);


        Model<DB.User> model = DB.User.instance().findFirst("SELECT * FROM users WHERE userName=? and password=?", userName, DigestUtils.md5Hex(password));

        if (model != null) {
            User data = new User();
            data.setId(model.getInt("id"));
            data.setPassword(model.getStr("password"));
            data.setUserName(model.getStr("userName"));
            data.setDateCreated(model.getDate("dateCreated"));
            return data;
        }
        return null;
    }



    /**
     * 获取所有用户
     * @return
     */
    public List<User> all() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<DB.User> list = DB.User.instance().find("select * from users");
        if(list!=null) {
            List<User> result = new ArrayList<>(list.size());

            for (DB.User it : list) {
                result.add(it.build(User.class));
            }
            return result;
        }
        return null;
    }


//    public void activate(User user)
//    {
//        //if(user.getId())
//    }



}
