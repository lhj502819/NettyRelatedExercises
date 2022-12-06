**增加给客户端发送消息**
- `MyServerHandler#channelActive`中接收到Channel活跃事件时，返回给客户端连接成功
- `MyServerHandler#channelRead`在接收客户端消息后，返回给客户端消息接收成功

![img.png](img.png)