#ifndef RUNTIME_H
#define RUNTIME_H

#include <iostream>
#include <string>
#include <vector>
#include <map>

#include <log4cplus\logger.h>


#include <boost\thread.hpp>
#include <boost\thread\condition.hpp>
#include <boost\thread\once.hpp>
#include <boost\typeof\typeof.hpp>

#include <log4cplus\consoleappender.h>
#include <log4cplus\layout.h>
#include <log4cplus\loggingmacros.h>


#include "config.h"
#include "ACI.h"
#include "device.h"

using namespace std;
using namespace log4cplus;
using namespace log4cplus::helpers;

namespace agilor
{
	namespace framework
	{
		template<class _DE>
		class runtime
		{

		public:
			typedef typename device<_DE> _device_type;
			typedef _device_type* p_device_type;
			typedef typename std::vector<p_device_type> _device_vector;
			typedef typename _device_vector::iterator _device_vec_iterator;
			typedef typename _device_vector::const_iterator _const_device_vec_iterator;
			typedef _device_vec_iterator* p_device_vec_iterator;

			typedef typename std::map<string, p_device_type> _device_map;
			typedef typename std::map<string, p_device_type>::iterator _device_map_iterator;
			typedef typename std::map<string, p_device_type>::const_iterator _const_device_map_iterator;


		private:
			int _max_device_size = 170;
			string _base;

			_device_vector _busy;
			_device_vector _idle;

			int _idle_count = 0;
			int _busy_count = 0;

			std::mutex* _mutex = new std::mutex();
			boost::mutex* _revise_mutex = new boost::mutex();

			boost::condition* _revise_cond = new boost::condition();


			boost::thread* _revise_thread=NULL;



		private:
			static Logger _logger;
			static boost::once_flag _init_logger_once;
			static boost::once_flag _init_connect_once;
			static boost::once_flag _init_revise_thread_once;
			static void _init_agilor_connect()
			{
				aci::startup();
				aci::createConnNode(config::agilor_connect_name());
				aci::connect(config::agilor_connect_name(), LOCALHOST, config::agilor_connect_name(), config::agilor_connect_name());
			}
			static void _init_logger()
			{
				SharedAppenderPtr console_appender(new ConsoleAppender());
				console_appender->setLayout(std::unique_ptr<Layout>(
					new log4cplus::PatternLayout("[%p][%c] %d{%Y-%m-%d %H:%M:%S} - %m %n")
					));

				_logger.addAppender(console_appender);

				_logger.setLogLevel(DEBUG_LOG_LEVEL);
			}

			
		private:

			void _foreach(std::function<bool(_device_vec_iterator&)> fn)
			{
				for (_device_vec_iterator it = _busy.begin(); it != _busy.end(); it++)
				{
					if (!fn(it))break;
				}
			}

			void _revise_exec()
			{
				while (config::program_is_run())
				{
					
					

					_mutex->lock();
					int c = 0;
					for (_device_vec_iterator it = _busy.begin(); it != _busy.end();)
					{
						if ((*it)->state() == var::states::REMOVING)
						{
							if ((*it)->destory() == var::states::EMPTY)
							{
								_idle_count++;
								_busy_count--;
							}
							c++;
						}
						else it++;
					}
					cout << "空闲设备" << _idle_count << "占用设备:" << _busy_count << endl;
					_mutex->unlock();
			
					if (c > 0)
						boost::this_thread::sleep(boost::posix_time::seconds(1));
					else
						_revise_cond->wait(boost::mutex::scoped_lock(*_revise_mutex));
					//Sleep(5000);
				}
			}
		public:
			string test_string = "abc";
			runtime(const string& base, int max_device_size = 170)
			{
				this->_base = base;
				this->_max_device_size = max_device_size;

				

				boost::call_once(&_init_logger, _init_logger_once);
				boost::call_once(&_init_agilor_connect, _init_connect_once);
				

				
				_busy.reserve(max_device_size);
				//_idle.reserve(max_device_size);

				for (int i = 0; i < _max_device_size; i++)
				{
					ACI_TAG_NODE node;
					aci::init(node);
					string tmp = this->_base + "-" + to_string(i);
					memcpy(node.name, ("%%%" + tmp + "%%%").data(), boost::extent<BOOST_TYPEOF(node.name)>::value);
					memcpy(node.sourcetag, ("%%%" + tmp + "%%%").data(), boost::extent<BOOST_TYPEOF(node.sourcetag)>::value);
					memcpy(node.sourceserver, tmp.data(), boost::extent<BOOST_TYPEOF(node.sourceserver)>::value);
					if (!aci::addNewTag(config::agilor_connect_name(), node, true))
					{
						int count = 3;

						while (count-- > 0)
						{
							if (count < 2)
								boost::this_thread::sleep(boost::posix_time::seconds(3));
							if (!aci::registerDevice(LOCALHOST, RTDB_PORT, tmp))break;
						}

						if (count == -1)
							throw var::exceptions::REGISTEREXCEPTION;
						else
						{
							_busy.push_back(new device<_DE>(tmp));
							_idle_count++;

							LOG4CPLUS_INFO(_logger, "the device " << tmp << " connected");
						}
					}
				}

				_revise_thread = new boost::thread(boost::bind(&runtime::_revise_exec, this));
				
			}
			~runtime(){}

			_const_device_vec_iterator find(string name)
			{
				
				_mutex->lock();

				_const_device_vec_iterator _it = _busy.end();

				_foreach([&](_device_vec_iterator& it){
					if ( (*it)->name() == name&&(*it)->state()==var::states::NORMAL)
					{
						_it = it;
						return false;
					}
					return true;
				});
				_mutex->unlock();

				return _it;
			}

			void foreach(std::function<bool(_device_type&) > fn)
			{
	
				_mutex->lock();
				_foreach([&](_device_vec_iterator& it){
					return fn(**it);
				});
				_mutex->unlock();
			}

			void insert(const _DE& dev)
			{
				_mutex->lock();
				var::exceptions ex = var::exceptions::NOEXCEPTION;
				if (_idle_count == 0)
					ex = var::exceptions::MAXDEVICEEXCEPTION;



				_device_vec_iterator _it = _busy.end();
				_device_vec_iterator _idle_it = _busy.end();

				_foreach([&](_device_vec_iterator& it){

					if ((_idle_it == _busy.end()) && (*it)->state() == var::states::EMPTY)
						_idle_it = it;
					
					if ((*it)->name() == dev.name)
						_it = it;
					return (*it)->name() != dev.name;
				});
				if (_it != _busy.end())
					ex = var::exceptions::DEVICEISEXISTEXCEPTION;

				if (ex != var::exceptions::NOEXCEPTION)
				{
					_mutex->unlock();
					throw ex;
				}


				(**_idle_it) = dev;
				_idle_count--;
				_busy_count++;

				_mutex->unlock();
			}

			void remove(_device_vec_iterator& it)
			{
				remove((_const_device_vec_iterator&)it);
			}

			void remove(_const_device_vec_iterator& it)
			{
				_mutex->lock();
				if (it != _busy.end())
				{
					if ((*it)->destory() == var::states::EMPTY)
					{
						_idle_count++;
						_busy_count--;
					}
					else
						_revise_cond->notify_one();
				} 
				_mutex->unlock();
			}


			int max_device_size(){ return _max_device_size; }

			int idle_device_size(){ return _idle_count; }

			int busy_device_size(){ return _busy_count; }

			_const_device_vec_iterator end() const
			{
				return _busy.end();
			}
		};

		template<class _DE>
		Logger runtime<_DE>::_logger = Logger::getInstance("agilor runtime");

		template<class _DE>
		boost::once_flag runtime<_DE>::_init_logger_once = BOOST_ONCE_INIT;
		template<class _DE>
		boost::once_flag runtime<_DE>::_init_connect_once = BOOST_ONCE_INIT;

		template<class _DE>
		boost::once_flag runtime<_DE>::_init_revise_thread_once = BOOST_ONCE_INIT;

	}
}



#endif