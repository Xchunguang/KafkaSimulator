# Kafka消息模拟器
Kafka消息模拟器，支持消息的发送和消费

## 生产数据

支持自定义格式随机生成某种类型的字符串插入到kafka topic

- 设置IP地址和端口号

  ip和端口信息可以是kafka集群中任意一台机器
  
- 设置topic

  topic为kafka通道
  
- 设置分区数量
  
  通过设置分区数量可以将随机消息均匀发送到某topic的多个分区上。

- 设置传输字符串

  通过设置字符串格式可以生成随机字符串，其中
  - %s 代表随机字符串
  - %d 代表随机整数
  - %f 代表随机浮点型
  - %ts 代表随机时间戳字符串
  - %tl 代表随机时间戳long型串
  - 例：
 
		{'name':'%s','value':%d}
  
- 设置发送数量，发送数量为-1表示持续发送

- 设置发送间隔，单位为ms，表示发送频率

- 点击发送即开始生成随机串进行发送，发送成功则会在结果框中显示发送结果，发送失败则不显示。

## 消费数据

点击左下角按钮消费数据即打开消费数据框，消费数据与生产数据使用同一套配置，也就是主页面上的配置。打开消费数据页面会从当前topic的每个分区的偏移量为0开始消费。

消费数据和生产数据窗口可同时打开，可用于验证发送是否成功也可单独使用。

## 下载使用

链接：https://pan.baidu.com/s/1iA9ckFbt1kqUDZxBRNPtHQ 
提取码：ef09 

## 软件截图

![soft1](https://github.com/Xchunguang/KafkaSimulator/blob/master/src/main/resources/pages/images/soft-1.JPG)

![soft2](https://github.com/Xchunguang/KafkaSimulator/blob/master/src/main/resources/pages/images/soft-2.JPG)