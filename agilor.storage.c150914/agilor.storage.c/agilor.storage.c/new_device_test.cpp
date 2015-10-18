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

	
	//��ʼ������ --�ɹ�
	runtime<DEVICE> _run("test",5);



	//�����豸���� --�ɹ�
	for (int i = 0; i < _run.max_device_size(); i++)
	{
		DEVICE dev;
		dev.name = device_prefix + to_string(i);
		_run.insert(dev);
		//������� ���м�ռ���豸���� ������
		//cout << "�����豸:" << _run.idle_device_size() << "ռ���豸:" << _run.busy_device_size() << "����:"<<(_run.idle_device_size()+_run.busy_device_size())<< endl;
	}

	//cout << "�����豸:" << _run.idle_device_size() << "ռ���豸:" << _run.busy_device_size() << "����:"<<(_run.idle_device_size()+_run.busy_device_size())<< endl;

	//lambda���ѭ�� ���Գɹ�
	_run.foreach([&](runtime<DEVICE>::_device_type& tmp){
		//������� ������
		//cout << "��ʵ����:" << tmp.base() << "  ��������:" << tmp.name() << endl;
		return true;
	});


	//����Ӳ��� -- �ɹ�

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


	



	//ɾ���豸���� --�ɹ�

	for (int i = 0; i < _run.max_device_size(); i++)
	{
		//find���� ���Գɹ�
		runtime<DEVICE>::_const_device_vec_iterator it = _run.find(device_prefix + to_string(i));
		if (it == _run.end())
		{
			cout << "û�ҵ�" << device_prefix + to_string(i) << endl;
		}
		else
		{
			try
			{
				_run.remove(it);
				cout << "delete " << "�����豸:" << _run.idle_device_size() << "ռ���豸:" << _run.busy_device_size() << "����:" << (_run.idle_device_size() + _run.busy_device_size()) << endl;
			}
			catch (var::exceptions e)
			{

			}
			
		}
	}

	/*�豸ɾ���쳣���� 
	   �˲�����������룬ģ���豸ɾ��ʧ��
	   
	   */

	//std::thread _revise_result_thread([&](){
	//	while (_run.busy_device_size())
	//	{
	//		
	//		//cout << "delete " << "�����豸:" << _run.idle_device_size() << "ռ���豸:" << _run.busy_device_size() << "����:" << (_run.idle_device_size() + _run.busy_device_size()) << endl;
	//		//
	//		//boost::this_thread::sleep(boost::posix_time::seconds(1));
	//	}
	//
	//});

	//_revise_result_thread.join();
	






	//�豸�޸Ĳ���

	//DEVICE dev2;
	//dev2.name = "test_update-0";
	//it = _run.find(device_prefix + to_string(0));
	//(**it) = dev2;

	//cout << (*it)->name() << endl;
	
	
	//��ɾ������


	//device<DEVICE>& dev_re = **it;
	//target* p_target = dev_re.find(device_prefix + to_string(0) + "target-0");
	//if (p_target == NULL)
	//	cout << "û�ҵ��õ�" << endl;
	//else
	//	dev_re.remove(*p_target);


	//����Ҳ���--�ɹ�
	//���޸Ĳ���--�ϳ�
	


	//��дֵ����

	//p_target = dev_re.find(device_prefix + to_string(0) + "target-1");

	//for (int i = 0; i < 10; i++)
	//{
	//	int v = rand();
	//	p_target->value<float>((float)v);
	//}

	//p_target->value();

	//�㵱ǰֵ��ȡ����



	//���߳��豸�������
	//���߳��豸ɾ������


	//���̵߳�������
	//���̵߳�ɾ������

	//���߳�дֵ����
	
	//���ձ������

	boost::thread *snampshot_thread = new boost::thread(boost::bind(&aci::snapshot::_snapshot_save_exec, &_run));
	_run.test_string = "123";

	//���ն�ȡ����
	

	snampshot_thread->join();
	


	getchar();
}



#endif