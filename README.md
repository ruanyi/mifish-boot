# mifish-boot

## 一、背景

   Spring Boot 是一个非常优秀的开源框架，可以非常方便地就构建出一个基于 Spring 的应用程序，但是，在使用过程中，还是不太方便，因此，构建mifish-boot工程，制定一些规范，方便使用。
   
   基于业务：对图片、视频、音频这三种媒体做一些业务处理，也顺便不断地完善mifish-boot的基础框架能力。

## 二、多媒体处理体系

   原计划应该独立一项工程的，但是，后面想想，还是先合在一起，若：真发展起来了，再拆开，先不想得太复杂，先按照自己的想法，把代码垒起来。
   
   包括：1、对音频、视频、图片的一些基础处理。
        2、画质评测：主观评测、客观评测
        3、媒体库：对媒体的管理功能。
        4、基于：faas的资源管理、调度
        5、算法、模型工程化
        6、分治法加速：切片 -> 并行处理 -> 合并，一般针对于视频
        7、dag引擎，编排：音视频处理
        8、同步转异步

## 三、各项子功能

   对音频、视频、图片做处理，都是重CPU密集型 or GPU密集型操作，非常费cpu，因此，采用：总控进程 + 处理进程拆开部署的方式。
   
     1、总控进程：一些业务处理，报文包装，一般几个pod足够
     2、处理进程：真正处理：音频、视频、图片的进程：
         对于视频而言：一个pod处理一笔业务即可，对于图片、音频而言，一个pod处理1~2个业务即可
     3、总控进程与处理进程：制定标准的报文协议，采用异步回调的方式，通过消息中间件进行隔离。
   
   media-mifish-starter：提供处理的基础代码
   
    对视频做一些处理：x264、x265、x264文字水印，图片水印，片尾水印，斜文字水印等

    对音频做一些处理：音频aac转码，格式转换等

    对图片做一些处理：缩略、毛玻璃、加水印、圆角等
    
   kaproxy-mifish-starter：
     
    处理进程，一般不直连MQ，例如：kafka，正是因为：它是cpu密集型，假设直连kakfa，会导致kafka的boker不间断断地认为：处理进程已死，触发Partitions不断地reblance
    
    因此，将消费与处理拆成2个进程来处理。kaproxy是一种：消费进程的解决方案。
    
   mifish-video-processor：
   
     音视频处理进程，cpu密集型操作  

   tomcat-mifish-starter：
   
     为总控进程提供一些基础的封装，一些基础的能力。例如：健康检查、tomcat9参数配置、access log日志规范、通用filter、限流等
     
   x8583-mifish-starter：
   
     与多媒体处理关系不大，只是对曾经工作的总结，该功能比较简单，也暂时不想切换工程，因此保留在这里面。
     
   mifish-web：
   
      总控进程：包装的很多业务接口