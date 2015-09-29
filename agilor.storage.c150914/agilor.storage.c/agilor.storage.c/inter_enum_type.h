#ifndef INTER_ENUM_TYPE_H
#define INTER_ENUM_TYPE_H
namespace aci
{
	enum State
	{
		NORMAL = 0,
		MONTIY = 1,
		REMOVED = 2
	};
	
		enum ValueType
		{
			STRING = 'S',
			LONG = 'L',
			FLOAT = 'R',
			BOOL = 'B'
		};
	

		enum agilor_exception
		{
			DEVICE_IS_EXIST = 0,
			MAX_DEVICE_COUNT = 1,
			DEVICE_NOT_FOUND = 2
		};
	
}

#endif