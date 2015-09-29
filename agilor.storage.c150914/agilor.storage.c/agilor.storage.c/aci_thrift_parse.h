#ifndef ACI_THRIFT_PARSE
#define ACI_THRIFT_PARSE

#include "ACI.h"
#include "inter_types.h"
#include <boost\type_traits.hpp>
#include <boost\typeof\typeof.hpp>
#include <boost\lexical_cast.hpp>

namespace aci
{
	void parse(const DEV_INFO& source, DEVICE& dest);
	void parse(const ACI_TAGVAL& source, TAGVAL& dest);
	void parse(const TAG_INFO& source, TAGNODE& dest);
	void parse(const TAGNODE& source, ACI_TAG_NODE& dest);
	void parse(const TAG_VALUE_LOCAL& source, TAGVAL& dest);
	void parse(const TAGVAL& source, TAG_VALUE_LOCAL& dest);
	//void parse(const ACI_TA)
}

#endif