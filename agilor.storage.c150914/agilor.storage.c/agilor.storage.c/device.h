#ifndef DEVICE_H
#define DEVICE_H

#include <iostream>
#include <string>
#include <vector>

#include "inter_enum_type.h"
#include "target.h"

using namespace std;

class device
{
private:
	string _base_name;
	string _name;

	vector<Target> _targets;

	bool _is_connected;

public:
	device(const string& name);
	void connect() const ;
	void disconnect();
	bool isConnected() const;

	void set_name(const string& name);
	string name() const;
	
	void insert_target(Target& t);

	bool operator==(const device& device);

};


#endif