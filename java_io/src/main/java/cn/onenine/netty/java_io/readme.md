# 注意事项

- `register(Selector sel, int ops)`实际上是`ServerSocketChannel`的父类`AbstractSelectableChannel`提供的一个方法，也就是说只要继承了`AbstractSelectableChannel`，都可以注册到`Selector`中。

![image.png](https://cdn.nlark.com/yuque/0/2022/png/1171730/1669822203452-37032ec2-a01d-464d-8967-d55aae80fe54.png)

- SelectionKey.OP_ACCEPT：不同的Channel可以注册的“其所关心的事件”是不一样的。例如`ServerSocketChannel`只允许关注`OP_ACCEPT`事件，不允许关心其他的事件。每个`Channel`可以注册的感兴趣的事件可以在每个实现类的`validOps()`方法中看到，比如`SocketChannel`：

![image.png](https://cdn.nlark.com/yuque/0/2022/png/1171730/1669822373970-62c1923c-8e8c-45ed-9f85-0df8710dd57b.png)

- selector.selectedKeys().iterator(): 当选择器Selector接收到操作系统的IO操作事件之后，它的selectedKeys将在下一次轮询操作中收到这些事件的关键描述字（不同的channel，就算关键字一样，也会存储成两个对象）。但是每一个“事件关键字”被处理后都必须移除，否则下一次轮询时，这个事件会被重复处理。