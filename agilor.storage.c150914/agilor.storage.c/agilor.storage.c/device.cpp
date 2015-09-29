#include "device.h"

device::device(const string& name)
{
	this->_base_name = name;
	this->_name = name;
}

void device::connect()const
{
}
void device::disconnect()
{
};
bool device::isConnected()const
{
}

void device::set_name(const string& name)
{

}
string device::name() const
{
	return _name;
}

void device::insert_target(Target& t)
{

}
bool device::operator==(const device& device)
{
	return true;
}