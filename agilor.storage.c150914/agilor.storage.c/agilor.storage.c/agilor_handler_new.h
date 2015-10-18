#ifndef AGILOR_HANDLER_NEW_H
#define AGILOR_HANDLER_NEW_H

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

#include "runtime.h"


using namespace std;
using namespace boost;
using namespace aci;
using namespace log4cplus;
using namespace log4cplus::helpers;

using namespace agilor::framework;


class undefinition
{
public:
	undefinition(string start)
	{
		cout << start << "未定义" << endl;
	}
};


class agilor_handler_new :public virtual AgilorIf
{
private:
	static int threadId;
	static Logger logger;
	static bool isConnected;
	static unsigned int max_of_device;
	static vector<string> devices;


	runtime<DEVICE> * _runtime = NULL;

	int target_count = 0;
	string agilor_name = "agilor";

protected:
	bool find_device(const string& name);

public:
	static void closeLogger();

	agilor_handler_new()
	{
		config::init();
		_runtime = new runtime<DEVICE>(config::device_base(), config::max_device_size());
	}
	~agilor_handler_new()	{	}

	void ping();

	//设备相关
	void AddDevice(const DEVICE& device)
	{
		try
		{
			_runtime->insert(device);
		}
		catch (var::exceptions e)
		{
			switch (e)
			{
			case var::exceptions::DEVICEISEXISTEXCEPTION:
				throw aci::exception::EDeviceIsExistException(device.name); break;
			case var::exceptions::MAXDEVICEEXCEPTION:
				throw aci::exception::EDeviceMaxException(); break;

			default:
				throw undefinition("AddDevice exception");

			}
		}
	}
	void DeleteDevice(const std::string& deviceName)
	{
		runtime<DEVICE>::_const_device_vec_iterator it =  _runtime->find(deviceName);
		if (it == _runtime->end())
			throw  aci::exception::EDeviceNotFoundException(deviceName);
		try
		{
			_runtime->remove(it);
		}
		catch (var::exceptions e)
		{
			switch (e)
			{
			default:
				throw undefinition("AddDevice exception");

			}
		}


	}
	bool ModifyDevice(const DEVICE& device)
	{
		runtime<DEVICE>::_const_device_vec_iterator it = _runtime->find(device.name);
		if (it == _runtime->end())
			throw aci::exception::EDeviceNotFoundException(device.name);
		
		try
		{
			(**it) = device;
		}
		catch (var::exceptions e)
		{
			switch (e)
			{
			case agilor::framework::var::BUSYEXCEPTION:
				break;
			case agilor::framework::var::NULLEXCEPTION:
				break;
			case agilor::framework::var::DEVICEISEXISTEXCEPTION:
				break;
			case agilor::framework::var::UNKNOWNEXCEPTION:
				break;
			case agilor::framework::var::DEVICENOTFOUNDEXCEPTION:
				break;
			default:
				throw undefinition("ModifyDevice");
				break;
			}
		}
		return true;
			



	}
	
	//废弃 用 get_all_devices 替代
	int32_t QueryDeviceInfo()	{
		return 0;
	}
	//废弃 用 get_all_devices 替代
	void EnumDeviceInfo(DEVICE& _return, const int32_t hRecordset){}

	//由于通信原语 即将修改，暂不实现
	void get_all_devices(){}

	/*
	   以后实现流式获取deivce,但在多线程下 不保证其可靠性
	*/


	//废弃
	void GetAllDevices(std::vector<DEVICE> & _return){}


	int32_t TagCountByDevice(const std::string& deviceName)
	{
		runtime<DEVICE>::_const_device_vec_iterator it = _runtime->find(deviceName);
		if (it == _runtime->end())
			throw  aci::exception::EDeviceNotFoundException(deviceName);
		return (*it)->target_size();
	}


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


	//device 下的key 用 target id替代 （以后修改）
	void RemoveTag(const int32_t tagId)
	{
		
	}

	//暂时废弃
	void GetTagInfo(TAGNODE& _return, const std::string& tagName){}

	//isOverwrite参数废弃，不允许点重复
	void AddNewTag(const TAGNODE& node, const bool isOverwrite)
	{
		runtime<DEVICE>::_const_device_vec_iterator it = _runtime->find(node.deviceName);
		if (it == _runtime->end())
			throw  aci::exception::EDeviceNotFoundException(node.deviceName);

		ACI_TAG_NODE ACI;
		aci::parse(node, ACI);

		try
		{
			(*it)->insert(ACI);
		}
		catch (var::exceptions e)
		{
			switch (e)
			{
			case agilor::framework::var::NOEXCEPTION:
				break;
			case agilor::framework::var::BUSYEXCEPTION:
				break;
			case agilor::framework::var::NULLEXCEPTION:
				break;
			case agilor::framework::var::TARGETEXISETEXCEPTION:
				break;
			case agilor::framework::var::VALUETYPEEXCEPTION:
				break;
			case agilor::framework::var::MAXDEVICEEXCEPTION:
				break;
			case agilor::framework::var::DEVICEISEXISTEXCEPTION:
				break;
			case agilor::framework::var::REGISTEREXCEPTION:
				break;
			case agilor::framework::var::UNKNOWNEXCEPTION:
				break;
			case agilor::framework::var::DEVICENOTFOUNDEXCEPTION:
				break;
			default:
				break;
			}
		}
	}

	//暂时废弃
	void EnumNextTag(TAGNODE& _return, const int32_t hwnd){}


	//订阅点相关
	int32_t SubscribeTags(const std::string& tagNames, const int16_t count){ return 0; }
	int32_t UnSubscribeTags(const std::string& tagNames, const int16_t count){ return 0; }

	int32_t UnSubscribeAll(){ return 0; }
	void GetSubTagValue(TAGVAL& _return){  }

public:
	static vector<string>  getDevices();
};

#endif

#endif