`SocketServer2`为`SocketServer1`的改良版，通过使用Map将未读取完的数据进行缓存，以免浪费Buffer的内存空间。

# 注意事项

- `register(Selector sel, int ops)`实际上是`ServerSocketChannel`的父类`AbstractSelectableChannel`提供的一个方法，也就是说只要继承了`AbstractSelectableChannel`，都可以注册到`Selector`中。

![image.png](https://cdn.nlark.com/yuque/0/2022/png/1171730/1669822203452-37032ec2-a01d-464d-8967-d55aae80fe54.png)

- SelectionKey.OP_ACCEPT：不同的Channel可以注册的“其所关心的事件”是不一样的。例如`ServerSocketChannel`只允许关注`OP_ACCEPT`事件，不允许关心其他的事件。每个`Channel`可以注册的感兴趣的事件可以在每个实现类的`validOps()`方法中看到，比如`SocketChannel`：

![image.png](https://cdn.nlark.com/yuque/0/2022/png/1171730/1669822373970-62c1923c-8e8c-45ed-9f85-0df8710dd57b.png)

- selector.selectedKeys().iterator(): 当选择器Selector接收到操作系统的IO操作事件之后，它的selectedKeys将在下一次轮询操作中收到这些事件的关键描述字（不同的channel，就算关键字一样，也会存储成两个对象）。但是每一个“事件关键字”被处理后都必须移除，否则下一次轮询时，这个事件会被重复处理。


# 多路复用IO的优缺点
## 优点
- 不用再使用多线程进行IO处理了（包括操作系统内核IO管理模块和应用程序进程而言）。当然实际业务处理中，应用程序进程还可以引入线程池技术
- 同一个端口可以处理多种协议，例如，使用ServerSocketChannel服务器端口监听，既可以处理TCP协议又可以处理UDP协议。
- 操作系统级别的优化：多路复用IO及时可以是操作系统级别在一个端口上能够同时接受多个客户端的IO事件，同时具有之前我们讲到的阻塞式同步IO和非阻塞时同步IO的所有特点。Selector的一部分作用更相当于“”轮询代理器
## 缺点
- 都是同步IO：阻塞式IO、非阻塞时IO甚至包括多路复用IO，这些都是操作系统级别对“同步IO”的实现。

# 什么是同步IO？
只有上层（包括上层的某种代理机制）系统询问我是否有某个事件发生了，否则我不会主动告诉上层系统事件发生了。