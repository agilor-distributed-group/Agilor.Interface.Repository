struct DEVICE
{
  1:string name,
  2:i32 id,
}

struct TAGVAL
{
   1:string name,
   2:i32 timestamp,
   3:i32 state,
   4:byte type,
   5:string value,
  
   6:i32 id,
   7:string device
}


struct TAGNODE
{
  1:string name,
  2:string desc,
  3:string engUnit,
  4:i32 id,
  5:byte type,
  6:byte IOState,
  7:double typicalVal,
  8:string value,
  9:string enumDesc,
  10:i32 timestamp,
  11:i32 state,
  12:string deviceName,
  13:string groupName,
  14:string sourceTag,
  15:double upperLimit,
  16:double lowerLimit,
  17:i16 pushreference,
  18:i16 ruleReference,
  19:i32 exceptionMin,
  20:i32 exceptionMax,
  21:double exceptionDev,
  22:i16 alarmType,
  23:i16 alarmState,
  24:double alarmHi,
  25:double alarmLo,
  26:double alarmHiHi,
  27:double alarmLolo,
  28:i16 hiPriority,
  29:i16 loPriority,
  30:i16 hihiPriority,
  31:i16 loloPriority,
  32:bool isArchived,
  33:bool isCompressed,
  34:byte interMethod,
  35:i32 hisIndex,
  36:i32 compressMin,
  37:i32 compressMax,
  38:double lastValue,
  39:i32 creationDate,
 
}


enum AGGREGATE
{
  AF_SUMMARY=0,
  AF_MINIMUM=1,
  AF_MAXIMUM=2,
  AF_AVERAGE=3,
  AF_COUNT=4,
  AF_SUMMARY_FOR_CONTINOUS=5,
  AF_AVERAGE_FOR_CONTINOUS=6,
}

enum EXCEPTIONTYPE
{
   INSERTFAIL=0,
   REGISTERFAIL=1
}


exception DeviceIsExistException{
   1:string name;
}
exception DeviceNotFoundException{
   1:string name;
}
exception DeviceMaxException{
}

exception DeviceInsertException{
   1:EXCEPTIONTYPE type
}

exception TargetInsertException{
}

exception HandleErrorException{
   1:i32 hwnd
}
exception WriteValueException{
   1:string name,
   2:TAGVAL val
}
exception ParamErrorException{
   1:string name
}




service Agilor{
//连接相关
 void ping();
 



 //设备相关
 void AddDevice(1:DEVICE device) throws (1:DeviceIsExistException a,2:DeviceMaxException b ,3:DeviceInsertException c,4:ParamErrorException d);
 void DeleteDevice(1:string deviceName) throws (1:DeviceNotFoundException e);
 bool ModifyDevice(1:DEVICE device);
 i32 QueryDeviceInfo();
 DEVICE EnumDeviceInfo(1:i32 hRecordset) throws (1:HandleErrorException e);
 list<DEVICE> GetAllDevices();
 i32 TagCountByDevice(1:string deviceName) throws (1:DeviceIsExistException e);
 
//点相关
 i32  QuerySnapshots(1:string tagNames,2:i32 tagCount); //获取点集合
 TAGVAL GetNextTagValue(1:i32 hRecordset,2:bool isRemoved) throws (1:HandleErrorException e);//根据结果集句柄获取点值
 i32 QueryTagHistory(1:string tagName,2:i32 startTime,3:i32 endTime,4:i32 step);//tagName实际为fullName,后台必须进行组合 
 TAGVAL GetAggregateValue(1:i32 hRecordset,2:AGGREGATE ag,3:bool isRemoved);
 i32 QueryTagsbyDevice(1:string deviceName) throws (1:DeviceNotFoundException e);
 string EnumTagName(1:i32 hRecordset) throws (1:HandleErrorException e);
 string GetTagNamebyId(1:i32 tagId);

 void SetValue(1:TAGVAL value) throws (1:WriteValueException e);
 void SetValues(1:list<TAGVAL> values) throws (1:WriteValueException e);
 
 void RemoveTag(1:i32 tagId);
 TAGNODE GetTagInfo(1:string tagName);//tagName 实际为fullName，后台必须进行组合
 void AddNewTag(1:TAGNODE node,2:bool isOverwrite) throws (1:WriteValueException a,2:TargetInsertException b,3:DeviceNotFoundException c,4:ParamErrorException d);
 TAGNODE EnumNextTag(1:i32 hwnd) throws (1:HandleErrorException e);

 
 //订阅点相关
 i32 SubscribeTags(1:string tagNames,2:i16 count);//订阅点集合
 TAGVAL GetSubTagValue();//获取订阅的点值
 i32 UnSubscribeTags(1:string tagNames,2:i16 count);//取消点订阅
 i32 UnSubscribeAll();//取消所有点的订阅

 
 
 
 
 
 
 
}




