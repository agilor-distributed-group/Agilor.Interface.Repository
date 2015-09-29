#ifndef TEST_MAIN
#define TEST_MAIN
//#define TEST
#ifdef TEST

//#define ACI_API
#define NEW_ACI_API

#include "AgilorHandler.h"

#include <sstream>

#include <log4cplus\fileappender.h>
#include <boost\date_time\posix_time\posix_time.hpp>

using namespace boost;
using namespace boost::posix_time;




void main()
{
	AgilorHandler handler;
	handler.closeLogger();

	//����ʱ������
	ptime start, end;
	time_duration duration;

	std::locale::global(std::locale("chs"));
	//��ʼ��Logger
	Logger logger = Logger::getInstance("TEST");
	SharedAppenderPtr console_appender(new ConsoleAppender());
	SharedAppenderPtr file_appender(new FileAppender("c:/log.txt"));
	//console_appender->setLayout(std::unique_ptr<Layout>(
	//	new log4cplus::PatternLayout("[%p][%c] %d{%Y-%m-%d %H:%M:%S} - %m %n")
	//	));

	file_appender->setLayout(std::unique_ptr<Layout>(
		new log4cplus::PatternLayout("[%p][%c] %d{%Y-%m-%d %H:%M:%S} - %m %n")
		));

	//logger.addAppender(console_appender);
	logger.addAppender(file_appender);
	logger.setLogLevel(DEBUG_LOG_LEVEL);

#define LOG_DEBUG(logevent) LOG4CPLUS_DEBUG(logger,logevent)
#define LOG_DEBUG_SUM(fn,times,duration,success) LOG4CPLUS_DEBUG(logger,"����: "<< fn <<" ���д���: "<< times <<" , ������ʱ��: "<<duration<<" ,ƽ������ʱ��: "<< duration*1.0/times<<" �ɹ�: "<<success<<" , ʧ��: "<<times-success) 

	

	//AddDevice����
	int device_count = 170;
	int real_device_count = 0;

	string device_prefix = "device_";


	start = microsec_clock::local_time();

	for (int i =0; i < device_count - 1; i++)
	{
		DEVICE device;
		device.name = device_prefix + to_string(i);

#ifdef NEW_ACI_API
		try
		{
			handler.AddDevice(device);
			real_device_count++;
			//LOG_DEBUG("����豸: " << device.name);

		}
		catch (aci::exception::EDeviceInsertException e)
		{
			switch (e.type)
			{
			case EXCEPTIONTYPE::INSERTFAIL:
				LOG_DEBUG("����豸: " << device.name << " ʧ�ܣ�������쳣"); break;
			case EXCEPTIONTYPE::REGISTERFAIL:
				LOG_DEBUG("����豸: " << device.name << " ʧ��,�����豸ʧ��"); break;
			default:
				break;
			}
		}
#else
		ACI_TAG_NODE node;
		aci::init(node);

		strcpy_s(node.name, sizeof(node.name), device.name.data());
		strcpy_s(node.sourceserver, sizeof(node.sourceserver), device.name.data());
		strcpy_s(node.sourcetag, sizeof(node.sourcetag), device.name.data());

		Agpt_AddNewTag("agilor", &node, true);
		DRTDB_RegisterDevice("127.0.0.1", 700, (LPSTR)device.name.data());
		LOG_DEBUG("����豸: " << device.name);
#endif

	}
	end = microsec_clock::local_time();

	duration = end - start;

	LOG_DEBUG_SUM("AddDevice", device_count - 1, duration.total_milliseconds(), real_device_count);

#ifdef NEW_ACI_API
	//�����豸�ظ��쳣

	vector<string> device_list = handler.getDevices();

	try
	{
		DEVICE exist_device;
		exist_device.name = device_list.at(0);
		handler.AddDevice(exist_device);
	}
	catch (aci::exception::EDeviceIsExistException e)
	{
		LOG_DEBUG("�豸�Ѿ������쳣���ԣ���ȷ"<<endl);
	}



	//��������豸�������쳣
	for (int i = real_device_count; i <= device_count; i++)
	{
		DEVICE max_plus_1_device;
		max_plus_1_device.name = device_prefix + to_string(170 + i);
		try
		{
			handler.AddDevice(max_plus_1_device);
			real_device_count++;
		}
		catch (aci::exception::EDeviceMaxException e)
		{
			LOG_DEBUG("����豸�� <=170 �쳣���ԣ���ȷ"<<endl);
			
		}
	}
#endif
	
	

	//QueryDeviceInfo����
	start = microsec_clock::local_time();
	int hwnd = handler.QueryDeviceInfo();
	end = microsec_clock::local_time();
	duration = end - start;

	LOG_DEBUG_SUM("QueryDeviceInfo", 1, duration.total_milliseconds(), (int)(hwnd > 0));

	
	//EnumDeviceInfo����
	if (hwnd > 0)
	{
		start = microsec_clock::local_time();
		int count = 0;
		while (true)
		{
			DEVICE de;
			try
			{
				handler.EnumDeviceInfo(de, hwnd);
				count++;
			}
			catch (aci::exception::EHandleErrorException e)
			{
				break;
			}
		}
		end = microsec_clock::local_time();
		duration = end - start;
		LOG_DEBUG("��ȡ�豸" << count << "��");
		LOG_DEBUG_SUM("EnumDeviceInfo", device_count, duration.total_milliseconds(), count);
	}




	
	//AddNewTag����
	
	start = microsec_clock::local_time();
	int every_device_target_count = 10;
	int add_target_success = 0;
	string target_prefix = "target_";
	for (int j = 0; j < device_count - 1; j++)
	{
		for (int i = 0; i < every_device_target_count; i++)
		{
			TAGNODE node;
			node.value = to_string(rand());
			node.deviceName = device_prefix + to_string(j);
			node.type = 'R';
			node.name = device_prefix + to_string(j) + "_" + target_prefix + to_string(i);
			
			try
			{
				handler.AddNewTag(node, true);
				//LOG_DEBUG("�豸: "<< node.deviceName << " ��ӵ�: " << node.name);
				add_target_success++;
			}
			catch (aci::exception::EParamErrorException e)
			{
				LOG_DEBUG(e.name << "��������ȷ");
			}
			catch (aci::exception::EDeviceNotFoundException e)
			{
				LOG_DEBUG(e.name << "������");
			}
			catch (aci::exception::ETargetInsertException e)
			{
				LOG_DEBUG("�����" << node.name << "ʧ��");
			}
		}
	}
	end = microsec_clock::local_time();
	duration = end - start;
	LOG_DEBUG_SUM("AddNewTag", ((device_count - 1)*every_device_target_count), duration.total_milliseconds(), add_target_success);

	
	//TagCountByDevice����
	for (int i = 0; i < device_count - 1; i++)
	{
		int c = handler.TagCountByDevice(device_prefix+to_string(i));
		LOG_DEBUG("�豸 " << device_prefix + to_string(i) << " ������ " << c);
	}

	
	//QueryTagsbyDevice����
	start = microsec_clock::local_time();
	hwnd = handler.QueryTagsbyDevice(device_prefix + to_string(0));
	if (hwnd > 0)
	{

		int count = 0;
		while (true)
		{
			TAGNODE node;
			try
			{
				handler.EnumNextTag(node, hwnd);
				count++;
			}
			catch (aci::exception::EHandleErrorException e)
			{
				break;
			}
		}
		end = microsec_clock::local_time();
		duration = end - start;
		LOG_DEBUG_SUM("QueryTagsbyDevice", every_device_target_count, duration.total_milliseconds(), count);

		if (count == every_device_target_count)
		{
			LOG_DEBUG("��ȡ�㼯�ϳɹ�");
		}
		else
			LOG_DEBUG("��ȡ�㼯��ʧ��");
	}
	else
	{
		LOG_DEBUG("��ȡ�㼯�ϴ���");
	}


	//SetValue����
	start = microsec_clock::local_time();
	int target_value_count = 1000000;
	long begin_time = time(NULL);
	LOG_DEBUG("��ʼʱ��: " << begin_time);
	int write_value_success = 0;
	string target_name = device_prefix + to_string(0) + "_" + target_prefix + to_string(0);

	string* values = new string[target_value_count];
	

	for (int i = 0; i < target_value_count; i++)
	{
		TAGVAL val;
		val.name = target_name;
		val.timestamp = begin_time+i;
		val.value = to_string(rand() % 500);
		val.device = device_prefix + to_string(0);
		val.type = 'R';

		try
		{
			//handler.SetValue(val);
			*(values + i) = val.value;
			write_value_success++;
			//cout << val.value << " ";
		}
		catch (aci::exception::EWriteValueException e)
		{
			LOG_DEBUG(val.value << "д���쳣");
		}
	}
	cout << endl;
	end = microsec_clock::local_time();
	duration = end - start;

	LOG_DEBUG_SUM("SetValue", target_value_count, duration.total_milliseconds(), write_value_success);



	//����SetValues

	
	//vector<TAGVAL>* vals = new vector<TAGVAL>();
	//long max_size = vals->max_size();
	//vals->reserve(target_value_count);
	//long size = sizeof(TAGVAL)*target_value_count;
	//for (int i = 0; i < target_value_count; i++)
	//{
	//	if (i == 3597)
	//	{
	//		int a = 0;
	//	}
	//	TAGVAL val;
	//	val.name = target_name;
	//	val.timestamp = begin_time + i;
	//	val.value = to_string(rand() % 500+1.5);
	//	val.device = device_prefix + to_string(0);
	//	val.type = 'R';
	//	vals->push_back(val);
	//}
	//start = microsec_clock::local_time();
	//handler.SetValues(*vals);
	//end = microsec_clock::local_time();
	//duration = end - start;
	//LOG_DEBUG_SUM("SetValues", target_value_count, duration.total_milliseconds(), target_value_count);
	



	start = microsec_clock::local_time();
	hwnd = handler.QueryTagHistory(target_name, begin_time, begin_time + target_value_count, 0);

	int ii = 0;

	if (hwnd > 0)
	{
		TAGVAL v;
		while (true)
		{
			try
			{
				handler.GetNextTagValue(v, hwnd, true);
				
				if (boost::lexical_cast<float>(v.value) != boost::lexical_cast<float>(*(values + (ii++))))
				{
					LOG_DEBUG("ʱ��: " << v.timestamp << " ��ʷֵ: " << v.value << "ʵ��ֵ: " << *(values + (ii-1)));
				}
				else cout << v.value << " ";
			} 
			catch (aci::exception::EHandleErrorException e)
			{
				break;
			}
		}
		cout << endl;
		end = microsec_clock::local_time();
		duration = end - start;

		LOG_DEBUG_SUM("QueryTagHistory", target_value_count, duration.total_milliseconds(), (ii-1));
		
	}
	else
		LOG_DEBUG("��ȡ��ʷֵʧ��");


	






	
	
	
	//ɾ�����ԣ����Ժ�������Щ���⣬APIҲ�����⣬������ζ�����true����ʱ�޷������ԭ���޷��ڱ�֤Ч�ʵ�ͬʱ��֤��ԭ���ԣ�
	start = microsec_clock::local_time();

	for (int i = 0; i < device_count; i++)
	{
		string name;
		if (i < device_count - 1)
			name = (device_prefix + to_string(i));
		else
			name = (device_prefix + to_string(170 + i));
		string old = name;
#ifdef NEW_ACI_API
		try
		{
			handler.DeleteDevice(name);
			
		}
		catch (aci::exception::EDeviceNotFoundException e)
		{
			
			LOG_DEBUG("�豸 " << old  << " ������");
		}

#endif
	}
	end = microsec_clock::local_time();
	duration = end - start;
	
	LOG4CPLUS_DEBUG(logger, "1");

	LOG_DEBUG_SUM("DeleteDevice", device_count,duration.total_milliseconds(), device_count);
	
	

	//�����豸�������쳣
	try
	{
		handler.DeleteDevice("not_exist_device");
	}
	catch (aci::exception::EDeviceNotFoundException e)
	{
		LOG_DEBUG("�豸�������쳣����,��ȷ");
	}
	
	
	
	

	cout << "finish" << endl;

	getchar();
}

#endif
#endif