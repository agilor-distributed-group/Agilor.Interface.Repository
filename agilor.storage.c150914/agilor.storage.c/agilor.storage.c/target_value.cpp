#include "target_value.h"

using namespace agilor::framework;

value::~value()
{
	if (_base != NULL)	delete _base;
	
}



value& value::operator=(string val)
{
	if (_base != NULL)
		delete _base;
	_base = new string(val);
	return *this;
}
value& value::operator=(float val)
{
	if (_base != NULL)
		delete _base;
	_base = new float(val);
	return *this;
}
value& value::operator=(long val)
{
	if (_base != NULL)
		delete _base;
	_base = new long(val);
	return *this;
}
value& value::operator=(bool val)
{
	if (_base != NULL)
		delete _base;
	_base = new bool(val);
	return *this;
}

template<class T>
bool value::operator==(const T& val)
{
	return false;
}

template<class T>
T& value::base()
{
	return  *static_cast<T*>(_base);
}

