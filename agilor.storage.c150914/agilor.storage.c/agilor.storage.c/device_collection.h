#ifndef DEVICE_COLLECTION_H
#define DEVICE_COLLECTION_H


#include <mutex>
#include <functional>
#include "device.h"


using namespace std;
using namespace aci;


class device_collection 
{
private:
	vector<device> _base;
	

	int _device_max_size = 170;

	mutex _mutex;

public:
	device_collection(int max_size = 170);
	
	void insert(const device& d);

	void foreach(function<bool(device& )> fn);



	void remove(const device& d);

	void remove(const string& name);


	device* find(function<bool(const device&)> condition);


	device* find(const string& name);

	void exec(const string& name, function<void( device&)> condition,  bool only);
};

#endif