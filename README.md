# 数据库的主从复制与读写分离引导

## Redis主从复制

- sentinel哨兵模式

当主机宕机了，哨兵就会按规定策略从众多从机中选举出一个从机当任临时主机

直到主机恢复，哨兵会自动让从机下岗，主机继续担任核心职位

![Screenshot](master-slave-replication/docs/sentinel.jpg)

## Mysql主从复制与读写分离

- amoeba读写分离

数据请求都先指向amoeba代理，amoeba再判断该请求是读的还是写的

如果是写的数据请求就会分配给主机执行

如果是读的数据请求就会分配给从机执行

当主从一方宕机了，那么对应的读写功能就消失了，状态维持直至恢复

![Screenshot](master-slave-replication/docs/amoeba.png)
