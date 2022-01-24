# 1 绪论

## 1.1 课程大纲&理论篇概述

- 理论篇（RPC核心原理、相关技术介绍）

- 实战篇（`gk-rpc`代码实现、使用案例）

- 总结篇

## 1.2 概念讲解

**RPC**： Remote Procedure Call，即远程过程调用

是分布式系统常见的一种通信方法，从跨进程到跨物理机已经有几十年历史

**跨进程交互方式**：RESTful、WebService、HTTP、基于DB做数据交换、基于MQ做数据交换以及RPC

## 1.3 图解交互形式-现有框架对比

![image-20220124105403753](C:\Users\Jiang Haicheng\AppData\Roaming\Typora\typora-user-images\image-20220124105403753.png)

![image-20220124105506615](C:\Users\Jiang Haicheng\AppData\Roaming\Typora\typora-user-images\image-20220124105506615.png)

同步执行，客户端会影响到服务端，同步执行

------

在RPC中：

Server： Provider、服务提供者

Client： Consumer、服务消费者

Stub：存根、服务描述

![image-20220124111708964](C:\Users\Jiang Haicheng\AppData\Roaming\Typora\typora-user-images\image-20220124111708964.png)

## 1.4 核心原理

![image-20220124112258893](C:\Users\Jiang Haicheng\AppData\Roaming\Typora\typora-user-images\image-20220124112258893.png)

1、server把需要暴露的服务和地址信息注册到注册中心

2、client订阅注册中心，从注册中心关注需要的服务在哪里。如果server地址发生改变，需要重新注册到registry，然后registry通知client

3、client已经有了server的地址以及暴露的服务的信息，就可以进行调用了

注册中心不是必要的组件，client可以把服务端的信息写死，直接调用server

![image-20220124113624611](C:\Users\Jiang Haicheng\AppData\Roaming\Typora\typora-user-images\image-20220124113624611.png)

client要调用接口里的方法，也就是存根里的方法，方法的实现在远程，所以要通过网络传输才能达到调用（socket与server连接传输数据）。首先要把传输的对象转化为二进制，这个过程叫做序列化，server收到数据后要做的第一步(4)进行反序列化成对象。在对象中包含了客户端要调用的服务端的信息（哪个接口、接口的哪个方法、方法的参数类型、返回值类型、实参），server找到具体实现类的对象，找到对象后通过反射调用方法，调用完成后把结果序列化成二进制通过网络响应给client。client接收到数据后反序列化为结果

RPC需要网络模块、序列化模块（对象与二进制互转）、client端（如何通过调用接口调用到远程方法？内部有存根的代理对象，网络交互、序列化操作都是由代理对象完成）、server端（里面有服务管理组件，完成服务查找、服务反射调用）

## 1.5 技术栈

基础知识：JavaCore、Maven、反射

动态代理（生成client存根实际调用对象）：Java动态代理

序列化（Java对象与二进制数据互转）：Fastjson

网络通信（传输序列化后的数据，走HTTP）：jetty（服务端）、URLConnection（客户端）

# 2 实战篇

## 2.1 概述

第一步：创建工程、（server与client）指定协议、通用工具方法

第二步：实现序列化模块

第三步：实现网络模块（根据RPC通信场景，对网络通信做一层抽象）

第四步：实现Server模块（暴露服务，对服务做管理）

第五步：实现Client模块（用动态代理，代理对象内部通过网络通信与server进行交互）

第六步：`gk-rpc`使用案例

## 2.2 类图

![image-20220124122026361](C:\Users\Jiang Haicheng\AppData\Roaming\Typora\typora-user-images\image-20220124122026361.png)

协议模块：两个核心的类Request（需要请求server的哪个服务，请求服务时携带的参数）和Response（server响应给client的返回信息）

序列化模块：Encoder（对象转二进制）、Decoder（二进制转对象），都是基于JSON实现

网络模块：server端和client端，基于HTTP实现

server模块：`ServiceManager`维护`RpcServer`需要暴露的服务，`ServiceInstance`暴露出去的这个服务的具体对象、具体实现。暴露的服务注册到`ServiceManager`内

client模块：核心`RpcClient`，其中的`RemoteInvoker`会把client请求和server进行交互，交互的信息通过Request和Response进行封装。`TransportSelector`：因为一个client可能会连接多个server，把网络连接做了一个Selector抽象

## 2.3 创建工程

