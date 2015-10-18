#ifndef DEVICE_H
#define DEVICE_H

#include <iostream>
#include <string>
#include <vector>
#include <map>
#include <mutex>
#include "target.h"
#include "ACI.h"
#include "config.h"
#include "inter_enum_types.h"
#include "device_base.h"

using namespace std;

namespace agilor
{
	namespace framework
	{
		
		template <class T>
		class device:public device_base
		{

		public:
			typedef typename target* p_target_type;
			typedef typename map<string,p_target_type> target_map;
			typedef typename target_map::iterator target_map_iterator;
			typedef typename target_map::const_iterator const_target_map_iterator;
		private:
			string _base;

			int exce_count = 3;//设备异常测试变量
			target_map _targets;
			var::states _state;
			T* _dev=NULL;
			std::mutex* _mutex=NULL;


		public:
			
			device(const string& base)
			{
				_mutex = new std::mutex();
				this->_base = base;

				long hwnd = aci::queryTagsbyDevice(config::agilor_connect_name(), this->_base);
				if (hwnd < 0) throw var::exceptions::DEVICENOTFOUNDEXCEPTION;

				string t_name;
				while (aci::enumTagName(hwnd, NULL, t_name))
				{
					TAG_INFO info;
					
					aci::getTagInfo(config::agilor_connect_name(), t_name, &info);
					if (info.IOState != 0 &&info.IOState != 16)
						this->_targets.insert(pair<string, target*>(string(info.TagName), new target(this,info)));
					
				}


				this->_state = var::states::EMPTY;
			}
			device(const string& base, const T& dev)
			{
				_mutex = new std::mutex();
				this->_base = base;
				*this = dev;
				this->_state = var::states::NORMAL;
			}
			~device(){
				std::cout << "xi gou" << endl;
			}

			var::states state() const
			{
				return  this->_state;
			}

			string name() const
			{
				return _dev != NULL ? _dev->name : "";
			};

			string base() const
			{
				return _base;
			}


			var::states destory()
			{
				_mutex->lock();

				//this->_state = var::states::EMPTY;
				//for (map<string, target*>::iterator it = this->_targets.begin(); it != this->_targets.end();)
				//{
				//	TAG_INFO info;
				//	aci::getTagInfo(config::agilor_connect_name(), it->first, &info);
				//	if (aci::removeTag(config::agilor_connect_name(), info.TagID))
				//	{
				//		this->_state = var::states::REMOVING;
				//		it++;
				//	}
				//	else
				//	{
				//		delete it->second;
				//		it = _targets.erase(it);
				//	}

				//}

				if (--exce_count)
					this->_state = var::states::REMOVING;
				else this->_state = var::states::EMPTY;


				if (this->state() == var::states::EMPTY)
					//delete this->_dev;
					this->_dev = NULL;
				_mutex->unlock();
				return this->state();
			}

			void remove(const target& t) 
			{
				remove(t.name());
			}

			void remove(const string& name)
			{
				_mutex->lock();

				const_target_map_iterator it = _targets.find(name);
				if (it != _targets.end())
				{
					if (!aci::removeTag(config::agilor_connect_name(), (it->second)->id()))
					{
						delete it->second;
						_targets.erase(it);
					}
					else
					{
						_mutex->unlock();
						throw var::exceptions::UNKNOWNEXCEPTION;
					}
				}


				_mutex->unlock();
			}

			T* dev() const
			{
				return _dev;
			}

			device& operator=(const T& dev)
			{
				if (this->state() == var::states::EMPTY||this->state()==var::states::NORMAL)
				{
					this->_dev =new T(dev);
					this->_state = var::states::NORMAL;
				}
				else if (this->state() == var::states::REMOVING)
				{
					throw var::exceptions::BUSYEXCEPTION;
				}
				return *this;
			}

			void insert( ACI_TAG_NODE& node)
			{
				string node_name = string(node.name);
				memcpy(node.sourceserver, this->_base.data(), boost::extent<BOOST_TYPEOF(node.sourceserver)>::value);

				_mutex->lock();
				var::exceptions ex = var::exceptions::NOEXCEPTION;

				if (this->state() == var::states::EMPTY)
					ex = var::exceptions::NULLEXCEPTION;
				if (this->state() == var::states::REMOVING)
					ex = var::exceptions::BUSYEXCEPTION;
				map<string, target*>::iterator it = _targets.find(node_name);
				if (it != _targets.end())
					ex = var::exceptions::TARGETEXISETEXCEPTION;

				if (ex != var::exceptions::NOEXCEPTION)
				{
					_mutex->unlock();
					throw ex;
				}

				if (aci::addNewTag(config::agilor_connect_name(), node, true))
				{
					_mutex->unlock();
					throw var::exceptions::UNKNOWNEXCEPTION;
				}
				TAG_INFO info;
				if (aci::getTagInfo(config::agilor_connect_name(), node_name, &info))
				{
					_mutex->unlock();
					throw var::exceptions::UNKNOWNEXCEPTION;
				}
				node.pointid = info.TagID;
				this->_targets.insert(pair<string, target*>(node_name, new target(this,node)));

				_mutex->unlock();
			}

			target* find(string name)
			{

				target* result = NULL;

				_mutex->lock();

				target_map_iterator it = this->_targets.find(name);

				if (it != _targets.end())
					result = it->second;
				_mutex->unlock();
				return result;
			}


			long target_size() const
			{
				return _targets.size();
			}


			const_target_map_iterator end()
			{
				return _targets.end();
			}
		};
	}

}




#endif