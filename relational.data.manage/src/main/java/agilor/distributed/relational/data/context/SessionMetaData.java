package agilor.distributed.relational.data.context;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by LQ on 2015/12/25.
 */
public class SessionMetaData implements Serializable {

    private String            id;
    /**session的创建时间*/
    private Long              createTime;
    /**session的最大空闲时间*/
    private Long              maxIdle;
    /**session的最后一次访问时间*/
    private Long              lastAccessTime;
    /**是否可用*/
    private Boolean           validate         = false;
    /**当前版本*/
    private int               version          = 0;


    public SessionMetaData(Object data)
    {

    }



    public SessionMetaData(byte[] data)
    {

    }

    public SessionMetaData(){}


    public byte[] serializable()
    {
        return null;
    }




    public String getKey() {
        if (StringUtils.isEmpty(id))
            id = UUID.randomUUID().toString();

        return id;
    }

    public boolean isValid() {

        return System.currentTimeMillis() - lastAccessTime > Config.global.getSessionTimeOut();
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Long maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public Boolean getValidate() {
        return validate;
    }

    public void setValidate(Boolean validate) {
        this.validate = validate;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
