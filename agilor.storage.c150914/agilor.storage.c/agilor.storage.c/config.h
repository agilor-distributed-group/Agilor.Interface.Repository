#ifndef CONFIG_H
#define CONFIG_H

#include <iostream>
#include <log4cplus\loglevel.h>

using namespace std;
using namespace log4cplus;

class config
{
private:
	static string _agilor_connect_name;//数据库连接名称
	static LogLevel _log_level;//log输出等级
	static long  _snapshot_save_interval;//second 快照保存间隔
	static string _snapshot_path; //快照路径
	static int _snapshot_size;//快照数量
	static bool _program_is_run;//程序是否允许
	static string _device_base; //设备名称基础
	static int _max_device_size; //设备最大连接数
	
	

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