2016-1-15 更新内容

修复部分bug，完善部分细节


2016-1-13 更新内容

修复部分bug

2016-1-11 更新内容

relational.data.manage 部分代码优化


2016-1-7 更新内容
socket-new模块更新,增加agilor.distributed.communication.socket.Connection.write2()函数
废弃 旧的 wirte函数，write(byte[] data) 没有废弃

relational.data.manage 模块更新，service和session层 增加部分接口

web接口大体完成（未测试，不能使用）



2016-1-4 更新内容(2)
1.缓存机制完成


2016-1-4 更新内容
1.建立用户数据相关的关系数据库
2.提供操作数据库相关的服务层（relational.data.manage）
3.建立分布式缓存机制（未完全测试，暂不提供使用）
4.建立分布式session（未完全测试，暂不提供使用，可先实现agilor.distributed.relational.data.context.IConnection接口）
6.部分服务调用方式见test代码
7.结构会在未来有变化

