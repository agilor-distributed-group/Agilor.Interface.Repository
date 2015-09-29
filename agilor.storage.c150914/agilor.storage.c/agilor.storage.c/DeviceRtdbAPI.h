//Author YURui, version3.2

#ifndef _DEVICERTDBAPI_H_
#define _DEVICERTDBAPI_H_

#define DRTDBAPI __stdcall
#define DRTDBEXPORTS	__declspec(dllexport)

//deleted by yurui, 04/05/05
//#define TOTALPACKAGESEND						1000
//deleted by yurui, 04/04/24, the const used to 过滤数据，不再使用
//#define COMPRESSRATIO							0.000001
#define	SENDMESSAGEPERIOD						1000

#ifndef _STAT_QUALITY_GOOD_
#define _STAT_QUALITY_GOOD_
const long STAT_QUALITY_GOOD = 0x00000010L; 
#endif

#define WM_DRTDB_STATE_INFORMATION					WM_USER + 1000

#define WM_DRTDBAPI_STATE_CONNECTED						WM_USER + 1001
#define WM_DRTDBAPI_STATE_CONNECTING					WM_USER + 1002
#define WM_DRTDBAPI_STATE_RECONNECTING					WM_USER + 1003
#define WM_DRTDBAPI_STATE_DISCONNECTED					WM_USER + 1004
#define WM_DRTDBAPI_STATE_SENDBUFFERDATA				WM_USER + 1005
#define WM_DRTDBAPI_STATE_SENDBUFFERDATA_END			WM_USER + 1006
#define WM_DRTDBAPI_STATE_SENDREALTIMEDATA				WM_USER + 1007
#define WM_DRTDBAPI_STATE_SENDDATATOBUFFER				WM_USER + 1008
#define WM_DRTDBAPI_STATE_TAGINFORM						WM_USER + 1009
#define WM_DRTDBAPI_STATE_SERVER_REQUEST_DISCONNECT				WM_USER + 1010
#define WM_DRTDBAPI_STATE_SENDDATA						WM_USER + 1011

#define DRTDB_STATE_NONE							0
#define DRTDB_STATE_CONNECTED							1
#define DRTDB_STATE_CONNECTING							2
#define DRTDB_STATE_RECONNECTING						3
#define DRTDB_STATE_DISCONNECTED						4


typedef struct STRU_TAG_NODE{
	long	lLocalID;			// 本地重新分配的ID
	long 	lTagID;				// long		//negative ID indicates delelted tag
	char 	szTagSource[128];	// char[32]-->128// 工位号
	float 	fExcDev;			// float	// 灵敏度
	long 	lRefTimer;			// long		// 数据刷新周期（秒）
	long 	lMaxInterval;		// Added by WQ, 2003-8-28 
	WORD 	cTagType;			// char		//(R 浮点数/S字符串/B开关/L整形)
	WORD 	cIOState;			// char		//0或>=0x80("禁止"),1("输入"),2("输出")
	long	lTimestamp; 	//added by wq@2004-9-21
	//added by yurui, 04/02/13, 
	long	cTagState; 			//for tag quality and state such as realtime or buffered data
	union{
		float 	fValue; 		// （类型视cTagType而定）float，BOOL,long,char[128]
		long	lValue;
		BOOL	bValue;
		char	szValue[128];
	};
}TAG_NODE, *LPTAGNODE;

#ifndef struct_STRU_TAG_VALUE_LOCAL
	typedef struct STRU_TAG_VALUE_LOCAL
	{
		long	lLocalID;			// 本地重新分配的ID
		long	lTagID;
		char 	szTagSource[128];
		WORD 	cTagType;			// char		//(R 浮点数/S字符串/B开关/L整形)
		long	nTagState;			// 增加数据质量，作为RTDB中State中Byte2,Byte1
		long	lTimeStamp;
		union{
			float 	fValue; 		// （类型视cTagType而定）float，BOOL,long,char[128]
			long	lValue;
			BOOL	bValue;
			char	szValue[128];
		};
	}TAG_VALUE_LOCAL, *LPTAGVALUELOCAL;
	#define struct_STRU_TAG_VALUE_LOCAL
#endif

typedef struct STRU_SERVER_INFO
{
	char	sServerName[32];
	char	sServerType[16];	// "AGILOR" for default
	char	sVersion[8];	

}SERVER_INFO, FAR* LPSERVERINFO;

long DRTDBEXPORTS DRTDBAPI DRTDB_GetTagCount(long& lTagCount);
//<-added by yuuri 04/02/11
long DRTDBEXPORTS DRTDBAPI DRTDB_GetCurrentState();
//->
long DRTDBEXPORTS DRTDBAPI DRTDB_GetNextTagNode(long &lTagID, TAG_NODE &TagNode);
//->added by yurui, 04/02/18
long DRTDBEXPORTS DRTDBAPI DRTDB_FindTagBySource(LPSTR sourcename, TAG_NODE &tagnode);
//->

long DRTDBEXPORTS DRTDBAPI DRTDB_SendNewValues(LPTAGVALUELOCAL lpTagValues, long lTagCount);
long DRTDBEXPORTS DRTDBAPI DRTDB_SendNewValue(TAG_VALUE_LOCAL &TagValue, bool bImmediate=true);
//->added by yurui, 04/06/15, bFilter = true表示过滤， 否则表示不过滤 
long DRTDBEXPORTS DRTDBAPI DRTDB_SendNewValue_EX(TAG_VALUE_LOCAL &TagValue, bool bImmediate, BOOL bFiltered);

long DRTDBEXPORTS DRTDBAPI DRTDB_SendNewValue_UsingTagID(TAG_VALUE_LOCAL &TagValue, bool bImmediate=true);
long DRTDBEXPORTS DRTDBAPI DRTDB_SendHisValuesUsingTagID(LPTAGVALUELOCAL lpHisTagValues, long lCount);

//->
long DRTDBEXPORTS DRTDBAPI DRTDB_Flush();

long DRTDBEXPORTS DRTDBAPI DRTDB_RegisterDevice(LPSTR sServerName,UINT lPortNo,LPSTR sDeviceName);
long DRTDBEXPORTS DRTDBAPI DRTDB_UnregisterDevice(LPSTR sCause, long lErrCode = 0);

//wk, 2005.08.18, add->
long DRTDBEXPORTS DRTDBAPI DRTDB_RegisterDeviceEx(LPSTR sServerName, UINT lPortNo, LPSTR sDeviceName, BOOL bNeedTimeSync = false);
//<- wk, 2005.08.18, add

//<-added by yurui 04/02/09
void DRTDBEXPORTS DRTDBAPI DRTDB_SetParameter(BOOL isDataBuf = false, BOOL isBufSend = false);
void DRTDBEXPORTS DRTDBAPI DRTDB_GetParameter(BOOL &isDataBuf, BOOL &isBufSend);

//send state information 04/02/10
//DRTDB_SetStateCallBackFunction is unused now.
void DRTDBEXPORTS DRTDBAPI DRTDB_SetStateCallBackFunction(void (__cdecl *fOnGetStateInformation)(UINT, LPSTR));
void DRTDBEXPORTS DRTDBAPI DRTDB_SetWindowHandle(HWND hWnd);
//->
void DRTDBEXPORTS DRTDBAPI DRTDB_SetCallBackFunction(void (__cdecl *fOnNewTagNodeArrive)(LPTAGNODE), void (__cdecl *fOnRemoveTagNode)(LPTAGNODE), void (__cdecl *fOnSetDeviceTagValue)(LPTAGVALUELOCAL), void (__cdecl *fOnGetDeviceTagValue)(LPTAGNODE));

long DRTDBEXPORTS DRTDBAPI DRTDB_SetDeviceStatus(BOOL bDeviceIsConnected);

long DRTDBEXPORTS DRTDBAPI DRTDB_MD_FindTagBySource(LPSTR sDeviceName, LPSTR sourcename, TAG_NODE &tagnode);
long DRTDBEXPORTS DRTDBAPI DRTDB_MD_Flush(LPSTR sDeviceName);
long DRTDBEXPORTS DRTDBAPI DRTDB_MD_GetCurrentState(LPSTR sDeviceName);
long DRTDBEXPORTS DRTDBAPI DRTDB_MD_GetNextTagNode(LPSTR sDeviceName, long &lTagID, TAG_NODE &TagNode);
void DRTDBEXPORTS DRTDBAPI DRTDB_MD_GetParameter(LPSTR sDeviceName, BOOL &isDataBuf, BOOL &isBufSend);
long DRTDBEXPORTS DRTDBAPI DRTDB_MD_GetTagCount(LPSTR sDeviceName, long& lTagCount);
long DRTDBEXPORTS DRTDBAPI DRTDB_MD_SendNewValue(LPSTR sDeviceName, TAG_VALUE_LOCAL &TagValue, bool bImmediate=true);
long DRTDBEXPORTS DRTDBAPI DRTDB_MD_SendNewValue_EX(LPSTR sDeviceName, TAG_VALUE_LOCAL &TagValue, bool bImmediate, BOOL bFiltered);
long DRTDBEXPORTS DRTDBAPI DRTDB_MD_SendNewValues(LPSTR sDeviceName, LPTAGVALUELOCAL lpTagValues, long lTagCount);
void DRTDBEXPORTS DRTDBAPI DRTDB_MD_SetCallBackFunction(LPSTR sDeviceName, void (__cdecl *fOnNewTagNodeArrive)(LPTAGNODE), void (__cdecl *fOnRemoveTagNode)(LPTAGNODE), void (__cdecl *fOnSetDeviceTagValue)(LPTAGVALUELOCAL), void (__cdecl *fOnGetDeviceTagValue)(LPTAGNODE));
long DRTDBEXPORTS DRTDBAPI DRTDB_MD_SetDeviceStatus(LPSTR sDeviceName, BOOL bDeviceIsConnected);
void DRTDBEXPORTS DRTDBAPI DRTDB_MD_SetWindowHandle(LPSTR sDeviceName, HWND hWnd);
long DRTDBEXPORTS DRTDBAPI DRTDB_MD_UnregisterDevice(LPSTR sDeviceName, LPSTR sCause, long lErrCode = 0);
long DRTDBEXPORTS DRTDBAPI DRTDB_MD_SendHisValuesUsingTagID(LPSTR sDeviceName, LPTAGVALUELOCAL lpHisTagValues, long lCount);

#endif