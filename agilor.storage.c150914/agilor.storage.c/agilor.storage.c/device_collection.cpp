#include "device_collection.h"


device_collection::device_collection(int max_size)
{
	this->_device_max_size = max_size;
	this->_base.reserve(max_size);
}


void device_collection::insert(const device& d)
{
	_mutex.lock();
	if (_base.size() >= _device_max_size)
		throw agilor_exception::MAX_DEVICE_COUNT;

	if (this->find(d.name()) != NULL)
		throw agilor_exception::DEVICE_IS_EXIST;


	this->_base.push_back(d);
	_mutex.unlock();
}

void device_collection::foreach(function<bool(device&)> fn)
{
	_mutex.lock();

	for (vector<device>::iterator it = _base.begin(); it != _base.end(); it++)
	{
		if (!fn(*it))break;
	}
	_mutex.unlock();
}

device* device_collection::find(const string& name)
{
	return this->find([&](const device& tmp){
		return tmp.name() == name;
	});
}

device* device_collection::find(function<bool(const device&)> condition)
{
	device * result = NULL;
	this->foreach([&](device & tmp){
		if (condition(tmp))
		{
			result = &tmp;
			return false;
		}
		return true;
	});
	return result;
}

void device_collection::remove (const device& d)
{
	_mutex.lock();
	
	auto it = std::find(this->_base.begin(), this->_base.end(), d);
	if (it == _base.end())
	{
		
		_mutex.unlock();
		throw agilor_exception::DEVICE_NOT_FOUND;
	}
	else
	{
		if (it->isConnected())
			it->disconnect();
		_base.erase(it);
	}

	_mutex.unlock();
}

void device_collection::remove(const string& name)
{

}


void device_collection::exec(const string& name, function<void(device&)> condition, bool only)
{
	
}