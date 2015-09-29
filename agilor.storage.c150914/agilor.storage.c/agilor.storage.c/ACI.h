#ifndef ACI_H
#define ACI_H

#include <iostream>
#include <string>
#include <Windows.h>
#include "DeviceRtdbAPI.h"
#include "ACI_H.H"
#include <boost\type_traits.hpp>
#include <boost\lexical_cast.hpp>

using namespace std;
using namespace boost;

namespace aci
{
#define LOCALHOST "127.0.0.1"
#define RTDB_PORT 700
	void init(ACI_TAG_NODE& data);
	void parseValue(TAG_VALUE_LOCAL& tagValue, const string value, short type);
	long addNewTag(const string& serverName, ACI_TAG_NODE& data, bool isOverwrite = true);
	long registerDevice(const string& ip, int port, string deviceName);
	long connect(const string& serverName, const string& ip, const string& userName, const string& password, int port = 900);
	long createConnNode(const string& name, HWND hwnd = 0, unsigned long threadID = 0UL, bool allowEvent = false);
	long startup();
	long removeTag(const string& serverName, long& tagId);
	long queryDeviceInfo(const string& serverName);
	long enumDeviceInfo(long hwnd, long* deviceId, DEV_INFO* info);
	long md_UnregisterDevice(const string& deviceName, string* cause, long errCode = 0);
	long md_sendNewValue(const string& deviceName, TAG_VALUE_LOCAL& value, bool bImmediate = true);
	long md_sendNewValues(const string&deviceName, LPTAGVALUELOCAL values, long size);
	long md_flush(const string& deviceName);

	//long registerAllDevice();
	long queryTagsbyDevice(const string& serverName, const string& deviceName);

	long enumTagName(long hRecordset, long *tagId, string& name);
	long getTagInfo(const string& serverName,const string& tagName,TAG_INFO* info);
	long queryTagHistory(const string& serverName, const string& tagName, long start, long end, long step=0);
	long querySnapshots(const string& serverName, const char* tagNames, int count);
	long getNextTagValue(long hRecordset,ACI_TAGVAL* val, bool isRemoved);
	long subscribeTags(const string& serverName,const char* tags, int count);
	long getTagNamebyID(const string& serverName, const long tagId, string& name);



	
}

#endif