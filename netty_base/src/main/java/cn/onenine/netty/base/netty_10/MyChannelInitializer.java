package cn.onenine.netty.base.netty_10;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/9 21:30
 */
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //数据编码操作
        ch.pipeline().addLast(new HttpResponseEncoder());
        //数据解码操作
        ch.pipeline().addLast(new HttpRequestDecoder());
        //在管道中添加我们自己的接收数据的实现方法
        ch.pipeline().addLast(new MyServerHandler());
    }
}
