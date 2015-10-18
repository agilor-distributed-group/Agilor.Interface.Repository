#ifndef INTER_ENUM_TYPES_H
#define INTER_ENUM_TYPES_H

namespace agilor
{
	namespace framework
	{
		namespace var
		{
			enum states
			{
				NORMAL = 0,
				EMPTY=1,
				REMOVING=2
			};
			enum exceptions
			{
				NOEXCEPTION=0,
				BUSYEXCEPTION=1,
				NULLEXCEPTION=2,

				TARGETEXISETEXCEPTION=3,
				VALUETYPEEXCEPTION=4,
				MAXDEVICEEXCEPTION=5,
				DEVICEISEXISTEXCEPTION=6,
				REGISTEREXCEPTION=7,
				UNKNOWNEXCEPTION=8,
				DEVICENOTFOUNDEXCEPTION=9
			};
			enum valuetype
			{
				UNDEFINE = -1, 
				FLOAT = 'R', 
				STRING = 'S', 
				BOOL = 'B', 
				LONG = 'L',
			};
		}
	}
}

#endif