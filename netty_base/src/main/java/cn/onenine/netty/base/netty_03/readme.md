**增加字符串解码器**
- `MyChannelInitializer`中初始化Channel 时添加解码器
- `MyServerHandler`在接收客户端消息时不需要再进行手动解码
![img.png](img.png)