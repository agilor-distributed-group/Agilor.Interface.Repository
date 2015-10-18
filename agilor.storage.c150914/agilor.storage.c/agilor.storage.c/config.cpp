#include "config.h"

string config::_agilor_connect_name="agilor_";
LogLevel config::_log_level = DEBUG_LOG_LEVEL;
long config::_snapshot_save_interval = 300;//√Î 
string config::_snapshot_path = "snapshot/";
int config::_snapshot_size = 3;

bool config::_program_is_run = true;

string  config::_device_base="base"; 
int config::_max_device_size=170;