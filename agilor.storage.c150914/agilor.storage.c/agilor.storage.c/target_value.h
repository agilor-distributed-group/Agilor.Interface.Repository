#ifndef TARGET_VALUE_H
#define TARGET_VALUE_H

#include <string>

using namespace std;

namespace agilor
{
	namespace framework
	{
		//Î´Íê³É

		class value
		{
		private:
			void* _base = NULL;


		public:
			~value();
			value& operator=(string val);
			value& operator=(float val);
			value& operator=(long val);
			value& operator=(bool val);



			template<class T> bool operator==(const T& val);

			template<class T> T& base();


		};

	}
}
#endif