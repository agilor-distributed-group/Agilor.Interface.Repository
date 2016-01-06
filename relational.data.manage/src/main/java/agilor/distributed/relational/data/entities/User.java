package agilor.distributed.relational.data.entities;

import com.jfinal.plugin.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LQ on 2015/12/21.
 */
public class User implements Serializable {

    private int id;
    private String userName;
    private String password;
    private Date dateCreated;

    public User() {
        setDateCreated(new Date());
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
