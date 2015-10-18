#include "target.h"

using namespace agilor::framework;


target::target(device_base* dev, const ACI_TAG_NODE& node)
{
	this->_dev = dev;
	*this = node;

}

target::target(device_base* dev, const TAG_INFO& node)
{
	this->_dev = dev;
	*this = node;
}

target::target(device_base* dev, const string& name, const var::valuetype& type)
{
	this->_dev = dev;
	this->_name = name;
	this->_type = type;
}

target::~target(){
}

string target::name() const{ return _name; }

value  target::value() const
{
	return _value;
}

void target::value(const string val, long timestamp)
{
	this->lock();
	if (_type != var::valuetype::STRING)
	{
		this->unlock();
		throw var::exceptions::VALUETYPEEXCEPTION;
	}

	TAG_VALUE_LOCAL VAL;
	_init_value_local(VAL, var::valuetype::STRING, timestamp);
	memcpy(VAL.szValue, val.data(), boost::extent<BOOST_TYPEOF(VAL.szValue)>::value);


	if (!aci::md_sendNewValue(this->_dev->base(), VAL, true))
	{
		this->_value = val;
		this->_timestamp = timestamp;
	}
	else
	{
		this->unlock();
		throw var::exceptions::UNKNOWNEXCEPTION;
	}
	this->unlock();
}
void target::value(const bool val, long timestamp)
{
	this->lock();
	if (_type != var::valuetype::BOOL)
	{
		this->unlock();
		throw var::exceptions::VALUETYPEEXCEPTION;
	}
	TAG_VALUE_LOCAL VAL;
	_init_value_local(VAL, var::valuetype::BOOL, timestamp);
	VAL.bValue = val;


	if (!aci::md_sendNewValue(this->_dev->base(), VAL, true))
	{
		this->_value = val;
		this->_timestamp = timestamp;
	}
	else
	{
		this->unlock();
		throw var::exceptions::UNKNOWNEXCEPTION;
	}
	this->unlock();
}
void target::value(const float val, long timestamp)
{
	this->lock();
	if (_type != var::valuetype::FLOAT)
	{
		this->unlock();
		throw var::exceptions::VALUETYPEEXCEPTION;
	}

	TAG_VALUE_LOCAL VAL;
	_init_value_local(VAL, var::valuetype::FLOAT, timestamp);
	VAL.fValue = val;

	string naaa = this -> _dev->base();

	if (!aci::md_sendNewValue(this->_dev->base(), VAL, true))
	{
		this->_value = val;
		this->_timestamp = timestamp;
	}
	else
	{
		this->unlock();
		throw var::exceptions::UNKNOWNEXCEPTION;
	}

	this->unlock();
}
void target::value(const long val, long timestamp)
{
	this->lock();
	if (_type != var::valuetype::LONG)
	{
		this->unlock();
		throw var::exceptions::VALUETYPEEXCEPTION;
	}
	TAG_VALUE_LOCAL VAL;
	_init_value_local(VAL, var::valuetype::LONG, timestamp);
	VAL.lValue = val;


	if (!aci::md_sendNewValue(this->_dev->base(), VAL, true))
	{
		this->_value = val;
		this->_timestamp = timestamp;
	}
	else
	{
		this->unlock();
		throw var::exceptions::UNKNOWNEXCEPTION;
	}
	this->unlock();

}

var::valuetype target::type() const
{
	return _type;
}

long  target::timestamp() const
{

	return _timestamp;
}

target& target::operator=(const ACI_TAG_NODE& node)
{
	this->lock();
	this->_id = node.pointid;
	this->_name = string(node.name);
	this->_type = (var::valuetype)node.pointtype;
	this->_timestamp = node.timedate;

	switch (this->_type)
	{
	case var::valuetype::BOOL:this->_value = (bool)node.bval; break;
	case var::valuetype::FLOAT: this->_value = node.rval; break;
	case var::valuetype::LONG:this->_value = node.lval; break;
	case var::valuetype::STRING:this->_value = node.sval; break;
	default:
		break;
	}
	this->unlock();
	return *this;
}

target& target::operator=(const TAG_INFO& node)
{
	this->lock();
	this->_id = node.TagID;
	this->_name = string(node.TagName);
	this->_type = (var::valuetype)node.TagType;
	this->_timestamp = node.Timestamp;

	switch (this->_type)
	{
	case var::valuetype::BOOL:this->_value = (bool)node.bval; break;
	case var::valuetype::FLOAT: this->_value = node.rval; break;
	case var::valuetype::LONG:this->_value = node.lval; break;
	case var::valuetype::STRING:this->_value = node.sval; break;
	default:
		break;
	}
	this->unlock();
	return *this;
}


long target::id() const
{
	return _id;
}

void target::lock() const
{
	_mutex->lock();
}

void target::unlock() const
{
	_mutex->unlock();
}

void target::destory()
{

}