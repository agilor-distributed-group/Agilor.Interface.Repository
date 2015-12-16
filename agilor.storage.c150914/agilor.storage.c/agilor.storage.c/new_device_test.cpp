#define NEW_DEVICE_TEST_H
#ifdef NEW_DEVICE_TEST_H

#include "runtime.h"
#include "inter_types.h"
#include <thread>
#include "snapshot.hpp"


using namespace agilor::framework;

void main()
{



	const string device_prefix = "real-device-";

	
	//初始化测试 --成功
	runtime<DEVICE> _run("test",5);



	//插入设备测试 --成功
	for (int i = 0; i < _run.max_device_size(); i++)
	{
		DEVICE dev;
		dev.name = device_prefix + to_string(i);
		_run.insert(dev);
		//插入测试 空闲及占用设备数量 输出语句
		//cout << "空闲设备:" << _run.idle_device_size() << "占用设备:" << _run.busy_device_size() << "总量:"<<(_run.idle_device_size()+_run.busy_device_size())<< endl;
	}

	//cout << "空闲设备:" << _run.idle_device_size() << "占用设备:" << _run.busy_device_size() << "总量:"<<(_run.idle_device_size()+_run.busy_device_size())<< endl;

	//lambda输出循环 测试成功
	_run.foreach([&](runtime<DEVICE>::_device_type& tmp){
		//插入测试 输出语句
		//cout << "真实名称:" << tmp.base() << "  对外名称:" << tmp.name() << endl;
		return true;
	});


	//点添加测试 -- 成功

	runtime<DEVICE>::_const_device_vec_iterator it = _run.find(device_prefix + to_string(0));
	if (it == _run.end())
		cout << "~~~~" << endl;
	else
	for (int i = 0; i < 10; i++)
	{
		ACI_TAG_NODE node;
		aci::init(node);
		node.scan = 1;
		node.pointtype = var::valuetype::FLOAT;
		memcpy(node.name, (device_prefix + to_string(0) + "target-" + to_string(i)).data(), sizeof(node.name));
		memcpy(node.sourceserver, (device_prefix + to_string(0)).data(), sizeof(node.sourceserver));
		memcpy(node.sourcetag, (device_prefix + to_string(0) + "target-" + to_string(i)).data(), sizeof(node.sourcetag));

		try

		{
				(*it)->insert(node);
		}
		catch (var::exceptions ex){
			cout << (int)ex << endl;
		}
	}


	



	//删除设备测试 --成功

	for (int i = 0; i < _run.max_device_size(); i++)
	{
		//find函数 测试成功
		runtime<DEVICE>::_const_device_vec_iterator it = _run.find(device_prefix + to_string(i));
		if (it == _run.end())
		{
			cout << "没找到" << device_prefix + to_string(i) << endl;
		}
		else
		{
			try
			{
				_run.remove(it);
				cout << "delete " << "空闲设备:" << _run.idle_device_size() << "占用设备:" << _run.busy_device_size() << "总量:" << (_run.idle_device_size() + _run.busy_device_size()) << endl;
			}
			catch (var::exceptions e)
			{

			}
			
		}
	}

	/*设备删除异常测试 
	   此测试需调整代码，模拟设备删除失败
	   
	   */

	//std::thread _revise_result_thread([&](){
	//	while (_run.busy_device_size())
	//	{
	//		
	//		//cout << "delete " << "空闲设备:" << _run.idle_device_size() << "占用设备:" << _run.busy_device_size() << "总量:" << (_run.idle_device_size() + _run.busy_device_size()) << endl;
	//		//
	//		//boost::this_thread::sleep(boost::posix_time::seconds(1));
	//	}
	//
	//});

	//_revise_result_thread.join();
	






	//设备修改测试

	//DEVICE dev2;
	//dev2.name = "test_update-0";
	//it = _run.find(device_prefix + to_string(0));
	//(**it) = dev2;

	//cout << (*it)->name() << endl;
	
	
	//点删除测试


	//device<DEVICE>& dev_re = **it;
	//target* p_target = dev_re.find(device_prefix + to_string(0) + "target-0");
	//if (p_target == NULL)
	//	cout << "没找到该点" << endl;
	//else
	//	dev_re.remove(*p_target);


	//点查找测试--成功
	//点修改测试--废除
	


	//点写值测试

	//p_target = dev_re.find(device_prefix + to_string(0) + "target-1");

	//for (int i = 0; i < 10; i++)
	//{
	//	int v = rand();
	//	p_target->value<float>((float)v);
	//}

	//p_target->value();

	//点当前值获取测试



	//多线程设备插入测试
	//多线程设备删除测试


	//多线程点插入测试
	//多线程点删除测试

	//多线程写值测试
	
	//快照保存测试

	boost::thread *snampshot_thread = new boost::thread(boost::bind(&aci::snapshot::_snapshot_save_exec, &_run));
	_run.test_string = "123";

	
	//快照读取测试
	

	snampshot_thread->join();
	


	getchar();
}



#endif