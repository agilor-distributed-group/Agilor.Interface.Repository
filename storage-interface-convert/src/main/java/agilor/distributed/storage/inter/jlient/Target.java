package agilor.distributed.storage.inter.jlient;

import agilor.distributed.storage.inter.client.IClient;
import agilor.distributed.storage.inter.thrift.TAGVAL;
import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by LQ on 2015/8/10.
 *
 */
public class Target extends AWidget {

    private final   static  int CACHE_MAX= 1000;

    private int id;
    private String name;
    private Val value;
    private String deviceName;
    private Calendar dateCreated;

    public String desc; // required
    public String engUnit; // required
    //public short type; // required
    public byte IOState; // required
    public double typicalVal; // required
    public String enumDesc; // required
    public long timestamp; // required
    public int state; // required
    public String groupName; // required
    public String sourceTag; // required
    public double upperLimit; // required
    public double lowerLimit; // required
    public short pushreference; // required
    public short ruleReference; // required
    public int exceptionMin; // required
    public int exceptionMax; // required
    public double exceptionDev; // required
    public short alarmType; // required
    public short alarmState; // required
    public double alarmHi; // required
    public double alarmLo; // required
    public double alarmHiHi; // required
    public double alarmLolo; // required
    public short hiPriority; // required
    public short loPriority; // required
    public short hihiPriority; // required
    public short loloPriority; // required
    public boolean isArchived; // required
    public boolean isCompressed; // required
    public byte interMethod; // required
    public int hisIndex; // required
    public int compressMin; // required
    public int compressMax; // required
    public double lastValue; // required


    private IClient client=null;

    private List<Val> cache = Collections.synchronizedList(new ArrayList<Val>());


    @Deprecated
    public  Target(Agilor agilor) {
        this();
        this.agilor = agilor;

    }

    @Deprecated
    public Target(Device device) {

        this.dateCreated = Calendar.getInstance();
        this.value=new Val();
        this.IOState=1;
    }
    @Deprecated
    public Target() {
        this.dateCreated = Calendar.getInstance();
        this.IOState = 1;
        this.value=new Val();
    }


    public Target(String name,ValType type)
    {
        this.dateCreated = Calendar.getInstance();
        this.IOState=1;
        this.value = new Val(type);
        this.name=name;
    }

    /**
     * 获取历史点值(未测试)
     * @param calendar 时间点
     * @return
     */
    public Val history(Calendar calendar) throws TException, NoSuchFieldException, IllegalAccessException {

        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(calendar.getTimeInMillis());
        end.add(Calendar.SECOND,1);
        List<Val> list = histories(calendar,end);
        if(list==null||list.size()==0)return null;
        return list.get(0);
    }

    /**
     * 获取某段时间内的历史点
     * @param start 起始时间
     * @param end 终止时间
     * @return
     * @throws TException
     */
    public List<Val> histories(Calendar start,Calendar end) throws TException, NoSuchFieldException, IllegalAccessException {

        return histories(start, end, 0);
    }


    /**
     * 获取某段时间内的历史点
     * @param start 起始时间
     * @param end 终止时间
     * @param step 步长
     * @return
     * @throws TException
     */
    public List<Val> histories(Calendar start,Calendar end,long step) throws TException, NoSuchFieldException, IllegalAccessException {
        return histories(start.getTimeInMillis(), end.getTimeInMillis(), step);
    }


    /**
     * 获取某段时间内的数据集合,可设置步长
     * @param start 起始时间
     * @param end 终止时间
     * @param step 步长
     * @return
     * @throws TException
     */
    public List<Val> histories(long start,long end,long step) throws TException, NoSuchFieldException, IllegalAccessException {

        Iterator<Val> it = new HistoryIterator(this, start, end, step);

        List<Val> list = new ArrayList<>();

        while (it.hasNext()) {
            list.add(it.next());
        }

        it.close();
        return list;
    }


    public List<Val> histories(long start,long end) throws TException, NoSuchFieldException, IllegalAccessException {
        return histories(start,end,0);
    }



    public Val getValue() {
        return value;
    }


    public void setValue(Val value) {
        this.value = value;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public void save()
    {
    }

    public void write(Val val) throws TException {
        TAGVAL VAL = AParse.parse(val);
        VAL.setDevice(this.deviceName);
        VAL.setName(this.name);
        agilor.getClient().SetValue(VAL);
    }


    public void write(Val val,boolean noCache) throws TException {

        cache.add(val);
        if(noCache||cache.size()>=CACHE_MAX)
            flush();
    }

    public void write(List<Val> vals) throws TException {

        for (Val i : vals)
            cache.add(i);
        flush();

    }

    public synchronized void flush() throws TException {
        if(!cache.isEmpty())
        {
            List<TAGVAL> list = new ArrayList<>();
            for(Val val:cache)
            {
                TAGVAL VAL =AParse.parse(val);
                VAL.setDevice(this.deviceName);
                VAL.setName(this.name);
                list.add(VAL);
            }
            agilor.getClient().SetValues(list);
            list.clear();
        }
    }



    public void setDateCreated(Calendar dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEngUnit() {
        return engUnit;
    }

    public void setEngUnit(String engUnit) {
        this.engUnit = engUnit;
    }

    public byte getIOState() {
        return IOState;
    }

    public void setIOState(byte IOState) {
        this.IOState = IOState;
    }

    public double getTypicalVal() {
        return typicalVal;
    }

    public void setTypicalVal(double typicalVal) {
        this.typicalVal = typicalVal;
    }

    public String getEnumDesc() {
        return enumDesc;
    }

    public void setEnumDesc(String enumDesc) {
        this.enumDesc = enumDesc;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSourceTag() {
        return sourceTag;
    }

    public void setSourceTag(String sourceTag) {
        this.sourceTag = sourceTag;
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(double upperLimit) {
        this.upperLimit = upperLimit;
    }

    public double getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(double lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public short getPushreference() {
        return pushreference;
    }

    public void setPushreference(short pushreference) {
        this.pushreference = pushreference;
    }

    public short getRuleReference() {
        return ruleReference;
    }

    public void setRuleReference(short ruleReference) {
        this.ruleReference = ruleReference;
    }

    public int getExceptionMin() {
        return exceptionMin;
    }

    public void setExceptionMin(int exceptionMin) {
        this.exceptionMin = exceptionMin;
    }

    public int getExceptionMax() {
        return exceptionMax;
    }

    public void setExceptionMax(int exceptionMax) {
        this.exceptionMax = exceptionMax;
    }

    public double getExceptionDev() {
        return exceptionDev;
    }

    public void setExceptionDev(double exceptionDev) {
        this.exceptionDev = exceptionDev;
    }

    public short getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(short alarmType) {
        this.alarmType = alarmType;
    }

    public short getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(short alarmState) {
        this.alarmState = alarmState;
    }

    public double getAlarmHi() {
        return alarmHi;
    }

    public void setAlarmHi(double alarmHi) {
        this.alarmHi = alarmHi;
    }

    public double getAlarmLo() {
        return alarmLo;
    }

    public void setAlarmLo(double alarmLo) {
        this.alarmLo = alarmLo;
    }

    public double getAlarmHiHi() {
        return alarmHiHi;
    }

    public void setAlarmHiHi(double alarmHiHi) {
        this.alarmHiHi = alarmHiHi;
    }

    public double getAlarmLolo() {
        return alarmLolo;
    }

    public void setAlarmLolo(double alarmLolo) {
        this.alarmLolo = alarmLolo;
    }

    public short getHiPriority() {
        return hiPriority;
    }

    public void setHiPriority(short hiPriority) {
        this.hiPriority = hiPriority;
    }

    public short getLoPriority() {
        return loPriority;
    }

    public void setLoPriority(short loPriority) {
        this.loPriority = loPriority;
    }

    public short getHihiPriority() {
        return hihiPriority;
    }

    public void setHihiPriority(short hihiPriority) {
        this.hihiPriority = hihiPriority;
    }

    public short getLoloPriority() {
        return loloPriority;
    }

    public void setLoloPriority(short loloPriority) {
        this.loloPriority = loloPriority;
    }

    public boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }

    public boolean isCompressed() {
        return isCompressed;
    }

    public void setIsCompressed(boolean isCompressed) {
        this.isCompressed = isCompressed;
    }

    public byte getInterMethod() {
        return interMethod;
    }

    public void setInterMethod(byte interMethod) {
        this.interMethod = interMethod;
    }

    public int getHisIndex() {
        return hisIndex;
    }

    public void setHisIndex(int hisIndex) {
        this.hisIndex = hisIndex;
    }

    public int getCompressMin() {
        return compressMin;
    }

    public void setCompressMin(int compressMin) {
        this.compressMin = compressMin;
    }

    public int getCompressMax() {
        return compressMax;
    }

    public void setCompressMax(int compressMax) {
        this.compressMax = compressMax;
    }

    public double getLastValue() {
        return lastValue;
    }

    public void setLastValue(double lastValue) {
        this.lastValue = lastValue;
    }



}
