package cn.onenine.netty.base.netty_07;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/8 21:27
 */
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //自定义解码器
        ch.pipeline().addLast(new MyDecoder());
        //自定义编码器
        ch.pipeline().addLast(new MyEncoder());
        //在管道中添加我们自己的接受数据实现方法
        ch.pipeline().addLast(new MyServerHandler());

    }
}
