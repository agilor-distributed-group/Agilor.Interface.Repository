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
   4:i16 type,
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


exception DeviceIsExistException{
   1:string name;
}
exception DeviceNotFoundException{
   1:string name;
}

exception HandleErrorException{
   1:i32 hwnd
}
exception WriteValueException{
   1:string name,
   2:TAGVAL val
}


service Agilor{
//�������
 void ping();
 



 //�豸���
 bool AddDevice(1:DEVICE device) throws (1:DeviceIsExistException e);
 bool DeleteDevice(1:string deviceName) throws (1:DeviceNotFoundException e);
 bool ModifyDevice(1:DEVICE device);
 i32 QueryDeviceInfo();
 DEVICE EnumDeviceInfo(1:i32 hRecordset) throws (1:HandleErrorException e);
 list<DEVICE> GetAllDevices();
 i32 TagCountByDevice(1:string deviceName);
 
//�����
 i32  QuerySnapshots(1:string tagNames,2:i32 tagCount); //��ȡ�㼯��
 TAGVAL GetNextTagValue(1:i32 hRecordset,2:bool isRemoved);//���ݽ���������ȡ��ֵ
 i32 QueryTagHistory(1:string tagName,2:i32 startTime,3:i32 endTime,4:i32 step);//tagNameʵ��ΪfullName,��̨���������� 
 TAGVAL GetAggregateValue(1:i32 hRecordset,2:AGGREGATE ag,3:bool isRemoved);
 i32 QueryTagsbyDevice(1:string deviceName);
 string EnumTagName(1:i32 hRecordset);
 string GetTagNamebyId(1:i32 tagId);
 //i32 SetTagValue(1:string tagName,2:string value,3:bool isManual,4:string comment);//tagName ʵ��ΪfullName,��̨����������
 i32 SetValue(1:TAGVAL value) throws (1:WriteValueException e);
 i32 SetValues(1:list<TAGVAL> values) throws (1:WriteValueException e);
 
 i32 RemoveTag(1:i32 tagId);
 TAGNODE GetTagInfo(1:string tagName);//tagName ʵ��ΪfullName����̨����������
 i32 AddNewTag(1:TAGNODE node,2:bool isOverwrite) throws (1:WriteValueException e);
 TAGNODE EnumNextTag(1:i32 hwnd);

 
 //���ĵ����
 i32 SubscribeTags(1:string tagNames,2:i16 count);//���ĵ㼯��
 TAGVAL GetSubTagValue();//��ȡ���ĵĵ�ֵ
 i32 UnSubscribeTags(1:string tagNames,2:i16 count);//ȡ���㶩��
 i32 UnSubscribeAll();//ȡ�����е�Ķ���

 
 
 
 
 
 
 
}




