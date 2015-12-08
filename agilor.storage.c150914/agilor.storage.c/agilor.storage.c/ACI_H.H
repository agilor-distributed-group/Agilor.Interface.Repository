//*****************************************
//***      Agilor Call Interface        ***
//***   Right reserved by IEL@ISCAS     ***
//***       2004.2 created by wq        ***
//*****************************************

#ifndef _AGILOR_CALL_INTERFACE_H_
#define _AGILOR_CALL_INTERFACE_H_

//****************************
#define MAX_SERVER_NUMBER		4	//the max number of server supported
#define EVENT_WAIT_TIMEOUT		30000
#define AGILOR_SERVICE_PORT		900
#define SUB_DATA_BUFFER_SIZE	30000

//******服务器节点连接状态******
#define AG_NODE_STATE_INVALID		0
#define AG_NODE_STATE_CONNECTED		1
#define AG_NODE_STATE_DISCONNECTED	2

#ifdef __cplusplus
extern "C" {
#endif  /* __cplusplus */

//*********窗口消息或者事件*********
#define	WM_SUBDATAARRIVAL		WM_USER+101	//monitor the arrival of sub data
#define	WM_DISCONNECTED			WM_USER+102	//monitor the connection with server
#define EVENT_SUBDATAARRIVAL	"AGNE_SUBDATAARRIVAL"
#define EVENT_DISCONNECTED		"AGNE_DISCONNECTED"	


//wk, 2005.10.20, add:
#ifdef __cplusplus


//****** 关键字段长度定义 ******
const DWORD C_FULL_TAGNAME_LEN	=	80;	//SERVERNAME.TAGNAME, sucha as LGCAG.ZL_AI1001
const DWORD C_SERVERNAME_LEN	=	16;	
const DWORD C_SERVERADDR_LEN	=	16;
const DWORD C_USERNAME_LEN		=	32;
const DWORD C_PASSWORD_LEN		=	16;
const DWORD C_TAGNAME_LEN		=	64;	//maybe some tags on different server have the same name
const DWORD C_TAGDESC_LEN		=	32;
const DWORD C_TAGUNIT_LEN		=	16;
const DWORD C_DEVICENAME_LEN	=	32;
const DWORD C_GROUPNAME_LEN		=	32;
const DWORD C_STRINGVALUE_LEN	=	128;
const DWORD C_SOURCETAG_LEN		=	128;	//the physical tag on devices
const DWORD C_ENUMDESC_LEN		=	128;	

//****** 数据点报警状态位 *********
const WORD ALARM_TYPE_HILIMIT_MASK		= 0x0001;	//高报警 
const WORD ALARM_TYPE_LOLIMIT_MASK		= 0x0002;	//低报警 
const WORD ALARM_TYPE_HIHILIMIT_MASK	= 0x0004;	//高高报警
const WORD ALARM_TYPE_LOLOLIMIT_MASK	= 0x0008;	//低低报警
const WORD ALARM_TYPE_SWITCHON_MASK		= 0x0010;	//开报警 
const WORD ALARM_TYPE_SWITCHOFF_MASK	= 0x0020;	//关报警 

//****** 启动数据采集标志 *********
const BYTE SCAN_INPUT		= 0x01;	//输入允许
const BYTE SCAN_OUTPUT		= 0x02;	//输出允许
const BYTE SCAN_DISABLE		= 0x80;	//禁止I/O

//wk, 2005.10.20, add:
#endif

//wk, 2005.10.20, add ->

#ifndef __cplusplus

//****** 关键字段长度定义 ******
#define C_FULL_TAGNAME_LEN	80	//SERVERNAME.TAGNAME, sucha as LGCAG.ZL_AI1001
#define C_SERVERNAME_LEN	16	
#define C_SERVERADDR_LEN	16
#define C_USERNAME_LEN		32
#define C_PASSWORD_LEN		16
#define C_TAGNAME_LEN		64	//maybe some tags on different server have the same name
#define C_TAGDESC_LEN		32
#define C_TAGUNIT_LEN		16
#define C_DEVICENAME_LEN	32
#define C_GROUPNAME_LEN		32
#define C_STRINGVALUE_LEN	128
#define C_SOURCETAG_LEN		128	//the physical tag on devices
#define C_ENUMDESC_LEN		128	

//****** 数据点报警状态位 *********
#define ALARM_TYPE_HILIMIT_MASK		0x0001	//高报警 
#define ALARM_TYPE_LOLIMIT_MASK		0x0002	//低报警 
#define ALARM_TYPE_HIHILIMIT_MASK	0x0004	//高高报警
#define ALARM_TYPE_LOLOLIMIT_MASK	0x0008	//低低报警
#define ALARM_TYPE_SWITCHON_MASK	0x0010	//开报警 
#define ALARM_TYPE_SWITCHOFF_MASK	0x0020	//关报警 

//****** 启动数据采集标志 *********
#define SCAN_INPUT		0x01	//输入允许
#define SCAN_OUTPUT		0x02	//输出允许
#define SCAN_DISABLE	0x80	//禁止I/O

#endif

#define DEVICE_STATE_TAG_NAME_PREFIX	"%#_DeviceState_"

//<- wk, 2005.10.20, add

#include <tchar.h>

//****** 历史数据统计函数 *********
/* wk, 2005.11.17, del:
#define SUMMARY_ARCTOTAL	1		//统计求和
#define SUMMARY_ARCMINIMUM	2		//最大值
#define SUMMARY_ARCMAXIMUM	3		//最小值
#define SUMMARY_ARCRANGE	4		//变化范围
#define SUMMARY_ARCAVERAGE	5		//算术平均值
*/

typedef struct struServer_Info
{
	_TCHAR	szServerName[C_SERVERNAME_LEN];
	_TCHAR szServerAddr[C_SERVERADDR_LEN];
	_TCHAR szUserName[C_USERNAME_LEN];
	_TCHAR szPassword[C_PASSWORD_LEN];
	BOOL bIsConnected;
}ACI_SERVER_INFO;

typedef struct struDevice_Info{
	_TCHAR szDeviceName[C_DEVICENAME_LEN];
	BOOL bIsOnline;
}DEV_INFO;

typedef struct struTagNode
{
	char	name[C_TAGNAME_LEN];		//@*采集数据点名(必须)
	char	descriptor[32];		//*描述
	char	engunits[16];		//*采集点数据单位
	long	pointid;		//@采集数据点编号
	BYTE	pointtype;		//*@采集点数据类型//(R浮点数/S字符串/B开关/L整形/E枚举)
	BYTE	scan;			//*@启动数据采集标志(IO status)	  0或>=0x80("禁止"),1("输入"),2("输出")
	WORD    reserved1;
	float	typicalvalue;		//典型值//WQ@2003-9-10
	//	float	displaydigits;		//采集点数据显示缺省精度
    
	union {							//@当前值(初始值)
		float	rval;		// 浮点	
		long	lval;		// 长整
		BOOL	bval;		// 开关
		//char	sval[C_STRINGVALUE_LEN];// 字符串
		//wk, 2005.10.20, del: struct EVAL{
		//wk, 2005.10.20, add:
		//struct {
		//	short	type;					// 0x0001表示使用key,0x0002表示使用name (0x0003 表示同时使用key,name),
		//	short	key;					// 枚举(值)	
		//	char	name[C_STRINGVALUE_LEN];	// 枚举(字符串)
		//}eval;						 // 枚举
	};
	char	enumdesc[C_ENUMDESC_LEN];	//*@枚举描述   eg. "2:1,2,on:0,3,off" 
			// "枚举个数:标志1,枚举名1长度,枚举名1:标志2,枚举名2长度,枚举名2...   

	long	timedate;		//@时间戳
	long	istat;			//@点状态

	char	sourceserver[C_DEVICENAME_LEN];	//*采集点的数据源站(设备名)
	char	sourcegroup[32];	//*采集点的数据源结点组
	//WQ@2003-9-10/char	sourcetag[32];		//*@采集点的数据源结点(工位号)
	char	sourcetag[C_SOURCETAG_LEN];
	//	float   zero;			//数据放缩零值
	float   upperlimit;			//数据上限
	float	lowerlimit;			//数据下限

	WORD	pushref1;		//@实时推理规则标志
	WORD	ruleref1;		//@实时推理规则标志
	
	//BYTE	tagonline;		//@启动数据处理的状态
	//BYTE	newtag;			//@值更新标志
	long	excmin;			//*实时数据处理最短间隔（处理周期）
	long	excmax;			//*实时数据处理最大间隔
	float   excdev;			//*实时数据处理灵敏度

	//long	ptalarm;		//报警数据表记录号
	WORD	alarmtype;		//*报警
	WORD	alarmstate;		//状态报警
	float	alarmmax;		//*上限报警
	float	alarmmin;		//*下限报警
	float	alarmhihi;
	float	alarmlolo;

	//wk, 2003.9, add ->
	WORD	hipriority;			//报警优先级，暂时不处理
	WORD	lopriority;
	WORD	hihipriority;
	WORD	lolopriority;
	//<- wk, 2003.9, add

	BYTE	archiving;		//*是否存储历史数据
	BYTE	compressing;		//*是否进行历史压缩
	BYTE	step;			//*历史数据的插值形式（线形，台阶）
	BYTE	reserved2;
	long	hisidx;			//历史记录索引号
	long	compmin;		//*压缩最断间隔
	long	compmax;		//*压缩最长间隔
	float	compdev;		//*压缩灵敏度

	float	lastval;		//上次数据存档的值
	long	lasttime;		//上次数据存档的时间

	long	creationdate;		//采集点创建日期
	//	char	creator[16];		//采集点创建人
	//	long	changedate;		//采集点最近修改日期
	//	char	changer[16];		//采集点最近修改人

	//	short	ptowner;		//结点拥有者
	//	short	ptgroup;		//结点所在组
	//	BYTE	ptAccess;		//组访问权限	
} ACI_TAG_NODE;

//****** 类型定义-数据点定义 ******
typedef struct struTagNode_Define{
	_TCHAR	TagName[C_TAGNAME_LEN];			//采集数据点名-(Required)
	_TCHAR	TagDesc[C_TAGDESC_LEN];			//描述-(Optional)
	_TCHAR	EngUnit[C_TAGUNIT_LEN];			//采集点数据单位-(Optional)
	BYTE	TagType;						//数据类型(R浮点/S字符串/B开关/L整形)-(Rrequired)
	float	TypicalVal;						//典型值-(Optional, default =0)
	_TCHAR	DeviceName[C_DEVICENAME_LEN];	//采集点的数据源站-(Required) 
	_TCHAR	GroupName[C_GROUPNAME_LEN];		//采集点的数据源结点组-(Optional)
	_TCHAR	SourceTag[C_SOURCETAG_LEN];		//采集点的数据源结点-(Optional)
	BYTE	IOState;						//启动数据采集标,0-"禁止",1-"输入",2-"输出"-(Rrequired)
	long	ExceptionMin;					//数据处理最短间隔-处理周期, default = 1
	long	ExceptionMax;					//数据处理最大间隔, default = 1
	float 	ExceptionDev;					//数据处理灵敏度, default = 0
	float 	UpperLimit;						//数据上限-(Optional, default = 0)
	float	LowerLimit;						//数据下限-(Optional, default = 0)
	WORD 	AlarmType;						//报警类型, default = 0
	float	AlarmHiHi;						//高高报警, default = 0
	float	AlarmHi;						//高报警, default = 0
	float	AlarmLo;						//低报警, default = 0
	float	AlarmLoLo;						//低低报警, default = 0
	BYTE	IsArchived;						//是否存储历史数据, default = 0
	BYTE	IsCompressed;					//是否进行历史压缩, default = 1
	BYTE	InterMethod;					//历史数据的插值形式(线形/台阶)
	long	CompressMin;					//压缩最断间隔
	long	CompressMax;					//压缩最长间隔
	float	CompressDev;					//压缩灵敏度
	long	CreationDate;					//采集点创建日期
}TAG_DEF; 

//****** 类型定义-数据点信息 ******
typedef struct struTagNode_Info{
	_TCHAR	TagName[C_TAGNAME_LEN];			//采集数据点名-(Required)
	_TCHAR	TagDesc[C_TAGDESC_LEN];			//描述-(Optional)
	_TCHAR	EngUnit[C_TAGUNIT_LEN];			//采集点数据单位-(Optional)
	long	TagID;							//Unique ID, Integer
	BYTE	TagType;						//数据类型(R浮点/S字符串/B开关/L整形)-(Rrequired)
	BYTE	IOState;						//启动数据采集标,0-"禁止",1-"输入",2-"输出"-(Rrequired)
	WORD	Reserved1;
	float	TypicalVal;						//典型值-(Optional, default =0)
	union {									//当前值
		float	rval;					// 浮点	
		long	lval;					// 长整
		BOOL	bval;					// 开关
		_TCHAR	sval[C_STRINGVALUE_LEN];// 字符串
		//wk, 2005.10.20, del: struct EVAL{
		//wk, 2005.10.20, add:
		struct {
			short	type;					// 0x0001表示使用key,0x0002表示使用name
			short	key;					// 枚举(值)	
			_TCHAR	name[C_STRINGVALUE_LEN];	// 枚举(字符串)
		}eval;							// 枚举
	} ;
	_TCHAR	EnumDesc[C_ENUMDESC_LEN];		//枚举描述
	long	Timestamp;						//时间戳
	long	TagState;						//点状态
	_TCHAR	DeviceName[C_DEVICENAME_LEN];	//采集点的数据源站-(Required) 
	_TCHAR	GroupName[C_GROUPNAME_LEN];		//采集点的数据源结点组-(Optional)
	_TCHAR	SourceTag[C_SOURCETAG_LEN];		//采集点的数据源结点-(Optional)
	float 	UpperLimit;						//数据上限-(Optional, default = 0)
	float	LowerLimit;						//数据下限-(Optional, default = 0)
	WORD	PushReference;
	WORD	RuleReference;
	long	ExceptionMin;					//数据处理最短间隔-处理周期, default = 1
	long	ExceptionMax;					//数据处理最大间隔, default = 1
	float 	ExceptionDev;					//数据处理灵敏度, default = 0
	WORD 	AlarmType;						//报警类型, default = 0
	WORD	AlarmState;
	float	AlarmHi;						//高报警, default = 0
	float	AlarmLo;						//低报警, default = 0
	float	AlarmHiHi;						//高高报警, default = 0
	float	AlarmLoLo;						//低低报警, default = 0
	WORD	HiPriority;						//报警优先级，暂时不处理
	WORD	LoPriority;
	WORD	HiHiPriority;
	WORD	LoLoPriority;
	BYTE	IsArchived;						//是否存储历史数据, default = 0
	BYTE	IsCompressed;					//是否进行历史压缩, default = 1
	BYTE	InterMethod;					//历史数据的插值形式(线形/台阶)
	BYTE	Reserved2;
	long	HisIndex;
	long	CompressMin;					//压缩最断间隔
	long	CompressMax;					//压缩最长间隔
	float	CompressDev;					//压缩灵敏度
	float	LastValue;						//上次数据存档的值
	long	LastTimestamp;					//上次数据存档的时间
	long	CreationDate;					//采集点创建日期
}TAG_INFO;

typedef struct struValue{
	BYTE	Type;		// 类型
	long	lTimestamp;
	union {				// 点值				
		float	rval;						// 浮点	
		long	lval;						// 长整
		BOOL	bval;						// 开关
		_TCHAR	sval[C_STRINGVALUE_LEN];	// 字符串
	};
}VALUE;

typedef struct struTagValue{
	_TCHAR	TagName[C_FULL_TAGNAME_LEN];
	long	Timestamp;		// 时间戳
	long	TagState;		// 状态
	BYTE	TagType;		// 点值类型
	union {					// 点值				
		float	rval;		// 浮点	
		long	lval;		// 长整
		BOOL	bval;		// 开关
		_TCHAR	sval[C_STRINGVALUE_LEN];	// 字符串
	};
}ACI_TAGVAL, *LPTAGVAL;

enum Enum_Aggregate_Function 
{
	AF_SUMMARY,
	AF_MINIMUM,
	AF_MAXIMUM,
	AF_AVERAGE,
	AF_COUNT,
	AF_SUMMARY_FOR_CONTINOUS,
	AF_AVERAGE_FOR_CONTINOUS,
};

//handle to the recordset, forward only
#define HRECORDSET		long	


//wk, 2005.10.20, add:
#ifdef __cplusplus


//********* AGCN - AGILOR CONNECTION FUNCTIONS ***********
//--------------------------------------------------------

//this function must be executed before other functions
extern "C"  BOOL  PASCAL   Agcn_Startup();

//before connect to an agilor server, a server node must be created
//parameter 1 - named the agilor server, must be unique on local machine
//parameter 2 - handle to a window, be notified when sub data arrival or disconnected
//parameter 3 - a threadid which indicates this thread care for the related events
//parameter 4 - if allow named events, signaled when sub data arrival or disconnected
extern "C" BOOL PASCAL   Agcn_CreateNode
(LPCTSTR szServerName, HWND hWnd = NULL, DWORD hThreadID = NULL, BOOL bAllowEvents=FALSE);

//connect to specific agilor server node, which must be created by agcn_createnode
//return  0 : connection be created successfully
//return -1 : the server node hasn't been created
//return -2 : the server node has been connected
//return -3 : failed to connect, maybe the server is down or refused
//return -4 : timeout when attempt to connect the agilor server
//return -5 : socket error, socket error code is returned as negative
extern "C" long PASCAL  Agcn_Connect
(LPCTSTR szServerName, LPCTSTR szServerAddr, LPCTSTR szUserName, LPCTSTR szPassword, UINT nPort = 900);

extern "C" long PASCAL  Agcn_ConnectForDI
(LPCTSTR szServerName, LPCTSTR szServerAddr, LPCTSTR szUserName, LPCTSTR szPassword, UINT nPort = 900);

//disconnect from specific agilor server
//return  0 : disconnect successfully
//return -1 : the specific server is nonexistent
//return -2 : connection with the specific server has been closed
extern "C" long PASCAL  Agcn_Disconnect(LPCTSTR szServerName);

//enumerate the server nodes, which has been created 
//if return ture, lpSvrInfo pointers to a valid struct containing related information
//nServerID = 0 indicates the start of a new iterative process
extern "C" BOOL PASCAL  Agcn_EnumNodeInfo(long * nServerID, ACI_SERVER_INFO * pSvrInfo);

//when a server is used no longer, must be removed to free related resources
extern "C" BOOL PASCAL  Agcn_RemoveNode(LPCTSTR szServerName);

//before close application, clean up the resources used in the library
extern "C" BOOL PASCAL  Agcn_Cleanup();

//*********************** AGILOR DATA **********************

//subscribe real-time values of some tags
//sztagnames must be defined as [nTagCount][C_TAGNAME_LEN]
extern "C" long PASCAL  Agda_SubscribeTags
(LPCTSTR szServerName, LPCTSTR szTagNames, UINT nTagCount);

//cancel some subscription on specific tags
extern "C" long PASCAL  Agda_UnSubscribeTags
(LPCTSTR szServerName, LPCTSTR szTagNames, UINT nTagCount);

//cancel all subscription on specific server node
extern "C" long PASCAL  Agda_UnSubscribeAll(LPCTSTR szServerName);

//when notified on sub data arrival, call this funtion to get the data
extern "C" BOOL PASCAL  Agda_GetSubTagValue(ACI_TAGVAL * pTagVal);

//query real-time values of some tags
//sztagnames must be defined as [nTagCount][C_TAGNAME_LEN]
extern "C" HRECORDSET PASCAL  Agda_QuerySnapshots
(LPCTSTR szServerName, LPCTSTR szTagNames, UINT nTagCount);

//query a series history value of specific tag
//szfulltagname named as servername.tagname
extern "C" HRECORDSET PASCAL  Agda_QueryTagHistory
(LPCTSTR szFullTagName, long nStartTime, long nEndTime, long nStep=0);

extern "C" HRECORDSET PASCAL  Agda_QueryMultiTagHistory
(LPCTSTR szServerName, LPCTSTR szTagNames, long lTagCount, long nStartTime, long nEndTime, long nStep=0);

extern "C" long PASCAL  Agda_QueryTagHisStatistic
(LPCTSTR szFullTagName, long nStartTime, long nEndTime, long lStatisticType, ACI_TAGVAL * pTagVal);

//this function is used to get the results of querysnapshots or querytaghistory
extern "C" BOOL PASCAL  Agda_GetNextTagValue
(HRECORDSET hRecordset, ACI_TAGVAL * pTagVal, BOOL bRemoved=TRUE);

//aggregate funtion on a series history value of specific tag
//wherein szfulltagname named as servername.tagname
extern "C" long PASCAL  Agda_GetAggregateValue
(HRECORDSET hRecordset, ACI_TAGVAL * pTagVal, long nAFunction=AF_SUMMARY, BOOL bRemoved=TRUE);

//set a value to specific tag
extern "C" long PASCAL  Agda_SetTagValue
(LPCTSTR szFullTagName, VALUE * pValue, BOOL bManual = FALSE, LPCTSTR szComment = NULL);

//add comment to a specifc tag state on specific time
extern "C" long PASCAL  Agda_AddTagComment
(LPCTSTR szFullTagName, long nTimeStamp, long nTagState, LPCTSTR szComment);

//*********************** AGILOR POINT **********************

//remove tag from server
extern "C" long PASCAL  Agpt_RemoveTag
(LPCTSTR szServerName, long lTagID);

//add new tag to server
extern "C" long PASCAL  Agpt_AddNewTag
(LPCTSTR szServerName, ACI_TAG_NODE* pTagNode, BOOL bOverwrite = TRUE);

//retrieve devices information on specific server
extern "C" HRECORDSET PASCAL  Agpt_QueryDeviceInfo(LPCTSTR szServerName);

//enumerate the device information returned by querydeviceinfo
//nDeviceID must better be set to zero at beginning
extern "C" BOOL PASCAL  Agpt_EnumDeviceInfo
(HRECORDSET hRecordset, long * nDeviceID, DEV_INFO * pDevInfo);

//query all tags on specific device on specific server
extern "C" HRECORDSET PASCAL  Agpt_QueryTagsbyDevice
(LPCTSTR szServerName, LPCTSTR szDeviceName);

//query tags by namemask such as "LH*" on specific server
extern "C" HRECORDSET PASCAL  Agpt_QueryTagsbyNameMask
(LPCTSTR szServerName, LPCTSTR szTagNameMask);

//enumerate the tagname returned by querytags
extern "C" BOOL PASCAL  Agpt_EnumTagName
(HRECORDSET hRecordset, long * nTagID, LPTSTR szTagName);

//retrieve detailed information of specific tag
extern "C" long PASCAL  Agpt_GetTagInfo
(LPCTSTR szFullTagName, TAG_INFO * pTagInfo);

//********************** AGILOR TIME ***********************

//convert time to a long integer as second count since 1970.1.1
extern "C" long PASCAL  Agtm_DateTime2Long
(int hour, int min, int sec, int year, int mon, int mday);

//only local time is supported at present
extern "C" long PASCAL  Agtm_GetCurrTime(BOOL bLocal=TRUE);

//convert the long time to YYYY-MM-DD HH:MM:SS
extern "C" long PASCAL  Agtm_Long2DateString
(long nDateTime, LPTSTR szDateString);

extern "C" BOOL PASCAL  Agpt_GetTagNamebyID
(LPSTR szServerName, long nTagID, LPTSTR szTagName);

extern "C" long PASCAL  Agcn_GetServerTime
(LPCTSTR szServerName, long* plServerTime);

extern "C" BOOL PASCAL  Agpt_TagExist
(LPTSTR szFullTagName);

extern "C" BOOL PASCAL  Agda_CloseRecordset
(HRECORDSET hRecordset);

/////////面向气象应用的访问接口

extern "C" HRECORDSET PASCAL  Agda_Wea_QueryMultiTagHistory
(LPCTSTR szServerName, LPCTSTR szStationNames, LPCTSTR szElementNames, long nStartTime, long nEndTime);

extern "C" HRECORDSET PASCAL  Agda_Wea_QueryMultiTagHistoryWithCondition
(LPCTSTR szServerName, LPCTSTR szStationNames, LPCTSTR szElementNames, long nStartTime, long nEndTime, LPCTSTR szCondition = NULL);

extern "C" long PASCAL  Agda_Wea_CloseRecordset
(HRECORDSET hRecordset);

extern "C"  long PASCAL  Agda_Wea_MoveFirst
(HRECORDSET hRecordset);

extern "C"  long PASCAL  Agda_Wea_MoveTo
(HRECORDSET hRecordset, long lRowOffset);

extern "C"  long PASCAL  Agda_Wea_IsEOF
(HRECORDSET hRecordset);

extern "C"  long PASCAL  Agda_Wea_MoveNext
(HRECORDSET hRecordset);

extern "C"  long PASCAL  Agda_Wea_GetRowAmount
(HRECORDSET hRecordset);

extern "C"  long PASCAL  Agda_Wea_GetStationName
(HRECORDSET hRecordset, char* sStationName);

extern "C"  long PASCAL  Agda_Wea_GetTimestamp
(HRECORDSET hRecordset, long* lTimestamp);

extern "C"  long PASCAL  Agda_Wea_GetElementLongValue
(HRECORDSET hRecordset, long lElementIndex, long* pLongValue);

extern "C"  long PASCAL  Agda_Wea_GetElementFloatValue
(HRECORDSET hRecordset, long lElementIndex, float* pFloatValue);

extern "C" long PASCAL  Agda_Wea_GetElementStringValue
(HRECORDSET hRecordset, long lElementIndex, char* sStringValue);

extern "C" long PASCAL  Agda_Wea_GetElementTimeValue
(HRECORDSET hRecordset, long lElementIndex, char* sFormat, char* sTimeString);

//**********************************************************
//----------------------------------------------------------

//wk, 2005.10.20, add:
#endif


//wk, 2005.10.20, add ->

#ifndef __cplusplus

//********* AGCN - AGILOR CONNECTION FUNCTIONS ***********
//--------------------------------------------------------

//this function must be executed before other functions
BOOL PASCAL  Agcn_Startup();

//before connect to an agilor server, a server node must be created
//parameter 1 - named the agilor server, must be unique on local machine
//parameter 2 - handle to a window, be notified when sub data arrival or disconnected
//parameter 3 - a threadid which indicates this thread care for the related events
//parameter 4 - if allow named events, signaled when sub data arrival or disconnected
BOOL PASCAL  Agcn_CreateNode
(LPCTSTR szServerName, HWND hWnd, DWORD hThreadID, BOOL bAllowEvents);

//connect to specific agilor server node, which must be created by agcn_createnode
//return  0 : connection be created successfully
//return -1 : the server node hasn't been created
//return -2 : the server node has been connected
//return -3 : failed to connect, maybe the server is down or refused
//return -4 : timeout when attempt to connect the agilor server
//return -5 : socket error, socket error code is returned as negative
long PASCAL  Agcn_Connect
(LPCTSTR szServerName, LPCTSTR szServerAddr, LPCTSTR szUserName, LPCTSTR szPassword, UINT nPort);
long PASCAL  Agcn_ConnectForDI
(LPCTSTR szServerName, LPCTSTR szServerAddr, LPCTSTR szUserName, LPCTSTR szPassword, UINT nPort);

//disconnect from specific agilor server
//return  0 : disconnect successfully
//return -1 : the specific server is nonexistent
//return -2 : connection with the specific server has been closed
long PASCAL  Agcn_Disconnect(LPCTSTR szServerName);

//enumerate the server nodes, which has been created 
//if return ture, lpSvrInfo pointers to a valid struct containing related information
//nServerID = 0 indicates the start of a new iterative process
BOOL PASCAL  Agcn_EnumNodeInfo(long * nServerID, ACI_SERVER_INFO * pSvrInfo);

//when a server is used no longer, must be removed to free related resources
BOOL PASCAL  Agcn_RemoveNode(LPCTSTR szServerName);

//before close application, clean up the resources used in the library
BOOL PASCAL  Agcn_Cleanup();

//*********************** AGILOR DATA **********************

//subscribe real-time values of some tags
//sztagnames must be defined as [nTagCount][C_TAGNAME_LEN]
long PASCAL  Agda_SubscribeTags
(LPCTSTR szServerName, LPCTSTR szTagNames, UINT nTagCount);

//cancel some subscription on specific tags
long PASCAL  Agda_UnSubscribeTags
(LPCTSTR szServerName, LPCTSTR szTagNames, UINT nTagCount);

//cancel all subscription on specific server node
long PASCAL  Agda_UnSubscribeAll(LPCTSTR szServerName);

//when notified on sub data arrival, call this funtion to get the data
BOOL PASCAL  Agda_GetSubTagValue(ACI_TAGVAL * pTagVal);

//query real-time values of some tags
//sztagnames must be defined as [nTagCount][C_TAGNAME_LEN]
HRECORDSET PASCAL  Agda_QuerySnapshots
(LPCTSTR szServerName, LPCTSTR szTagNames, UINT nTagCount);

//query a series history value of specific tag
//szfulltagname named as servername.tagname
HRECORDSET PASCAL  Agda_QueryTagHistory
(LPCTSTR szFullTagName, long nStartTime, long nEndTime, long nStep);

HRECORDSET PASCAL  Agda_QueryMultiTagHistory
(LPCTSTR szServerName, LPCTSTR szTagNames, long lTagCount, long nStartTime, long nEndTime, long nStep);

long PASCAL  Agda_QueryTagHisStatistic
(LPCTSTR szFullTagName, long nStartTime, long nEndTime, long lStatisticType, ACI_TAGVAL * pTagVal);

//this function is used to get the results of querysnapshots or querytaghistory
BOOL PASCAL  Agda_GetNextTagValue
(HRECORDSET hRecordset, ACI_TAGVAL * pTagVal, BOOL bRemoved);

//aggregate funtion on a series history value of specific tag
//wherein szfulltagname named as servername.tagname
long PASCAL  Agda_GetAggregateValue
(HRECORDSET hRecordset, ACI_TAGVAL * pTagVal, long nAFunction, BOOL bRemoved);

//set a value to specific tag
long PASCAL  Agda_SetTagValue
(LPCTSTR szFullTagName, VALUE * pValue, BOOL bManual, LPCTSTR szComment);

//add comment to a specifc tag state on specific time
long PASCAL  Agda_AddTagComment
(LPCTSTR szFullTagName, long nTimeStamp, long nTagState, LPCTSTR szComment);

//*********************** AGILOR POINT **********************

//remove tag from server
long PASCAL  Agpt_RemoveTag
(LPCTSTR szServerName, long lTagID);

//add new tag to server
long PASCAL  Agpt_AddNewTag
(LPCTSTR szServerName, ACI_TAG_NODE* pTagNode, BOOL bOverwrite);

//retrieve devices information on specific server
HRECORDSET PASCAL  Agpt_QueryDeviceInfo(LPCTSTR szServerName);

//enumerate the device information returned by querydeviceinfo
//nDeviceID must better be set to zero at beginning
BOOL PASCAL  Agpt_EnumDeviceInfo
(HRECORDSET hRecordset, long * nDeviceID, DEV_INFO * pDevInfo);

//query all tags on specific device on specific server
HRECORDSET PASCAL  Agpt_QueryTagsbyDevice
(LPCTSTR szServerName, LPCTSTR szDeviceName);

//query tags by namemask such as "LH*" on specific server
HRECORDSET PASCAL  Agpt_QueryTagsbyNameMask
(LPCTSTR szServerName, LPCTSTR szTagNameMask);

//enumerate the tagname returned by querytags
BOOL PASCAL  Agpt_EnumTagName
(HRECORDSET hRecordset, long * nTagID, LPTSTR szTagName);

//retrieve detailed information of specific tag
long PASCAL  Agpt_GetTagInfo
(LPCTSTR szFullTagName, TAG_INFO * pTagInfo);

//********************** AGILOR TIME ***********************

//convert time to a long integer as second count since 1970.1.1
long PASCAL  Agtm_DateTime2Long
(int hour, int min, int sec, int year, int mon, int mday);

//only local time is supported at present
long PASCAL  Agtm_GetCurrTime(BOOL bLocal);

//convert the long time to YYYY-MM-DD HH:MM:SS
long PASCAL  Agtm_Long2DateString
(long nDateTime, LPTSTR szDateString);

BOOL PASCAL  Agpt_GetTagNamebyID
(LPSTR szServerName, long nTagID, LPTSTR szTagName);

long PASCAL  Agcn_GetServerTime
(LPCTSTR szServerName, long* plServerTime);

BOOL PASCAL  Agpt_TagExist
(LPTSTR szFullTagName);

BOOL PASCAL  Agda_CloseRecordset
(HRECORDSET hRecordset);

/////////面向气象应用的访问接口

HRECORDSET PASCAL  Agda_Wea_QueryMultiTagHistory
(LPCTSTR szServerName, LPCTSTR szStationNames, LPCTSTR szElementNames, long nStartTime, long nEndTime);

HRECORDSET PASCAL  Agda_Wea_QueryMultiTagHistoryWithCondition
(LPCTSTR szServerName, LPCTSTR szStationNames, LPCTSTR szElementNames, long nStartTime, long nEndTime, LPCTSTR szCondition = NULL)

long PASCAL  Agda_Wea_CloseRecordset
(HRECORDSET hRecordset);

long PASCAL  Agda_Wea_MoveFirst
(HRECORDSET hRecordset);

long PASCAL  Agda_Wea_MoveTo
(HRECORDSET hRecordset, long lRowOffset);

long PASCAL  Agda_Wea_IsEOF
(HRECORDSET hRecordset);

long PASCAL  Agda_Wea_MoveNext
(HRECORDSET hRecordset);

long PASCAL  Agda_Wea_GetRowAmount
(HRECORDSET hRecordset);

long PASCAL  Agda_Wea_GetStationName
(HRECORDSET hRecordset, char* sStationName);

long PASCAL  Agda_Wea_GetTimestamp
(HRECORDSET hRecordset, long* lTimestamp);

long PASCAL  Agda_Wea_GetElementLongValue
(HRECORDSET hRecordset, long lElementIndex, long* pLongValue);

long PASCAL  Agda_Wea_GetElementFloatValue
(HRECORDSET hRecordset, long lElementIndex, float* pFloatValue);

long PASCAL  Agda_Wea_GetElementStringValue
(HRECORDSET hRecordset, long lElementIndex, char* sStringValue);

long PASCAL  Agda_Wea_GetElementTimeValue
(HRECORDSET hRecordset, long lElementIndex, char* sFormat, char* sTimeString);

//**********************************************************
//----------------------------------------------------------

#endif

//<- wk, 2005.10.20, add


#ifdef __cplusplus
   }
#endif

#endif	//_AGILOR_CALL_INTERFACE_H_