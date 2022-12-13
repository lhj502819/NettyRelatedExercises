package cn.onenine.netty.intermediate.future.server;

import cn.onenine.netty.intermediate.future.codec.RpcDecoder;
import cn.onenine.netty.intermediate.future.codec.RpcEncoder;
import cn.onenine.netty.intermediate.future.msg.Request;
import cn.onenine.netty.intermediate.future.msg.Response;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/10 17:57
 */
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //protobuf处理
        ch.pipeline().addLast(new RpcDecoder(Request.class),
                new RpcEncoder(Response.class));

        //在管道中添加我们自己的接收数据实现方式
        ch.pipeline().addLast(new MyServerHandler());
    }
}
