@echo off

thrift -r --gen cpp inter.thrift
thrift -r --gen java inter.thrift

pause