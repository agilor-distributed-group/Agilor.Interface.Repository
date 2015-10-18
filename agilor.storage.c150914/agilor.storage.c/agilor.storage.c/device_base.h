#ifndef DEVICE_BASE_H
#define DEVICE_BASE_H

#include <string>

using namespace std;

namespace agilor
{
	namespace framework
	{
		class device_base
		{
		public:
			virtual string base() const=0;
		};
	}
}







#endif