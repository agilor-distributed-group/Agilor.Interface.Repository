package agilor.distributed.relational.data.services;

import agilor.distributed.relational.data.context.RequestContext;
import agilor.distributed.relational.data.db.DB;
import agilor.distributed.relational.data.entities.User;
import agilor.distributed.relational.data.exceptions.ExceptionTypes;
import agilor.distributed.relational.data.exceptions.NullParameterException;
import agilor.distributed.relational.data.exceptions.ValidateParameterException;
import com.jfinal.plugin.activerecord.Model;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by LQ on 2015/12/23.
 */
public class UserService {

    private RequestContext context=null;


    public UserService(RequestContext context) {
        this.context = context;
    }





    /**
     * 注册
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     * @throws NullParameterException     空字符串异常
     * @throws ValidateParameterException 字段验证不同过异常
     */
    public User register(String userName, String password) throws NullParameterException, ValidateParameterException {

        if (StringUtils.isEmpty(userName))
            throw new NullParameterException("userName");

        //缺少验证是否含有特殊字符

        if (userName.length() < 6)
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

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User check(String userName, String password) throws NullParameterException, ValidateParameterException {
        if (StringUtils.isEmpty(userName))
            throw new NullParameterException("userName");

        //缺少验证是否含有特殊字符

        if (userName.length() < 6)
            throw new ValidateParameterException("userName", ExceptionTypes.STRTOOSHORT);

        if (userName.length() > 45)
            throw new ValidateParameterException("userName", ExceptionTypes.STRTOOLONG);


        if (StringUtils.isEmpty(password))
            throw new NullParameterException("password");
        if (password.length() < 6)
            throw new ValidateParameterException("password", ExceptionTypes.STRTOOSHORT);
        if (password.length() > 45)
            throw new ValidateParameterException("password", ExceptionTypes.STRTOOLONG);


        Model<DB.User> model = DB.User.instance().findFirst("SELECT * FROM users WHERE userName='{}' and password='{}'", userName, DigestUtils.md5Hex(password));

        if (model.get("id") != null) {
            User data = new User();
            data.setId(model.getInt("id"));
            data.setPassword(model.getStr("password"));
            data.setUserName(model.getStr("userName"));
            data.setDateCreated(model.getDate("dateCreated"));
            return data;
        }
        return null;
    }


    /***
     * 登录（未完成）
     * @param user
     */
    public void login(User user) {

        //写到zookeeper中
    }

    /**
     * 退出登录(未完成)
     * @param user
     */
    public void logout(User user)
    {
        //从zookeeper中删除
    }


    /**
     * 获取所有用户
     * @return
     */
    public List<User> all() {
        List<DB.User> list = DB.User.instance().find("select * from users");

        List<User> result = new ArrayList<>(list.size());

        for (DB.User it : list) {
            User model = new User();
            model.setId(it.getInt("id"));
            model.setUserName(it.getStr("userName"));
            model.setPassword(it.getStr("password"));
            model.setDateCreated(it.getDate("dateCreated"));
        }
        return result;
    }


    public void activate(User user)
    {
        //if(user.getId())
    }



}
