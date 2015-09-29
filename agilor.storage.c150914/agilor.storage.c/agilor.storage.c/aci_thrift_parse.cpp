#include "aci_thrift_parse.h"

void aci::parse(const DEV_INFO& source, DEVICE& dest)
{
	dest.name = source.szDeviceName;
}

void aci::parse(const ACI_TAGVAL& source, TAGVAL& dest)
{
	dest.name = source.TagName;
	dest.state = source.TagState;
	dest.timestamp = source.Timestamp;
	dest.type = source.TagType;
	switch (source.TagType)
	{
	case 'R':dest.value = to_string(source.rval); break;
	case 'B':dest.value = to_string(source.bval); break;
	case 'S':dest.value = source.sval; break;
	default:
		break;
	}
}

void aci::parse(const TAG_INFO& source, TAGNODE& dest)
{
	dest.alarmHi = source.AlarmHi;
	dest.alarmHiHi = source.AlarmHiHi;
	dest.alarmLo = source.AlarmLo;
	dest.alarmLolo = source.AlarmLoLo;
	dest.alarmState = source.AlarmState;
	dest.alarmType = source.AlarmType;
	dest.compressMin = source.CompressMin;
	dest.compressMax = source.CompressMax;
	dest.creationDate = source.CreationDate;
	dest.desc = source.TagDesc;
	dest.deviceName = source.DeviceName;
	dest.engUnit = source.EngUnit;
	dest.enumDesc = source.EnumDesc;
	dest.exceptionDev = source.ExceptionDev;
	dest.exceptionMax = source.ExceptionMax;
	dest.exceptionMin = source.ExceptionMin;
	dest.groupName = source.GroupName;
	dest.hihiPriority = source.HiHiPriority;
	dest.hiPriority = source.HiPriority;
	dest.hisIndex = source.HisIndex;
	dest.id = source.TagID;
	dest.interMethod = source.InterMethod;
	dest.IOState = source.IOState;
	dest.isArchived = source.IsArchived & 0x01;
	dest.isCompressed = source.IsCompressed & 0x01;
	dest.lastValue = source.LastValue;
	dest.loloPriority = source.LoLoPriority;
	dest.loPriority = source.LoPriority;
	dest.lowerLimit = source.LowerLimit;
	dest.name = source.TagName;
	dest.pushreference = source.PushReference;
	dest.ruleReference = source.RuleReference;
	dest.sourceTag = source.SourceTag;
	dest.state = source.TagState;
	dest.timestamp = source.Timestamp;
	dest.type = source.TagType;
	dest.typicalVal = source.TypicalVal;
	dest.upperLimit = source.UpperLimit;
	switch (source.TagType)
	{
	case 'R':dest.value = to_string(source.rval); break;
	case 'B':dest.value = to_string(source.bval); break;
	case 'S':dest.value = source.sval; break;
	default:
		break;
	}
}

void aci::parse(const TAGNODE& source, ACI_TAG_NODE& dest)
{
	aci::init(dest);
	dest.alarmhihi = source.alarmHiHi;
	dest.alarmlolo = source.alarmLolo;
	dest.alarmstate = source.alarmState;
	dest.alarmtype = source.alarmType;
	dest.compmax = source.compressMax;
	dest.compmin = source.compressMin;
	dest.creationdate = source.creationDate;
	memcpy(dest.descriptor, source.desc.data(), boost::extent<BOOST_TYPEOF(dest.descriptor)>::value);
	memcpy(dest.engunits, source.engUnit.data(), boost::extent<BOOST_TYPEOF(dest.engunits)>::value);
	memcpy(dest.enumdesc, source.enumDesc.data(), boost::extent<BOOST_TYPEOF(dest.enumdesc)>::value);
	dest.excdev = source.exceptionDev;
	dest.excmax = source.exceptionMax;
	dest.excmin = source.exceptionMin;
	dest.hihipriority = source.hihiPriority;
	dest.hipriority = source.hiPriority;
	dest.hisidx = source.hisIndex;
	dest.istat = source.state;
	dest.pointtype = source.type;
	dest.scan = source.IOState;

	switch (source.type)
	{
	case 'R':dest.rval = boost::lexical_cast<float>(source.value); break;
	case 'S':memcpy(dest.sval, source.value.data(), sizeof(dest.sval)); break;
	case 'B':dest.bval = boost::lexical_cast<bool>(source.value); break;
	case 'L':dest.lval = boost::lexical_cast<long>(source.value); break;
	default:
		break;
	}
	memcpy(dest.name, source.name.data(), boost::extent<BOOST_TYPEOF(dest.name)>::value);
	memcpy(dest.sourceserver, source.deviceName.data(), boost::extent<BOOST_TYPEOF(dest.sourceserver)>::value);
	memcpy(dest.sourcegroup, source.groupName.data(), boost::extent<BOOST_TYPEOF(dest.sourcegroup)>::value);
	memcpy(dest.sourcetag, source.name.data(), boost::extent<BOOST_TYPEOF(dest.sourcetag)>::value);
}

void aci::parse(const TAG_VALUE_LOCAL& source, TAGVAL& dest)
{
	dest.id = 0;
	dest.name = source.szTagSource;
	dest.state = source.nTagState;
	dest.timestamp = source.lTimeStamp;
	dest.type = source.cTagType;
	switch (source.cTagType)
	{
	case 'R':dest.value = to_string(source.fValue); break;
	case 'B':dest.value = to_string(source.bValue); break;
	case 'L':dest.value = to_string(source.lValue); break;
	case 'S':dest.value = source.szValue; break;
	default:
		break;
	}
}
void aci::parse(const TAGVAL& source, TAG_VALUE_LOCAL& dest)
{
	strcpy_s(dest.szTagSource, sizeof(dest.szTagSource), source.name.data());

	aci::parseValue(dest, source.value, source.type);

	dest.nTagState = source.state;
	dest.cTagType = source.type;
	dest.lTimeStamp = source.timestamp;
}