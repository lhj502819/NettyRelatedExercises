package cn.onenine.netty.intermediate.future.client;

import cn.onenine.netty.intermediate.future.codec.RpcDecoder;
import cn.onenine.netty.intermediate.future.codec.RpcEncoder;
import cn.onenine.netty.intermediate.future.msg.Request;
import cn.onenine.netty.intermediate.future.msg.Response;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/10 18:43
 */
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        //对象传输处理
        channel.pipeline().addLast(new RpcDecoder(Response.class));
        channel.pipeline().addLast(new RpcEncoder(Request.class));
        // 在管道中添加我们自己的接收数据实现方法
        channel.pipeline().addLast(new MyClientHandler());
    }
}
