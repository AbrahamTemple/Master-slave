# Redis与Mysql主从复制并实现交互
## 最终要达到的需求蓝图

![Screenshot](target.png)

## 简单地实现一对一交互

> 该章对应的项目是ms

- 成功运行项目后，RabbitMQ里对应的交换机和队列就会被自动创建

![Screenshot](res_img/r3.png)

![Screenshot](res_img/2.png)

- 发送请求

![Screenshot](res_img/r1.png)

- Redis成功缓存

![Screenshot](res_img/r4.png)

- 控制台打印持久化业务成功日志

![Screenshot](res_img/r6.png)

- 数据成功写入mysql

![Screenshot](res_img/r5.png)

