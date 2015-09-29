//#define TEST_MAIN
#ifndef TEST_MAIN
#include "Agilor.h"
#include <thrift/protocol/TBinaryProtocol.h>
#include <thrift/server/TSimpleServer.h>
#include <thrift/transport/TServerSocket.h>

#include <thrift/transport/TBufferTransports.h>
#include <thrift/server/TThreadPoolServer.h>

#include <thrift/concurrency/ThreadManager.h>

#include <thrift/concurrency/StdThreadFactory.h>
#include "AgilorHandler.h"

using namespace ::apache::thrift;
using namespace ::apache::thrift::protocol;
using namespace ::apache::thrift::transport;
using namespace ::apache::thrift::server;
using namespace ::apache::thrift::concurrency;

using boost::shared_ptr;


int main(int argc, char **argv) {
	int port = 9090;

	auto* agilor = new AgilorHandler();
	agilor->closeLogger();
	boost::shared_ptr<AgilorHandler> handler(agilor);
	boost::shared_ptr<TProcessor> processor(new AgilorProcessor(handler));
	boost::shared_ptr<TServerTransport> serverTransport(new TServerSocket(port));
	boost::shared_ptr<TTransportFactory> transportFactory(new TBufferedTransportFactory());
	boost::shared_ptr<TProtocolFactory> protocolFactory(new TBinaryProtocolFactory());
	boost::shared_ptr<ThreadManager> threadManager = ThreadManager::newSimpleThreadManager(15);
	boost::shared_ptr<StdThreadFactory>threadFactory(new StdThreadFactory());

	threadManager->threadFactory(threadFactory);
	
	TThreadPoolServer server(processor, serverTransport, transportFactory, protocolFactory, threadManager);
	threadManager->start();
	server.serve();
	return 0;
}
#endif