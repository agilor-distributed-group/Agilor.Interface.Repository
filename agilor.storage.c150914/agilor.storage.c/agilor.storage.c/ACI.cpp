#include "ACI.h"


void aci::init(ACI_TAG_NODE& data)
{
	data.pointtype = 'S';
	data.sval[0] = 0;
	data.sourceserver[0] = 0;
	data.sourcegroup[0] = 0;

	data.name[0] = 0;
	data.sourcetag[0] = 0;
	data.descriptor[0] = 0;
	data.engunits[0] = 0;
	data.istat = 1638;
	data.typicalvalue = 0;
	data.excmin = 0;
	data.excmax = 0;
	data.excdev = 0;
	data.upperlimit = 0;
	data.lowerlimit = 0;
	data.alarmhihi = 0;
	data.alarmlolo = 0;
	data.alarmtype = 0;
	data.alarmmin = 0;
	data.alarmmax = 0;
	data.lasttime = 0;
	data.compdev = 0;
	data.lasttime = 0;
	data.scan = 0;



}


long aci::addNewTag(const string& serverName, ACI_TAG_NODE& data, bool isOverwrite)
{
	return Agpt_AddNewTag(serverName.data(), &data, isOverwrite);
}

long aci::registerDevice(const string& ip, int port, string deviceName)
{
	return DRTDB_RegisterDevice(const_cast<LPSTR>(ip.data()), port, const_cast<LPSTR>(deviceName.data()));
}

long aci::connect(const string& serverName, const string& ip, const string& userName, const string& password, int port)
{
	return Agcn_Connect(serverName.data(), ip.data(), userName.data(), password.data(), port);
}

long aci::createConnNode(const string& name, HWND hwnd, unsigned long threadID, bool allowEvent)
{
	return Agcn_CreateNode(name.data(), hwnd, threadID, allowEvent);
}

long aci::startup()
{
	return Agcn_Startup();
}


long aci::removeTag(const string& serverName, long& tagId)
{
	return Agpt_RemoveTag(serverName.data(), tagId);
}

long aci::queryDeviceInfo(const string& serverName)
{
	return Agpt_QueryDeviceInfo(serverName.data());
}

long aci::enumDeviceInfo(long hwnd, long* deviceId, DEV_INFO* info)
{
	long id = 0;
	long r = Agpt_EnumDeviceInfo(hwnd, &id, info);
	if (deviceId != NULL) *deviceId = id;
	return r;
}

long aci::md_UnregisterDevice(const string& deviceName, string* cause, long errCode)
{
	const char *c = NULL;
	if (cause != NULL) c = cause->data();
	return DRTDB_MD_UnregisterDevice((LPSTR)(deviceName.data()), const_cast<LPSTR>(c), errCode);
}


long aci::md_sendNewValue(const string& deviceName, TAG_VALUE_LOCAL& value, bool bImmediate)
{
	return DRTDB_MD_SendNewValue(const_cast<LPSTR>(deviceName.data()), value, bImmediate);
}

long aci::queryTagsbyDevice(const string& serverName, const string& deviceName)
{
	return Agpt_QueryTagsbyDevice(serverName.data(), deviceName.data());
}


long aci::enumTagName(long hRecordset, long *tagId, string& name)
{
	long id;
	char tagName[C_TAGNAME_LEN];
	long r = Agpt_EnumTagName(hRecordset, &id, tagName);
	name.append(tagName);
	if (tagId != NULL) *tagId = id;
	return r;
}

long aci::getTagInfo(const string& serverName, const string& tagName, TAG_INFO* info)
{
	return Agpt_GetTagInfo((serverName + "." + tagName).data(), info);
}

long aci::queryTagHistory(const string& serverName, const string& tagName, long start, long end, long step)
{
	return Agda_QueryTagHistory((serverName + "." + tagName).data(), start, end, step);
	
}

void aci::parseValue(TAG_VALUE_LOCAL& tagValue, const string value, short type)
{
	switch (type)
	{
	case 'R':tagValue.fValue = boost::lexical_cast<float>(value); break;
	case 'S':memcpy(tagValue.szValue, value.data(), sizeof(tagValue.szValue)); break;
	case 'B':tagValue.bValue = boost::lexical_cast<bool>(value); break;
	case 'L':tagValue.lValue = boost::lexical_cast<long>(value); break;
	default:
		break;
	}
}

long aci::md_sendNewValues(const string&deviceName, LPTAGVALUELOCAL values, long size)
{
	return DRTDB_MD_SendNewValues((LPSTR)deviceName.data(), values, size);
}

long aci::querySnapshots(const string& serverName,const char* tagNames, int count)
{
	return Agda_QuerySnapshots(serverName.data(), tagNames, count);
}

long aci::getNextTagValue(long hRecordset, ACI_TAGVAL* val,bool isRemoved)
{
	return Agda_GetNextTagValue(hRecordset, val, isRemoved);
	//return Agda_GetNextTagValue(hRecordset)
}

long aci::subscribeTags(const string& serverName, const char* tags, int count)
{
	return Agda_SubscribeTags(serverName.data(), tags, count);
}

long aci::getTagNamebyID(const string& serverName, const long tagId, string& name)
{
	char tagName[C_TAGNAME_LEN];

	int result = Agpt_GetTagNamebyID((LPTSTR)serverName.data(), tagId,tagName);
	if (result)
	{
		name.clear();
		name.append(tagName);
	}
	return result;
}

long aci::md_flush(const string& deviceName)
{
	return DRTDB_MD_Flush((LPSTR)deviceName.data());
}