#include "AgilorHandler.h"


Logger AgilorHandler::logger = Logger::getInstance("Agilor");
int AgilorHandler::threadId = 0;

bool AgilorHandler::isConnected = false;
vector<string> AgilorHandler::devices;
unsigned int AgilorHandler::max_of_device = 170;

mutex   AgilorHandler::_mDevice;

AgilorHandler::AgilorHandler()
{
	SharedAppenderPtr console_appender(new ConsoleAppender());
	
	//log4cplus::Layout * layout = NULL;

	console_appender->setLayout(std::unique_ptr<Layout>(
		 new log4cplus::PatternLayout("[%p][%c] %d{%Y-%m-%d %H:%M:%S:%Q} - %m %n")
		));

	logger.addAppender(console_appender);

	logger.setLogLevel(DEBUG_LOG_LEVEL);

	LOG4CPLUS_INFO(logger, "server is starting...,threadId is " << threadId++);

	if (!isConnected)
	{

		aci::startup();
		aci::createConnNode(this->agilor_name);
		assert(!aci::connect(this->agilor_name, LOCALHOST, this->agilor_name, this->agilor_name));


		LOG4CPLUS_INFO(logger, "load devices");

		long hwnd = aci::queryDeviceInfo(this->agilor_name);

		DEV_INFO dev;
		devices.clear();
		while (aci::enumDeviceInfo(hwnd, NULL, &dev))
		{
			devices.push_back(string(dev.szDeviceName));
			if (!dev.bIsOnline)
			{
				aci::registerDevice(LOCALHOST, RTDB_PORT, devices.back());
			}
		}
		isConnected = true;
		LOG4CPLUS_INFO(logger, "server is started");
	}
}
AgilorHandler::~AgilorHandler()
{
	if (isConnected)
	{
		Agcn_Disconnect(this->agilor_name.data());
		Agcn_RemoveNode(this->agilor_name.data());
		Agcn_Cleanup();
		isConnected = false;
	}

	for (vector<string>::iterator it = devices.begin(); it != devices.end(); it++)
	{
		aci::md_UnregisterDevice(*it, NULL);
		it->erase();
	}
}

void AgilorHandler::ping() {
	//LOG4CPLUS_INFO(logger, "trigger function that ping..");
	cout << "总运行次数" << target_count << endl;
}

bool AgilorHandler::find_device(const string& name)
{
	return find(devices.begin(), devices.end(), name) != devices.end();
}

void  AgilorHandler::AddDevice(const DEVICE& device)
{
	LOG4CPLUS_INFO(logger, "trigger function that AddDevice");

	if (device.name.empty())
		throw aci::exception::EParamErrorException("name");

	//目前Agilor最大设备连接数只能达到176-179
	if (devices.size() >= max_of_device)
		throw  aci::exception::EDeviceMaxException();

	if (find_device(device.name))
		throw  aci::exception::EDeviceIsExistException(device.name);

	ACI_TAG_NODE tag;
	aci::init(tag);
	strcpy(tag.sval, "DEFAULT");
	strcpy(tag.sourceserver, device.name.data());
	strcpy(tag.sourcegroup, device.name.data());
	strcpy(tag.sourcetag, device.name.data());
	strcpy(tag.name, ("%%%%" + device.name + "%%%%").data());

	LOG4CPLUS_INFO(logger, "begin to add a new device,name is " << device.name);

	if (!aci::addNewTag(this->agilor_name, tag, false))
	{
		LOG4CPLUS_INFO(logger, "the device is added,name is " << device.name);
		LOG4CPLUS_INFO(logger, "begin to register the devices,name is " << device.name);


		int i = 3;

		while (i-->0)
		{
			if (!aci::registerDevice(LOCALHOST, RTDB_PORT, device.name))
			{
				LOG4CPLUS_INFO(logger, "the device is registered,name is " << device.name << endl);
				devices.push_back(device.name);
				break;
			}
			Sleep(2);
		}
		
		if (i == -1)
		{
			throw  aci::exception::EDeviceInsertException(EXCEPTIONTYPE::REGISTERFAIL);
			LOG4CPLUS_INFO(logger, "the device register fail,name is " << device.name << endl);
		}
	}
	else
	{
		throw  aci::exception::EDeviceInsertException(EXCEPTIONTYPE::INSERTFAIL);
		LOG4CPLUS_INFO(logger, "the device add fail,name is " << device.name);
	}
}

void AgilorHandler::DeleteDevice(const std::string& name)
{

	LOG4CPLUS_INFO(logger, "trigger DeleteDevice,device'name is " << name);

	vector<string>::iterator it = find(devices.begin(), devices.end(), name);
	if (it == devices.end())
		throw  aci::exception::EDeviceNotFoundException(name);

	long hwnd = aci::queryTagsbyDevice(agilor_name, name);
	if (hwnd < 0)
	{
		LOG4CPLUS_INFO(logger, "memeory cache error, don't find any targer with " << name);
		return;
	}

	string tag_name;
	long tag_id = 0;

	aci::md_UnregisterDevice(name, NULL);
	while (aci::enumTagName(hwnd, &tag_id, tag_name))
	{
		
		aci::removeTag(this->agilor_name, tag_id);
	}
	

	devices.erase(it++);
}

//函数需修改，改为从devices中获取
int32_t AgilorHandler::QueryDeviceInfo() {
	LOG4CPLUS_INFO(logger, "trigger function that QueryDeviceInfo");
	//return 1;
	return aci::queryDeviceInfo(this->agilor_name);
}

//函数需修改，改为从devices中获取
void AgilorHandler::EnumDeviceInfo(DEVICE& _return, const int32_t hRecordset) {
	LOG4CPLUS_INFO(logger, "trigger function that EnumDeviceInfo");

	DEV_INFO source;

	if (aci::enumDeviceInfo((int)hRecordset, NULL, &source))
	{
		aci::parse(source, _return);
		_return.id = 1;
	}
	else
	{
		_return.id = 0;
		throw  aci::exception::EHandleErrorException(hRecordset);
	}
}

bool AgilorHandler::ModifyDevice(const DEVICE& device)
{
	return true;
}



void AgilorHandler::GetAllDevices(std::vector<DEVICE> & _return)
{
	long hwnd = aci::queryDeviceInfo(this->agilor_name);
	if (hwnd > 0)
	{
		DEVICE data;
		do
		{
			EnumDeviceInfo(data, hwnd);
			if (data.id < 0) break;
		} while (true);
	}
}

int32_t AgilorHandler::TagCountByDevice(const std::string& deviceName)
{
	LOG4CPLUS_INFO(logger, "trigger function that TagCountByDevice");
	if (!find_device(deviceName))
		throw aci::exception::EDeviceNotFoundException(deviceName);


	long hwnd = aci::queryTagsbyDevice(this->agilor_name, deviceName);

	if (hwnd < 0)
	{
		LOG4CPLUS_INFO(logger, "the " << deviceName << "'s target handle is error");
		return 0;
	}
	string name;
	long count = 0;
	while (aci::enumTagName(hwnd, NULL, name))
		count++;
	return count>1 ? count - 2 : 0;
}

int32_t AgilorHandler::QuerySnapshots(const std::string & tagNames, const int32_t tagCount) {
	LOG4CPLUS_INFO(logger, "trigger function that QuerySnapshots");
	// Your implementation goes here
	return aci::querySnapshots(this->agilor_name, tagNames.data(), tagCount);

}

void AgilorHandler::GetNextTagValue(TAGVAL& _return, const int32_t hRecordset, const bool isRemoved) {
	LOG4CPLUS_INFO(logger, "trigger function that GetNextTagValue");
	// Your implementation goes here
	ACI_TAGVAL info;

	if (aci::getNextTagValue(hRecordset, &info, isRemoved)>0)
	{
		aci::parse(info, _return);
		_return.id = 1;
	}
	else throw  aci::exception::EHandleErrorException(hRecordset);
}

int32_t AgilorHandler::SubscribeTags(const std::string& tagNames, const int16_t count) {

	// Your implementation goes here
	return aci::subscribeTags(agilor_name, tagNames.data(), count);

	//return Agda_SubscribeTags(serverName.data(), tagNames.data(), tagNames.size());
}

void AgilorHandler::GetSubTagValue(TAGVAL& _return) {
	// Your implementation goes here

	ACI_TAGVAL val;
	Agda_GetSubTagValue(&val);
	aci::parse(val, _return);
}

int32_t AgilorHandler::UnSubscribeTags(const std::string& tagNames, const int16_t count) {
	// Your implementation goes here

	//return Agda_UnSubscribeTags(serverName.data(), tagNames.data(), tagNames.size());
	return 0;

}

int32_t AgilorHandler::UnSubscribeAll() {
	// Your implementation goes here
	//return Agda_UnSubscribeAll(serverName.data());
	return 0;
}


int32_t AgilorHandler::QueryTagHistory(const std::string& tagName, const int32_t startTime, const int32_t endTime, const int32_t step) {
	// Your implementation goes here
	LOG4CPLUS_INFO(logger, "trigger function that QueryTagHistory");

	return aci::queryTagHistory(agilor_name, tagName, startTime, endTime, 0);

}

void AgilorHandler::GetAggregateValue(TAGVAL& _return, const int32_t hRecordset, const AGGREGATE::type ag, const bool isRemoved) {
	// Your implementation goes here
	LOG4CPLUS_INFO(logger, "trigger function that GetAggregateValue");
	ACI_TAGVAL val;

	Agda_GetAggregateValue((long)hRecordset, &val, (int)ag, isRemoved);
	aci::parse(val, _return);
}





int32_t AgilorHandler::QueryTagsbyDevice(const std::string& deviceName) {
	LOG4CPLUS_INFO(logger, "trigger function that QueryTagsbyDevice");

	if (!find_device(deviceName))
		throw  aci::exception::EDeviceNotFoundException(deviceName);

	return aci::queryTagsbyDevice(this->agilor_name, deviceName);

	// Your implementation goes here
	//return Agpt_QueryTagsbyDevice(this->serverName.data(), deviceName.data());
}

void AgilorHandler::EnumTagName(std::string& _return, const int32_t hRecordset) {
	// Your implementation goes here
	//return Agpt_EnumTagName()

	LOG4CPLUS_INFO(logger, "trigger function that EnumTagName");
	if (!aci::enumTagName(hRecordset, NULL, _return))
		throw  aci::exception::EHandleErrorException(hRecordset);
}

void AgilorHandler::GetTagNamebyId(std::string& _return, const int32_t tagId) {
	LOG4CPLUS_INFO(logger, "trigger function that GetTagNamebyId");
	// Your implementation goes here

	aci::getTagNamebyID(agilor_name, tagId, _return);
}

void AgilorHandler::SetValue(const TAGVAL& value)
{
	LOG4CPLUS_INFO(logger, "trigger function that SetValue");
	TAG_VALUE_LOCAL tag;
	aci::parseValue(tag, value.value, value.type);
	tag.lTagID = value.id;
	tag.cTagType = value.type;
	tag.lTimeStamp = value.timestamp;
	tag.nTagState = value.state;
	strcpy_s(tag.szTagSource, sizeof(tag.szTagSource), value.name.data());
	if (aci::md_sendNewValue(value.device, tag))
		throw aci::exception::EWriteValueException(value.name, value);
}

void AgilorHandler::RemoveTag(const int32_t tagId) {
	LOG4CPLUS_INFO(logger, "trigger function that SetValue");
	long id = (long)tagId;
	aci::removeTag(this->agilor_name, id);
	//return Agpt_RemoveTag(this->serverName.data(), tagId);
	// Your implementation goes here
}

void AgilorHandler::GetTagInfo(TAGNODE& _return, const std::string& tagName){

	LOG4CPLUS_INFO(logger, "trigger function that GetTagInfo");

	// Your implementation goes here
	TAG_INFO info;

	if (aci::getTagInfo(this->agilor_name, tagName, &info))
	{
		aci::parse(info, _return);
		_return.id = 1;
	}
}

void AgilorHandler::AddNewTag(const TAGNODE& node, const bool isOverwrite) {

	LOG4CPLUS_INFO(logger, "trigger function that AddNewTag"<<target_count);
	target_count++;
	if (node.name.empty())
		throw aci::exception::EParamErrorException("name");
	if (node.deviceName.empty())
		throw aci::exception::EParamErrorException("deviceName");
	if (!(node.type == 'R' || node.type == 'B' || node.type == 'S' || node.type == 'L'))
		throw aci::exception::EParamErrorException("type");
	if (!find_device(node.deviceName))
		throw aci::exception::EDeviceNotFoundException(node.deviceName);


	ACI_TAG_NODE aci_node;

	aci::parse(node, aci_node);
	aci_node.scan = 1;

	if (!aci::addNewTag(this->agilor_name, aci_node, isOverwrite))
	{
		//TAG_VALUE_LOCAL val;
		//aci::parseValue(val, node.value, node.type);
		//val.lTimeStamp = node.timestamp;
		//val.cTagType = node.type;
		//val.nTagState = 8208;
		//memcpy(val.szTagSource, node.sourceTag.data(), sizeof(val.szTagSource));
		//
		//if (aci::md_sendNewValue(node.deviceName, val, true))
		//{
		//	TAGVAL e_val;
		//	aci::parse(val, e_val);
		//	throw  aci::exception::EWriteValueException(node.name, e_val);
		//}
	}
	else throw aci::exception::ETargetInsertException();

	
	
}


void AgilorHandler::EnumNextTag(TAGNODE& _return, const int32_t hwnd)
{
	LOG4CPLUS_INFO(logger, "trigger function that EnumNextTag");
	string name;

	_return.id = -1;

	while (aci::enumTagName(hwnd, NULL, name))
	{
		TAG_INFO info;
		if (!aci::getTagInfo(this->agilor_name, name, &info))
		{
			if (info.IOState != 16 && info.IOState != 0)
			{
				aci::parse(info, _return);
				_return.id = 1;
				break;
			}
			else LOG4CPLUS_INFO(logger, "getTagInfo fail, the target'name is " << name);
		}
		
		name.clear();
	}
	if (_return.id < 0)
		throw aci::exception::EHandleErrorException(hwnd);
}

void AgilorHandler::SetValues(const std::vector<TAGVAL>& values)
{
	LOG4CPLUS_INFO(logger, "trigger function that SetValues");
	if (values.empty())
		throw  aci::exception::EParamErrorException("values");
	string dev = values.at(0).device;
	if (dev.empty() || !find_device(dev))
		throw aci::exception::EDeviceNotFoundException(dev);
	int i = 0;
	for (std::vector<TAGVAL>::const_iterator it = values.begin(); it != values.end(); it++)
	{
		TAG_VALUE_LOCAL local;
		aci::parse(*it, local);
		if (aci::md_sendNewValue(dev, local, false))
			throw aci::exception::EWriteValueException(it->name, *it);
	}
	/*if (aci::md_sendNewValues(values.at(0).device, locals, values.size()))
	{
		throw aci::exception::EWriteValueException(values.at(0).name);
	}*/
	aci::md_flush(dev);
}

void AgilorHandler::closeLogger()
{
	logger.setLogLevel(OFF_LOG_LEVEL);
}

vector<string> AgilorHandler::getDevices()
{
	return devices;
}
