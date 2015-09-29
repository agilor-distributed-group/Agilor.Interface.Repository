#ifndef BASESERVEREVENTHANDLER_H
#define BASESERVEREVENTHANDLER_H

#include <thrift\server\TServer.h>
#include <thrift\transport\TBufferTransports.h>
#include <thrift\transport\TServerSocket.h>
#include <thrift\transport\TSocket.h>

using namespace apache::thrift::server;
using namespace apache::thrift::transport;

class BaseServerEventHandler :virtual public TServerEventHandler
{
public:
	BaseServerEventHandler();
	void* createContext(boost::shared_ptr<TProtocol> input, boost::shared_ptr<TProtocol> output)override;
	void deleteContext(void* serverContext, boost::shared_ptr<TProtocol>input, boost::shared_ptr<TProtocol>output)override;



};


#endif