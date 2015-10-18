#ifndef TARGET_H
#define TARGET_H

#include <string>
#include <vector>
#include <mutex>
#include <boost\date_time\posix_time\posix_time.hpp>
#include <boost\typeof\typeof.hpp>
#include "target_value.h"
#include "inter_enum_types.h"
#include "ACI.h"
#include "device_base.h"

using namespace std;

namespace agilor
{
	namespace framework
	{

		class target
		{
		private:

			long _id;
			string _name;
			var::valuetype _type;//值类型
			value _value;//当前值
			long _timestamp;//写入值的时间戳
			std::mutex* _mutex = new std::mutex();

			device_base* _dev = NULL;

		private:

			void _init_value_local(TAG_VALUE_LOCAL& VAL, var::valuetype type, long timestamp)
			{
				VAL.lTagID = this->_id;
				VAL.lTimeStamp = timestamp;
				VAL.cTagType = type;
				VAL.lLocalID = this->_id;
				VAL.nTagState = 8208;
				memcpy(VAL.szTagSource, this->_name.data(), boost::extent<BOOST_TYPEOF(VAL.szTagSource)>::value);
			}


		public:

			target( device_base* dev, const ACI_TAG_NODE& node);
			target( device_base* dev, const TAG_INFO& node);

			//此构造函数有 没有合适时机设置ID的缺陷，暂不适用
			target( device_base* dev, const string& name, const var::valuetype& type);
			~target();

			long id() const;
			string name() const;
			value value() const;


			/*void value(const string& val);
			void value(const bool& val);
			void value(const float& val);
			void value(const long& val);*/


			template<class T>
			void value(T val){ value(val, time(NULL)); }




			void value(const string val,long timestamp);
			void value(const bool val,long timestamp);
			void value(const float val,long timestamp);
			void value(const long val,long timestamp);

			var::valuetype type() const;
			long timestamp() const;

			void destory();


			void lock() const;
			void unlock() const;

			target& operator=(const ACI_TAG_NODE& node);
			target& operator=(const TAG_INFO& node);

		};

	}




}
#endif