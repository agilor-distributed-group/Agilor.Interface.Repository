#include "BaseServerEventHandler.h"


BaseServerEventHandler::BaseServerEventHandler(){
}
void* BaseServerEventHandler::createContext(boost::shared_ptr<TProtocol> input, boost::shared_ptr<TProtocol> output)
{
	TBufferedTransport *tbuf = dynamic_cast<TBufferedTransport *>(input->getTransport().get());
	TSocket *sock = dynamic_cast<TSocket*>(tbuf->getUnderlyingTransport().get());
	
	return NULL;

}
void BaseServerEventHandler::deleteContext(void* serverContext, boost::shared_ptr<TProtocol>input, boost::shared_ptr<TProtocol>output)
{

}