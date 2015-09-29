package agilor.distributed.storage.inter.jlient;



import com.sun.istack.NotNull;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by LQ on 2015/8/12.
 * 点值，以后会根据需要实现 +, -, x, /
 */
public class Val implements Serializable {


    protected Object value;

    protected ValType type;

    protected Calendar time;


    public Val()
    {
        this.value=0;
        this.type= ValType.FLOAT;
        this.time=Calendar.getInstance();
    }


    public Val(Val val)
    {
        this.value=val.value;
        this.type=val.type;
        this.time=val.time;
    }


    public Val(String value,@NotNull Calendar time) {
        this.value = value;
        this.type = ValType.STRING;
        this.time = time;
    }

    public Val(float value,@NotNull Calendar time) {
        this.value = value;
        this.type = ValType.FLOAT;
        this.time = time;
    }

    public Val(boolean value,@NotNull Calendar time) {
        this.value = value;
        this.type = ValType.BOOLEAN;
        this.time = time;
    }

    public Val(String value)
    {
        this(value, Calendar.getInstance());
    }
    public Val(float value) {
        this(value, Calendar.getInstance());
    }

    public Val(boolean value) {
        this(value, Calendar.getInstance());
    }


    public Val(ValType type)
    {
        this.type = type;
    }





    public Object getObject() {
        return value;
    }


    public ValType getType() {
        return type;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    @Override
    public String toString()
    {
        if(value==null)
        {
            switch (type)
            {
                case BOOLEAN:return new Boolean(false).toString();
                case FLOAT:return new Integer(0).toString();
                case STRING:return "";

            }
        }
        return this.value.toString();
    }
}
