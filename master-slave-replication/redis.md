# Linux下Redis主从复制操作流程
## 更改密码

进入redis命令：
> redis-cli

查看现有的redis密码: 
> config get requirepass

设置redis密码: 
> config set requirepass ****

退出并重新登录: 
> redis-cli -h 127.0.0.1 -p 6379 -a ****

改了密码之后还是可以不用账号密码登入Redis，但没有任何权限

## 查看当前库信息

``` Bash
127.0.0.1:6379> info replication
# Replication
role:master
connected_slaves:0
master_failover_state:no-failover
master_replid:e4176b2a9f41b62e60e6603fdfe8756a45e8bbaf
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:0
second_repl_offset:-1
repl_backlog_active:0
repl_backlog_size:1048576
repl_backlog_first_byte_offset:0
repl_backlog_histlen:0
```

## 复制多个配置

``` Bash
[root@iZwz995sygwab04zwh0glhZ server]# cd /www/server
[root@iZwz995sygwab04zwh0glhZ server]# ls ./redis
00-RELEASENOTES  deps       redis.conf       runtest-moduleapi  tests
BUGS             INSTALL    redis.log        runtest-sentinel   TLS.md
CONDUCT          Makefile   redis.pid        sentinel.conf      utils
CONTRIBUTING     MANIFESTO  runtest          src                version_check.pl
COPYING          README.md  runtest-cluster  start.pl           version.pl


[root@iZwz995sygwab04zwh0glhZ server]# cd redis
[root@iZwz995sygwab04zwh0glhZ redis]# cp redis.conf redis79.conf
[root@iZwz995sygwab04zwh0glhZ redis]# cp redis.conf redis80.conf
[root@iZwz995sygwab04zwh0glhZ redis]# cp redis.conf redis81.conf
[root@iZwz995sygwab04zwh0glhZ redis]# ls ./
00-RELEASENOTES  deps       redis79.conf  redis.pid          sentinel.conf  utils
BUGS       INSTALL        redis80.conf                  runtest            src            version_check.pl
CONDUCT          Makefile   redis81.conf  runtest-cluster    start.pl       version.pl
CONTRIBUTING     MANIFESTO  redis.conf    runtest-moduleapi  tests
COPYING          README.md  redis.log     runtest-sentinel   TLS.md

[root@iZwz995sygwab04zwh0glhZ redis]# vim redis79.conf
158>pidfile /www/server/redis/redis79.pid
171>logfile "/www/server/redis/redis79.log"
253>dbfilename dump79.rdb

[root@iZwz995sygwab04zwh0glhZ redis]# vim redis80.conf
92>port 6380
159>pidfile /www/server/redis/redis80.pid
172>logfile "/www/server/redis/redis80.log"
254>dbfilename dump80.rdb

[root@iZwz995sygwab04zwh0glhZ redis]# vim redis81.conf
93>port 6381
160>pidfile /www/server/redis/redis81.pid
173>logfile "/www/server/redis/redis81.log"
255>dbfilename dump81.rdb
```

## 启动多个实例

正常情况下直接到/bin目录下执行：

redis-server /www/server/redis/redis80.conf

redis-server /www/server/redis/redis81.conf

但由于宝塔管理，因此与正常操作不同

> https://sangsir.com/archives/bt-redis.html

``` Bash
[root@iZwz995sygwab04zwh0glhZ init.d]# cd /etc/init.d
[root@iZwz995sygwab04zwh0glhZ init.d]# cp redis redis6380
[root@iZwz995sygwab04zwh0glhZ init.d]# vim redis6380
```

看见以下的扩展名前都加上6380

> /www/server/redis/redis.conf

> /www/server/redis/start.pl

> STAR_PORT=

``` Bash
[root@iZwz995sygwab04zwh0glhZ init.d]# chkconfig --add redis6380
[root@iZwz995sygwab04zwh0glhZ init.d]# chkconfig --list
[root@iZwz995sygwab04zwh0glhZ init.d]# ./redis6380 start
Starting redis server...25096:C 19 Apr 2021 22:11:21.068 # Fatal error, can't open config file '/www/server/redis/redis6380.conf': Permission denied
Starting redis success!
```
以上的问题是因为配置文件没有访问权限

``` Bash
[root@iZwz995sygwab04zwh0glhZ init.d]# sudo chmod -R 777 /www/server/redis/redis6380.conf
[root@iZwz995sygwab04zwh0glhZ init.d]# ./redis6380 start
Starting redis server...
Starting redis success!
[root@iZwz995sygwab04zwh0glhZ init.d]# ps -ef|grep redis
redis    24398     1  0 22:04 ?        00:00:02      /www/server/redis/src/redis-server 127.0.0.1:6379
redis    25568     1  0 22:16 ?        00:00:00      /www/server/redis/src/redis-server 127.0.0.1:6380
root     25592  6407  0 22:16 pts/0    00:00:00       grep --color=auto redis
```

## 主从配置

登入6380端口的Redis并进行配置

``` Bash
[root@iZwz995sygwab04zwh0glhZ init.d]# redis-cli -p 6380
127.0.0.1:6380> slaveof 127.0.0.1 6379
OK

127.0.0.1:6380> info replication
# Replication
role:slave
master_host:127.0.0.1
master_port:6379
master_link_status:up
master_last_io_seconds_ago:4
master_sync_in_progress:0
slave_repl_offset:28
slave_priority:100
slave_read_only:1
connected_slaves:0
master_failover_state:no-failover
master_replid:56572e3ce6e48cc19b6de19e2d459f4633b22535
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:28
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:28
127.0.0.1:6380> 

127.0.0.1:6379> info replication
# Replication
role:master
connected_slaves:1
slave0:ip=127.0.0.1,port=6380,state=online,offset=196,lag=0
master_failover_state:no-failover
master_replid:56572e3ce6e48cc19b6de19e2d459f4633b22535
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:196
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:196
127.0.0.1:6379>
```

## 修改停止脚本

原来的Redis停止脚本

``` Bash
redis_stop(){
        echo "Stopping ..."
        $CLIEXEC shutdown
        sleep 1
        pkill -9 redis-server
        rm -f ${PIDFILE}
        echo "Redis stopped"
}
```

修改原来的Redis:6380的停止脚本

``` Bash
redis_stop(){
        echo "Stopping ..."
        $CLIEXEC shutdown
        sleep 1
        PID=`ps aux|grep "sudo -u redis"|grep -v "grep"|grep -v "/etc/init.d/redis6380"|awk '{print $2}'`
                if [ "${PID}" != "" ];then
                        sleep 3
                        pkill -9 redis-server
                        rm -f $PIDFILE
                fi
        echo "Redis stopped"
}
```

修改原来的Redis:6379的停止脚本

``` Bash
redis_stop(){
        echo "Stopping ..."
        $CLIEXEC shutdown
        sleep 1
        PID=`ps aux|grep "sudo -u redis"|grep -v "grep"|grep -v "/etc/init.d/redis"|awk '{print $2}'`
                if [ "${PID}" != "" ];then
                        sleep 3
                        pkill -9 redis-server
                        rm -f $PIDFILE
                fi
        echo "Redis stopped"
}
```

不再会因redis:6379停止并同执行redis:6380的停止

## 哨兵模式

``` Bash
[root@iZwz995sygwab04zwh0glhZ init.d]# cd /www/server/redis/src
[root@iZwz995sygwab04zwh0glhZ src]# ./redis-sentinel ../sentinel.conf

[root@iZwz995sygwab04zwh0glhZ init.d]# vim /www/server/redis/sentinel.conf
> sentinel moitor myredis 127.0.0.1 6379 1









31605:X 19 Apr 2021 23:15:52.910 # oO0OoO0OoO0Oo Redis is starting oO0OoO0OoO0Oo
31605:X 19 Apr 2021 23:15:52.910 # Redis version=6.2.1, bits=64, commit=00000000, modified=0, pid=31605, just started
31605:X 19 Apr 2021 23:15:52.910 # Configuration loaded
31605:X 19 Apr 2021 23:15:52.911 * monotonic clock: POSIX clock_gettime
                _._                                                  
           _.-``__ ''-._                                             
      _.-``    `.  `_.  ''-._           Redis 6.2.1 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._                                   
 (    '      ,       .-`  | `,    )     Running in sentinel mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 26379
 |    `-._   `._    /     _.-'    |     PID: 31605
  `-._    `-._  `-./  _.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |           http://redis.io        
  `-._    `-._`-.__.-'_.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |                                  
  `-._    `-._`-.__.-'_.-'    _.-'                                   
      `-._    `-.__.-'    _.-'                                       
          `-._        _.-'                                           
              `-.__.-'                                               

31605:X 19 Apr 2021 23:15:52.911 # WARNING: The TCP backlog setting of 511 cannot be enforced because /proc/sys/net/core/somaxconn is set to the lower value of 128.
31605:X 19 Apr 2021 23:15:52.919 # Sentinel ID is 0ac52fbaa85299c613057bd83fdba1a7c9d20e85
31605:X 19 Apr 2021 23:15:52.919 # +monitor master mymaster 127.0.0.1 6379 quorum 1
31605:X 19 Apr 2021 23:15:52.919 * +slave slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
```

编写启动脚本

``` Bash
#!/bin/sh
#
# chkconfig: 2345 55 25
# description: Redis server daemon
#
# processname: redis_6379 #需要修改与$port相同
# Simple Redis init.d script conceived to work on Linux systems
# as it does use of the /proc filesystem.
REDISPORT=26379   # 需要修改与$port相同
BINPATH=/www/server/redis/src
EXEC=${BINPATH}/redis-sentinel
CLIEXEC=${BINPATH}/redis-cli
CONF="/www/server/redis/sentinel.conf"
case "$1" in
    start)
        if [ -f $PIDFILE ]
        then
                echo "$PIDFILE exists, process is already running or crashed"
        else
                echo "Starting Redis $REDISPORT ..."
                $EXEC $CONF
        fi
        ;;
    stop)
        if [ ! -f $PIDFILE ]
        then
                echo "$PIDFILE does not exist, process is not running"
        else
                PID=$(cat $PIDFILE)
                echo "Stopping ..."
                $CLIEXEC -p $REDISPORT shutdown
                while [ -x /proc/${PID} ]
                do
                    echo "Waiting for Redis to shutdown ..."
                    sleep 1
                done
                echo "Redis stopped"
        fi
        ;;
    restart)
        $0 stop
        $0 start
        ;;
    *)
        echo "Usage: $0 {start|stop|restart}"
        exit
esac
```

## 开启远程连接

- 配置服务器安全组打开对应端口

- 更改配置

> bind 0.0.0.0

