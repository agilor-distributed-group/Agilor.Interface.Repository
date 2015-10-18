#ifndef CONFIG_H
#define CONFIG_H

#include <iostream>
#include <log4cplus\loglevel.h>

using namespace std;
using namespace log4cplus;

class config
{
private:
	static string _agilor_connect_name;//���ݿ���������
	static LogLevel _log_level;//log����ȼ�
	static long  _snapshot_save_interval;//second ���ձ�����
	static string _snapshot_path; //����·��
	static int _snapshot_size;//��������
	static bool _program_is_run;//�����Ƿ�����
	static string _device_base; //�豸���ƻ���
	static int _max_device_size; //�豸���������
	
	

public:
	static string agilor_connect_name(){ return _agilor_connect_name; }
	static LogLevel log_level(){ return _log_level; }
	static long snapshot_save_interval(){ return _snapshot_save_interval; }
	static string snapshot_path(){ return _snapshot_path; }
	static int snapshot_size(){ return _snapshot_size; }

	static string device_base(){ return _device_base; }
	static int max_device_size(){ return _max_device_size; }
	static bool program_is_run(){ return _program_is_run; }

	static void close(){ _program_is_run = false; }
	static void init(){ _program_is_run = true; }


};




#endif