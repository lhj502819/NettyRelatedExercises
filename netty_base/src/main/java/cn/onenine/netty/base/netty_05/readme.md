**实现NettyServer群发消息**
在Netty中可以使用`ChannelGroup`方式进行群发消息。
- 增加`ChannelHandler`，用于存放ChannelGroup
- 在`MyServerHandler`中对ChannelGroup进行处理
![img.png](img.png)