# MySQL数据库主从复制

## 准备

主机：127.0.0.1:3306

从机：127.0.0.1:3307

## 主机

在my.cnf里增加一项配置

``` xml
server-id=1
```

授权一个账户供给从机以vong的用户和618168的密码登录我们的主机（%代表所有ip都可连）

``` mysql
CRANT REPLICATION SLAVE ON *.* TO 'vong'@'%' IDENTIFIED BY '618168'
```

刷新已执行的命令

``` mysql
flush privileges
```

查看主机状态，找到bin文件与其当前对应的位置

``` mysql
show master status
```

## 从机

在my.cnf里增加一项配置

``` xml
server-id=2
```

连接主机

``` mysql
CHANGE MASTER TO MASTER_HOST='127.0.0.1', 
MASTER_USER='vong', 
MASTER_PASSWORD='618168', 
MASTER_LOG_FILE='mysql-bin.000019',
MASTER_LOG_POS=698
```

开启从机并查看状态

``` mysql
start slave
show slave status
```

看见这两个状态Yes才代表主从复制完成

``` bash
Slave_IO_Running:Yes
Slave_SOL_Running:Yes
```

## 测试

在主机创建的库和表都会同步到从机，从机没有写的权限，即使数据写入了表在主机也无法被查到，只会占据一行

``` mysql
INSERT INTO o_user(id,NAME) VALUES(2,'Yan')
```





