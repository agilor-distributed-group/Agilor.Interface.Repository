#ifndef EXCEPTION_TYPES_H
#define EXCEPTION_TYPES_H

#include <string>

#include "inter_types.h"

using namespace std;

namespace aci
{
	namespace exception
	{

		class EDeviceIsExistException :public DeviceIsExistException
		{
		public:
			EDeviceIsExistException(const string& name)
			{
				this->name = name;
			}
		};

		class EDeviceNotFoundException:public DeviceNotFoundException
		{
		public:
			EDeviceNotFoundException(const string& name)
			{
				this->name = name;
			}
		};

		class EHandleErrorException :public HandleErrorException
		{
		public:
			EHandleErrorException(const int32_t hwnd)
			{
				this->hwnd = hwnd;
			}
		};

		class EWriteValueException :public WriteValueException
		{
		public:
			EWriteValueException(const string& name,const TAGVAL& val)
			{
				this->name = name;
				this->val = val;
			}
			EWriteValueException(const string& name)
			{
				this->name = name;
			}
		};

		class EDeviceMaxException :public DeviceMaxException
		{

		};
		class EDeviceInsertException :public DeviceInsertException
		{
		public:
			EDeviceInsertException(EXCEPTIONTYPE::type type)
			{
				this->type = type;
			}
		};


		class ETargetInsertException :public TargetInsertException
		{

		};

		class EParamErrorException : public ParamErrorException
		{
		public:
			EParamErrorException(string name)
			{
				this->name = name;
			}
		};


	}
}

#endif