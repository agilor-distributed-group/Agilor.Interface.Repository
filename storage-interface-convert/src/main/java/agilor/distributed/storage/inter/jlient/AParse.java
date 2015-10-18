package agilor.distributed.storage.inter.jlient;

import agilor.distributed.storage.inter.thrift.DEVICE;
import agilor.distributed.storage.inter.thrift.TAGNODE;
import agilor.distributed.storage.inter.thrift.TAGVAL;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Calendar;

/**
 * Created by LQ on 2015/8/12.
 * 主要用于thrift接口转换
 */
public class AParse {

    public static Device parse(DEVICE data)
    {
        Device r = new Device(data.name);
        return r;
    }


    public static DEVICE parse(Device data) {
        DEVICE r = new DEVICE();
        r.setName(data.getName());
        return r;
    }

    public static Val parse(TAGVAL data) {

        Val val = parse(data.value,ValType.value(data.type));
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis((long)(data.timestamp)*1000);
        val.setTime(c);
        return val;
    }



    public static TAGVAL parse(Val data)
    {
        TAGVAL VAL = new TAGVAL();
        VAL.setValue(data.toString());
        VAL.setType(data.getType().value());
        VAL.setTimestamp((int)(data.getTime().getTimeInMillis()/1000));
        return VAL;
    }


    public static TAGNODE parse(Target data) {
        TAGNODE r = new TAGNODE();
        r.setName(data.getName());
        r.setType((byte) data.getValue().getType().value());

        r.setSourceTag(data.getName());
        r.setDeviceName(data.getDeviceName());
        r.setAlarmHi(data.getAlarmHi());
        r.setAlarmHiHi(data.getAlarmHiHi());
        r.setAlarmLo(data.getAlarmLo());
        r.setAlarmLolo(data.getAlarmLolo());
        r.setAlarmState(data.getAlarmState());
        r.setAlarmType(data.getAlarmType());
        r.setIsArchived(data.getIsArchived());
        r.setCompressMax(data.getCompressMax());
        r.setCompressMin(data.getCompressMin());
        r.setCreationDate((int)data.getDateCreated().getTimeInMillis() / 1000);
        r.setDesc(data.getDesc());
        r.setDeviceName(data.getDeviceName());
        r.setEngUnit(data.getEngUnit());
        r.setEnumDesc(data.getEnumDesc());
        r.setExceptionDev(data.getExceptionDev());
        r.setExceptionMax(data.getExceptionMax());
        r.setExceptionMin(data.getExceptionMin());
        r.setGroupName(data.getGroupName());
        r.setHihiPriority(data.getHihiPriority());
        r.setHiPriority(data.getHiPriority());
        r.setHisIndex(data.getHisIndex());
        r.setId(data.getId());
        r.setInterMethod(data.getInterMethod());
        r.setIOState(data.getIOState());
        r.setIsCompressed(data.isCompressed());
        r.setLastValue(data.getLastValue());
        r.setLoloPriority(data.getLoloPriority());
        r.setLoPriority(data.getLoPriority());
        r.setLowerLimit(data.getLowerLimit());
        r.setName(data.getName());
        r.setPushreference(data.getPushreference());
        r.setRuleReference(data.getRuleReference());
        r.setSourceTag(data.getSourceTag());
        r.setState(data.getState());
        r.setTimestamp((int)data.getTimestamp());
        r.setTypicalVal(data.getTypicalVal());
        r.setUpperLimit(data.getUpperLimit());
        r.setValue(data.getValue().toString());
        r.setSourceTag(data.getName());

        return r;
    }

    public static Val parse(String data,ValType type)
    {
        switch (type)
        {
            case FLOAT: return new Val(Float.parseFloat(data));
            case STRING:return new Val(data);
            case BOOLEAN:return new Val(Boolean.parseBoolean(data));
            default:return null;
        }
    }

    public static Target parse(TAGNODE data)
    {
        Target r = new Target(data.getName(),ValType.value(data.type));
        r.setName(data.getName());

        r.setValue(parse(data.value, ValType.value(data.type)));
        r.setSourceTag(data.getName());
        r.setDeviceName(data.getDeviceName());
        r.setAlarmHi(data.getAlarmHi());
        r.setAlarmHiHi(data.getAlarmHiHi());
        r.setAlarmLo(data.getAlarmLo());
        r.setAlarmLolo(data.getAlarmLolo());
        r.setAlarmState(data.getAlarmState());
        r.setAlarmType(data.getAlarmType());
        r.setIsArchived(data.isIsArchived());
        r.setCompressMax(data.getCompressMax());
        r.setCompressMin(data.getCompressMin());
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(data.creationDate*1000);
        r.setDateCreated(c);
        r.setDesc(data.getDesc());
        r.setDeviceName(data.getDeviceName());
        r.setEngUnit(data.getEngUnit());
        r.setEnumDesc(data.getEnumDesc());
        r.setExceptionDev(data.getExceptionDev());
        r.setExceptionMax(data.getExceptionMax());
        r.setExceptionMin(data.getExceptionMin());
        r.setGroupName(data.getGroupName());
        r.setHihiPriority(data.getHihiPriority());
        r.setHiPriority(data.getHiPriority());
        r.setHisIndex(data.getHisIndex());
        r.setId(data.getId());
        r.setInterMethod(data.getInterMethod());
        r.setIOState(data.getIOState());
        r.setIsCompressed(data.isIsCompressed());
        r.setLastValue(data.getLastValue());
        r.setLoloPriority(data.getLoloPriority());
        r.setLoPriority(data.getLoPriority());
        r.setLowerLimit(data.getLowerLimit());
        r.setName(data.getName());
        r.setPushreference(data.getPushreference());
        r.setRuleReference(data.getRuleReference());
        r.setSourceTag(data.getSourceTag());
        r.setState(data.getState());
        r.setTimestamp(data.getTimestamp());
        r.setTypicalVal(data.getTypicalVal());
        r.setUpperLimit(data.getUpperLimit());

        return r;
    }


}
