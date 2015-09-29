#ifndef REALAGILORHANDLER_H
#define REALAGILORHANDLER_H

#include <Windows.h>
#include "inter_types.h"
#include "Agilor.h"
#include <iostream>
#include <vector>
#include <mutex>
#include <memory>
#include "ACI.h"
#include <log4cplus\logger.h>

#include <log4cplus\consoleappender.h>
#include <log4cplus\layout.h>
#include <log4cplus\loggingmacros.h>

#include "exception_types.h"
#include "aci_thrift_parse.h"


using namespace std;
using namespace boost;
using namespace aci;
using namespace log4cplus;
using namespace log4cplus::helpers;


class AgilorHandler :public virtual AgilorIf
{
private:
	static int threadId;
	static Logger logger;
	static bool isConnected;
	static unsigned int max_of_device;
	static vector<string> devices;
	static mutex _mDevice;
	

	string agilor_name = "agilor";

protected:
	bool find_device(const string& name);

public:
	static void closeLogger();

	AgilorHandler();
	~AgilorHandler();

	void ping();

	//设备相关
	void AddDevice(const DEVICE& device);
	void DeleteDevice(const std::string& deviceName);
	bool ModifyDevice(const DEVICE& device);
	int32_t QueryDeviceInfo();
	void EnumDeviceInfo(DEVICE& _return, const int32_t hRecordset);
	void GetAllDevices(std::vector<DEVICE> & _return);
	int32_t TagCountByDevice(const std::string& deviceName);


	//点相关
	int32_t QuerySnapshots(const std::string& tagNames, const int32_t tagCount);
	void GetNextTagValue(TAGVAL& _return, const int32_t hRecordset, const bool isRemoved);

	int32_t QueryTagHistory(const std::string& tagName, const int32_t startTime, const int32_t endTime, const int32_t step);	

	void GetAggregateValue(TAGVAL& _return, const int32_t hRecordset, const AGGREGATE::type ag, const bool isRemoved);

	

	int32_t QueryTagsbyDevice(const std::string& deviceName);

	void EnumTagName(std::string& _return, const int32_t hRecordset);

	void GetTagNamebyId(std::string& _return, const int32_t tagId);


	void SetValue(const TAGVAL& value);

	void SetValues(const std::vector<TAGVAL> & values);

	void RemoveTag(const int32_t tagId);

	void GetTagInfo(TAGNODE& _return, const std::string& tagName);

	void AddNewTag(const TAGNODE& node, const bool isOverwrite);
	void EnumNextTag(TAGNODE& _return, const int32_t hwnd);


	//订阅点相关
	int32_t SubscribeTags(const std::string& tagNames, const int16_t count) ;
	int32_t UnSubscribeTags(const std::string& tagNames,const int16_t count);

	int32_t UnSubscribeAll();
	void GetSubTagValue(TAGVAL& _return);

public:
	static vector<string>  getDevices();
};

#endif