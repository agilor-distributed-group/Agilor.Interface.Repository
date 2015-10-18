#ifndef SNAPSHOT_H
#define SNAPSHOT_H

#include <iostream>
#include <string>
#include <sstream>

#include <boost\algorithm\string.hpp>
#include <boost\thread\once.hpp>
#include <boost\thread.hpp>
#include <boost\filesystem.hpp>
#include <boost\filesystem\fstream.hpp>
#include <boost\date_time\posix_time\posix_time.hpp>
#include <boost\property_tree\json_parser.hpp>
#include <boost\property_tree\ptree.hpp>
#include "inter_types.h"
#include "runtime.h"
#include "config.h"


using namespace std;
using namespace agilor::framework;

namespace aci
{
	namespace snapshot
	{
		class snapshot_data
		{
		public:
			string base;
			string name;
		};

		const string _snapshot_prefix = "snapshot";

		const  void _snapshot_save_exec(runtime<DEVICE>* _run)
		{
			cout << "snapshot" << endl;
			runtime<DEVICE>& database = *_run;
			cout << database.test_string << endl;
			while (config::program_is_run())
			{
				boost::this_thread::sleep(boost::posix_time::seconds(config::snapshot_save_interval()));

				boost::filesystem::path path(config::snapshot_path());


				if (!boost::filesystem::exists(path) || !boost::filesystem::is_directory(path))
					boost::filesystem::create_directory(path);

				boost::filesystem::directory_iterator directory_it(path);
				boost::filesystem::directory_iterator end;


				//获取所有的快照文件
				vector<boost::filesystem::path> snapshot_directories;
				for (; directory_it != end; directory_it++)
				{

					if (boost::filesystem::is_directory(*directory_it) && boost::starts_with(directory_it->path().filename().string(), _snapshot_prefix))
					{
						snapshot_directories.push_back(directory_it->path());
					}
				}
				//删除多余历史快照
				if (snapshot_directories.size() > config::snapshot_size())
				{
					for (auto it = snapshot_directories.end() - config::snapshot_size() - 1; it >= snapshot_directories.begin(); it--)
					{
						try
						{
							boost::filesystem::remove_all(*it);
						}
						catch (boost::filesystem::filesystem_error& err)
						{
							cout << err.what() << endl;
						}
					}
				}

				

				vector<snapshot_data> _data;
				_data.reserve(database.max_device_size());
				//开始整理需要保存的数据
				database.foreach([&](device<DEVICE>& tmp){
					
					snapshot_data _snap;

					_snap.base = tmp.base();
					_snap.name = tmp.name();
					_data.push_back(_snap);
					return true;
				});




				//创建文件
				
				

				try
				{
				
					boost::filesystem::path _new_path = path.string() + _snapshot_prefix + to_string(time(NULL));

					boost::filesystem::create_directory(_new_path);

					//写入文件
					auto& out = boost::filesystem::ofstream(boost::filesystem::path(_new_path.string() + "/data"), std::ios::trunc);

					boost::property_tree::ptree root;
					
					for (auto it = _data.begin(); it != _data.end(); it++)
					{
						boost::property_tree::ptree child;
						child.put("base", it->base);
						child.put("name", it->name);

						root.push_back(make_pair("", child));
					}

					std::stringstream sstr;

					boost::property_tree::write_json(sstr, root);
					out << sstr.str();
					out.close();
				}
				catch (boost::filesystem::filesystem_error& err)
				{
					cout << err.what() << endl;
				}

				try
				{
					boost::filesystem::remove_all((snapshot_directories.at(snapshot_directories.size() - 3)));
				}
				catch (boost::filesystem::filesystem_error& err)
				{
					cout << err.what() << endl;
				}
			}
		}

		const void _snapshot_load_exec(runtime<DEVICE>& database)
		{
			boost::filesystem::path path = config::snapshot_path();

			boost::filesystem::directory_iterator it(path);
			boost::filesystem::directory_iterator end;


			//挑出最大的时间戳
			boost::filesystem::path* snapshot_path = NULL;
			long time_tmp = 0;
			for (; it != end; it++)
			{
				if (boost::starts_with(it->path().filename().string(), _snapshot_prefix))
				{
					if (snapshot_path == NULL)
						*snapshot_path = it->path();
					else
					{
						 long it_time = boost::lexical_cast<long> (it->path().filename().string().substr(_snapshot_prefix.length()));
						 long la_time = boost::lexical_cast<long> (snapshot_path->filename().string().substr(_snapshot_prefix.length()));

						 if (la_time < it_time)
							 *snapshot_path = it->path();
					}
				}
			}

			if (snapshot_path != NULL)
			{
				boost::filesystem::ifstream in;
				in.open(path);

				string json;
				std::getline(in, json);

				std::istringstream issr;
				issr.str(json.c_str());


				map<string, DEVICE>  json_data;
			
				boost::property_tree::ptree parser;
				boost::property_tree::json_parser::read_json(issr, parser);
				

				for (boost::property_tree::ptree::iterator it = parser.begin(); it != parser.end(); it++)
				{
					boost::property_tree::ptree p = it->second;
					DEVICE _de;
					_de.name = p.get<string>("name");
					json_data.insert(pair<string, DEVICE>(p.get<string>("base"), _de));
				}


				database.foreach([&](device<DEVICE>& tmp){
					tmp = json_data.find(tmp.base())->second;
					return true;
				});
				in.close();
			}
		}
		


	}
}

#endif